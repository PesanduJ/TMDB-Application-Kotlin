package com.example.tmdbapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.*


class BookSeat : AppCompatActivity() {

    private lateinit var firebase: FirebaseDatabase
    private lateinit var database: DatabaseReference
    var bookedInfo = mutableListOf<Booking>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_seat)

        var test: TextView = findViewById(R.id.test)
        var btn_buyTickets: Button = findViewById(R.id.btn_BookSeat)
        var calendarView: CalendarView = findViewById(R.id.calendarView)


        var r1s1: ToggleButton = findViewById(R.id.r1seat1)
        var r1s2: ToggleButton = findViewById(R.id.r1seat2)
        var r1s3: ToggleButton = findViewById(R.id.r1seat3)
        var r1s4: ToggleButton = findViewById(R.id.r1seat4)
        var r1s5: ToggleButton = findViewById(R.id.r1seat5)
        var r1s6: ToggleButton = findViewById(R.id.r1seat6)

        var r2s1: ToggleButton = findViewById(R.id.r2seat1)
        var r2s2: ToggleButton = findViewById(R.id.r2seat2)
        var r2s3: ToggleButton = findViewById(R.id.r2seat3)
        var r2s4: ToggleButton = findViewById(R.id.r2seat4)
        var r2s5: ToggleButton = findViewById(R.id.r2seat5)
        var r2s6: ToggleButton = findViewById(R.id.r2seat6)
        var r2s7: ToggleButton = findViewById(R.id.r2seat7)
        var r2s8: ToggleButton = findViewById(R.id.r2seat8)

        var r3s1: ToggleButton = findViewById(R.id.r3seat1)
        var r3s2: ToggleButton = findViewById(R.id.r3seat2)
        var r3s3: ToggleButton = findViewById(R.id.r3seat3)
        var r3s4: ToggleButton = findViewById(R.id.r3seat4)
        var r3s5: ToggleButton = findViewById(R.id.r3seat5)
        var r3s6: ToggleButton = findViewById(R.id.r3seat6)
        var r3s7: ToggleButton = findViewById(R.id.r3seat7)
        var r3s8: ToggleButton = findViewById(R.id.r3seat8)


        var r4s1: ToggleButton = findViewById(R.id.r4seat1)
        var r4s2: ToggleButton = findViewById(R.id.r4seat2)
        var r4s3: ToggleButton = findViewById(R.id.r4seat3)
        var r4s4: ToggleButton = findViewById(R.id.r4seat4)
        var r4s5: ToggleButton = findViewById(R.id.r4seat5)
        var r4s6: ToggleButton = findViewById(R.id.r4seat6)
        var r4s7: ToggleButton = findViewById(R.id.r4seat7)
        var r4s8: ToggleButton = findViewById(R.id.r4seat8)
        var r4s9: ToggleButton = findViewById(R.id.r4seat9)

        var r5s1: ToggleButton = findViewById(R.id.r5seat1)
        var r5s2: ToggleButton = findViewById(R.id.r5seat2)
        var r5s3: ToggleButton = findViewById(R.id.r5seat3)
        var r5s4: ToggleButton = findViewById(R.id.r5seat4)
        var r5s5: ToggleButton = findViewById(R.id.r5seat5)
        var r5s6: ToggleButton = findViewById(R.id.r5seat6)
        var r5s7: ToggleButton = findViewById(R.id.r5seat7)
        var r5s8: ToggleButton = findViewById(R.id.r5seat8)
        var r5s9: ToggleButton = findViewById(R.id.r5seat9)

        var r6s1: ToggleButton = findViewById(R.id.r6seat1)
        var r6s2: ToggleButton = findViewById(R.id.r6seat2)
        var r6s3: ToggleButton = findViewById(R.id.r6seat3)
        var r6s4: ToggleButton = findViewById(R.id.r6seat4)
        var r6s5: ToggleButton = findViewById(R.id.r6seat5)
        var r6s6: ToggleButton = findViewById(R.id.r6seat6)
        var r6s7: ToggleButton = findViewById(R.id.r6seat7)
        var r6s8: ToggleButton = findViewById(R.id.r6seat8)
        var r6s9: ToggleButton = findViewById(R.id.r6seat9)

//*****************************************************************************************************************************************

        var myintent = intent
        var mid = myintent.getStringExtra("ID")

        //Toast.makeText(applicationContext,"${mid.toString()}", Toast.LENGTH_SHORT).show()

        var seatArray = ArrayList<String>()


//*****************************************************************************************************************************************

        var counter: Int = 0

        r1s1.setOnClickListener {
            if (r1s1.text.toString() == "•") {
                r1s1.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r1s1")
            } else {
                r1s1.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r1s1")
            }

        }
        r1s2.setOnClickListener {
            if (r1s2.text.toString() == "•") {
                r1s2.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r1s2")
            } else {
                r1s2.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r1s2")
            }

        }
        r1s3.setOnClickListener {
            if (r1s3.text.toString() == "•") {
                r1s3.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r1s3")
            } else {
                r1s3.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r1s3")
            }

        }
        r1s4.setOnClickListener {
            if (r1s4.text.toString() == "•") {
                r1s4.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r1s4")
            } else {
                r1s4.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r1s4")
            }

        }
        r1s5.setOnClickListener {
            if (r1s5.text.toString() == "•") {
                r1s5.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r1s5")

            } else {
                r1s5.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r1s5")
            }

        }
        r1s6.setOnClickListener {
            if (r1s6.text.toString() == "•") {
                r1s6.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r1s6")
            } else {
                r1s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r1s6")
            }

        }

        r2s1.setOnClickListener {
            if (r2s1.text.toString() == "•") {
                r2s1.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s1")
            } else {
                r2s1.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s1")
            }

        }
        r2s2.setOnClickListener {
            if (r2s2.text.toString() == "•") {
                r2s2.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s2")
            } else {
                r2s2.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s2")
            }

        }
        r2s3.setOnClickListener {
            if (r2s3.text.toString() == "•") {
                r2s3.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s3")
            } else {
                r2s3.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s3")
            }

        }
        r2s4.setOnClickListener {
            if (r2s4.text.toString() == "•") {
                r2s4.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s4")
            } else {
                r2s4.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s4")
            }

        }
        r2s5.setOnClickListener {
            if (r2s5.text.toString() == "•") {
                r2s5.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s5")
            } else {
                r2s5.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s5")
            }

        }
        r2s6.setOnClickListener {
            if (r2s6.text.toString() == "•") {
                r2s6.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s6")
            } else {
                r2s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s6")
            }

        }
        r2s7.setOnClickListener {
            if (r2s7.text.toString() == "•") {
                r2s7.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s7")
            } else {
                r2s7.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s7")
            }

        }
        r2s8.setOnClickListener {
            if (r2s8.text.toString() == "•") {
                r2s8.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r2s8")
            } else {
                r2s8.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r2s8")
            }

        }

        r3s1.setOnClickListener {
            if (r3s1.text.toString() == "•") {
                r3s1.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s1")
            } else {
                r3s1.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s1")
            }

        }
        r3s2.setOnClickListener {
            if (r3s2.text.toString() == "•") {
                r3s2.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s2")
            } else {
                r3s2.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s2")
            }

        }
        r3s3.setOnClickListener {
            if (r3s3.text.toString() == "•") {
                r3s3.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s3")
            } else {
                r3s3.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s3")
            }

        }
        r3s4.setOnClickListener {
            if (r3s4.text.toString() == "•") {
                r3s4.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s4")
            } else {
                r3s4.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s4")
            }

        }
        r3s5.setOnClickListener {
            if (r3s5.text.toString() == "•") {
                r3s5.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s5")
            } else {
                r3s5.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s5")
            }

        }
        r3s6.setOnClickListener {
            if (r3s6.text.toString() == "•") {
                r3s6.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s6")
            } else {
                r3s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s6")
            }

        }
        r3s7.setOnClickListener {
            if (r3s7.text.toString() == "•") {
                r3s7.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s7")
            } else {
                r3s7.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s7")
            }

        }
        r3s8.setOnClickListener {
            if (r3s8.text.toString() == "•") {
                r3s8.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r3s8")
            } else {
                r3s8.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r3s8")
            }

        }

        r4s1.setOnClickListener {
            if (r4s1.text.toString() == "•") {
                r4s1.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s1")
            } else {
                r4s1.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s1")
            }

        }
        r4s2.setOnClickListener {
            if (r4s2.text.toString() == "•") {
                r4s2.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s2")
            } else {
                r4s2.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s2")
            }

        }
        r4s3.setOnClickListener {
            if (r4s3.text.toString() == "•") {
                r4s3.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s3")
            } else {
                r4s3.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s3")
            }

        }
        r4s4.setOnClickListener {
            if (r4s4.text.toString() == "•") {
                r4s4.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s4")
            } else {
                r4s4.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s4")
            }

        }
        r4s5.setOnClickListener {
            if (r4s5.text.toString() == "•") {
                r4s5.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s5")
            } else {
                r4s5.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s5")
            }

        }
        r4s6.setOnClickListener {
            if (r4s6.text.toString() == "•") {
                r4s6.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s6")
            } else {
                r4s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s6")
            }

        }
        r4s7.setOnClickListener {
            if (r4s7.text.toString() == "•") {
                r4s7.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s7")
            } else {
                r4s7.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s7")
            }

        }
        r4s8.setOnClickListener {
            if (r4s8.text.toString() == "•") {
                r4s8.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s8")
            } else {
                r4s8.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s8")
            }

        }
        r4s9.setOnClickListener {
            if (r4s9.text.toString() == "•") {
                r4s9.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r4s9")
            } else {
                r4s9.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r4s9")
            }

        }

        r5s1.setOnClickListener {
            if (r5s1.text.toString() == "•") {
                r5s1.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s1")
            } else {
                r5s1.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s1")
            }

        }
        r5s2.setOnClickListener {
            if (r5s2.text.toString() == "•") {
                r5s2.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s2")
            } else {
                r5s2.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s2")
            }

        }
        r5s3.setOnClickListener {
            if (r5s3.text.toString() == "•") {
                r5s3.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s3")
            } else {
                r5s3.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s3")
            }

        }
        r5s4.setOnClickListener {
            if (r5s4.text.toString() == "•") {
                r5s4.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s4")
            } else {
                r5s4.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s4")
            }

        }
        r5s5.setOnClickListener {
            if (r5s5.text.toString() == "•") {
                r5s5.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s5")
            } else {
                r5s5.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s5")
            }

        }
        r5s6.setOnClickListener {
            if (r5s6.text.toString() == "•") {
                r5s6.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s6")
            } else {
                r5s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s6")
            }

        }
        r5s7.setOnClickListener {
            if (r5s7.text.toString() == "•") {
                r5s7.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s7")
            } else {
                r5s7.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s7")
            }

        }
        r5s8.setOnClickListener {
            if (r5s8.text.toString() == "•") {
                r5s8.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s8")
            } else {
                r5s8.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s8")
            }

        }
        r5s9.setOnClickListener {
            if (r5s9.text.toString() == "•") {
                r5s9.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r5s9")
            } else {
                r5s9.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r5s9")
            }

        }

        r6s1.setOnClickListener {
            if (r6s1.text.toString() == "•") {
                r6s1.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s1")
            } else {
                r6s1.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s1")
            }

        }
        r6s2.setOnClickListener {
            if (r6s2.text.toString() == "•") {
                r6s2.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s2")
            } else {
                r6s2.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s2")
            }

        }
        r6s3.setOnClickListener {
            if (r6s3.text.toString() == "•") {
                r6s3.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s3")
            } else {
                r6s3.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s3")
            }

        }
        r6s4.setOnClickListener {
            if (r6s4.text.toString() == "•") {
                r6s4.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s4")
            } else {
                r6s4.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s4")
            }

        }
        r6s5.setOnClickListener {
            if (r6s5.text.toString() == "•") {
                r6s5.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s5")
            } else {
                r6s5.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s5")
            }

        }
        r6s6.setOnClickListener {
            if (r6s6.text.toString() == "•") {
                r6s6.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s6")
            } else {
                r6s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s6")
            }

        }
        r6s7.setOnClickListener {
            if (r6s7.text.toString() == "•") {
                r6s7.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s7")
            } else {
                r6s7.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s7")
            }

        }
        r6s8.setOnClickListener {
            if (r6s8.text.toString() == "•") {
                r6s8.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s8")
            } else {
                r6s8.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s8")
            }

        }
        r6s9.setOnClickListener {
            if (r6s9.text.toString() == "•") {
                r6s9.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg_orange)
                counter++
                test.text = counter.toString()
                seatArray.add("r6s9")
            } else {
                r6s9.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)
                counter--
                test.text = counter.toString()
                seatArray.remove("r6s9")
            }

        }

//*****************************************************************************************************************************************

        test.text = counter.toString()
        var Date: String = ""
        calendarView
            .setOnDateChangeListener(
                CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->

                    val Date = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)

                    checkSeats(Date)
                })


        btn_buyTickets.setOnClickListener() {


            if (r1s1.text == "" && r1s2.text == "" && r1s3.text == "" && r1s4.text == "" && r1s5.text == "" && r1s6.text == "" &&
                r2s1.text == "" && r2s2.text == "" && r2s3.text == "" && r2s4.text == "" && r2s5.text == "" && r2s6.text == "" && r2s7.text == "" && r2s8.text == "" &&
                r3s1.text == "" && r3s2.text == "" && r3s3.text == "" && r3s4.text == "" && r3s5.text == "" && r3s6.text == "" && r3s7.text == "" && r3s8.text == "" &&
                r4s1.text == "" && r4s2.text == "" && r4s3.text == "" && r4s4.text == "" && r4s5.text == "" && r4s6.text == "" && r4s7.text == "" && r4s8.text == "" && r4s9.text == "" &&
                r5s1.text == "" && r5s2.text == "" && r5s3.text == "" && r5s4.text == "" && r5s5.text == "" && r5s6.text == "" && r5s7.text == "" && r5s8.text == "" && r5s9.text == "" &&
                r6s1.text == "" && r6s2.text == "" && r6s3.text == "" && r6s4.text == "" && r6s5.text == "" && r6s6.text == "" && r6s7.text == "" && r6s8.text == "" && r6s9.text == ""
            ) {

                Toast.makeText(applicationContext, "Please select your seats!", Toast.LENGTH_SHORT)
                    .show()

            } else {

                var id: Int = 0
                var bookDate = Date.toString()

                database = FirebaseDatabase.getInstance().getReference("Reservation")
                val Booking = Booking(id.toString(), bookDate, mid, seatArray)

                //database.push().setValue(Booking)

                var intentSend = Intent(applicationContext, Payment::class.java)
                intentSend.putExtra("SelectedBookingDataId", id)
                intentSend.putExtra("SelectedBookingDataDate", bookDate)
                intentSend.putExtra("SelectedBookingDataMovie", mid)
                intentSend.putExtra("SelectedBookingDataSeats", seatArray)
                intentSend.putExtra("SelectedBookingDataNOS", test.getText().toString())
                startActivity(intentSend)

                var test1: TextView = findViewById(R.id.test1)
                test1.text = ""

            }
//            database.push().setValue(Booking).addOnSuccessListener {
//                Toast.makeText(applicationContext, "Booking Successfull!!", Toast.LENGTH_SHORT)
//                    .show()
//            }.addOnFailureListener {
//                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
//            }
        }

    }

    fun checkSeats(Date: String) {
        try {

            var userId: String = ""
            var date: String = ""
            var movieId: String = ""
            var seats: String = ""


            var myintent = intent
            var mid = myintent.getStringExtra("ID")

            firebase = FirebaseDatabase.getInstance()
            database = firebase.getReference("Reservation")
            var query: Query = database.orderByChild("date").equalTo("$Date")
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        for (snapshot in snapshot.children) {
                            snapshot.getValue(Booking::class.java)?.let { bookedInfo.add(it) }
                        }

                        for (data in bookedInfo) {
                            userId = data.userId.toString()
                            date = data.date.toString()
                            movieId = data.movieId.toString()
                            seats = data.seats.toString()
                        }

                        blockSeats(seats)

                        var test1: TextView = findViewById(R.id.test1)
                        test1.setText("$seats")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        } catch (e: IndexOutOfBoundsException) {

        }

    }


    fun blockSeats(seats: String){

        var r1s1: ToggleButton = findViewById(R.id.r1seat1)
        var r1s2: ToggleButton = findViewById(R.id.r1seat2)
        var r1s3: ToggleButton = findViewById(R.id.r1seat3)
        var r1s4: ToggleButton = findViewById(R.id.r1seat4)
        var r1s5: ToggleButton = findViewById(R.id.r1seat5)
        var r1s6: ToggleButton = findViewById(R.id.r1seat6)

        var r2s1: ToggleButton = findViewById(R.id.r2seat1)
        var r2s2: ToggleButton = findViewById(R.id.r2seat2)
        var r2s3: ToggleButton = findViewById(R.id.r2seat3)
        var r2s4: ToggleButton = findViewById(R.id.r2seat4)
        var r2s5: ToggleButton = findViewById(R.id.r2seat5)
        var r2s6: ToggleButton = findViewById(R.id.r2seat6)
        var r2s7: ToggleButton = findViewById(R.id.r2seat7)
        var r2s8: ToggleButton = findViewById(R.id.r2seat8)

        var r3s1: ToggleButton = findViewById(R.id.r3seat1)
        var r3s2: ToggleButton = findViewById(R.id.r3seat2)
        var r3s3: ToggleButton = findViewById(R.id.r3seat3)
        var r3s4: ToggleButton = findViewById(R.id.r3seat4)
        var r3s5: ToggleButton = findViewById(R.id.r3seat5)
        var r3s6: ToggleButton = findViewById(R.id.r3seat6)
        var r3s7: ToggleButton = findViewById(R.id.r3seat7)
        var r3s8: ToggleButton = findViewById(R.id.r3seat8)


        var r4s1: ToggleButton = findViewById(R.id.r4seat1)
        var r4s2: ToggleButton = findViewById(R.id.r4seat2)
        var r4s3: ToggleButton = findViewById(R.id.r4seat3)
        var r4s4: ToggleButton = findViewById(R.id.r4seat4)
        var r4s5: ToggleButton = findViewById(R.id.r4seat5)
        var r4s6: ToggleButton = findViewById(R.id.r4seat6)
        var r4s7: ToggleButton = findViewById(R.id.r4seat7)
        var r4s8: ToggleButton = findViewById(R.id.r4seat8)
        var r4s9: ToggleButton = findViewById(R.id.r4seat9)

        var r5s1: ToggleButton = findViewById(R.id.r5seat1)
        var r5s2: ToggleButton = findViewById(R.id.r5seat2)
        var r5s3: ToggleButton = findViewById(R.id.r5seat3)
        var r5s4: ToggleButton = findViewById(R.id.r5seat4)
        var r5s5: ToggleButton = findViewById(R.id.r5seat5)
        var r5s6: ToggleButton = findViewById(R.id.r5seat6)
        var r5s7: ToggleButton = findViewById(R.id.r5seat7)
        var r5s8: ToggleButton = findViewById(R.id.r5seat8)
        var r5s9: ToggleButton = findViewById(R.id.r5seat9)

        var r6s1: ToggleButton = findViewById(R.id.r6seat1)
        var r6s2: ToggleButton = findViewById(R.id.r6seat2)
        var r6s3: ToggleButton = findViewById(R.id.r6seat3)
        var r6s4: ToggleButton = findViewById(R.id.r6seat4)
        var r6s5: ToggleButton = findViewById(R.id.r6seat5)
        var r6s6: ToggleButton = findViewById(R.id.r6seat6)
        var r6s7: ToggleButton = findViewById(R.id.r6seat7)
        var r6s8: ToggleButton = findViewById(R.id.r6seat8)
        var r6s9: ToggleButton = findViewById(R.id.r6seat9)

        when (seats.contains("r4s6")) {

            true -> r4s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.booked_seat_bg)

            false -> r4s6.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        when (seats.contains("r4s7")) {

            true -> r4s7.background = ContextCompat.getDrawable(applicationContext, R.drawable.booked_seat_bg)

            false -> r4s7.background = ContextCompat.getDrawable(applicationContext, R.drawable.seat_bg)

            else -> {
                Toast.makeText(applicationContext, "HUH??", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}

