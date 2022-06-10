package com.ps.khanakhazana.utils

import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class NetworkStateManager {
    val TAG : String = NetworkStateManager::class.java.simpleName

    companion object {
        private var INSTANCE: NetworkStateManager? = null
        val activeNetworkStatusMLD = MutableLiveData<Boolean>()

        @Synchronized
        fun getInstace() : NetworkStateManager? {
            Log.d("NetworkStateManager", "getInstance() called: Creating new instance")
            synchronized(NetworkStateManager::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = NetworkStateManager()
                }
            }
            return INSTANCE
        }
    }

    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        Log.d(TAG, "setNetworkConnectivityStatus() called with: connectivityStatus = [$connectivityStatus]")
        if (Looper.myLooper() == Looper.getMainLooper()) {
            activeNetworkStatusMLD.setValue(connectivityStatus)
        } else {
            activeNetworkStatusMLD.postValue(connectivityStatus)
        }
    }

    fun getNetworkConnectivityStatus(): LiveData<Boolean?>? {
        Log.d(TAG, "getNetworkConnectivityStatus() called")
        return activeNetworkStatusMLD
    }

}