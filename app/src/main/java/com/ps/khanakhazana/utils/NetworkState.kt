package com.ps.khanakhazana.utils

import android.content.Context
import android.net.*
import android.util.Log

class NetworkState(private val context: Context) : ConnectivityManager.NetworkCallback() {
    private val TAG :String = NetworkState::class.java.simpleName
    private lateinit var networkRequest: NetworkRequest
    private lateinit var connectivityManager: ConnectivityManager
    private final var networkStateManager : NetworkStateManager? = null

    init {
        networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkStateManager = NetworkStateManager.getInstace()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d(TAG, "onAvailable() called: Connected to network");
        networkStateManager?.setNetworkConnectivityStatus(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Log.e(TAG, "onLost() called: Lost network connection");
        networkStateManager?.setNetworkConnectivityStatus(false)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
    }

    fun registerNetworkCallbackEvents() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
        NetworkStateManager.activeNetworkStatusMLD.postValue(false)
    }
}

/*
sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}

class NetworkStatusHelper(private val context: Context) : LiveData<Boolean>() {

    val TAG = NetworkStatusHelper::class.java.simpleName

    val valideNetworkConnections : ArrayList<Network> = ArrayList()
    var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    fun announceStatus(state: Boolean) {
        postValue(state)
        if (valideNetworkConnections.isNotEmpty()){
            //postValue(NetworkStatus.Available)

        } else {
            //postValue(NetworkStatus.Unavailable)
        }
    }

    fun getConnectivityManagerCallback() =
        object : ConnectivityManager.NetworkCallback(){

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkCapability = connectivityManager.getNetworkCapabilities(network)
                val hasNetworkConnection = networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?:false
                Log.d(TAG, "onAvailable" + hasNetworkConnection.toString())
                postValue(hasNetworkConnection)
                //announceStatus(hasNetworkConnection)
//                if (hasNetworkConnection){
//                    //determineInternetAccess(network)
//                    //valideNetworkConnections.add(network)
//                    //announceStatus(hasNetworkConnection)
//                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                //valideNetworkConnections.remove(network)
                Log.d(TAG, "onLost")
                postValue(false)
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                Log.d(TAG, "onCapabilitiesChanged")
                */
/*if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                    //determineInternetAccess(network)
                    Log.d(TAG, "onCapabilitiesChanged -- true")
                    valideNetworkConnections.add(network)
                } else {
                    valideNetworkConnections.remove(network)
                    Log.d(TAG, "onCapabilitiesChanged -- false")
                }*//*

                //announceStatus(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            }
        }

    */
/*private fun determineInternetAccess(network: Network) {
        CoroutineScope(Dispatchers.IO).launch{
            if (InernetAvailablity.check()){
                withContext(Dispatchers.Main){
                    valideNetworkConnections.add(network)
                    announceStatus()
                }
            }
        }
    }*//*



    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = getConnectivityManagerCallback()
        val networkRequest = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
       // connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

}

object InernetAvailablity {

    fun check() : Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8",53))
            socket.close()
            true
        } catch ( e: Exception){
            e.printStackTrace()
            false
        }
    }

}*/
