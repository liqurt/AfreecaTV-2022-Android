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

    override fun onCreate() {
        super.onCreate()

        //앱이 처음 생성되면, retrofit 인스턴스 생성
        retrofit = Retrofit.Builder()
            .baseUrl(GithubURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val logMsg = "app 이 처음 생성됨!"
        Log.d(logcatTag,logMsg)
        Log.d(logcatTag,"$retrofit")
    }
}