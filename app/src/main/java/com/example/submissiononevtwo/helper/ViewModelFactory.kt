package com.example.submissiononevtwo.helper

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissiononevtwo.ui.MainViewModel
import com.example.submissiononevtwo.ui.darkmode.SettingPreferences
import com.example.submissiononevtwo.ui.darkmode.dataStore
import com.example.submissiononevtwo.ui.insert.FavoriteUserViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val pref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstances(application: Application,context: Context):ViewModelFactory{
            if(INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(application, SettingPreferences.getInstance(context.dataStore))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)){
            return FavoriteUserViewModel(mApplication) as T
        }else if(modelClass.isAssignableFrom((MainViewModel::class.java))){
            return MainViewModel as T
        }
        throw IllegalArgumentException("Uknown ViewModel class:${modelClass.name}")
    }
}
