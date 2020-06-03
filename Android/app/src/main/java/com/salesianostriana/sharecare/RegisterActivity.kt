package com.salesianostriana.sharecare

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import coil.api.load
import com.google.gson.Gson
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.Resource
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.models.requests.RegisterReq
import com.salesianostriana.sharecare.viewmodel.UserAuthViewModel
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.activity_register.view.register_username
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var userAuthViewModel : UserAuthViewModel

    var uriPicture: Uri? = null
    var file: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        (applicationContext as MyApp).getApplicationComponent().inject(this)
        ButterKnife.bind(this)
        register_image.visibility = View.INVISIBLE
        setUploadButton()
        setSaveButton()
    }

    fun setUploadButton() {
        button_register_img_upload.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uriPicture = data?.data
            Log.d("URI", "LA URI ES $uriPicture")
            register_image.load(uriPicture)
            register_image.visibility = View.VISIBLE
            file = createMultipart(uriPicture)
        }
    }

    fun createMultipart(uri: Uri?): MultipartBody.Part? {
        var img: MultipartBody.Part? = null
        try {
            if (uri != null) {
                val imgPart =
                    this.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }
                        ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                img = MultipartBody.Part.createFormData("file", "file", imgPart!!)
            }
        } catch (e: IOException) {
            Log.d("EXCEPTION UPLOAD", e.message);
        }
        return img
    }

    fun setSaveButton() {
        buttonRegisterSave.setOnClickListener {
            if(register_username.text.toString().isNotEmpty()&&register_fullname.text.toString().isNotEmpty()&&register_phone.text.toString().isNotEmpty()&&register_localidad.text.toString().isNotEmpty()
                &&editTextRegisterPassword.text.toString().isNotEmpty()&&editTextRegisterPassword2.text.toString().isNotEmpty()) {

                if (editTextRegisterPassword.text.toString().equals(editTextRegisterPassword2.text.toString())) {
                    val request = RegisterReq(register_username.text.toString(),register_fullname.text.toString(),register_localidad.text.toString(),editTextRegisterPassword.text.toString(),
                        editTextRegisterPassword2.text.toString(),register_phone.text.toString(),editTextPrecioHora.text.toString(),checkBoxServicio.isChecked.toString())

                    val requestBody = Gson().toJson(request).toRequestBody("application/json".toMediaTypeOrNull())

                   if(file==null) {
                       userAuthViewModel.registerNewUser(requestBody)
                   }else {
                       userAuthViewModel.registerNewUserPhoto(requestBody, file!!)
                   }
                    userAuthViewModel.newUser.observe(this, Observer {
                        when (it) {
                            is Resource.Error -> {
                                register_progressbar.visibility = View.INVISIBLE
                                Handler().postDelayed({
                                    register_user_layout.visibility = View.VISIBLE
                                    Toast.makeText(MyApp.instance,"Se produjo un error en el registro",Toast.LENGTH_SHORT).show()
                                }, 2000)
                            }
                            is Resource.Success -> {
                                register_progressbar.visibility = View.GONE
                                register_user_layout.visibility = View.INVISIBLE
                                Toast.makeText(
                                    MyApp.instance,
                                    "Usuario registrado",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Handler().postDelayed({
                                    finish()
                                }, 2000)

                            }
                            is Resource.Loading -> {
                                register_progressbar.visibility = View.VISIBLE
                                register_user_layout.visibility = View.INVISIBLE
                            }
                        }

                    })
                }else {
                    Toast.makeText(MyApp.instance,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(MyApp.instance,"Hay algún dato necesario sin rellenar",Toast.LENGTH_SHORT).show()
            }
        }
    }

}