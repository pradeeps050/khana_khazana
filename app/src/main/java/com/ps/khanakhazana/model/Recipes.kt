package com.ps.khanakhazana.model

import com.google.gson.annotations.SerializedName

data class Recipes(@SerializedName("recipe" ) var recipe : ArrayList<Recipe> = arrayListOf()) {
}