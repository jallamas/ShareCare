package com.salesianostriana.sharecare.retrofit

import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MySharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareCareTokenInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val req: Request
        val token = MySharedPreferencesManager().getSharedPreferences().getString(Constantes.SHARED_PREFERENCES_TOKEN_KEY, "")

        val reqBuilder: Request.Builder = original.newBuilder().header("Authorization", "Bearer " + token)
        req = reqBuilder.build()

        return chain.proceed(req)
    }
}