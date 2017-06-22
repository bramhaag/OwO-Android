package me.bramhaag.owoandroid.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface OwOService {

    @Multipart
    @POST("upload/pomf")
    fun upload(@Part file: MultipartBody.Part): Call<ResponseBody>

    @GET("shorten/polr?action=shorten")
    fun shorten(@Query("url") url: String): Call<ResponseBody>
}