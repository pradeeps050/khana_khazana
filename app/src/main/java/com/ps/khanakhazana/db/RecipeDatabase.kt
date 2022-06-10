package com.ps.khanakhazana.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KParameter

@Database(entities = [RecipeEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao() : RecipeDao
    companion object {
        @Volatile
        private var INSTANCE : RecipeDatabase? = null
        fun getDbInstance(context: Context) : RecipeDatabase? {
            synchronized(RecipeDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RecipeDatabase::class.java,
                        "recipedb")
                        .build()
                }
            }
            return INSTANCE
        }

    }
}