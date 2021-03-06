package com.salesianostriana.sharecare.common

import com.salesianostriana.sharecare.*
import com.salesianostriana.sharecare.retrofit.ShareCareClient
import com.salesianostriana.sharecare.ui.UserServicioDetalleActivity
import com.salesianostriana.sharecare.ui.profile.ProfileActivity
import com.salesianostriana.sharecare.ui.disponibles.DisponiblesFragment
import com.salesianostriana.sharecare.ui.enviadas.SolicitudEnviadaDetalleActivity
import com.salesianostriana.sharecare.ui.enviadas.SolicitudesEnviadasFragment
import com.salesianostriana.sharecare.ui.recibidas.SolicitudRecibidaDetalleActivity
import com.salesianostriana.sharecare.ui.recibidas.SolicitudesRecibidasFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ShareCareClient::class])
interface ApplicationComponent {
    fun inject(main: LoginActivity)
    fun inject(disponiblesFragment: DisponiblesFragment)
    fun inject(solicitudesEnviadasFragment: SolicitudesEnviadasFragment)
    fun inject(solicitudesRecibidasFragment: SolicitudesRecibidasFragment)
    fun inject(registerActivity: RegisterActivity)
    fun inject(profileActivity: ProfileActivity)
    fun inject(userServicioDetalleActivity: UserServicioDetalleActivity)
    fun inject(solicitudEnviadaDetalleActivity: SolicitudEnviadaDetalleActivity)
    fun inject(solicitudRecibidaDetalleActivity: SolicitudRecibidaDetalleActivity)
}