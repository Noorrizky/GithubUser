package com.example.submissiononevtwo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.submissiononevtwo.R
import com.example.submissiononevtwo.data.response.DetailUserResponse
import com.example.submissiononevtwo.data.retrofit.ApiConfig
import com.example.submissiononevtwo.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: ""

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = if (position == 0)  "Following" else "Followers"
        }.attach()

        showLoading(true)

        getUserDetail(username)
    }

    private fun getUserDetail(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                showLoading(false)

                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        updateUserUI(it)
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {

                showLoading(false)

            }
        })
    }

    private fun updateUserUI(user: DetailUserResponse) {
        binding.apply {
            Glide.with(this@DetailUserActivity)
                .load(user.avatarUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(ivAvatar)

            tvName.text = user.name ?: "Unknown"
            tvUsername.text = user.login
            tvFollowing.text = getString(R.string.following_count, user.following)
            tvFollowers.text = getString(R.string.followers_count, user.followers)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    fun onBackButtonClicked(view: View) {
        finish()
    }
}


