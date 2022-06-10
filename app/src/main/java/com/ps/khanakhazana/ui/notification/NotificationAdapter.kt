package com.ps.khanakhazana.ui.notification

import android.content.Intent
import android.os.Bundle
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
import com.ps.khanakhazana.ui.favourite.FavoriteAdapter
import com.ps.khanakhazana.utils.Helper

class NotificationAdapter(private val recipeEntities: List<RecipeEntity>?, private val deleteClickListener: DeleteClickListener) : RecyclerView.Adapter<NotificationAdapter.NotificationVH>() {

    val TAG = NotificationFragment::class.java.name
    interface DeleteClickListener {
        fun deleteRecipe(recipeEntity : RecipeEntity, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_items, parent, false)
        return NotificationVH(view)
    }

    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
        recipeEntities?.let {
            holder.setData(recipeEntities.get(position),position)
        }
    }

    override fun getItemCount(): Int {
        var count : Int = 0
        recipeEntities?.let {
            count = it?.size
        }
        return count
    }

    inner class NotificationVH(private val viewItem : View) : RecyclerView.ViewHolder(viewItem) {
        private val txtTitle: TextView = viewItem.findViewById(R.id.txt_fav_recipe_title)
        private val dateTxt : TextView = viewItem.findViewById(R.id.txt_date)
        private val imageRecipe: ImageView = viewItem.findViewById(R.id.img_fav_recipe)
        private val imgDelete: ImageView = viewItem.findViewById(R.id.img_delete)
        private val favCard: CardView = viewItem.findViewById(R.id.fav_card)

        fun setData(recipeEntity: RecipeEntity, position: Int) {
            txtTitle.text = recipeEntity.title
            dateTxt.text = recipeEntity.time
            Glide.with(viewItem.context)
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
                val intent = Intent(viewItem.context, CookingActivity::class.java)
                intent.putExtras(bundle)
                viewItem.context.startActivity(intent)
            }
        }

    }
}