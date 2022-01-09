package com.example.afreecatv_android_yoonseungil.src.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.afreecatv_android_yoonseungil.R
import com.example.afreecatv_android_yoonseungil.src.dto.Item

class RepositoryAdapter(var repoList : MutableList<Item>) :
    RecyclerView.Adapter<RepositoryAdapter.RepositoryHolder>(){
    inner class RepositoryHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val avatarUrl : ImageView = itemView.findViewById(R.id.repoAvatarUrl)
        val fullName : TextView = itemView.findViewById(R.id.repoFullName)
        val ownerLogin : TextView = itemView.findViewById(R.id.repoOwnerLogin)
        val language : TextView = itemView.findViewById(R.id.repoLanguage)

        fun bindInfo(data : Item){
            Glide.with(itemView).load(data.owner.avatar_url).into(avatarUrl)
            fullName.text = data.full_name
            ownerLogin.text = data.owner.login
            language.text = data.language
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        return RepositoryHolder(view)
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