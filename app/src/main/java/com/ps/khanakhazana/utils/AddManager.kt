package com.ps.khanakhazana.utils

import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class AddManager {
    private val TAG = AddManager::class.java.simpleName

    fun addSetUp(view: View) {
        var adView: AdView = view as AdView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Code to be executed when an ad finishes loading.")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                Log.d(TAG, "Code to be executed when an ad opens an overlay that")
                // covers the screen.
            }

            override fun onAdClicked() {
                Log.d(TAG, "Code to be executed when the user clicks on an ad.")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }
}