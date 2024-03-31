package com.example.submissiononevtwo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissiononevtwo.data.response.ItemsItem
import com.example.submissiononevtwo.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragmentViewModel : ViewModel() {
    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    fun fetchFollowers(username: String) {
        val client = ApiConfig.getApiService()
        val call = client.getFollowers(username)

        call.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _followers.postValue(response.body())
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun fetchFollowing(username: String) {
        val client = ApiConfig.getApiService()
        val call = client.getFollowing(username)

        call.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _following.postValue(response.body())
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
