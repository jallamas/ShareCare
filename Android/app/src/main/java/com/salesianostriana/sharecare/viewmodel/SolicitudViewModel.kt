package com.salesianostriana.sharecare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.repository.SolicitudRepository
import javax.inject.Inject

class SolicitudViewModel @Inject constructor(solicitudRepository: SolicitudRepository) : ViewModel() {

    val repository = solicitudRepository

    fun getSolicitudesEnviadas() : LiveData<List<Solicitud>> = repository.getSolicitudesEnviadas()

    fun getSolicitudesRecibidas() : LiveData<List<Solicitud>> = repository.getSolicitudesRecibidas()
}