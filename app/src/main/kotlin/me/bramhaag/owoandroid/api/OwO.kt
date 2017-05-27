package me.bramhaag.owoandroid.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class OwO(key: String) {

    val service: OwOService

    init {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            var request = chain.request()
            val url = request.url().newBuilder().addQueryParameter("key", key).build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }.readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.awau.moe/")
                .client(client)
                .build()

        this.service = retrofit.create<OwOService>(OwOService::class.java)
    }
}