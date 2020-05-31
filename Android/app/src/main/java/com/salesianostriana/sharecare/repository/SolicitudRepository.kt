package com.salesianostriana.sharecare.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.retrofit.ShareCareService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolicitudRepository @Inject constructor(var shareCareService: ShareCareService){
    var solicitud: MutableLiveData<Solicitud> = MutableLiveData()
    var newSolicitud: MutableLiveData<Solicitud> = MutableLiveData()
    var solicitudes: MutableLiveData<List<Solicitud>> = MutableLiveData()

    fun getSolicitudesEnviadas() : MutableLiveData<List<Solicitud>> {
        val call: Call<List<Solicitud>>? = shareCareService.solicitudesEnviadas()
        call?.enqueue(object : Callback<List<Solicitud>> {
            override fun onResponse(call: Call<List<Solicitud>> , response: Response<List<Solicitud>>) {
                if (response.isSuccessful) {
                    solicitudes.value = response.body()
                } else {
                    Toast.makeText(MyApp.instance,"No hay solicitudes",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Solicitud>>, t: Throwable) {
                Toast.makeText(MyApp.instance, "Se produjo un error", Toast.LENGTH_SHORT).show()
            }
        })
        return solicitudes
    }

    fun getSolicitudesRecibidas() : MutableLiveData<List<Solicitud>> {
        val call: Call<List<Solicitud>>? = shareCareService.solicitudesRecibidas()
        call?.enqueue(object : Callback<List<Solicitud>> {
            override fun onResponse(call: Call<List<Solicitud>> , response: Response<List<Solicitud>>) {
                if (response.isSuccessful) {
                    solicitudes.value = response.body()
                } else {
                    Toast.makeText(MyApp.instance,"No hay solicitudes",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Solicitud>>, t: Throwable) {
                Toast.makeText(MyApp.instance, "Se produjo un error", Toast.LENGTH_SHORT).show()
            }
        })
        return solicitudes
    }
}