package com.example.tmdbapplication

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Search : AppCompatActivity() {

    lateinit var searchedMovieRecycle: RecyclerView   //you are not even declaring them only referring when you are using them
    var searchedMovieData = JSONArray()
    lateinit var user:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        var myintent = intent
        var movieName = myintent.getStringExtra("MovieName")
        user = myintent.getStringExtra("User").toString()

        loadSearchedMovie(movieName.toString())

        searchedMovieRecycle = findViewById(R.id.searchedMovieRecycler)
        searchedMovieRecycle.adapter = SearchMovieAdapter()
        searchedMovieRecycle.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL, false
        )

    }

    private inner class SearchMovieAdapter : RecyclerView.Adapter<SearchMovieViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)

            view.setOnClickListener(){
                var myintent = Intent(applicationContext, Info::class.java)
                myintent.putExtra("MovieID", SearchMovieViewHolder(view).movieId.text)
                myintent.putExtra("User", user)
                startActivity(myintent)
            }

            return SearchMovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
            try {
                holder.movieTitle.text = searchedMovieData.getJSONObject(position).getString("title")
                holder.releaseDate.text =
                    searchedMovieData.getJSONObject(position).getString("release_date")

                holder.movieId.text = searchedMovieData.getJSONObject(position).getString("id")

                var poster_path = searchedMovieData.getJSONObject(position).getString("poster_path")
                var iconUrl: String = "https://image.tmdb.org/t/p/original" + poster_path

                val uiHandler = Handler(Looper.getMainLooper())
                uiHandler.post(Runnable {
                    Picasso.get()
                        .load(iconUrl)
                        .into(holder.movieThumb)
                })


            } catch (e: JSONException) {

            }
        }

        override fun getItemCount(): Int {
            return searchedMovieData.length()
        }

    }

    private inner class SearchMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var movieTitle: TextView = itemView.findViewById(R.id.txtMovieNameSearched)
        var releaseDate: TextView = itemView.findViewById(R.id.txtReleaseDateSearched)
        var movieThumb: ImageView = itemView.findViewById(R.id.imageViewSearched)
        var movieId: TextView = itemView.findViewById(R.id.txtIdSearched)
    }



    private fun loadSearchedMovie(movieName:String){
        var request = JsonObjectRequest(
            Request.Method.GET,
            "\n" +
                    "https://api.themoviedb.org/3/search/movie?api_key=552d2cc1c293befeb2042aa156f6dcf1&language=en-US&query=$movieName&page=1&include_adult=false",
            JSONObject(),
            { res ->
                try {
                    searchedMovieData = res.getJSONArray("results")
                    searchedMovieRecycle.adapter?.notifyDataSetChanged()            //? = making this optional
                } catch (e: JSONException) {

                }
            },
            { error ->
                Log.e("Error", error.toString())
            })

        Volley.newRequestQueue(applicationContext).add(request)
    }


}