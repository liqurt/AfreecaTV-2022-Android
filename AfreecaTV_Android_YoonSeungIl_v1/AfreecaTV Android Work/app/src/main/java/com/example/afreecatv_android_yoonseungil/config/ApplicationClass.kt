package com.example.afreecatv_android_yoonseungil.config

import android.app.Application
import android.util.Log
import com.example.afreecatv_android_yoonseungil.config.Constants.logcatTag
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val GithubURL = "https://api.github.com/search/repositories/"

class ApplicationClass : Application() {
    companion object{
        lateinit var retrofit : Retrofit
    }

//    앱을 실행하면 config/ApplicationClass 에서 retrofit의 인스턴스를 만듭니다.
    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
            .baseUrl(GithubURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}