package com.example.submissiononevtwo.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.submissiononevtwo.database.FavoriteUser
import com.example.submissiononevtwo.database.FavoriteUserDao
import com.example.submissiononevtwo.database.FavoriteUserRoomDatabase
import com.example.submissiononevtwo.repository.FavoriteUserRepository


class FavoriteUserViewModel(application: Application = Application()): ViewModel() {
    private val favoriteUserRepository:FavoriteUserRepository = FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUser){
        favoriteUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser){
        favoriteUserRepository.delete(favoriteUser)
    }

    fun getAllFavoriteUser() = favoriteUserRepository.getAllFavoriteUser()
    fun getFavoriteUserByUsername(username:String) = favoriteUserRepository.getFavoriteUserByUsername(username)
}