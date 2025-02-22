package com.example.practica2y3

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class PeliAdapter (private val contex: Activity, private val arrayList: ArrayList<Peliculas>)
    : ArrayAdapter<Peliculas>(contex,R.layout.item, arrayList)
{
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(contex)
        val view: View = inflater.inflate(R.layout.item, null)

        val iconResId = when (arrayList[position].genero) {
            "accion" -> R.drawable.action
            "comedia" -> R.drawable.comedy
            "terror" -> R.drawable.horror
            else -> R.drawable.movie
        }

        view.findViewById<TextView>(R.id.nombre).text = arrayList[position].nombre.toString()
        view.findViewById<TextView>(R.id.anio).text = arrayList[position].anio.toString()
        view.findViewById<TextView>(R.id.genero).text = arrayList[position].genero.toString()
        view.findViewById<ImageView>(R.id.genreIcon).setImageResource(iconResId)

        return view
    }
}