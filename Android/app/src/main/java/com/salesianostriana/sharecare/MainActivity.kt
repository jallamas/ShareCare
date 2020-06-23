package com.salesianostriana.sharecare

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.common.MySharedPreferencesManager
import com.salesianostriana.sharecare.ui.enviadas.SolicitudEnviadaDetalleActivity
import com.salesianostriana.sharecare.ui.enviadas.SolicitudesEnviadasFragment
import com.salesianostriana.sharecare.ui.profile.ProfileActivity

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.up_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            MySharedPreferencesManager().removeStringValue(Constantes.SHARED_PREFERENCES_TOKEN_KEY)
            val exit : Intent = Intent(MyApp.instance, LoginActivity::class.java).apply{
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(exit)
            finish()
        }

        if(item.itemId==R.id.editProfile){
            val editProfile : Intent = Intent(MyApp.instance, ProfileActivity::class.java).apply{
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(editProfile)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_disponibles, R.id.navigation_enviadas, R.id.navigation_recibidas))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val i:Intent = intent
        val data : String? = i.getStringExtra("FromSolicitud")

        if(data !=null && data.contentEquals("1")){
            navView.setSelectedItemId(R.id.navigation_enviadas);
        }else if(data !=null && data.contentEquals("2")){
            navView.setSelectedItemId(R.id.navigation_recibidas);
        }
    }

    fun finishMe() { finish() }
}