package com.example.tmdbapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*

class CheckDB : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var myintent = intent
        var mid = myintent.getStringExtra("MovID")
        checkChild(mid.toString())


    }

    fun checkChild(id: String){
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Movie/$id")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //Toast.makeText(applicationContext,"DataFound!", Toast.LENGTH_SHORT).show()
                    var myintent = Intent(applicationContext,BookSeat::class.java)
                    myintent.putExtra("ID",id)
                    startActivity(myintent)

                }
                else{
                    Toast.makeText(applicationContext,"Unfortunate!", Toast.LENGTH_SHORT).show()
                    var myintent = Intent(applicationContext,UserDashboard::class.java)
                    startActivity(myintent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}