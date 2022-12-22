package com.example.tmdbapplication

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Payment : AppCompatActivity() {

    lateinit var movieName: TextView
    lateinit var bookedDate: TextView
    lateinit var seatNames: TextView
    lateinit var noSeats: TextView
    lateinit var totalPrice: TextView
    lateinit var btn_pay: TextView

    private lateinit var firebase: FirebaseDatabase
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        movieName = findViewById(R.id.movieName)
        bookedDate = findViewById(R.id.date)
        seatNames = findViewById(R.id.seatNames)
        noSeats = findViewById(R.id.noOfSeats)
        totalPrice = findViewById(R.id.payAmount)
        btn_pay = findViewById(R.id.btn_pay)


        var myintentReceive = intent
        var id = myintentReceive.getStringExtra("SelectedBookingDataId")
        var bookDate = myintentReceive.getStringExtra("SelectedBookingDataDate")
        var mid = myintentReceive.getStringExtra("SelectedBookingDataMovie")
        val seatArray =
            myintentReceive.getSerializableExtra("SelectedBookingDataSeats") as ArrayList<String>?
        var NOS = myintentReceive.getStringExtra("SelectedBookingDataNOS")

        bookedDate.text = "Date : " + bookDate.toString()
        seatNames.text = "Seats : " + seatArray.toString()
        noSeats.text = "No of Seats Booked : " + NOS.toString()
        if (NOS != null) {
            totalPrice.text = "RS." + calculatePrice(NOS.toInt()).toString()
        }

        btn_pay.setOnClickListener() {

            database = FirebaseDatabase.getInstance().getReference("Reservation")
            val Booking = Booking(id.toString(), bookDate, mid, seatArray)

            database.push().setValue(Booking(id.toString(), bookDate, mid, seatArray)).addOnSuccessListener {
                Toast.makeText(applicationContext, "Booking Successfull!!", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun calculatePrice(NOS: Int): Int {
        return 1500 * NOS
    }


}




