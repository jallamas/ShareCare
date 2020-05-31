package com.salesianostriana.sharecare.retrofit

import com.salesianostriana.sharecare.common.Constantes
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ShareCareClient {

    @Singleton
    @Provides
    @Named("url")
    fun provideBaseUrl(): String = Constantes.API_BASE_URL

    @Singleton
    @Provides
    fun createClient(@Named("url") baseUrl: String): ShareCareService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).addInterceptor(ShareCareTokenInterceptor()).build())
            .build()
            .create(ShareCareService::class.java)
    }
}