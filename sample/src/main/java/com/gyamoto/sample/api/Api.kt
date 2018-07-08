package com.gyamoto.sample.api

import com.gyamoto.sample.model.Sweets
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @GET("sweet")
    fun getSweet(): Call<List<Sweets>>

    @POST("sweet")
    fun postSweet(): Call<Unit>

    @DELETE("sweet")
    fun deleteSweet(): Call<Unit>
}
