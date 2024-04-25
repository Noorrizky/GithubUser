package com.example.submissiononevtwo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.submissiononevtwo.R
import com.example.submissiononevtwo.data.response.DetailUserResponse
import com.example.submissiononevtwo.data.retrofit.ApiConfig
import com.example.submissiononevtwo.database.FavoriteUser
import com.example.submissiononevtwo.databinding.ActivityDetailUserBinding
import com.example.submissiononevtwo.helper.ViewModelFactory
import com.example.submissiononevtwo.ui.insert.FavoriteUserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val favoriteUserViewModel: FavoriteUserViewModel by viewModels<FavoriteUserViewModel> {
        ViewModelFactory.getInstances(application,this)
    }
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

            favoriteUserViewModel.getFavoriteUserByUsername(user.login).observe(this@DetailUserActivity){
                data ->
                val isFavorite = data != null

        //        Favorite INSERT
                fabFavorite.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_1
                )

            fabFavorite.setOnClickListener {
                val favoriteUser = FavoriteUser(
                    username = user.login,
                    avatarUrl = user.avatarUrl
                    )
                if(isFavorite){
                    favoriteUserViewModel.delete(favoriteUser)
                    Toast.makeText(this@DetailUserActivity,"User deleted from favorite",Toast.LENGTH_SHORT).show()
                }
                else {
                    favoriteUserViewModel.insert(favoriteUser)
                    Toast.makeText(this@DetailUserActivity,"User Added to favorite",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
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


