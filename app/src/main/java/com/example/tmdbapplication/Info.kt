package com.example.tmdbapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tmdbapplication.databinding.ActivityInfoBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.URL

class Info : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var database: DatabaseReference

    lateinit var castRecycle: RecyclerView   //you are not even declaring them only referring when you are using them
    var castData = JSONArray()

    lateinit var similerMovieRecycle: RecyclerView
    var similerMovieData = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        var myintent = intent
        var mid = myintent.getStringExtra("MovieID")
        var user = myintent.getStringExtra("User")

        if (user == "User"){
            var btn_addtoshow:Button=findViewById(R.id.btn_addToShow)
            btn_addtoshow.isVisible=false
        }


//        //overriding default back button to go to main menu
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                startActivity(Intent(applicationContext, Movies::class.java))
//            }
//        })

        lifecycleScope.launchWhenCreated {
            withContext(Dispatchers.IO) {
                getMovieInfo(mid.toString())
            }
        }

        //do this
        castRecycle = findViewById(R.id.castRecycler)
        castRecycle.adapter = CastAdapter()
        castRecycle.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL, false
        )

        similerMovieRecycle = findViewById(R.id.similerRecycler)
        similerMovieRecycle.adapter = SimilerAdapter()
        similerMovieRecycle.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL, false
        )

        loadCast(mid.toString())
        loadSimilerMovies(mid.toString())

        var addToShow: Button = findViewById(R.id.btn_addToShow)
        addToShow.setOnClickListener() {
            addMovies(mid.toString())
        }


    }

    private fun addMovies(id: String) {
        var name: TextView = findViewById(R.id.txtMovieTitle)
        var post: TextView = findViewById(R.id.posterHide)
        var date: TextView = findViewById(R.id.txtTimeGenreYear)
        var rating: TextView = findViewById(R.id.txtRating)
        var movieID = id
        var movieName = name.text.toString()
        var moviePoster = post.text.toString()
        var movieDate = date.text.toString()
        var movieRating = rating.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Movie")
        val Movie = Movie(movieID, movieName, moviePoster, movieDate, movieRating)

        database.child(movieID).setValue(Movie).addOnSuccessListener {
            Toast.makeText(applicationContext, "Movie added successfully!", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getMovieInfo(id: String?) {

        var imgBanner: ImageView = findViewById(R.id.imgBanner)
        var txtMovieTitle: TextView = findViewById(R.id.txtMovieTitle)
        var txtRating: TextView = findViewById(R.id.txtRating)
        var txtDescription: TextView = findViewById(R.id.txtDescription)
        txtDescription.movementMethod = ScrollingMovementMethod()
        var txtTimeGenreYear: TextView = findViewById(R.id.txtTimeGenreYear)
        var idHideText: TextView = findViewById(R.id.idHide)
        var posterHideText: TextView = findViewById(R.id.posterHide)

        var url =
            "https://api.themoviedb.org/3/movie/$id?api_key=552d2cc1c293befeb2042aa156f6dcf1&language=en-US"
        val resultJson = URL(url).readText()
        val jsonObj = JSONObject(resultJson)

        //accessing JSON tag
        var backdrop_path = jsonObj.getString("backdrop_path")
        var rating = jsonObj.getString("vote_average")
        var original_title = jsonObj.getString("original_title")
        var runtime = jsonObj.getString("runtime")
        var release_date = jsonObj.getString("release_date")
        var overview = jsonObj.getString("overview")
        var idHide = jsonObj.getString("id")
        var posterHide = jsonObj.getString("poster_path")


        //publishing data
        txtMovieTitle.text = original_title
        txtRating.text = rating.toString().take(3)
        txtTimeGenreYear.text = "•" + convertTime(runtime) + "  •" + release_date.take(4)
        txtDescription.text = overview
        idHideText.text = idHide
        posterHideText.text = posterHide


        //using picasso
        var iconUrl: String = "https://image.tmdb.org/t/p/original" + backdrop_path

        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post(Runnable {
            Picasso.get()
                .load(iconUrl)
                .into(imgBanner)
        })

    }


    /*
Creating Adapters and ViewHolders for displaying cast
 */
    private inner class CastAdapter : RecyclerView.Adapter<CastViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
            return CastViewHolder(view)
        }

        override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
            try {
                holder.castName.text = castData.getJSONObject(position).getString("name")
                var profile_path = castData.getJSONObject(position).getString("profile_path")
                var iconUrl: String = "https://image.tmdb.org/t/p/original" + profile_path

                val uiHandler = Handler(Looper.getMainLooper())
                uiHandler.post(Runnable {
                    Picasso.get()
                        .load(iconUrl)
                        .into(holder.castThumb)
                })
            } catch (e: JSONException) {

            }
        }

        override fun getItemCount(): Int {
            return castData.length()
        }

    }

    private inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var castThumb: ShapeableImageView = itemView.findViewById(R.id.imgCast)
        var castName: TextView = itemView.findViewById(R.id.castName)
    }

    fun loadCast(id: String) {
        var request = JsonObjectRequest(
            Request.Method.GET,
            "https://api.themoviedb.org/3/movie/$id/credits?api_key=552d2cc1c293befeb2042aa156f6dcf1&language=en-US",
            JSONObject(),
            { res ->
                try {
                    castData = res.getJSONArray("cast")
                    castRecycle.adapter?.notifyDataSetChanged()            //? = making this optional
                } catch (e: JSONException) {

                }
            },
            { error ->
                Log.e("Error", error.toString())
            })

        Volley.newRequestQueue(applicationContext).add(request)

    }

    fun convertTime(minutes: String): String {

        var time: String = ""
        var a = minutes.toInt()

        if (a <= 60) {
            time = minutes + "m"
        } else {
            try {
                var aforhours = minutes.toFloat() / 60
                var bforminutes = aforhours - ((aforhours.toString()).take(1)).toFloat()
                var bequalsc = (bforminutes * 60)
                time =
                    ((aforhours.toString()).take(1)) + "h " + ((bequalsc.toString()).take(2)).toInt() + "m"

            } catch (e: java.lang.NumberFormatException) {
                Log.e("Error", e.toString())
            } catch (e: java.lang.NullPointerException) {
                Log.e("Error", e.toString())
            }
        }
        return time
    }


    /*
    Creating Adapters and ViewHolders for displaying similer movies
     */
    private inner class SimilerAdapter : RecyclerView.Adapter<SimilerMoviesViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilerMoviesViewHolder {
            val show =
                LayoutInflater.from(parent.context).inflate(R.layout.similer_movies, parent, false)

            show.setOnClickListener() {
                var myintent = Intent(applicationContext, Info::class.java)
                myintent.putExtra("MovieID", SimilerMoviesViewHolder(show).movID.text)
                myintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                //Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                startActivity(myintent)

            }

            return SimilerMoviesViewHolder(show)
        }

        override fun onBindViewHolder(holder: SimilerMoviesViewHolder, position: Int) {
            try {
                var fsdv: String = similerMovieData.getJSONObject(position).getString("id")
                if (fsdv.length.equals(0)) {
                    holder.movID.text = "24150" //temp value
                } else {
                    holder.movID.text = similerMovieData.getJSONObject(position).getString("id")
                }
                holder.similerMoviesName.text =
                    similerMovieData.getJSONObject(position).getString("title")
                var poster_path = similerMovieData.getJSONObject(position).getString("poster_path")
                var iconUrl: String = "https://image.tmdb.org/t/p/original" + poster_path

                val uiHandler = Handler(Looper.getMainLooper())
                uiHandler.post(Runnable {
                    Picasso.get()
                        .load(iconUrl)
                        .into(holder.similerMoviesThumb)
                })
            } catch (e: JSONException) {
                Log.e("Error", e.toString())
            } catch (e: java.lang.NullPointerException) {
                Log.e("Error", e.toString())
            } catch (e: FileNotFoundException) {
                Log.e("Error", e.toString())
            }
        }

        override fun getItemCount(): Int {
            return similerMovieData.length()
        }
    }

    private inner class SimilerMoviesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var similerMoviesThumb: ShapeableImageView = itemView.findViewById(R.id.similerMovie)
        var similerMoviesName: TextView = itemView.findViewById(R.id.similerMovieName)
        var movID: TextView = itemView.findViewById(R.id.movID)

    }

    fun loadSimilerMovies(id: String) {
        var request = JsonObjectRequest(
            Request.Method.GET,
            "https://api.themoviedb.org/3/movie/$id/similar?api_key=552d2cc1c293befeb2042aa156f6dcf1&language=en-US&page=1",
            JSONObject(),
            { res ->
                try {
                    similerMovieData = res.getJSONArray("results")
                    similerMovieRecycle.adapter?.notifyDataSetChanged()            //? = making this optional
                } catch (e: JSONException) {

                }
            },
            { error ->
                Log.e("Error", error.toString())
            })

        Volley.newRequestQueue(applicationContext).add(request)

    }

}