package com.ps.khanakhazana.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps.khanakhazana.db.RecipeDao
import com.ps.khanakhazana.db.RecipeDatabase
import com.ps.khanakhazana.db.RecipeEntity
import com.ps.khanakhazana.ui.favourite.FavoriteViewModel
import com.ps.khanakhazana.utils.RecipeApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    val TAG = FavoriteViewModel::class.java.name
    private var liveNotification : MutableLiveData<List<RecipeEntity>> = MutableLiveData<List<RecipeEntity>>()
    private var liveDataRefesh : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val recipeDao : RecipeDao?
    init {
        val db = RecipeDatabase.getDbInstance(RecipeApplication.applicationContext())
        recipeDao = db?.recipeDao()
    }

    fun getNotification() : MutableLiveData<List<RecipeEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            val list = recipeDao?.getNotification()
            list?.let {
                liveNotification.postValue(list!!)
            }
        }
        return liveNotification
    }

    fun deleteRcipe(recipeEntity: RecipeEntity) : MutableLiveData<Boolean> {
        CoroutineScope(Dispatchers.IO).launch {
            recipeDao?.deleteRecipe(recipeEntity)
            liveDataRefesh.postValue(true)
        }
        return liveDataRefesh
    }
}