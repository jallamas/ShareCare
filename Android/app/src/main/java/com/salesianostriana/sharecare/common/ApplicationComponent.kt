package com.salesianostriana.sharecare.common

import com.salesianostriana.sharecare.*
import com.salesianostriana.sharecare.retrofit.ShareCareClient
import com.salesianostriana.sharecare.ui.disponibles.DisponiblesFragment
import com.salesianostriana.sharecare.ui.enviadas.SolicitudesEnviadasFragment
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
}