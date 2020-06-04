package com.salesianostriana.sharecare.ui.disponibles

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.salesianostriana.sharecare.R
import com.salesianostriana.sharecare.common.Constantes
import com.salesianostriana.sharecare.common.MyApp
import com.salesianostriana.sharecare.models.User
import com.salesianostriana.sharecare.ui.UserServicioDetalleActivity
import kotlinx.android.synthetic.main.fragment_user_disponible.view.*


class MyUsersConServicioRecyclerViewAdapter() : RecyclerView.Adapter<MyUsersConServicioRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private var users: List<User> = ArrayList()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as User
            var intent = Intent(MyApp.instance, UserServicioDetalleActivity::class.java).apply{
                putExtra(Constantes.INTENT_DETAIL_KEY_ID,item.id)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            MyApp.instance.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_user_disponible, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[position]

        holder.fullname.text = item.fullName
        holder.localidad.text = item.localidad

        if (item.img == null)
            holder.img.load(Uri.parse("file:///mipmap-hdpi/ic_default_img.png"))
        else
            holder.img.load(item.img)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = users.size

    fun setData(usersList: List<User>){
        users = usersList
        Log.d("UsersAdapter",users.toString())
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val fullname = mView.textViewServivioListFullname
        val localidad = mView.textViewServicioListLocalidad
        val img = mView.user_img
    }

}