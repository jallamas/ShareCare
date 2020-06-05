package com.salesianostriana.sharecare.retrofit

import com.salesianostriana.sharecare.models.Solicitud
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.EditUserReq
import com.salesianostriana.sharecare.models.requests.LoginReq
import com.salesianostriana.sharecare.models.requests.NewSolicitudReq
import com.salesianostriana.sharecare.models.responses.LoginResponse
import com.salesianostriana.sharecare.models.responses.NewSolicitudResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ShareCareService {

    @POST("/auth/login")
    fun login(@Body request: LoginReq): Call<LoginResponse>

    @GET("/user/me")
    suspend fun verPerfil() : Response<User>

    @Multipart
    @POST("/user/")
    suspend fun registerUserPhoto(@Part("new") req : RequestBody,@Part file: MultipartBody.Part) : Response<User>

    @POST("/user/nophoto/")
    suspend fun registerUser(@Body req : RequestBody) : Response<User>

    @GET("/user/servicio/")
    suspend fun usersConServicio() : Response<List<User>>

    @GET("/user/servicio/buscar")
    suspend fun usersConServicioPorLocalidad(@Query("localidad") localidad : String) : Response<List<User>>

    @PUT("/user/")
    suspend fun editUser(@Body request : EditUserReq) : Response<User>

    @GET("/user/{userId}")
    suspend fun getUserPorId(@Path("userId") userId : String) : Response<User>

    @POST("/solicitud/{destinatarioId}")
    suspend fun newSolicitud(@Path("destinatarioId") destinatarioId : String, @Body req: NewSolicitudReq) : Response<NewSolicitudResponse>

    @GET("/solicitud/emitidas/")
    suspend fun solicitudesEnviadas() : Response<List<Solicitud>>

    @GET("/solicitud/recibidas/")
    suspend fun solicitudesRecibidas() : Response<List<Solicitud>>

    @GET("/solicitud/{solicitudId}")
    suspend fun getSolicitudPorId(@Path("solicitudId") solicitudId : String) : Response<Solicitud>

    @PUT("/solicitud/{solicitudId}")
    suspend fun editSolicitudPorId(@Path("solicitudId") solicitudId : String, @Body req: NewSolicitudReq) : Response<Solicitud>

    @DELETE("/solicitud/{solicitudId}")
    suspend fun deleteSolicitudPorId(@Path("solicitudId") solicitudId : String) : Response<Void>
}