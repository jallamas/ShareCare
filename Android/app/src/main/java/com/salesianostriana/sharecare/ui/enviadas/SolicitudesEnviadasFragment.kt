package com.salesianostriana.sharecare.ui.enviadas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.ui.disponibles.MyUsersConServicioRecyclerViewAdapter
import com.salesianostriana.sharecare.viewmodel.SolicitudViewModel
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import javax.inject.Inject

class SolicitudesEnviadasFragment : Fragment() {

    private var columnCount = 1
    @Inject
    lateinit var solicitudViewModel: SolicitudViewModel
    private lateinit var solicitudesEnviadasAdapter: MySolicitudesEnviadasRecyclerViewAdapter
    private var solicitudes: List<Solicitud> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).getApplicationComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_solicitudes_enviadas_list, container, false)
        solicitudesEnviadasAdapter = MySolicitudesEnviadasRecyclerViewAdapter()
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = solicitudesEnviadasAdapter
            }
        }

        solicitudViewModel.getSolicitudesEnviadas().observe(viewLifecycleOwner, Observer {
            if(it!=null){
                solicitudes = it
                Log.d("solicitudes enviadas",solicitudes.toString())
                solicitudesEnviadasAdapter.setData(solicitudes)
            }
        })
        return view
    }
}