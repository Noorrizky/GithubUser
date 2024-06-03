package com.example.submissiononevtwo.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissiononevtwo.database.FavoriteUser
import com.example.submissiononevtwo.repository.FavoriteUserRepository

class FavoriteUserViewModel(private val favoriteUserRepository: FavoriteUserRepository) : ViewModel() {
    val favoriteUsers: LiveData<List<FavoriteUser>> = favoriteUserRepository.getAllFavoriteUser()
}



//TODO ERORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRr