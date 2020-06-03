package com.salesianostriana.sharecare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.LoginReq
import com.salesianostriana.sharecare.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class UserAuthViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    var newUser : MutableLiveData<Resource<User>> = MutableLiveData()

    fun doLogin(req : LoginReq) : LiveData<User> = userRepository.login(req)

    fun registerNewUserPhoto(req : RequestBody, file : MultipartBody.Part) = viewModelScope.launch {
        newUser.value = Resource.Loading()
        val resp = userRepository.registerNewUserPhoto(req,file)
        newUser.value =handleResponse(resp)
    }

    fun registerNewUser(req : RequestBody) = viewModelScope.launch {
        newUser.value = Resource.Loading()
        val resp = userRepository.registerNewUser(req)
        newUser.value =handleResponse(resp)
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