package com.salesianostriana.sharecare.ui.enviadas

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
import kotlinx.android.synthetic.main.fragment_solicitudes_enviadas_list.*
import javax.inject.Inject

class SolicitudesEnviadasFragment : Fragment() {

    private var columnCount = 1
    @Inject lateinit var solicitudesEnviadasViewModel: SolicitudesEnviadasViewModel
    private lateinit var solicitudesEnviadasAdapter: MySolicitudesEnviadasRecyclerViewAdapter

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
        val recyclerView = view.findViewById<RecyclerView>(R.id.enviadasRecyclerView)

        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = solicitudesEnviadasAdapter
        }

        solicitudesEnviadasViewModel.solicitudesEnviadas.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { results ->
                        solicitudesEnviadasAdapter.setData(results)
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
        solicitudesEnviadas_ProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {

        solicitudesEnviadas_ProgressBar.visibility = View.VISIBLE
    }
}