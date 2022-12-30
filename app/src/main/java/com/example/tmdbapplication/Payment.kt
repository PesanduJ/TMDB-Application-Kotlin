package com.example.tmdbapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.net.URL

class Payment : AppCompatActivity() {

    lateinit var movieBanner:ShapeableImageView
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

        //remove 'android.os.NetworkOnMainThreadException' restriction and you override the default behavior
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

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
        var movieIdDate = "$mid"+bookDate.toString()



        getMovieInfo(mid.toString())
        movieName.text = id.toString()
        bookedDate.text = "Date : " + bookDate.toString()
        getSeatNumbers(seatArray.toString())
        //seatNames.text = "Seats : " + seatArray.toString()
        noSeats.text = "No of Seats Booked : " + NOS.toString()
        if (NOS != null) {
            totalPrice.text = "RS." + calculatePrice(NOS.toInt()).toString()
        }

        btn_pay.setOnClickListener() {

            database = FirebaseDatabase.getInstance().getReference("Reservation")
            val Booking = Booking(id.toString(), bookDate, mid, seatArray,movieIdDate)

            database.push().setValue(Booking(id.toString(), bookDate, mid, seatArray,movieIdDate)).addOnSuccessListener {
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

    fun getMovieInfo(mid:String){

        movieBanner = findViewById(R.id.imgBanner)
        movieName = findViewById(R.id.movieName)

        var url =
            "https://api.themoviedb.org/3/movie/${mid.toString()}?api_key=552d2cc1c293befeb2042aa156f6dcf1&language=en-US"
        val resultJson = URL(url).readText()
        val jsonObj = JSONObject(resultJson)

        //accessing JSON tag
        var backdrop_path = jsonObj.getString("backdrop_path")
        var original_title = jsonObj.getString("original_title")

        //publishing data
        val uiHandler2 = Handler(Looper.getMainLooper())
        uiHandler2.post(Runnable {
            movieName.text = original_title.toString()
        })


        //using picasso
        var iconUrl: String = "https://image.tmdb.org/t/p/original" + backdrop_path

        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post(Runnable {
            Picasso.get()
                .load(iconUrl)
                .into(movieBanner)
        })

    }

    fun getSeatNumbers(seats:String){

        var selectedSeats:String = ""

        //---------------Row 1--------------------------------------------------------------------------------------------------
        when (seats.contains("r1s1")) {

            true -> {
                selectedSeats = "R1 S1"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }



        seatNames.text = "Seats : " + selectedSeats
    }

}




