package com.example.news2.viewmodels.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news2.viewmodels.SourceViewModel
import java.lang.IllegalArgumentException

class SourceViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(SourceViewModel::class.java)) {
           @Suppress("UNCHECKED_CAST")
           return SourceViewModel(app) as T
       }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}