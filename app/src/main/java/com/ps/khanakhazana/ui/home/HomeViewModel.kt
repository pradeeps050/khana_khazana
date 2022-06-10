package com.ps.khanakhazana.ui.home

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.common.io.Resources.getResource
import com.ps.khanakhazana.R
import com.ps.khanakhazana.db.RecipeDatabase
import com.ps.khanakhazana.db.RecipeEntity
import com.ps.khanakhazana.model.Recipe
import com.ps.khanakhazana.model.Recipes
import com.ps.khanakhazana.network.RecipeService
import com.ps.khanakhazana.network.ServiceBuilder
import com.ps.khanakhazana.utils.Helper
import com.ps.khanakhazana.utils.RecipeApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val TAG = HomeViewModel::class.java.name
    private  val recipesLiveData : MutableLiveData<Recipes> by lazy {
        MutableLiveData<Recipes>().also {
            loadRecipes()
        }
    }

    private fun loadRecipes() {
        ServiceBuilder.buildService(RecipeService::class.java).getAllRecipes()
            .enqueue(object : Callback<Recipes> {

                override fun onResponse(call: Call<Recipes>, response: Response<Recipes>) {
                    if (response.isSuccessful) {
                        val recipes = response.body()!!
                        recipesLiveData.value = recipes
                    } else {
                        Log.d(TAG, "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<Recipes>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }
            })
    }

    fun getAllRecipes() : MutableLiveData<Recipes> {
        return recipesLiveData
    }

    fun insertFavRecipe(recipe: Recipe) {
        var recipeEntity = RecipeEntity(recipe.id, recipe.title, recipe.doc, recipe.img, true, false, null)
        CoroutineScope(Dispatchers.IO).launch {
            val db = RecipeDatabase.getDbInstance(RecipeApplication.applicationContext())
                ?.recipeDao()?.insert(recipeEntity)
        }
    }

    fun showSnakeBar(view: View, msg : String) {
        Helper.showSnakeBar(view, msg)
    }
}