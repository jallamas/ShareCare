package com.salesianostriana.sharecare.repository

import androidx.lifecycle.MutableLiveData
import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.models.requests.NewSolicitudReq
import com.salesianostriana.sharecare.retrofit.ShareCareService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolicitudRepository @Inject constructor(var shareCareService: ShareCareService){

    var solicitud: MutableLiveData<Solicitud> = MutableLiveData()

    suspend fun getSolicitudesEnviadas() = shareCareService.solicitudesEnviadas()

    suspend fun getSolicitudesRecibidas() = shareCareService.solicitudesRecibidas()

    suspend fun newSolicitud(destinatarioId:String, req: NewSolicitudReq) = shareCareService.newSolicitud(destinatarioId,req)

    suspend fun getSolicitudPorId(solicitudId:String) = shareCareService.getSolicitudPorId(solicitudId)

    suspend fun editSolicitud(solicitudId: String, req: NewSolicitudReq) = shareCareService.editSolicitudPorId(solicitudId,req)

    suspend fun deleteSolicitud(solicitudId: String) = shareCareService.deleteSolicitudPorId(solicitudId)
}