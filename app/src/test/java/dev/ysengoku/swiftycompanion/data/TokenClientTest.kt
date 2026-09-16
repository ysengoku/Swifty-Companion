package dev.ysengoku.swiftycompanion.data

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

/** A fake Clock we can jump forward instantly, so tests don't wait for real time to pass. */
private class MutableClock(private var current: Instant) : Clock() {
    override fun getZone(): ZoneId = ZoneOffset.UTC
    override fun withZone(zone: ZoneId): Clock = this
    override fun instant(): Instant = current
    fun advanceTo(instant: Instant) {
        current = instant
    }
}

class TokenClientTest {

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.close()
    }

    private fun tokenClient(clock: Clock): TokenClient {
        return TokenClient(OkHttpClient(), server.url("/token").toString(), clock)
    }

    // No token cached yet, so it must be fetched from the endpoint.
    @Test
    fun `fetches a token when none is cached yet`() {
        server.enqueue(MockResponse(body = """{"access_token":"first","expires_in":3600}"""))

        // Use a fake Clock we can jump forward instantly, so tests don't need to wait for real time to pass.
        val client = tokenClient(Clock.fixed(Instant.EPOCH, ZoneOffset.UTC))

        assertEquals("first", client.getToken())
        assertEquals(1, server.requestCount)
    }

    // Token is still valid, so the endpoint must not be called a second time.
    @Test
    fun `reuses the cached token while it is still valid`() {
        server.enqueue(MockResponse(body = """{"access_token":"first","expires_in":3600}"""))

        val client = tokenClient(Clock.fixed(Instant.EPOCH, ZoneOffset.UTC))

        client.getToken()
        client.getToken()

        assertEquals(1, server.requestCount)
    }

    // Core requirement: once the clock passes expiresAt, getToken() must fetch a new token on its own.
    @Test
    fun `refreshes the token automatically once it has expired`() {
        server.enqueue(MockResponse(body = """{"access_token":"first","expires_in":60}"""))
        server.enqueue(MockResponse(body = """{"access_token":"second","expires_in":60}"""))

        val clock = MutableClock(Instant.EPOCH)
        val client = tokenClient(clock)

        assertEquals("first", client.getToken())

        clock.advanceTo(Instant.EPOCH.plusSeconds(3600))

        assertEquals("second", client.getToken())
        assertEquals(2, server.requestCount)
    }

    // invalidate() must force a refetch even while the current token is still valid.
    @Test
    fun `invalidate forces the next call to fetch a new token`() {
        server.enqueue(MockResponse(body = """{"access_token":"first","expires_in":3600}"""))
        server.enqueue(MockResponse(body = """{"access_token":"second","expires_in":3600}"""))

        val client = tokenClient(Clock.fixed(Instant.EPOCH, ZoneOffset.UTC))

        assertEquals("first", client.getToken())
        client.invalidate()
        assertEquals("second", client.getToken())

        assertEquals(2, server.requestCount)
    }

    // A failed refresh must surface as a TokenException instead of crashing the app.
    @Test
    fun `throws a TokenException when the token endpoint returns an error`() {
        server.enqueue(MockResponse(code = 401))

        val client = tokenClient(Clock.fixed(Instant.EPOCH, ZoneOffset.UTC))

        val exception = assertThrows(TokenException::class.java) {
            client.getToken()
        }
        assertEquals(401, exception.code)
    }
}
