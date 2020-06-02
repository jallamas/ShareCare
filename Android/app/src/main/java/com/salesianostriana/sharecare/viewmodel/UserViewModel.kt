package com.salesianostriana.sharecare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.EditUserReq
import com.salesianostriana.sharecare.models.requests.LoginReq
import com.salesianostriana.sharecare.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class UserViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    var newUser : MutableLiveData<Resource<User>> = MutableLiveData()
    var user : MutableLiveData<Resource<User>> = MutableLiveData()
    var editUser : MutableLiveData<Resource<User>> = MutableLiveData()

    fun doLogin(req : LoginReq) : LiveData<User> = userRepository.login(req)

    fun registerNewUser(req : RequestBody, file : MultipartBody.Part) = viewModelScope.launch {
        newUser.value = Resource.Loading()
        val resp = userRepository.registerNewUser(req,file)
        newUser.value =handleResponse(resp)
    }

    fun getUsersConServicio() :LiveData<List<User>> = userRepository.usuariosConServicio()

    fun getPerfilUser() =viewModelScope.launch {
        user.value = Resource.Loading()
        delay(1000)
        val response =userRepository.verPerfilUser()
        user.value = handleResponse(response)
    }

    fun editUser(req : EditUserReq) = viewModelScope.launch {
        editUser.value = Resource.Loading()
        delay(1000)
        val response =userRepository.editUser(req)
        editUser.value = handleResponse(response)
    }

    fun handleResponse(response: Response<User>): Resource<User> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error("Se produjo un error")
    }
}