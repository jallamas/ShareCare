package com.salesianostriana.sharecare.ui.disponibles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.Resource
import kotlinx.android.synthetic.main.fragment_user_disponible_list.*
import javax.inject.Inject

class DisponiblesFragment : Fragment() {

    private var columnCount = 1
    @Inject lateinit var userDisponiblesViewModel: usuariosDisponiblesViewModel
    private lateinit var usersAdapter: MyUsersConServicioRecyclerViewAdapter

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.disponiblesRecyclerView)

        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = usersAdapter
        }

        userDisponiblesViewModel.usuariosDisponibles.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { results ->
                        usersAdapter.setData(results)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { message ->
                        Log.e("Error", "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        val buscar = view.findViewById<ImageButton>(R.id.imageButtonBuscarLocalidad)

        buscar.setOnClickListener {
            userDisponiblesViewModel.getUsuariosDisponiblesPorLocalidad(editTextViewBuscarLocalidad.text.toString())
        }

        return view
    }

    private fun hideProgressBar() {
        usuariosDisponibles_ProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {

        usuariosDisponibles_ProgressBar.visibility = View.VISIBLE
    }
}