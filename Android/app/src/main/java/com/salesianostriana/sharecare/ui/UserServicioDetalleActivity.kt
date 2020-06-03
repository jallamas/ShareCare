package com.salesianostriana.sharecare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import coil.api.load
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.requests.NewSolicitudReq
import com.salesianostriana.sharecare.viewmodel.SolicitudViewModel
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_user_servicio_detalle.*
import javax.inject.Inject

class UserServicioDetalleActivity @Inject constructor(): AppCompatActivity() {

    @Inject lateinit var userViewModel : UserViewModel
    @Inject lateinit var solicitudViewModel: SolicitudViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_servicio_detalle)
        (this.applicationContext as MyApp).getApplicationComponent().inject(this)
        ButterKnife.bind(this)

        val extras: Bundle? = intent.extras

        val id = extras?.getString(Constantes.INTENT_DETAIL_KEY_ID)

        userViewModel.getUserPorId(id!!)

        userViewModel.user.observe(this, Observer { response ->

            when(response) {

                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userDetail ->
                        textViewDetalleUserFullname.setText(userDetail.fullName)
                        textViewDetalleUserlocalidad.setText(userDetail.localidad)
                        imageViewDetalleUserImage.load(userDetail.img)
                        textViewDetalleUserPrecioHora.setText(userDetail.precioHora.toString())
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

        buttonDetalleUserNuevaSolicitud.setOnClickListener(View.OnClickListener {
            val req = NewSolicitudReq (editTextDetalleUserComentario.text.toString())

            solicitudViewModel.newSolicitud(id,req)

            solicitudViewModel.newSolicitud.observe(this,Observer{
                when (it) {
                    is Resource.Error -> {
                        detalleUser_progressbar.visibility = View.INVISIBLE
                        Handler().postDelayed({
                            detalleUser_layout.visibility = View.VISIBLE
                            Toast.makeText(MyApp.instance,"Se produjo un error al guardar",Toast.LENGTH_SHORT).show()
                        }, 2000)
                    }
                    is Resource.Success -> {
                        detalleUser_progressbar.visibility = View.GONE
                        detalleUser_layout.visibility = View.INVISIBLE
                        Toast.makeText(
                            MyApp.instance,
                            "Guardado",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler().postDelayed({
                            finish()
                        }, 2000)

                    }
                    is Resource.Loading -> {
                        detalleUser_progressbar.visibility = View.VISIBLE
                        detalleUser_layout.visibility = View.INVISIBLE
                    }
                }
            })


        })

    }

    private fun hideProgressBar() {
        detalleUser_progressbar.visibility = View.INVISIBLE
        textViewDetalleUserFullname.visibility = View.VISIBLE
        textViewDetalleUserlocalidad.visibility = View.VISIBLE
        imageViewDetalleUserImage.visibility = View.VISIBLE
        textViewDetalleUserPrecioHora.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        detalleUser_progressbar.visibility = View.VISIBLE
        textViewDetalleUserFullname.visibility = View.INVISIBLE
        textViewDetalleUserlocalidad.visibility = View.INVISIBLE
        imageViewDetalleUserImage.visibility = View.INVISIBLE
        textViewDetalleUserPrecioHora.visibility = View.VISIBLE
    }
}