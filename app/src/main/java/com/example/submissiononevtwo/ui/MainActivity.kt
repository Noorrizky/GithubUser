package com.example.submissiononevtwo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiononevtwo.R
import com.example.submissiononevtwo.data.response.GithubResponse
import com.example.submissiononevtwo.data.response.ItemsItem
import com.example.submissiononevtwo.data.retrofit.ApiConfig
import com.example.submissiononevtwo.databinding.ActivityMainBinding
import com.example.submissiononevtwo.ui.darkmode.DarkModeActivity
import com.example.submissiononevtwo.ui.darkmode.DarkModeViewModel
import com.example.submissiononevtwo.ui.darkmode.DarkModeViewModelFactory
import com.example.submissiononevtwo.ui.darkmode.SettingPreferences
import com.example.submissiononevtwo.ui.darkmode.dataStore
import com.example.submissiononevtwo.ui.favorite.FavoriteUserMainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var darkModeViewModel: DarkModeViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Observer Dark MODE
        val pref = SettingPreferences.getInstance(application.dataStore)
        darkModeViewModel = ViewModelProvider(this, DarkModeViewModelFactory(pref)).get(
            DarkModeViewModel::class.java)
//        Observer Dark MODE

        darkModeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
//        Observer Dark MODE

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (mainViewModel.listUser.value == null) mainViewModel.getUsers("")
        mainViewModel.listUser.observe(this) {
            it?.let {
                setUserList(it)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    getUsers(searchView.text.toString())
                    searchView.hide()
                    false
                }
        }

        reviewAdapter = ReviewAdapter()
        binding.rvReview.adapter = reviewAdapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        reviewAdapter.setOnItemClickListener { item ->
            val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
            intent.putExtra("username", item.login)
            startActivity(intent)
        }
        getUsers("Arif")

//        Favorite User Button
        val fabFavorite = findViewById<FloatingActionButton>(R.id.fab_favorite)
        fabFavorite.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteUserMainActivity::class.java)
            startActivity(intent)
        }
//        Favorite User Button


        val fabMode = findViewById<FloatingActionButton>(R.id.fab_dark_mode)
        fabMode.setOnClickListener {
            val intent = Intent(this@MainActivity, DarkModeActivity::class.java)
            startActivity(intent)
        }


    }


    private fun getUsers(query: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        setUserList(it.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserList(userList: List<ItemsItem>?) {
        if (userList.isNullOrEmpty()) {
            reviewAdapter.submitList(emptyList())
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        } else {
            reviewAdapter.submitList(userList)
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
