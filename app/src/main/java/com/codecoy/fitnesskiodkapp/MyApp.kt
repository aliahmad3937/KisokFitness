package com.codecoy.fitnesskiodkapp

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.NavController
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.repository.MainRepository
import com.codecoy.fitnesskiodkapp.retrofit.RetrofitAPI
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import com.codecoy.fitnesskiodkapp.viewmodels.MyViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApp : Application() {


    companion object {
        lateinit var bottomNavController: NavController
        lateinit var mainNavController: NavController
        lateinit var bottomNav: BottomNavigationView
        var logoUrl: String? = null
        var categoryType: String = ""
        var qrImage: String? = null
        var videoUrl: String? = null
        var userId: Int? = null
        var selectedCategory: Int? = null
        var equipmentList: ArrayList<Data>? = arrayListOf()



        @JvmStatic
        fun getPath(url: String): String {
            return "https://packfit.io/public/storage/$url"
        }

    }

    override fun onCreate() {
        super.onCreate()

    }


}