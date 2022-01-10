package com.example.afreecatv_android_yoonseungil.src.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.afreecatv_android_yoonseungil.R
import com.example.afreecatv_android_yoonseungil.databinding.RepositoryItemBinding
import com.example.afreecatv_android_yoonseungil.src.dto.Item

class RepositoryAdapter(var repoList : MutableList<Item>) :
    RecyclerView.Adapter<RepositoryAdapter.RepositoryHolder>(){

    inner class RepositoryHolder(private val binding : RepositoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(data : Item){
            Glide.with(binding.root).load(data.owner.avatar_url).into(binding.repoAvatarUrl)
            binding.repoFullName.text = data.full_name
            binding.repoOwnerLogin.text = data.owner.login
            binding.repoLanguage.text = data.language
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder {
        val binding = RepositoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryHolder(binding)
    }

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        holder.apply {
            bindInfo(repoList[position])
        }
    }

    override fun getItemCount(): Int {
        return repoList.size
    }
}