package com.salesianostriana.sharecare.ui.disponibles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import javax.inject.Inject

class DisponiblesFragment : Fragment() {

    private var columnCount = 1
    @Inject
    lateinit var userViewModel: UserViewModel
    private lateinit var usersAdapter: MyUsersConServicioRecyclerViewAdapter
    private var users: List<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).getApplicationComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_disponible_list, container, false)
        usersAdapter = MyUsersConServicioRecyclerViewAdapter()
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = usersAdapter
            }
        }

        userViewModel.getUsersConServicio().observe(viewLifecycleOwner, Observer {
            if(it!=null){
                users = it
                Log.d("usuarios",users.toString())
                usersAdapter.setData(users)
            }
        })
        return view
    }
}