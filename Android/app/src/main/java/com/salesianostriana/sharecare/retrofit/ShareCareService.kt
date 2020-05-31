package com.salesianostriana.sharecare.retrofit

import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.LoginReq
import com.salesianostriana.sharecare.models.responses.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ShareCareService {

    @POST("/auth/login")
    fun login(@Body request: LoginReq): Call<LoginResponse>

    @Multipart
    @POST("/user/")
    suspend fun registerUser(
        @Part("new") req : RequestBody,
        @Part file: MultipartBody.Part
    ) : Response<User>


    @GET("/user/servicio/")
    fun usersConServicio() : Call<List<User>>

    @GET("/solicitud/emitidas/")
    fun solicitudesEnviadas() : Call<List<Solicitud>>

    @GET("/solicitud/recibidas/")
    fun solicitudesRecibidas() : Call<List<Solicitud>>

}