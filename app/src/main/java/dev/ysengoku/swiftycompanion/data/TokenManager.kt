package dev.ysengoku.swiftycompanion.data

import com.google.gson.Gson
import java.time.Clock
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import dev.ysengoku.swiftycompanion.BuildConfig
import dev.ysengoku.swiftycompanion.data.model.TokenResponse

class TokenException(val code: Int): Exception("Token request failed: $code")

class TokenClient(
    private val okHttpClient: OkHttpClient,
    private val url: String,
    private val clock: Clock
) {
    private val gson = Gson()
    private var token: String? = null
    private var expiresAt: Long = 0

    @Synchronized
    fun getToken(): String {
        val current = token
        if (current != null && clock.millis() < expiresAt) {
            return current
        }
        return fetchToken()
    }

    @Synchronized
    fun invalidate() {
        token = null
        expiresAt = 0
    }

    private fun fetchToken():String {
        val body = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .add("client_id", BuildConfig.API_UID)
            .add("client_secret", BuildConfig.API_SECRET)
            .build()

        val request = Request.Builder().url(url).post(body).build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw TokenException(response.code)
            }
            val json = response.body.string()
            val parsed = gson.fromJson(json, TokenResponse::class.java)
            token = parsed.accessToken
            expiresAt = clock.millis() + (parsed.expiresIn - 60) * 1000
            return parsed.accessToken
        }
    }
}

object TokenManager {
    private val tokenClient = TokenClient(
        OkHttpClient(),
        ApiConfig.TOKEN_URL,
        Clock.systemUTC()
    )

    fun getToken(): String {
        return tokenClient.getToken()
    }

    fun invalidate() {
        tokenClient.invalidate()
    }
}
