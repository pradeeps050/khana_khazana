package com.ps.khanakhazana.ui.notification

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

class NotificationFragment : Fragment(), NotificationAdapter.DeleteClickListener {

    private val TAG : String = NotificationFragment::class.java.simpleName

    private lateinit var notificatiobRV: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var viewModel: NotificationViewModel
    private lateinit var notifications : List<RecipeEntity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val  view = inflater.inflate(R.layout.fragment_notification, container, false)
        notificatiobRV = view.findViewById(R.id.notification_rv)
        return view
    }

    override fun onResume() {
        super.onResume()
        loadNotification()
    }

    private fun loadNotification() {
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        viewModel.getNotification().observe(this, Observer {
            it?.let {
                notifications = it
                notificationAdapter = NotificationAdapter(it, this)
                val layoutManager = LinearLayoutManager(requireContext())
                layoutManager.orientation = RecyclerView.VERTICAL
                notificatiobRV.adapter = notificationAdapter
                notificatiobRV.setHasFixedSize(true)
                notificatiobRV.layoutManager = layoutManager
            }
        })
    }

    override fun deleteRecipe(recipeEntity: RecipeEntity, position: Int) {
        viewModel.deleteRcipe(recipeEntity).observe(this, Observer {
            if (it) {
                Log.d(TAG, "  ---> IT " + it)
                loadNotification()
            }
        })
    }
}