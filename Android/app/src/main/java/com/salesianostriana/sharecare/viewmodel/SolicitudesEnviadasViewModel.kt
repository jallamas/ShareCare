package com.salesianostriana.sharecare.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.repository.SolicitudRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class SolicitudesEnviadasViewModel @Inject constructor(private val solicitudRepository: SolicitudRepository) : ViewModel() {

    var solicitudesEnviadas : MutableLiveData<Resource<List<Solicitud>>> = MutableLiveData()

    init {
        getSolicitudesEnviadas()
    }

    fun getSolicitudesEnviadas()= viewModelScope.launch {
        solicitudesEnviadas.value = Resource.Loading()
        val response = solicitudRepository.getSolicitudesEnviadas()
        solicitudesEnviadas.value = handleSolicitudesEnviadasResponse(response)
    }

    private fun handleSolicitudesEnviadasResponse(response: Response<List<Solicitud>>) : Resource<List<Solicitud>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}