package com.ps.khanakhazana.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.ps.khanakhazana.ads.AppOpenManager

class RecipeApplication : Application() {

    val TAG = RecipeApplication::class.java.simpleName
    var networkState : NetworkState? = null

    init {
        instance = this
    }

    companion object {
        private var instance: RecipeApplication? = null
        private var appOpenManager : AppOpenManager? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = applicationContext()
        networkState = NetworkState(context)
        networkState?.registerNetworkCallbackEvents()
        MobileAds.initialize(context) { initializationStatus ->

        }
        appOpenManager = AppOpenManager(instance!!)
        subscribeTopic()
    }

    private fun subscribeTopic() {
        Firebase.messaging.subscribeToTopic("NewRecipes")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(TAG, msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
    }

}