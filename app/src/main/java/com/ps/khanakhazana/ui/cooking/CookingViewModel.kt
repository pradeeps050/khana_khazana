package com.ps.khanakhazana.ui.cooking

import android.util.Log
import android.view.View
import android.widget.MultiAutoCompleteTextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CookingViewModel(val collection: String, val docs: String) : ViewModel() {
    val TAG = CookingViewModel::class.java.name

    private val document : MutableLiveData<DocumentSnapshot> by lazy {
        MutableLiveData<DocumentSnapshot>().also {
            loadDocument()
        }
    }

    private fun loadDocument() {
        val db = Firebase.firestore
        db.collection(collection).document(docs)
            .get().addOnSuccessListener {
                //Log.d(TAG, "Docs -->  " + it)
                document.apply {
                    document.postValue(it)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Exception -->  " + it.message)
            }
    }

    fun getCookingSteps() : MutableLiveData<DocumentSnapshot> {
        return document
    }
}