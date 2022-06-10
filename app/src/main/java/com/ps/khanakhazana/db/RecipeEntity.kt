package com.ps.khanakhazana.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey
    var id : String,
    @ColumnInfo(name = "title")
    var title : String?,
    @ColumnInfo(name = "doc")
    var doc : String?,
    @ColumnInfo(name = "imageurl")
    var imgUrl : String?,
    @ColumnInfo(name = "favourite")
    var favourite : Boolean,
    @ColumnInfo(name = "notification")
    var notification : Boolean,
    @ColumnInfo(name = "time")
    var time : String?
)