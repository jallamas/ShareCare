package com.salesianostriana.sharecare.ui.recibidas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.repository.SolicitudRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class SolicitudesRecibidasViewModel @Inject constructor(private val solicitudRepository: SolicitudRepository) : ViewModel(){

    var solicitudesRecibidas : MutableLiveData<Resource<List<Solicitud>>> = MutableLiveData()

    init {
        getSolicitudesRecibidas()
    }

    fun getSolicitudesRecibidas()= viewModelScope.launch {
        solicitudesRecibidas.value = Resource.Loading()
        val response = solicitudRepository.getSolicitudesRecibidas()
        solicitudesRecibidas.value = handleSolicitudesRecibidasResponse(response)
    }

    private fun handleSolicitudesRecibidasResponse(response: Response<List<Solicitud>>) : Resource<List<Solicitud>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}