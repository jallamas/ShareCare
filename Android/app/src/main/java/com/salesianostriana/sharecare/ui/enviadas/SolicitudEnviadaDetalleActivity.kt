package com.salesianostriana.sharecare.ui.enviadas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.salesianostriana.sharecare.MainActivity
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.requests.NewSolicitudReq
import com.salesianostriana.sharecare.viewmodel.SolicitudDetalleViewModel
import com.salesianostriana.sharecare.viewmodel.SolicitudViewModel
import kotlinx.android.synthetic.main.activity_solicitud_enviada_detalle.*
import javax.inject.Inject

class SolicitudEnviadaDetalleActivity : AppCompatActivity() {

    @Inject lateinit var solicitudDetalleViewModel: SolicitudDetalleViewModel
    @Inject lateinit var solicitudViewModel: SolicitudViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitud_enviada_detalle)
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
                        textViewSolicitudDetalleFecha.setText(solicitudDetail.fecha)
                        textViewSolicitudDetalleDestinatario.setText(solicitudDetail.nombreSolicitado)
                        editTextSolicitudDetalleComentario.setText(solicitudDetail.descripcion)
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

        buttonSolicitudDetalleSave.setOnClickListener(View.OnClickListener {
            val req = NewSolicitudReq (editTextSolicitudDetalleComentario.text.toString())

            solicitudDetalleViewModel.editarSolicitud(id,req)

            solicitudDetalleViewModel.editSolicitud.observe(this,Observer{
                when (it) {
                    is Resource.Error -> {
                        detalleSolicitudEnviada_progressbar.visibility = View.INVISIBLE
                        Handler().postDelayed({
                            detalleSolicitudEnviada_layout.visibility = View.VISIBLE
                            Toast.makeText(MyApp.instance,"Se produjo un error al guardar",Toast.LENGTH_SHORT).show()
                        }, 2000)
                    }
                    is Resource.Success -> {
                        detalleSolicitudEnviada_progressbar.visibility = View.GONE
                        detalleSolicitudEnviada_layout.visibility = View.INVISIBLE
                        Toast.makeText(
                            MyApp.instance,
                            "Solicitud guardada",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler().postDelayed({
                            goToFragment()
                            finish()
                        }, 2000)

                    }
                    is Resource.Loading -> {
                        detalleSolicitudEnviada_progressbar.visibility = View.VISIBLE
                        detalleSolicitudEnviada_layout.visibility = View.INVISIBLE
                    }
                }
            })

        })

        buttonSolicitudEnviadaDetalleEliminar.setOnClickListener(View.OnClickListener {
            solicitudViewModel.deleteSolicitud(id!!)

            solicitudViewModel.deletedSolicitud.observe(this, Observer {
                when (it) {
                    is Resource.Error -> {
                        detalleSolicitudEnviada_progressbar.visibility = View.INVISIBLE
                        Handler().postDelayed({
                            detalleSolicitudEnviada_layout.visibility = View.VISIBLE
                            Toast.makeText(MyApp.instance,"Se produjo un error al eliminar",Toast.LENGTH_SHORT).show()
                        }, 2000)
                    }
                    is Resource.Success -> {
                        detalleSolicitudEnviada_progressbar.visibility = View.GONE
                        detalleSolicitudEnviada_layout.visibility = View.INVISIBLE
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
                        detalleSolicitudEnviada_progressbar.visibility = View.VISIBLE
                        detalleSolicitudEnviada_layout.visibility = View.INVISIBLE
                    }
                }
            })
        })


    }

    fun goToFragment(){
        var i:Intent = Intent(MyApp.instance, MainActivity::class.java).apply {
            putExtra("FromSolicitud", "1");
            flags = Intent.FLAG_ACTIVITY_NEW_TASK}
        startActivity(i)
    }

    @Override
    fun OnBackPressed (){
        startActivity(Intent(MyApp.instance, SolicitudesEnviadasFragment::class.java).apply {flags = Intent.FLAG_ACTIVITY_NEW_TASK})
    }

    private fun hideProgressBar() {
        detalleSolicitudEnviada_progressbar.visibility = View.INVISIBLE
        textViewSolicitudDetalleFecha.visibility = View.VISIBLE
        textViewSolicitudDetalleDestinatario.visibility = View.VISIBLE
        editTextSolicitudDetalleComentario.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        detalleSolicitudEnviada_progressbar.visibility = View.VISIBLE
        textViewSolicitudDetalleFecha.visibility = View.INVISIBLE
        textViewSolicitudDetalleDestinatario.visibility = View.INVISIBLE
        editTextSolicitudDetalleComentario.visibility = View.INVISIBLE
    }
}