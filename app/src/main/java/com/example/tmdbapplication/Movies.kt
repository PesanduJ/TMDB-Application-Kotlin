package com.example.tmdbapplication

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Movies : AppCompatActivity() {

    lateinit var movieRecycle: RecyclerView   //you are not even declaring them only referring when you are using them
    var movieData = JSONArray()


    private lateinit var dbref:DatabaseReference
    private lateinit var movienowPlayingRecyclerView: RecyclerView
    private lateinit var movienowPlayingArrayList: ArrayList<Movie>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        var search: Button = findViewById(R.id.search)
        var searchMovie: EditText = findViewById(R.id.searchMovie)

        search.setOnClickListener() {
            var myintent = Intent(applicationContext, Search::class.java)
            myintent.putExtra("MovieName", searchMovie.getText().toString())
            startActivity(myintent)
        }

        loadMovie()

        movieRecycle = findViewById(R.id.movieRecycler)
        movieRecycle.adapter = MovieAdapter()
        movieRecycle.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL, false
        )

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btm_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> Toast.makeText(applicationContext, "home", Toast.LENGTH_SHORT).show()

                R.id.search -> focusSearch()

                R.id.database -> focusDatabase()

                R.id.location -> focusLocation()

                else -> {}
            }
                true
        }


        movienowPlayingRecyclerView = findViewById(R.id.nowplayingRecycler)
        movienowPlayingRecyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.HORIZONTAL, false)
        movienowPlayingRecyclerView.setHasFixedSize(true)
        movienowPlayingArrayList = arrayListOf<Movie>()
        getMovieData()

    }


    private inner class MovieAdapter : RecyclerView.Adapter<MoviesViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)

            view.setOnClickListener {
                var myintent = Intent(applicationContext, Info::class.java)
                myintent.putExtra("MovieID", MoviesViewHolder(view).movieId.text)
                startActivity(myintent)
            }
            return MoviesViewHolder(view)
        }

        override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {


            try {
                holder.movieTitle.text = movieData.getJSONObject(position).getString("title")
                holder.releaseDate.text =
                    movieData.getJSONObject(position).getString("release_date")

                holder.movieId.text = movieData.getJSONObject(position).getString("id")

                var poster_path = movieData.getJSONObject(position).getString("poster_path")
                var iconUrl: String = "https://image.tmdb.org/t/p/original" + poster_path

                val uiHandler = Handler(Looper.getMainLooper())
                uiHandler.post(Runnable {
                    Picasso.get()
                        .load(iconUrl)
                        .into(holder.movieThumb)
                })

            } catch (e: JSONException) {
                Toast.makeText(applicationContext, "JSONException!", Toast.LENGTH_LONG).show()
            } catch (e: SQLiteException) {
                Toast.makeText(applicationContext, "SQLiteException!", Toast.LENGTH_LONG).show()
            }


        }

        override fun getItemCount(): Int {
            return movieData.length()
        }
    }

    private inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var movieTitle: TextView = itemView.findViewById(R.id.txtMovieName)
        var releaseDate: TextView = itemView.findViewById(R.id.txtReleaseDate)
        var movieThumb: ImageView = itemView.findViewById(R.id.imageView)
        var movieId: TextView = itemView.findViewById(R.id.txtId)

    }

    fun loadMovie() {
        var request = JsonObjectRequest(
            Request.Method.GET,
            "https://api.themoviedb.org/4/list/1?page=1&api_key=552d2cc1c293befeb2042aa156f6dcf1",
            JSONObject(),
            { res ->
                try {
                    movieData = res.getJSONArray("results")
                    movieRecycle.adapter?.notifyDataSetChanged()            //? = making this optional
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, "JSONException!", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("Error", error.toString())
            })

        Volley.newRequestQueue(applicationContext).add(request)
    }

    fun focusSearch(){
        val sm :EditText= findViewById(R.id.searchMovie)
        sm.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(sm, InputMethodManager.SHOW_IMPLICIT)
    }

    fun focusDatabase(){
        var myintent = Intent(applicationContext,Database::class.java)
        startActivity(myintent)
    }

    fun focusLocation(){

    }

    private fun getMovieData() {

        dbref = FirebaseDatabase.getInstance().getReference("Movie")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (movieSnapshot in snapshot.children){
                        val movie = movieSnapshot.getValue(Movie::class.java)
                        movienowPlayingArrayList.add(movie!!)
                    }
                    movienowPlayingRecyclerView.adapter = MyAdapterNP(movienowPlayingArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}