package com.salesianostriana.sharecare.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import coil.api.load
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.requests.EditUserReq
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

class ProfileActivity @Inject constructor(): AppCompatActivity() {
    @Inject lateinit var userViewModel : UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        (applicationContext as MyApp).getApplicationComponent().inject(this)
        ButterKnife.bind(this)

        userViewModel.user.observe(this, Observer { response ->

            when(response) {

                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userDetail ->
                        Log.d("userDetail", userDetail.toString())
                        profile_fullname.setText(userDetail.fullName)
                        profile_localidad.setText(userDetail.localidad)
                        profile_phone.setText(userDetail.phone)
                        profile_image.load(userDetail.img)
                        checkBoxProfileServicio.isChecked=userDetail.servicioCuidados
                        editTextProfilePrecioHora.setText(userDetail.precioHora.toString())
                    }
                }
                is Resource.Error -> {
                    Log.d("Response", response.toString())
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(this, "Error al cargar el perfil", Toast.LENGTH_LONG).show()
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
                            finish()
                        }, 2000)

                    }
                    is Resource.Loading -> {
                        profile_progressbar.visibility = View.VISIBLE
                        profile_user_layout.visibility = View.INVISIBLE
                    }
                }
            })
        })

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