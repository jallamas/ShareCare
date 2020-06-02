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
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var userViewModel: UserViewModel

    @BindView(R.id.register_user_layout)
    lateinit var layout: ConstraintLayout

    @BindView(R.id.register_username)
    lateinit var username: EditText

    @BindView(R.id.register_fullname)
    lateinit var fullname: EditText

    @BindView(R.id.register_phone)
    lateinit var phone: EditText

    @BindView(R.id.register_localidad)
    lateinit var localidad: EditText

    @BindView(R.id.editTextRegisterPassword)
    lateinit var password: EditText

    @BindView(R.id.editTextRegisterPassword2)
    lateinit var password2: EditText

    @BindView(R.id.editTextPrecioHora)
    lateinit var precioHora: EditText

    @BindView(R.id.checkBoxServicio)
    lateinit var servicio: CheckBox

    @BindView(R.id.register_image)
    lateinit var userImage: ImageView

    @BindView(R.id.button_register_img_upload)
    lateinit var upload: Button

    @BindView(R.id.buttonRegisterSave)
    lateinit var register: Button

    @BindView(R.id.register_progressbar)
    lateinit var progressBar: ProgressBar

    var uriPicture: Uri? = null

    var file: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        (applicationContext as MyApp).getApplicationComponent().inject(this)
        ButterKnife.bind(this)
        userImage.visibility = View.INVISIBLE
        setUploadButton()
        setSaveButton()
    }

    fun setUploadButton() {
        upload.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uriPicture = data?.data
            Log.d("URI", "LA URI ES $uriPicture")
            userImage.load(uriPicture)
            userImage.visibility = View.VISIBLE
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
        register.setOnClickListener {
            if(username.text.toString().isNotEmpty()&&fullname.text.toString().isNotEmpty()&&phone.text.toString().isNotEmpty()&&localidad.text.toString().isNotEmpty()
                &&password.text.toString().isNotEmpty()&&password2.text.toString().isNotEmpty()) {

                if (password.text.toString().equals(password2.text.toString())) {
                    val request = RegisterReq(username.text.toString(),fullname.text.toString(),localidad.text.toString(),password.text.toString(),
                        password2.text.toString(),phone.text.toString(),precioHora.text.toString(),servicio.isChecked.toString())

                    val requestBody = Gson().toJson(request).toRequestBody("application/json".toMediaTypeOrNull())

                   if(file==null) {
                       userViewModel.registerNewUser(requestBody)
                   }else {
                       userViewModel.registerNewUserPhoto(requestBody, file!!)
                   }
                    userViewModel.newUser.observe(this, Observer {
                        when (it) {
                            is Resource.Error -> {
                                progressBar.visibility = View.INVISIBLE
                                Handler().postDelayed({
                                    layout.visibility = View.VISIBLE
                                    Toast.makeText(MyApp.instance,"Se produjo un error en el registro",Toast.LENGTH_SHORT).show()
                                }, 2000)
                            }
                            is Resource.Success -> {
                                progressBar.visibility = View.GONE
                                layout.visibility = View.INVISIBLE
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
                                progressBar.visibility = View.VISIBLE
                                layout.visibility = View.INVISIBLE
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