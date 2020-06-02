package com.salesianostriana.sharecare.ui.profile

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.requests.EditUserReq
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

class ProfileFragment @Inject constructor(var userViewModel : UserViewModel): Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        userViewModel.user.observe(this, Observer { response ->
            Log.d("Response", response.toString())
            when(response) {

                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userDetail ->
                        Log.d("userDetail", userDetail.toString())
                        profile_fullname.setText(userDetail.fullName)
                        profile_localidad.setText(userDetail.localidad)
                        profile_phone.setText(userDetail.phone)
                        checkBoxProfileServicio.isChecked=userDetail.servicioCuidados
                        editTextProfilePrecioHora.setText(userDetail.precioHora.toString())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(MyApp.instance, "Error al cargar el perfil", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        buttonProfileSave.setOnClickListener(View.OnClickListener{
            val req = EditUserReq(
                profile_fullname.text.toString(),
                profile_phone.text.toString(),
                profile_localidad.text.toString(),
                checkBoxProfileServicio.isChecked.toString(),
                editTextProfilePrecioHora.text.toString()
            )

            userViewModel.editUser(req)

            userViewModel.editUser.observe(this,Observer{
                when (it) {
                    is Resource.Error -> {
                        profile_progressbar.visibility = View.INVISIBLE
                        Handler().postDelayed({
                            profile_user_layout.visibility = View.VISIBLE
                            Toast.makeText(MyApp.instance,"Se produjo un error al guardar",Toast.LENGTH_SHORT).show()
                        }, 2000)
                    }
                    is Resource.Success -> {
                        profile_progressbar.visibility = View.GONE
                        profile_user_layout.visibility = View.INVISIBLE
                        Toast.makeText(
                            MyApp.instance,
                            "Guardado",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler().postDelayed({

                        }, 2000)

                    }
                    is Resource.Loading -> {
                        profile_progressbar.visibility = View.VISIBLE
                        profile_user_layout.visibility = View.INVISIBLE
                    }
                }
            })
        })

        return view
    }

    private fun hideProgressBar() {
        profile_progressbar.visibility = View.INVISIBLE
        profile_fullname.visibility = View.VISIBLE
        profile_localidad.visibility = View.VISIBLE
        profile_phone.visibility = View.VISIBLE
        checkBoxProfileServicio.visibility = View.VISIBLE
        editTextProfilePrecioHora.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        profile_progressbar.visibility = View.VISIBLE
        profile_fullname.visibility = View.INVISIBLE
        profile_localidad.visibility = View.INVISIBLE
        profile_phone.visibility = View.INVISIBLE
        checkBoxProfileServicio.visibility = View.VISIBLE
        editTextProfilePrecioHora.visibility = View.VISIBLE
    }

}