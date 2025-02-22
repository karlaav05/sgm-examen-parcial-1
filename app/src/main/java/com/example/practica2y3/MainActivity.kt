package com.example.practica2y3

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LogIn::class.java))
        } else {
            Toast.makeText(this, "Hola ${currentUser.email}", Toast.LENGTH_SHORT).show()
        }
    }

    lateinit var datos: ArrayList<Peliculas>

    val database = Firebase.database
    val myRef = database.getReference("peliculas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        leerBase()
    }

    private fun leerBase(){
        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.value
                Log.d(TAG, "Value is: " + value)
                datos = ArrayList<Peliculas>()
                snapshot.children.forEach{
                    hijo ->
                    var pelicula = Peliculas(hijo.child("nombre").value.toString()?: "", hijo.child("anio").value.toString()?: "", hijo.child("genero").value.toString()?: "", hijo.key.toString())

                    datos.add(pelicula)
                }
                llenarLista()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }

    private fun llenarLista() {
        val adaptador = PeliAdapter(this, datos)
        val lista = findViewById<ListView>(R.id.lista)
        lista.adapter = adaptador
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.salir){
            auth.signOut()
            startActivity(Intent(this, LogIn::class.java))
        }
        else if(item.itemId == R.id.perfil){
            Toast.makeText(this, "Ir a perfil", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}