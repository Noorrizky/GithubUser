package com.example.submissiononevtwo.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submissiononevtwo.database.FavoriteUser
import com.example.submissiononevtwo.database.FavoriteUserDao
import com.example.submissiononevtwo.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUsersDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUsersDao = db.favoriteuserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUsersDao.getAllFavoriteUser()
    fun getFavoriteUserByUsername(username:String) : LiveData<FavoriteUser> = mFavoriteUsersDao.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser){
        executorService.execute { mFavoriteUsersDao.insert(favoriteUser)}
    }

    fun delete(favoriteUser: FavoriteUser){
        executorService.execute{ mFavoriteUsersDao.delete(favoriteUser)}
    }



}