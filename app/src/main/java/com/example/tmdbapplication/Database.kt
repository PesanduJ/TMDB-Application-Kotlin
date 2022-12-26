package com.example.tmdbapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Database : AppCompatActivity() {

    private lateinit var dbref:DatabaseReference
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieArrayList: ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_showing)

        movieRecyclerView = findViewById(R.id.nowshowingRecyclerView)
        movieRecyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL, false)
        movieRecyclerView.setHasFixedSize(true)

        movieArrayList = arrayListOf<Movie>()

        getMovieData()

    }

    private fun getMovieData() {

        dbref = FirebaseDatabase.getInstance().getReference("Movie")

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (movieSnapshot in snapshot.children){
                        val movie = movieSnapshot.getValue(Movie::class.java)
                        movieArrayList.add(movie!!)
                    }
                    movieRecyclerView.adapter = MyAdapterNS(movieArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



}

