package com.example.afreecatv_android_yoonseungil.src.api

import com.example.afreecatv_android_yoonseungil.src.dto.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoryInterface {
    @GET("/search/repositories")
    fun getRepository(@Query("q") q : String,
                      @Query("per_page") per_page : String,
                      @Query("page") page : String
    ) : Call<Repository>
}