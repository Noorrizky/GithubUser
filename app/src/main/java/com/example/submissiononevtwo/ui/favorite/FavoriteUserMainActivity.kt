package com.example.submissiononevtwo.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiononevtwo.database.FavoriteUser
import com.example.submissiononevtwo.databinding.ActivityFavoriteUserMainBinding
import com.example.submissiononevtwo.helper.ViewModelFactory
import com.example.submissiononevtwo.ui.DetailUserActivity

class FavoriteUserMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserMainBinding
    private lateinit var favoriteUserViewModel: ListFavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        supportActionBar?.title = "Favorite User"
        loading(true)
        favoriteUserViewModel = getViewModel(this)
        favoriteUserViewModel.favoriteUsers.observe(this) { favoriteUsers ->
            Log.d("FavoriteUser", favoriteUsers.toString())
            loading(false)
            showFavoriteData(favoriteUsers)
        }
    }

    private fun showFavoriteData(favoriteUsers: List<FavoriteUser>) {
        binding.apply {
            rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteUserMainActivity)
            val adapter = FavoriteUserAdapter(favoriteUsers)
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.adapter = adapter
            adapter.setOnItemClickCallBack(object : FavoriteUserAdapter.OnItemClickCallBack {
                override fun onItemClicked(data: FavoriteUser) {
                    Intent(this@FavoriteUserMainActivity, DetailUserActivity::class.java).also {
                        it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.username)
                        startActivity(it)
                    }
                }
            })
        }
    }

    private fun getViewModel(activity: AppCompatActivity): ListFavoriteUserViewModel {
        val factory = ViewModelFactory.getInstances(activity.application, activity)
//        val factory = FavoriteUserViewModelFactory.getInstance(activity.application)
//        val factory = ViewModelFactory.getInstances(activity.application, activity)

        return ViewModelProvider(activity, factory)[ListFavoriteUserViewModel::class.java]
    }

    private fun loading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
