package com.ps.khanakhazana.ui.favourite

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ps.khanakhazana.db.RecipeDao
import com.ps.khanakhazana.db.RecipeDatabase
import com.ps.khanakhazana.db.RecipeEntity
import com.ps.khanakhazana.utils.RecipeApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = FavoriteViewModel::class.java.name
    private var liveRecipes : MutableLiveData<List<RecipeEntity>> = MutableLiveData<List<RecipeEntity>>()
    private var liveDataRefesh : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val recipeDao : RecipeDao?
    init {
        val db = RecipeDatabase.getDbInstance(application)
        recipeDao = db?.recipeDao()
    }

    fun getFavRecipes() : MutableLiveData<List<RecipeEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            val list = recipeDao?.getFavRecipes()
            list?.let {
                liveRecipes.postValue(list!!)
            }
        }
        return liveRecipes
    }

    fun deleteRcipe(recipeEntity: RecipeEntity) : MutableLiveData<Boolean>{
        CoroutineScope(Dispatchers.IO).launch {
            val db = RecipeDatabase.getDbInstance(RecipeApplication.applicationContext())
                ?.recipeDao()?.deleteRecipe(recipeEntity)
            liveDataRefesh.postValue(true)
        }
        return liveDataRefesh
    }
}