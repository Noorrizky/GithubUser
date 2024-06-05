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
import com.example.submissiononevtwo.repository.FavoriteUserRepository
import com.example.submissiononevtwo.ui.DetailUserActivity

class FavoriteUserMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserMainBinding
    private lateinit var listFavoriteUserViewModel: ListFavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.title = "Favorite User"
        supportActionBar?.hide()

        loading(true)
        val favoriteUserRepository = FavoriteUserRepository(application)
        val factory = FavoriteUserViewModelFactory.getInstance(favoriteUserRepository)
        listFavoriteUserViewModel = ViewModelProvider(this, factory)[ListFavoriteUserViewModel::class.java]

//        listFavoriteUserViewModel = getViewModel(this)
        listFavoriteUserViewModel.favoriteUsers.observe(this) { favoriteUsers ->
            Log.d("FavoriteUser", favoriteUsers.toString())
            loading(false)
            showFavoriteData(favoriteUsers)
        }
    }

    private fun showFavoriteData(favoriteUsers: List<FavoriteUser>) {
        binding.apply {
            if (favoriteUsers.isEmpty()) {
                rvFavoriteUser.visibility = View.GONE
                tvNoUser.visibility = View.VISIBLE
            } else {
                rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteUserMainActivity)
                val adapter = FavoriteUserAdapter(favoriteUsers)
                rvFavoriteUser.setHasFixedSize(true)
                rvFavoriteUser.adapter = adapter
                adapter.setOnItemClickCallBack(object : FavoriteUserAdapter.OnItemClickCallBack {
                    override fun onItemClicked(item: FavoriteUser) {
                        val intent = Intent(this@FavoriteUserMainActivity, DetailUserActivity::class.java)
                        intent.putExtra("username", item.username)
                        startActivity(intent)
                    }
                })
                rvFavoriteUser.visibility = View.VISIBLE
                tvNoUser.visibility = View.GONE
            }
        }
    }


//    private fun getViewModel(activity: AppCompatActivity): ListFavoriteUserViewModel {
//        val factory = FavoriteUserViewModelFactory.getInstances(activity.application)
//
//        return ViewModelProvider(activity, factory)[ListFavoriteUserViewModel::class.java]
//    }

    private fun loading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
