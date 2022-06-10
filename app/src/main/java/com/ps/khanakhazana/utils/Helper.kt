package com.ps.khanakhazana.utils

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.messaging.RemoteMessage
import com.ps.khanakhazana.MainActivity
import com.ps.khanakhazana.R
import com.ps.khanakhazana.db.RecipeEntity
import com.ps.khanakhazana.ui.cooking.CookingActivity
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object Helper {
    val TAG = Helper::class.java.simpleName
    val STEPS = "steps"
    val INGREDIENT = "ingredients"
    val TITLE = "title"
    val IMGURL  = "imageUrl"
    val COLLECTION_PATH = "collection"
    val DOCUMENT_PATH = "doc"
    val ACTION = "action"
    val DESC = "desc"

    fun showSnakeBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun getTime() : String = SimpleDateFormat("dd/M/yyyy hh:mm a").format(Date())

    fun sendNotification(remoteMessage: RemoteMessage) {
        val context = RecipeApplication.applicationContext()
        val desc = remoteMessage.data.get(DESC)
        val recipeEntity = getRecipeEntity(remoteMessage)
        Log.d(TAG, "Recipe --> " + recipeEntity.toString())
        var url = URL(recipeEntity.imgUrl)
        val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        val bundle = Bundle()
        bundle?.putString(COLLECTION_PATH, recipeEntity.id)
        bundle?.putString(DOCUMENT_PATH, recipeEntity.doc)
        bundle?.putString(IMGURL, recipeEntity.imgUrl)
        bundle?.putString(TITLE, recipeEntity.title)
        val intent = Intent(context, CookingActivity::class.java).apply {
            putExtras(bundle)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        val channelId = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.splash_icon)
            .setLargeIcon(bitmap)
            .setContentTitle(recipeEntity.title)
            .setContentText(desc)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                context.getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify((0..1000).random() /* ID of notification */, notificationBuilder.build())
    }

    fun getRecipeEntity(remoteMessage: RemoteMessage) : RecipeEntity {
        val collection = remoteMessage.data.get(COLLECTION_PATH)
        val doc = remoteMessage.data.get(DOCUMENT_PATH)
        val title = remoteMessage.data.get(TITLE)
        val imageUrl = remoteMessage.data.get(IMGURL)
        val time = getTime()
        Log.d(TAG, "Time --> " + time)
        var recipeEntity = RecipeEntity(collection as String, title, doc, imageUrl, false, true, time)
        return recipeEntity
    }

    fun showAlertDialog(context: Context) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.no_internet))
        builder.setMessage(context.getString(R.string.no_internet_msg))
        builder.setPositiveButton(context.getString(R.string.try_again)) { dialog, which ->
            context.startActivity(Intent(context, MainActivity::class.java))
        }
        builder.show()

        /*builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("Maybe") { dialog, which ->
            Toast.makeText(applicationContext,
                "Maybe", Toast.LENGTH_SHORT).show()
        }*/
    }

    fun showNetworkMessage(isConnected: Boolean?, view: View) {
        var snackbar = Snackbar.make(view, "You are offline", Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        if (!isConnected!!) {
            snackbar?.show()
            view.visibility = View.GONE
        } else {
            snackbar?.dismiss()
            view.visibility = View.VISIBLE
        }
    }
}