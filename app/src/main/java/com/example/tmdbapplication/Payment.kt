package com.example.tmdbapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject
import java.math.BigDecimal
import java.net.URL

class Payment : AppCompatActivity() {

    lateinit var movieBanner:ShapeableImageView
    lateinit var movieName: TextView
    lateinit var bookedDate: TextView
    lateinit var seatNames: TextView
    lateinit var noSeats: TextView
    lateinit var totalPrice: TextView
    lateinit var btn_pay: TextView

    lateinit var razorPay:RadioButton
    lateinit var googlePay:RadioButton
    lateinit var paypal:RadioButton

    private lateinit var firebase: FirebaseDatabase
    private lateinit var database: DatabaseReference

    lateinit var paymentMethod:String
    var config: PayPalConfiguration?=null
    var amount:Double=0.0

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

        razorPay = findViewById(R.id.razorPay)
        googlePay = findViewById(R.id.googlePay)
        paypal = findViewById(R.id.paypal)


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
        noSeats.text = "No of Seats Booked : " + NOS.toString()
        if (NOS != null) {
            totalPrice.text = calculatePrice(NOS.toInt()).toString()
        }

        //selecting payment method
        // Get radio group selected item using on checked change listener
        radio_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                paymentMethod = radio.text.toString()
            })

        //paypal config
        var client_id :String = "ASUAp08lbI6zCaBMMAKBuMmL2snEqKgWpvYoMWMfs_2Kiyuoxi6PaXEbBnITxO31981re_A3Allr0EwK"
        config = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(client_id)
        var i = Intent(this, PayPalService::class.java)
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
        startService(i)


        btn_pay.setOnClickListener() {

            if (paymentMethod == "Razorpay"){

            }else if(paymentMethod == "Google Pay"){

            }else if(paymentMethod == "Paypal"){
                amount = totalPrice.text.toString().toDouble()
                var payment= PayPalPayment(
                    BigDecimal.valueOf(amount),"USD","Book My Show",
                    PayPalPayment.PAYMENT_INTENT_SALE)
                var intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment)
                startActivityForResult(intent,123)
            }



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

    override fun onDestroy() {
        stopService(Intent(this,PayPalService::class.java))
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==123){
            if (resultCode== Activity.RESULT_OK){
                var obj = Intent(this,Finish::class.java)
                startActivity(obj)
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

        when (seats.contains("r1s2")) {

            true -> {
                selectedSeats = selectedSeats + " , R1 S2"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r1s3")) {

            true -> {
                selectedSeats = selectedSeats + " , R1 S3"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r1s4")) {

            true -> {
                selectedSeats = selectedSeats + " , R1 S4"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r1s5")) {

            true -> {
                selectedSeats = selectedSeats + " , R1 S5"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r1s6")) {

            true -> {
                selectedSeats = selectedSeats + " , R1 S6"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        //---------------Row 2--------------------------------------------------------------------------------------------------
        when (seats.contains("r2s1")) {

            true -> {
                selectedSeats = selectedSeats + " , R2 S1"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r2s2")) {

            true -> {
                selectedSeats = selectedSeats + " , R2 S2"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r2s3")) {

            true -> {
                selectedSeats = selectedSeats + ", R2 S3"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r2s4")) {

            true -> {
                selectedSeats = selectedSeats + " , R2 S4"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r2s5")) {

            true -> {
                selectedSeats = selectedSeats + " , R2 S5"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r2s6")) {

            true -> {
                selectedSeats = selectedSeats + " , R2 S6"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r2s7")) {

            true -> {
                selectedSeats = selectedSeats + " , R2 S7"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r2s8")) {

            true -> {
                selectedSeats = selectedSeats + " , R2 S8"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        //---------------Row 3--------------------------------------------------------------------------------------------------
        when (seats.contains("r3s1")) {

            true -> {
                selectedSeats = selectedSeats + " , R3 S1"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r3s2")) {

            true -> {
                selectedSeats = selectedSeats + " , R3 S2"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r3s3")) {

            true -> {
                selectedSeats = selectedSeats + ", R3 S3"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r3s4")) {

            true -> {
                selectedSeats = selectedSeats + " , R3 S4"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r3s5")) {

            true -> {
                selectedSeats = selectedSeats + " , R3 S5"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r3s6")) {

            true -> {
                selectedSeats = selectedSeats + " , R3 S6"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r3s7")) {

            true -> {
                selectedSeats = selectedSeats + " , R3 S7"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r3s8")) {

            true -> {
                selectedSeats = selectedSeats + " , R3 S8"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        //---------------Row 4--------------------------------------------------------------------------------------------------
        when (seats.contains("r4s1")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S1"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s2")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S2"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s3")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S3"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s4")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S4"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s5")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S5"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s6")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S6"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s7")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S7"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s8")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S8"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s9")) {

            true -> {
                selectedSeats = selectedSeats + " , R4 S9"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        //---------------Row 5--------------------------------------------------------------------------------------------------
        when (seats.contains("r5s1")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S1"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s2")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S2"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s3")) {

            true -> {
                selectedSeats = selectedSeats + ", R5 S3"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s4")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S4"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s5")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S5"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s6")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S6"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s7")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S7"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s8")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S8"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r5s9")) {

            true -> {
                selectedSeats = selectedSeats + " , R5 S9"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        //---------------Row 6--------------------------------------------------------------------------------------------------
        when (seats.contains("r6s1")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S1"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s2")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S2"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s3")) {

            true -> {
                selectedSeats = selectedSeats + ", R6 S3"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s4")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S4"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s5")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S5"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s6")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S6"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s7")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S7"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s8")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S8"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r6s9")) {

            true -> {
                selectedSeats = selectedSeats + " , R6 S9"
            }

            false -> {

            }

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        if (selectedSeats.contains("R1 S1") == false){
            selectedSeats = selectedSeats.substring(3)
        }
        seatNames.text = "Seats : " + selectedSeats
    }
















//    btn_pay.setOnClickListener() {
//
//        database = FirebaseDatabase.getInstance().getReference("Reservation")
//        val Booking = Booking(id.toString(), bookDate, mid, seatArray,movieIdDate)
//
//        database.push().setValue(Booking(id.toString(), bookDate, mid, seatArray,movieIdDate)).addOnSuccessListener {
//            Toast.makeText(applicationContext, "Booking Successfull!!", Toast.LENGTH_SHORT)
//                .show()
//        }.addOnFailureListener {
//            Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
//        }
//    }


}




