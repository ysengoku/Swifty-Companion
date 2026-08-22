package dev.ysengoku.swiftycompanion.data

import dev.ysengoku.swiftycompanion.data.model.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface IntraService {
    @GET("v2/users/{login}")
    suspend fun getUser(@Path("login") login: String): User
}

private val authInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .header("Authorization", "Bearer ${TokenManager.getToken()}")
        .build()

    val response = chain.proceed(request)

    if (response.code == 401) {
        response.close()
        TokenManager.invalidate()
        val retried = chain.request().newBuilder()
            .header("Authorization", "Bearer ${TokenManager.getToken()}")
            .build()
        chain.proceed(retried)
    } else {
        response
    }
}

object IntraApi {
    val service: IntraService = Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor(authInterceptor).build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IntraService::class.java)
}