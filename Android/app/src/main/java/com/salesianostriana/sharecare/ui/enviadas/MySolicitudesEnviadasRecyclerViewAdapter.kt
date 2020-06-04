package com.salesianostriana.sharecare.ui.enviadas

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.models.Solicitud
import kotlinx.android.synthetic.main.fragment_solicitudes_enviadas.view.*
import java.util.ArrayList

class MySolicitudesEnviadasRecyclerViewAdapter() : RecyclerView.Adapter<MySolicitudesEnviadasRecyclerViewAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener
    private var solicitudes: List<Solicitud> = ArrayList()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Solicitud
            var intent = Intent(MyApp.instance, SolicitudEnviadaDetalleActivity::class.java).apply{
                putExtra(Constantes.INTENT_DETAIL_KEY_ID,item.id.toString())
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            MyApp.instance.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_solicitudes_enviadas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = solicitudes[position]

        holder.descripcion.text = item.descripcion
        holder.fecha.text = item.fecha
        holder.nombreUserSolicitado.text = item.nombreSolicitado

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
        val descripcion = mView.textViewEnviadasDescripcion
        val fecha = mView.textViewEnviadasFecha
        val nombreUserSolicitado = mView.textViewEnviadasDestinatario
    }

}