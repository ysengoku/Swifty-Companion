package dev.ysengoku.swiftycompanion.data

import dev.ysengoku.swiftycompanion.data.model.User
import okhttp3.Interceptor
/*import okhttp3.logging.HttpLoggingInterceptor*/
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface IntraService {
    @GET("v2/users/{login}")
    suspend fun getUser(@Path("login") login: String): User
}

private val authInterceptor = Interceptor { chain ->
    val baseRequest = chain.request()

    fun tokenErrorResponse(e: TokenException): Response =
        Response.Builder()
            .request(baseRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(e.code)
            .message(e.message ?: "Token error")
            .body("".toResponseBody(null))
            .build()

    val token = try {
        TokenManager.getToken()
    } catch (e: TokenException) {
        return@Interceptor tokenErrorResponse(e)
    }

    val request = baseRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

    val response = chain.proceed(request)

    if (response.code == 401 || response.code == 403) {
        response.close()
        TokenManager.invalidate()
        val retriedToken = try {
            TokenManager.getToken()
        } catch (e: TokenException) {
            return@Interceptor tokenErrorResponse(e)
        }
        val retried = baseRequest.newBuilder()
            .header("Authorization", "Bearer $retriedToken")
            .build()
        chain.proceed(retried)
    } else {
        response
    }
}

/*private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.HEADERS
}*/

object IntraApi {
    val service: IntraService = Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                /*.addInterceptor(loggingInterceptor)*/
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IntraService::class.java)
}
