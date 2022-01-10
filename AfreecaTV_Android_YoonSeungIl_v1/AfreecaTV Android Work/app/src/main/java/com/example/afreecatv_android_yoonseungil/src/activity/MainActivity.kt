package com.example.afreecatv_android_yoonseungil.src.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.example.afreecatv_android_yoonseungil.databinding.ActivityMainBinding
import android.view.KeyEvent

import android.widget.TextView
import android.widget.TextView.OnEditorActionListener


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var itemList = mutableListOf<Item>()
    var itemListAdapter = RepositoryAdapter(itemList)
    var currentKeyword: String = ""
    var requirePage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchImage.setOnClickListener {
            currentKeyword = binding.keyword.text.toString()
            hideKeyboard()
            initialSearch()
        }

        binding.reposRcv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = itemListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        additionalSearch()
                    }
                }
            })
        }

        binding.keyword.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    currentKeyword = binding.keyword.text.toString()
                    hideKeyboard()
                    initialSearch()
                    true
                } else false
            }
        }

    }

    // 키보드를 아래로 숨깁니다
    private fun hideKeyboard(){
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if(view == null)
            view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initialSearch() { // 검색버튼이나 키워드를 입력해서 검색합니다.
        itemList = mutableListOf() // 리사이클러뷰에 매칭시킬 리스트를 비웁니다.
        val message: String = if (currentKeyword.isEmpty()) {
            "키워드를 입력해주세요"
        } else {
            requirePage = 1
            getRepositoryData(currentKeyword, requirePage)
            "$currentKeyword 로 검색 합니다"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun additionalSearch() { // 스크롤을 내려 추가적으로 검색합니다.
        if (currentKeyword.isNotEmpty()) {
            requirePage += 1 // 다음 페이지를 가져올 것 입니다.
            getRepositoryData(currentKeyword, requirePage)
        } else {
            Toast.makeText(this, "키워드를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRepositoryData(q: String, page: Int) {
        binding.loading.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ // 초기에 만들었던 retrofit의 인스턴스와 api를 이용하여 "https://api.github.com/search/repositories/" 에 Get 요청을 보냅니다.
            val repositoryInterface = ApplicationClass.retrofit.create(RepositoryInterface::class.java)
            repositoryInterface.getRepository(q, per_page, page.toString()).enqueue(
                object : Callback<Repository> {
                    override fun onResponse(
                        call: Call<Repository>,
                        response: Response<Repository>,
                    ) {
                        if (response.code() == 200) {
                            val res = response.body()
                            val jsonRes = Gson().toJson(res?.items)
                            val data: Array<Item> = Gson().fromJson(jsonRes, Array<Item>::class.java)
                            itemList.addAll(data)
                            itemListAdapter.repoList = itemList
                            itemListAdapter.notifyDataSetChanged()
                        } else {
                            Log.d(logcatTag, "Error : $response")
                            requirePage -= 1 // 결국 '다음' 페이지의 결과를 보여주지 못했기에, 원상복귀 시킵니다.
                            if(response.code() == 403){ // Github의 정책때문에, 짧은 시간 내에 여러번 request를 보낼수 없습니다. 이 경우, 사용자에게 양해를 구하는 메시지를 보여줬습니다.
                                Toast.makeText(this@MainActivity,"잠시 후 다시 시도해주세요",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Repository>, t: Throwable) {
                        Log.d(logcatTag, "Failure : $t")
                    }
                })
            binding.loading.visibility = View.GONE
        }, 1000) // 딜레이 없이 위 작업을 진행하면, 너무 빨리 진행되서 로딩중임을 나타내는것도 순식간에 지나갔습니다. 부득이하게, 로딩중임을 보여주고자 1초의 딜레이를 주었습니다.
    }

}