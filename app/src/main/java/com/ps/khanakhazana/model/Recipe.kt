package com.ps.khanakhazana.model

import com.google.gson.annotations.SerializedName

data class Recipe (
    @SerializedName("id") var id: String = "",
    @SerializedName("title") var title: String? = null,
    @SerializedName("doc") var doc: String? = null,
    @SerializedName("img") var img: String? = null,
    @SerializedName("desc") var desc: String? = null,
    var favorite : Boolean = false
)