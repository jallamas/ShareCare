package com.salesianostriana.sharecare.ui.disponibles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class usuariosDisponiblesViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    var usuariosDisponibles : MutableLiveData <Resource<List<User>>> = MutableLiveData()

    init {
        getUsuariosDisponibles()
    }

    fun getUsuariosDisponibles() = viewModelScope.launch {
        usuariosDisponibles.value = Resource.Loading()
        val response = userRepository.usuariosConServicio()
        usuariosDisponibles.value = handleUsuariosDisponiblesResponse(response)
    }

    private fun handleUsuariosDisponiblesResponse(response: Response<List<User>>) : Resource<List<User>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}