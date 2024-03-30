package com.example.submissiononevtwo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissiononevtwo.data.response.DetailUserResponse
import com.example.submissiononevtwo.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    fun getUser(username: String) {
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {

            }
        })
    }
}
