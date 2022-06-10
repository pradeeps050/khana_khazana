package com.ps.khanakhazana.network

import com.ps.khanakhazana.model.Recipe
import com.ps.khanakhazana.model.Recipes
import retrofit2.Call
import retrofit2.http.GET

interface RecipeService {

    @GET("recipe.json")
    fun getAllRecipes() : Call<Recipes>
}