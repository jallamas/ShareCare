package com.salesianostriana.sharecare.repository

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.salesianostriana.sharecare.LoginActivity
import com.salesianostriana.sharecare.MainActivity
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.MySharedPreferencesManager
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.EditUserReq
import com.salesianostriana.sharecare.models.requests.LoginReq
import com.salesianostriana.sharecare.models.responses.LoginResponse
import com.salesianostriana.sharecare.retrofit.ShareCareService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(var shareCareService: ShareCareService){
    var user: MutableLiveData<User> = MutableLiveData()
    var users: MutableLiveData<List<User>> = MutableLiveData()

    fun login(req: LoginReq) : MutableLiveData<User>{
        val call : Call<LoginResponse>? = shareCareService.login(req)
        call?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    user.value = response.body()?.user
                    MySharedPreferencesManager().setStringValue(
                        Constantes.SHARED_PREFERENCES_TOKEN_KEY,
                        response.body()!!.token
                    )
                }else{
                    user.postValue(null)
                    MySharedPreferencesManager().removeStringValue(Constantes.SHARED_PREFERENCES_TOKEN_KEY)
                    Toast.makeText(MyApp.instance, "El usuario o contraseña no son correctos", Toast.LENGTH_SHORT).show()
                    val login: Intent = Intent(MyApp.instance, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    MyApp.instance.startActivity(login)
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(MyApp.instance, "Se produjo un error", Toast.LENGTH_SHORT).show()
            }
        })
        return user
    }

    suspend fun registerNewUserPhoto(req: RequestBody, file: MultipartBody.Part) = shareCareService.registerUserPhoto(req,file)

    suspend fun registerNewUser(req: RequestBody) = shareCareService.registerUser(req)

    suspend fun usuariosConServicio() = shareCareService.usersConServicio()

    suspend fun verPerfilUser() = shareCareService.verPerfil()

    suspend fun editUser(req : EditUserReq) = shareCareService.editUser(req)

    suspend fun getUserPorId(userId : String) = shareCareService.getUserPorId(userId)

}