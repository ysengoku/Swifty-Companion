package dev.ysengoku.swiftycompanion.data

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.Gson
import dev.ysengoku.swiftycompanion.BuildConfig
import dev.ysengoku.swiftycompanion.data.model.TokenResponse
import dev.ysengoku.swiftycompanion.data.model.User

object TokenManager {
    private val client = OkHttpClient()
    private val gson = Gson()

    private var token: String? = null
    private var expiresAt: Long = 0

    @Synchronized
    fun getToken(): String {
        val current = token
        if (current != null && System.currentTimeMillis() < expiresAt) {
            return current
        }
        return fetchToken()
    }

    @Synchronized
    fun invalidate() {
        token = null
        expiresAt = 0
    }

    fun fetchToken():String {
        val body = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .add("client_id", BuildConfig.API_UID)
            .add("client_secret", BuildConfig.API_SECRET)
            .build()

        val request = Request.Builder().url(ApiConfig.TOKEN_URL).post(body).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Token request failed: ${response.code}")
            }
            val json = response.body.string()
            val parsed = gson.fromJson(json, TokenResponse::class.java)
            token = parsed.accessToken
            expiresAt = System.currentTimeMillis() + (parsed.expiresIn - 60) * 1000
            return parsed.accessToken
        }
    }


}