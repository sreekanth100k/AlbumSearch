package com.android.myapplication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface ApiService {
    @GET("search?term=all")
    fun fetchApiResponse():Call<TestResponse>

}