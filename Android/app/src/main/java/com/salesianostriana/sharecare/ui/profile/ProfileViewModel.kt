package com.salesianostriana.sharecare.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.EditUserReq
import com.salesianostriana.sharecare.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    var user : MutableLiveData<Resource<User>> = MutableLiveData()
    var editUser : MutableLiveData<Resource<User>> = MutableLiveData()

    init {
        getPerfilUser()
    }

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