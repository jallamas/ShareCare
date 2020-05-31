package com.salesianostriana.sharecare.ui.recibidas

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
import com.salesianostriana.sharecare.ui.enviadas.MySolicitudesEnviadasRecyclerViewAdapter
import com.salesianostriana.sharecare.viewmodel.SolicitudViewModel
import javax.inject.Inject

class SolicitudesRecibidasFragment : Fragment() {

    private var columnCount = 1
    @Inject
    lateinit var solicitudViewModel: SolicitudViewModel
    private lateinit var solicitudesRecibidasAdapter: MySolicitudesRecibidasRecyclerViewAdapter
    private var solicitudes: List<Solicitud> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as MyApp).getApplicationComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_solicitudes_recibidas_list, container, false)
        solicitudesRecibidasAdapter = MySolicitudesRecibidasRecyclerViewAdapter()
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = solicitudesRecibidasAdapter
            }
        }

        solicitudViewModel.getSolicitudesRecibidas().observe(viewLifecycleOwner, Observer {
            if(it!=null){
                solicitudes = it
                Log.d("solicitudes recibidas",solicitudes.toString())
                solicitudesRecibidasAdapter.setData(solicitudes)
            }
        })
        return view
    }

}