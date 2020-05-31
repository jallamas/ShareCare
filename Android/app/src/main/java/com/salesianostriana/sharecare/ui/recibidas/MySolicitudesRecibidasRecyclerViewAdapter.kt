package com.salesianostriana.sharecare.ui.recibidas

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.models.Solicitud
import kotlinx.android.synthetic.main.fragment_solicitudes_recibidas.view.*
import java.util.ArrayList

class MySolicitudesRecibidasRecyclerViewAdapter : RecyclerView.Adapter<MySolicitudesRecibidasRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener
    private var solicitudes: List<Solicitud> = ArrayList()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Solicitud
//            var intent = Intent(MyApp.instance, UserServicioDetalleActivity::class.java).apply{
//                putExtra(Constantes.INTENT_DETAIL_KEY_ID,item.id.toString())
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            }
//            MyApp.instance.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_solicitudes_recibidas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = solicitudes[position]

        holder.descripcion.text = item.descripcion
        holder.fecha.text = item.fecha
        holder.nombreUserSolicitante.text = item.nombreSolicitante
        holder.phoneSolicitante.text = item.phoneSolicitante

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = solicitudes.size

    fun setData(solicitudesList: List<Solicitud>){
        solicitudes = solicitudesList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val descripcion = mView.textViewRecibidasDescripcion
        val fecha = mView.textViewRecibidasFecha
        val nombreUserSolicitante = mView.textViewRecibidasSolicitante
        val phoneSolicitante = mView.textViewRecibidasPhoneSolicitante
    }

}