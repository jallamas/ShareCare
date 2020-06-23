package com.salesianostriana.sharecare.ui.recibidas

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
import com.salesianostriana.sharecare.common.Resource
import kotlinx.android.synthetic.main.fragment_solicitudes_recibidas_list.*
import javax.inject.Inject

class SolicitudesRecibidasFragment : Fragment() {

    private var columnCount = 1
    @Inject lateinit var solicitudesRecibidasViewModel: SolicitudesRecibidasViewModel
    lateinit var solicitudesRecibidasAdapter: MySolicitudesRecibidasRecyclerViewAdapter

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recibidasRecyclerView)

        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = solicitudesRecibidasAdapter
        }


        solicitudesRecibidasViewModel.solicitudesRecibidas.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { results ->
                        solicitudesRecibidasAdapter.setData(results)
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
        return view
    }

    private fun hideProgressBar() {
        solicitudesRecibidas_ProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {

        solicitudesRecibidas_ProgressBar.visibility = View.VISIBLE
    }

}