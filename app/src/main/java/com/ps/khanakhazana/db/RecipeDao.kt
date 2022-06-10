package com.ps.khanakhazana.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipeEntity: RecipeEntity)
    @Insert
    fun insertNotification(recipeEntity: RecipeEntity)
    @Query("SELECT * FROM recipe WHERE favourite = 1")
    fun getFavRecipes() : List<RecipeEntity>
    @Delete
    fun deleteRecipe(recipeEntity: RecipeEntity)
    @Query("SELECT * FROM recipe WHERE notification = 1")
    fun getNotification() : List<RecipeEntity>
}