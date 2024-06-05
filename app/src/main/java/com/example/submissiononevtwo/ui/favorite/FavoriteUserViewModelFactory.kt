package com.example.submissiononevtwo.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissiononevtwo.repository.FavoriteUserRepository
import com.example.submissiononevtwo.ui.favorite.ListFavoriteUserViewModel

class FavoriteUserViewModelFactory private constructor(private val favoriteUserRepository: FavoriteUserRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListFavoriteUserViewModel::class.java)){
            return ListFavoriteUserViewModel(favoriteUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: "+modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserViewModelFactory? = null

        @JvmStatic
        fun getInstance(favoriteUserRepository: FavoriteUserRepository): FavoriteUserViewModelFactory {
            if (INSTANCE == null) {
                synchronized(FavoriteUserViewModelFactory::class.java) {
                    INSTANCE = FavoriteUserViewModelFactory(favoriteUserRepository)
                }
            }
            return INSTANCE as FavoriteUserViewModelFactory
        }
    }
}