package com.salesianostriana.sharecare.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.NewSolicitudReq
import com.salesianostriana.sharecare.models.responses.NewSolicitudResponse
import com.salesianostriana.sharecare.repository.SolicitudRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class SolicitudViewModel @Inject constructor(val solicitudRepository: SolicitudRepository) : ViewModel() {

    var newSolicitud : MutableLiveData<Resource<NewSolicitudResponse>> = MutableLiveData()
    var deletedSolicitud :  MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun getSolicitudesEnviadas() : LiveData<List<Solicitud>> = solicitudRepository.getSolicitudesEnviadas()

    fun getSolicitudesRecibidas() : LiveData<List<Solicitud>> = solicitudRepository.getSolicitudesRecibidas()

    fun newSolicitud(destinatarioId:String, req : NewSolicitudReq) = viewModelScope.launch {
        newSolicitud.value = Resource.Loading()
        val resp = solicitudRepository.newSolicitud(destinatarioId,req)
        newSolicitud.value =handleNewSolicitudResponse(resp)
    }

    fun handleNewSolicitudResponse(response: Response<NewSolicitudResponse>): Resource<NewSolicitudResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error("Se produjo un error")
    }


    fun deleteSolicitud(solicitudId : String) = viewModelScope.launch {
        deletedSolicitud.value = Resource.Loading()
        delay(1000)
        val response = solicitudRepository.deleteSolicitud(solicitudId)
        deletedSolicitud.value = handleResponseDelete(response)
    }

    fun handleResponseDelete(response: Response<Void>): Resource<Unit> {
        if(response.isSuccessful) {
            return Resource.Success(Unit)
        }
        return Resource.Error("Se produjo un error")
    }
}