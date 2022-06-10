package com.ps.khanakhazana.ui.cooking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.ads.MobileAds
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ps.khanakhazana.R
import com.ps.khanakhazana.utils.AddManager
import com.ps.khanakhazana.utils.Helper
import com.ps.khanakhazana.utils.NetworkStateManager

class CookingActivity : AppCompatActivity() {
    val TAG: String = CookingActivity::class.java.simpleName
    private lateinit var mainView: LinearLayout
    private lateinit var img: ImageView
    private lateinit var textIngredients: TextView
    private lateinit var textSteps: TextView
    private lateinit var loading: ProgressBar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private var collectionPath: String? = null
    private var docsPath: String? = null
    private var title: String? = null
    private val networkStateManager : NetworkStateManager = NetworkStateManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooking)
        MobileAds.initialize(this)
        initView()
        networkStateManager.getNetworkConnectivityStatus()?.observe(this) {
            Log.d(TAG, "Internet --> " + it.toString())
            //Helper.showNetworkMessage(it, mainView)
            showAlertDialog(it)
        }
    }

    private fun initView() {
        mainView = findViewById(R.id.main_view)
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        img = findViewById(R.id.img_cook)
        textIngredients = findViewById(R.id.txt_ingrediant)
        textSteps = findViewById(R.id.txt_steps)
        loading = findViewById(R.id.loading)
        mainView.visibility = View.GONE
        loading.visibility = View.VISIBLE

        MobileAds.initialize(this)
        AddManager().addSetUp(findViewById(R.id.adView))
        Log.d(TAG, "ACTION ---> " + intent.action)

        val bundle = intent.extras!!
        val imgUrl: String? = bundle.getString(Helper.IMGURL)
        title = bundle.getString(Helper.TITLE, null)
        collectionPath = bundle.getString(Helper.COLLECTION_PATH)
        docsPath = bundle.getString(Helper.DOCUMENT_PATH)
        Log.d(TAG, "title- " + title + "|"+ collectionPath +"|"+ docsPath+" | " + imgUrl)
        collapsingToolbarLayout.title = title
        Glide.with(this).
        load(imgUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(img)
        Log.d(TAG, title+"|"+"|"+collectionPath+"|"+docsPath+"|"+imgUrl)
    }

    override fun onResume() {
        super.onResume()
        loadSteps()
    }

    private fun loadSteps() {
        val db = Firebase.firestore
        collectionPath?.let {
            docsPath?.let { it1 ->
                db.collection(it).document(it1)
                    .get().addOnSuccessListener {
                        //Log.d(TAG, "Docs -->  " + it)
                        loading.visibility = View.GONE
                        mainView.visibility = View.VISIBLE
                        val obj1 = it.get(Helper.INGREDIENT)
                        obj1?.let {
                            val ingredients: List<String?> = obj1 as List<String>
                            //Log.d(TAG, " Ing --> "  + ing)
                            for (i in 0..ingredients.size - 1) {
                                textIngredients.append((i + 1).toString() + ".)  " + ingredients[i] + "\n\n")
                            }
                        }
                        val obj2 = it.get(Helper.STEPS)
                        obj2?.let {
                            val steps: List<String> = obj2 as List<String>
                            //Log.d(TAG, " steps --> "  + steps)
                            for (i in 0..steps.size - 1) {
                                textSteps.append((i + 1).toString() + ".)  " + steps[i] + "\n\n")
                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Exception -->  " + it.message)
                    }
            }
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

}

