package com.ps.khanakhazana

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.ps.khanakhazana.utils.AddManager
import com.ps.khanakhazana.utils.Helper
import com.ps.khanakhazana.utils.NetworkStateManager


class MainActivity : AppCompatActivity() {

    var TAG = MainActivity::class.java.simpleName
    private val REQUEST_CODE: Int = 100
    private var appUpdateManager :AppUpdateManager? = null
    private lateinit var toolbar        : MaterialToolbar
    private lateinit var navController  : NavController
    private lateinit var navigationView : NavigationView
    private lateinit var drawerLayout   : DrawerLayout
    private lateinit var bottpmNavViw   : BottomNavigationView
    private lateinit var appBarConfig: AppBarConfiguration
    private val networkStateManager : NetworkStateManager = NetworkStateManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this)
        installUpdateListener?.let { appUpdateManager?.registerListener(it) }
        initView()
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_frag) as NavHostFragment
        navController = navHost.navController
        val destination = setOf(R.id.fragmentHome, R.id.fragmentFav, R.id.fragmentNotification)
        //val appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)
        appBarConfig = AppBarConfiguration(destination, drawerLayout)
        //setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfig)
        navigationView.setupWithNavController(navController)
        bottpmNavViw.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener { item ->
            val id = item.itemId
            Log.d(TAG,item.title.toString())
            when (id) {
                R.id.rate -> {
                    Log.d(TAG, "RATE")
                    inAppReview()
                }
                R.id.share -> {
                    Log.d(TAG, "Share")
                    shareApp()
                }
                R.id.feedback -> {
                    Log.d(TAG, "Feedback")
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_EMAIL,
                            arrayOf("pradeep.1mobility@gmail.com")
                        ) // recipients
                        putExtra(Intent.EXTRA_SUBJECT, "Feedback-Khana Khazana")
                        putExtra(Intent.EXTRA_TEXT, "Email message text")
                    }
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
        networkStateManager.getNetworkConnectivityStatus()?.observe(this) {
            Log.d(TAG, "Internet --> " + it.toString())
            //showNetworkMessage(it, drawerLayout)
            showAlertDialog(it)
        }
    }

    private fun initView() {
        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
        bottpmNavViw = findViewById(R.id.bottom_nav_view)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        AddManager().addSetUp(findViewById(R.id.adView))
        checkForUpdate()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    private fun inProgressUpdate() {
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager?.startUpdateFlowForResult(it, AppUpdateType.FLEXIBLE, this, REQUEST_CODE)
                Log.d(TAG, "Update available")
            } else {
                Log.d(TAG, "No Update available")
            }
        }
    }

    override fun onBackPressed() {
//        if (drawerLayout.isOpen) {
//            drawerLayout.close()
//        } else {
//            super.onBackPressed()
//        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        moveTaskToBack(true)
    }

    private  fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                &&  (it.clientVersionStalenessDays() ?: -1) >= 5
                && it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                appUpdateManager?.startUpdateFlowForResult(it, AppUpdateType.FLEXIBLE, this, REQUEST_CODE)
                Log.d(TAG, "Update available")
            } else {
                Log.d(TAG, "No Update available")
            }
        }
    }

    private val installUpdateListener : InstallStateUpdatedListener? =
        InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            Log.d(TAG, "Update is download")
            Helper.showSnakeBar(findViewById(android.R.id.content), "Update Successfully Downloaded")
            var snackbar = Snackbar.make(findViewById(android.R.id.content), "Update Successfully Downloaded",
            Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Reload App") {
                appUpdateManager?.completeUpdate()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d(TAG, "" + "Result Ok")
                    //  handle user's approval }
                }
                Activity.RESULT_CANCELED -> {
                    {
//if you want to request the update again just call checkUpdate()
                    }
                    Log.d(TAG, "" + "Result Cancelled")
                    //  handle user's rejection  }
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    //if you want to request the update again just call checkUpdate()
                    checkForUpdate()
                    Log.d(TAG, "" + "Update Failure")
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        installUpdateListener?.let { appUpdateManager?.unregisterListener(it) }
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

    fun showAlertDialog(show : Boolean?) {
        if (!show!!) {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.no_internet))
            builder.setMessage(getString(R.string.no_internet_msg))
            builder.setPositiveButton(getString(R.string.try_again)) { dialog, which ->
                finish()
            }
            builder.show()
        }
    }

    private fun inAppReview() {
        val reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo : ReviewInfo = task.getResult()
                val flow = reviewManager.launchReviewFlow(this@MainActivity, reviewInfo)
                flow.addOnCompleteListener {
                    Log.d(TAG, "Rating dialog shown")
                }
            } else {
                // There was some problem, log or handle the error code.
                Log.d(TAG, task.exception?.message!!)
            }
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        var shareMessage = "\nLet me recommend you this application\n\n"
        shareMessage =
            """
            ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
            
            
            """.trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }
}