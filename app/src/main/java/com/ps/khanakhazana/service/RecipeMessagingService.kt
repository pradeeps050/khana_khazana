package com.ps.khanakhazana.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.rpc.Help
import com.ps.khanakhazana.MainActivity
import com.ps.khanakhazana.R
import com.ps.khanakhazana.db.RecipeDatabase
import com.ps.khanakhazana.db.RecipeEntity
import com.ps.khanakhazana.ui.cooking.CookingActivity
import com.ps.khanakhazana.utils.Helper
import com.ps.khanakhazana.utils.RecipeApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class RecipeMessagingService : FirebaseMessagingService() {

    val TAG = RecipeMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived")
        Log.d(TAG, "From: ${remoteMessage.from}")
        Helper.sendNotification(remoteMessage)
        val recipeEntity = Helper.getRecipeEntity(remoteMessage)
        recipeEntity?.let {
            insert(it)
        }
    }

    private fun insert(recipeEntity: RecipeEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = RecipeDatabase.getDbInstance(RecipeApplication.applicationContext())
            db?.let {
                it.recipeDao().insert(recipeEntity)
                Log.d(TAG, "Notification Insert")
            }
        }
    }
}