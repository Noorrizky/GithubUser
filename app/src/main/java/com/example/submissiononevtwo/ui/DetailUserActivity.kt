package com.example.submissiononevtwo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.submissiononevtwo.data.response.DetailUserResponse
import com.example.submissiononevtwo.data.retrofit.ApiConfig
import com.example.submissiononevtwo.databinding.ActivityDetailUserBinding
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

        // Show loading indicator
        showLoading(true)

        // Make API call to fetch detailed user information
        getUserDetail(username)
    }

    private fun getUserDetail(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                // Hide loading indicator
                showLoading(false)

                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        // Update UI with fetched user information
                        updateUserUI(it)
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                // Hide loading indicator
                showLoading(false)

                // Handle failure
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
            tvFollowers.text = user.followers.toString()
            tvFollowing.text = user.following.toString()
            // Update other UI elements as needed
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
