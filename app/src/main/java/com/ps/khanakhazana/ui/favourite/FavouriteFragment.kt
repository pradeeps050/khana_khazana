package com.ps.khanakhazana.ui.favourite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ps.khanakhazana.R
import com.ps.khanakhazana.db.RecipeEntity

class FavouriteFragment : Fragment(), FavoriteAdapter.DeleteClickListener {
    val TAG = FavouriteFragment::class.java.simpleName

    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favoriteAdapter : FavoriteAdapter
    private lateinit var favViewModel : FavoriteViewModel
    private lateinit var recipes : List<RecipeEntity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        favRecyclerView = view.findViewById(R.id.favorite_rv)
        return view
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteRecipe()

    }

    private fun loadFavoriteRecipe() {
        favViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        favViewModel.getFavRecipes().observe(this, Observer {
            it?.let {
                recipes = it
                favoriteAdapter = FavoriteAdapter(it, this)
                val layoutManager = LinearLayoutManager(requireContext())
                layoutManager.orientation = RecyclerView.VERTICAL
                favRecyclerView.adapter = favoriteAdapter
                favRecyclerView.setHasFixedSize(true)
                favRecyclerView.layoutManager = layoutManager
            }
        })
    }

    override fun deleteRecipe(recipeEntity: RecipeEntity, position : Int) {
        favViewModel.deleteRcipe(recipeEntity).observe(this, Observer {
            if (it) {
                Log.d(TAG, "  ---> IT " + it)
                loadFavoriteRecipe()
            }
        })
    }
}