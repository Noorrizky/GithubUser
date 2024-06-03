package com.example.submissiononevtwo.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissiononevtwo.database.FavoriteUser
import com.example.submissiononevtwo.databinding.ItemReviewBinding

class FavoriteUserAdapter(private val listFavoriteUser: List<FavoriteUser>) : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    class FavoriteUserViewHolder(var binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val view = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        val data = listFavoriteUser[position]
        Glide.with(holder.itemView.context)
            .load(data.avatarUrl)
            .circleCrop()
            .into(holder.binding.ivAvatar)
        holder.binding.tvItem.text = data.username
        holder.itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(data) }
    }

    override fun getItemCount(): Int = listFavoriteUser.size

    interface OnItemClickCallBack {
        fun onItemClicked(data: FavoriteUser)
    }
}
