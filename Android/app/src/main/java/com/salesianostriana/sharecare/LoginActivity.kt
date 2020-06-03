package com.salesianostriana.sharecare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.MySharedPreferencesManager
import com.salesianostriana.sharecare.models.requests.LoginReq
import com.salesianostriana.sharecare.viewmodel.UserAuthViewModel
import com.salesianostriana.sharecare.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var userAuthViewModel : UserAuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (applicationContext as MyApp).getApplicationComponent().inject(this)
        ButterKnife.bind(this)
        val token: String? = MySharedPreferencesManager().getSharedPreferences()
            .getString(Constantes.SHARED_PREFERENCES_TOKEN_KEY, "")
        if (!token.isNullOrEmpty()) {
            val autologin: Intent = Intent(MyApp.instance, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(autologin)
            finish()
        }

        buttonLogin.setOnClickListener(View.OnClickListener { v ->

            userAuthViewModel.doLogin(
                LoginReq(
                    editTextLoginUsername.text.toString(),
                    editTextLoginPassword.text.toString()

                )
            ).observe(this, Observer {
                if (it != null) {
                    val login: Intent = Intent(MyApp.instance, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(login)
                    finish()
                }
            })
        })

        textViewLoginRegister.setOnClickListener(View.OnClickListener {
            val registro : Intent=Intent(MyApp.instance,RegisterActivity::class.java).apply{
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(registro)
        })
    }

}