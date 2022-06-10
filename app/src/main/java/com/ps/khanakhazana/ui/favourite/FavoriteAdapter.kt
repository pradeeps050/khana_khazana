package com.ps.khanakhazana.ui.favourite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ps.khanakhazana.R
import com.ps.khanakhazana.db.RecipeEntity
import com.ps.khanakhazana.ui.cooking.CookingActivity
import com.ps.khanakhazana.utils.Helper

class FavoriteAdapter(private val recipeEntities: List<RecipeEntity>?, private val deleteClickListener: DeleteClickListener) : RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>() {

    val TAG = FavoriteAdapter::class.java.name
    interface DeleteClickListener {
        fun deleteRecipe(recipeEntity : RecipeEntity, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_items, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        recipeEntities?.let {
            holder.setData(recipeEntities.get(position),position)
        }

    }

    override fun getItemCount(): Int  {
        var count : Int = 0
        recipeEntities?.let {
            count = it?.size
        }
        return count
    }

    inner class FavViewHolder(private val item : View) : RecyclerView.ViewHolder(item) {
        private val txtTitle: TextView = item.findViewById(R.id.txt_fav_recipe_title)
        private val imageRecipe: ImageView = item.findViewById(R.id.img_fav_recipe)
        private val imgDelete: ImageView = item.findViewById(R.id.img_delete)
        private val favCard: CardView = item.findViewById(R.id.fav_card)

        fun setData(recipeEntity: RecipeEntity, position: Int) {
            txtTitle.text = recipeEntity.title
            Glide.with(item.context)
                .load(recipeEntity.imgUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageRecipe)
            imgDelete.setOnClickListener {
                deleteClickListener.deleteRecipe(recipeEntity, position)
            }
            favCard.setOnClickListener {
                var bundle = Bundle()
                bundle.putString(Helper.IMGURL, recipeEntity.imgUrl)
                bundle.putString(Helper.COLLECTION_PATH, recipeEntity.id)
                bundle.putString(Helper.TITLE, recipeEntity.title)
                bundle.putString(Helper.DOCUMENT_PATH, recipeEntity.doc)
                val intent = Intent(item.context, CookingActivity::class.java)
                intent.putExtras(bundle)
                item.context.startActivity(intent)
            }
        }
    }
}