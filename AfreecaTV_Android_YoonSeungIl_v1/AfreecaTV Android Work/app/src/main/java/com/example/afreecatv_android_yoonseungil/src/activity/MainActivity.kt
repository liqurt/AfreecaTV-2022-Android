package com.example.afreecatv_android_yoonseungil.src.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afreecatv_android_yoonseungil.R
import com.example.afreecatv_android_yoonseungil.config.ApplicationClass
import com.example.afreecatv_android_yoonseungil.config.Constants.logcatTag
import com.example.afreecatv_android_yoonseungil.src.adapter.RepositoryAdapter
import com.example.afreecatv_android_yoonseungil.src.api.RepositoryInterface
import com.example.afreecatv_android_yoonseungil.src.dto.Item
import com.example.afreecatv_android_yoonseungil.src.dto.Repository
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import com.example.afreecatv_android_yoonseungil.config.Constants.per_page


class MainActivity : AppCompatActivity() {

    lateinit var search: EditText
    lateinit var searchImage: ImageView
    lateinit var reposRcv : RecyclerView
    lateinit var loading : ProgressBar

    var itemList = mutableListOf<Item>()
    var itemListAdapter = RepositoryAdapter(itemList)

    var currentKeyword : String = ""
    var page : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search = findViewById(R.id.search)
        searchImage = findViewById(R.id.searchImage)
        reposRcv = findViewById(R.id.reposRcv)
        loading = findViewById(R.id.loading)

        search.setOnEditorActionListener { textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리기
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(search.windowToken, 0)
                handled = true

                startSearch(search.text.toString())
            }
            handled
        }
        searchImage.setOnClickListener {
            startSearch(search.text.toString())
        }
        reposRcv.layoutManager = LinearLayoutManager(this)
        reposRcv.adapter = itemListAdapter
        reposRcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    getMoreData()
                }
            }
        })

    }

    private fun startSearch(keyword: String) {
        itemList = mutableListOf()
        currentKeyword = keyword
        val message: String = if (currentKeyword.isEmpty()) {
            "키워드를 입력해주세요"
        } else {
            val q = currentKeyword
            page = 1
            getRepositoryData(q,page)
            "$currentKeyword 로 검색 중~"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getMoreData(){
        if(currentKeyword.isNotEmpty()){
            loading.visibility = View.VISIBLE
            val q = currentKeyword
            page+=1
            getRepositoryData(q,page)
            loading.visibility = View.GONE
        }
    }

    private fun getRepositoryData(q : String, page : Int){
        val repositoryInterface = ApplicationClass.retrofit.create(RepositoryInterface::class.java)
        repositoryInterface.getRepository(q, per_page, page.toString()).enqueue(
            object : Callback<Repository>{
                override fun onResponse(
                    call: Call<Repository>,
                    response: Response<Repository>
                ) {
                    if(response.code() == 200){
                        val res = response.body()
                        val jsonRes = Gson().toJson(res?.items)
                        val data : Array<Item> = Gson().fromJson(jsonRes, Array<Item>::class.java)
                        itemList.addAll(data)
                        itemListAdapter.repoList = itemList
                        itemListAdapter.notifyDataSetChanged()
                    }else{
                        Log.d(logcatTag,"Error : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Repository>, t: Throwable) {
                    Log.d(logcatTag,"Failure : $t")
                }

            })
    }


}