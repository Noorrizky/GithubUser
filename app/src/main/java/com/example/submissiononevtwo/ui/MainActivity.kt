package com.example.submissiononevtwo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
import com.example.submissiononevtwo.helper.ViewModelFactory
import com.example.submissiononevtwo.utils.SettingPreferences
import com.example.submissiononevtwo.utils.dataStore
import com.google.android.material.switchmaterial.SwitchMaterial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    mainViewModel.getUsers(searchView.text.toString())
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

//        TODO SWITCH THEME
//        val switchTheme = findViewById<Button>(R.id.switch_theme)
//
//        val pref = SettingPreferences.getInstance(application.dataStore)
//        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java)
//
//// Observe the theme settings
//        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
//            if (isDarkModeActive) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }
//
//        switchTheme.setOnClickListener {
//            // Get the current theme setting
//            val currentThemeSetting = mainViewModel.getThemeSettings().value ?: false
//
//            // Save the new theme setting
//            val newThemeSetting = !currentThemeSetting
//            mainViewModel.saveThemeSetting(newThemeSetting)
//        }
//          TODO SWTICH THEME

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
