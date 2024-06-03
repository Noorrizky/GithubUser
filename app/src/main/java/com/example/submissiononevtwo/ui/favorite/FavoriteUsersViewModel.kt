package com.example.submissiononevtwo.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissiononevtwo.database.FavoriteUser
import com.example.submissiononevtwo.repository.FavoriteUserRepository

class FavoriteUsersViewModel(application: Application) : ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    val favoriteUsers: LiveData<List<FavoriteUser>> = favoriteUserRepository.getAllFavoriteUser()
}