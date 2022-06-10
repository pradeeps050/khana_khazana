package com.ps.khanakhazana.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ps.khanakhazana.R
import com.ps.khanakhazana.model.Recipe
import com.ps.khanakhazana.model.Recipes
import com.ps.khanakhazana.ui.cooking.CookingActivity
import com.ps.khanakhazana.utils.Helper

class HomeAdapter(var recipes : Recipes, val favClickLiserner: FavClickLiserner) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    interface FavClickLiserner {
        fun onClickFavIcon(recipe: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_list_items, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.setData(recipes.recipe.get(position))
    }

    override fun getItemCount(): Int {
        return recipes.recipe.size
    }

    inner class HomeViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        private val txtTitle: TextView = item.findViewById(R.id.txt_rep_title)
        private val txtDesc: TextView = item.findViewById(R.id.txt_desc)
        private val imageView: ImageView = item.findViewById(R.id.imageView)
        private val imageFav: ImageView = item.findViewById(R.id.img_fav)
        private val icFavoriteFilledImage = ResourcesCompat.getDrawable(item.context.resources,
            R.drawable.ic_favorite_bordered, null)

        fun setData(recipe: Recipe) {
            txtTitle.text = recipe.title
            txtDesc.text = recipe.desc
            imageFav.setImageDrawable(icFavoriteFilledImage)
            Glide.with(item.context).load(recipe.img)
                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView)
            imageView.setOnClickListener{
                var bundle = Bundle()
                bundle.putString(Helper.IMGURL, recipe.img)
                bundle.putString(Helper.COLLECTION_PATH, recipe.id)
                bundle.putString(Helper.TITLE, recipe.title)
                bundle.putString(Helper.DOCUMENT_PATH, recipe.doc)
                val intent = Intent(item.context, CookingActivity::class.java)
                intent.putExtras(bundle)
                item.context.startActivity(intent)
            }
            imageFav.setOnClickListener {
                imageFav.setImageResource(R.drawable.ic_baseline_favorite_24)
                favClickLiserner.onClickFavIcon(recipe)
            }
        }
    }
}