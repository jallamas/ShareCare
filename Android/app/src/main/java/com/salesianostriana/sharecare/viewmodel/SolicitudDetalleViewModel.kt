package com.salesianostriana.sharecare.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.models.requests.NewSolicitudReq
import com.salesianostriana.sharecare.repository.SolicitudRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class SolicitudDetalleViewModel @Inject constructor(val solicitudRepository: SolicitudRepository) : ViewModel(){

    var solicitud : MutableLiveData<Resource<Solicitud>> = MutableLiveData()
    var editSolicitud : MutableLiveData<Resource<Solicitud>> = MutableLiveData()

    fun getSolicitudId(solicitudId : String) = viewModelScope.launch {
        solicitud.value = Resource.Loading()
        delay(1000)
        val response = solicitudRepository.getSolicitudPorId(solicitudId)
        solicitud.value = handleResponse(response)
    }

    fun editarSolicitud(solicitudId : String, req : NewSolicitudReq) = viewModelScope.launch {
        editSolicitud.value = Resource.Loading()
        delay(1000)
        val response =solicitudRepository.editSolicitud(solicitudId, req)
        editSolicitud.value = handleResponse(response)
    }

    fun handleResponse(response: Response<Solicitud>): Resource<Solicitud> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error("Se produjo un error")
    }
}