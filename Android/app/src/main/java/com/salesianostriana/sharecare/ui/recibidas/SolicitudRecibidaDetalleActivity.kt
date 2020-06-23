package com.salesianostriana.sharecare.ui.recibidas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.salesianostriana.sharecare.MainActivity
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.viewmodel.SolicitudDetalleViewModel
import com.salesianostriana.sharecare.viewmodel.SolicitudViewModel
import kotlinx.android.synthetic.main.activity_solicitud_recibida_detalle.*
import javax.inject.Inject

class SolicitudRecibidaDetalleActivity : AppCompatActivity() {

    @Inject lateinit var solicitudDetalleViewModel: SolicitudDetalleViewModel
    @Inject lateinit var solicitudesRecibidasViewModel: SolicitudesRecibidasViewModel
    @Inject lateinit var solicitudViewModel: SolicitudViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitud_recibida_detalle)

        (applicationContext as MyApp).getApplicationComponent().inject(this)
        ButterKnife.bind(this)

        val extras: Bundle? = intent.extras
        val id = extras?.getString(Constantes.INTENT_DETAIL_KEY_ID)

        solicitudDetalleViewModel.getSolicitudId(id!!)

        solicitudDetalleViewModel.solicitud.observe(this, Observer { response ->

            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { solicitudDetail ->
                        textViewSolicitudRecibidaDetalleFecha.setText(solicitudDetail.fecha)
                        textViewSolicitudRecibidaDetalleRemitente.setText(solicitudDetail.nombreSolicitante)
                        textViewSolicitudRecibidaDetallePhoneRemitente.setText(solicitudDetail.phoneSolicitante)
                        textViewSolicitudRecibidaDetalleComentario.setText(solicitudDetail.descripcion)
                    }
                }
                is Resource.Error -> {
                    Log.d("Response", response.toString())
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(this, "Error al cargar el usuario", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        buttonSolicitudRecibidaDetalleEliminar.setOnClickListener(View.OnClickListener {
            solicitudViewModel.deleteSolicitud(id)

            solicitudViewModel.deletedSolicitud.observe(this, Observer {
                when (it) {
                    is Resource.Error -> {
                        detalleSolicitudRecibida_progressbar.visibility = View.INVISIBLE
                        Handler().postDelayed({
                            detalleSolicitudRecibida_layout.visibility = View.VISIBLE
                            Toast.makeText(MyApp.instance,"Se produjo un error al eliminar",Toast.LENGTH_SHORT).show()
                        }, 2000)
                    }
                    is Resource.Success -> {
                        detalleSolicitudRecibida_progressbar.visibility = View.GONE
                        detalleSolicitudRecibida_layout.visibility = View.INVISIBLE
                        Toast.makeText(
                            MyApp.instance,
                            "Solicitud eliminada",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler().postDelayed({
                            goToFragment()
                            finish()
                        }, 2000)

                    }
                    is Resource.Loading -> {
                        detalleSolicitudRecibida_progressbar.visibility = View.VISIBLE
                        detalleSolicitudRecibida_layout.visibility = View.INVISIBLE
                    }
                }
            })

        })
    }

    fun goToFragment(){
        var i: Intent = Intent(MyApp.instance, MainActivity::class.java).apply {
            putExtra("FromSolicitud", "2");
            flags = Intent.FLAG_ACTIVITY_NEW_TASK}
        startActivity(i)
    }

    private fun hideProgressBar() {
        detalleSolicitudRecibida_progressbar.visibility = View.INVISIBLE
        textViewSolicitudRecibidaDetalleFecha.visibility = View.VISIBLE
        textViewSolicitudRecibidaDetalleRemitente.visibility = View.VISIBLE
        textViewSolicitudRecibidaDetallePhoneRemitente.visibility = View.VISIBLE
        textViewSolicitudRecibidaDetalleComentario.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        detalleSolicitudRecibida_progressbar.visibility = View.VISIBLE
        textViewSolicitudRecibidaDetalleFecha.visibility = View.INVISIBLE
        textViewSolicitudRecibidaDetalleRemitente.visibility = View.INVISIBLE
        textViewSolicitudRecibidaDetallePhoneRemitente.visibility = View.INVISIBLE
        textViewSolicitudRecibidaDetalleComentario.visibility = View.VISIBLE
    }
}