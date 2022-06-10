package com.ps.khanakhazana.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ps.khanakhazana.R
import com.ps.khanakhazana.model.Recipe
import com.ps.khanakhazana.model.Recipes
import com.ps.khanakhazana.network.RecipeService
import com.ps.khanakhazana.network.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), HomeAdapter.FavClickLiserner {

    val TAG : String = "HomeFragment"
    private lateinit var recyclerView: RecyclerView
    private lateinit var progress : ProgressBar
    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View =  inflater.inflate(R.layout.fragment_home, container, false)
        progress = view.findViewById(R.id.progress)
        progress.visibility = View.VISIBLE
        recyclerView = view.findViewById(R.id.recipe_rv)
        return view
    }

    override fun onResume() {
        super.onResume()
        loadRecipe()
    }
    private fun loadRecipe() {
        homeViewModel.getAllRecipes().observe(this, Observer<Recipes> {
            it?.also {
                progress.visibility = View.GONE
                val adapter = HomeAdapter(it, this)
                recyclerView.adapter = adapter
            }
        })
    }

    override fun onClickFavIcon(recipe: Recipe) {
        homeViewModel.insertFavRecipe(recipe)
    }
}