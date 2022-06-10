package com.ps.khanakhazana.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Lifecycle.Event.ON_START
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.ps.khanakhazana.utils.RecipeApplication
import java.util.*


class AppOpenManager(private val recipeApplication: RecipeApplication) : LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private  val  TAG = "AppOpenManager";
    private val AD_UNIT_ID = "ca-app-pub-2412295850922700/4366626352"//"ca-app-pub-3940256099942544/3419835294";
    private var appOpenAd : AppOpenAd? = null;
    private lateinit var loadCallback : AppOpenAd.AppOpenAdLoadCallback
    private  var currentActivity: Activity? = null
    private var loadTime : Long = 0

    companion object {
        var isShowingAd : Boolean = false
    }

    init {
        recipeApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    /** LifecycleObserver methods  */
    @OnLifecycleEvent(ON_START)
    fun onStart() {
        showAdIfAvailable()
        Log.d(TAG, "onStart")
        showAdIfAvailable()
    }

    fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {
            Log.d(TAG, "Will show ad.")

            var fullScreenContentCallback = object : FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    fetchAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }
            appOpenAd?.let {
                it.setFullScreenContentCallback(fullScreenContentCallback);
                currentActivity?.let { it1 -> it.show(it1) }
            }

        } else {
            Log.d(TAG, "Can not show ad.")
            fetchAd()
        }
    }

    /** Request an ad */
    fun fetchAd() {
        if (isAdAvailable()) {
            return
        }
        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {

            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */
            override fun onAdLoaded(add: AppOpenAd) {
                appOpenAd = add
                loadTime = Date().time

            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.d(TAG, loadAdError.message)
            }
        }
        var adRequest : AdRequest = getAdRequest()
        AppOpenAd.load(recipeApplication,
        AD_UNIT_ID, adRequest, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
        loadCallback)

    }

    /** Creates and returns ad request. */
    private fun  getAdRequest() : AdRequest{
        return AdRequest.Builder().build();
    }

    /** Utility method that checks if ad exists and can be shown. */
    fun isAdAvailable() : Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {
        currentActivity = null
    }

    /** Utility method to check if ad was loaded more than n hours ago.  */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }
}