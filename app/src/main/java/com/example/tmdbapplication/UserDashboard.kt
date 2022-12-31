package com.example.tmdbapplication

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.File

class UserDashboard : AppCompatActivity() {

    lateinit var topmovieRecycle: RecyclerView   //you are not even declaring them only referring when you are using them
    var topmovieData = JSONArray()

    private lateinit var dbref:DatabaseReference
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var userNowPremieringRecyclerView: RecyclerView
    private lateinit var userNowPremieringArrayList: ArrayList<Movie>

    lateinit var btn_userSearch:Button
    lateinit var usersearch:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        loadMovie()
        getUserData()

        topmovieRecycle = findViewById(R.id.userTopMoviesRecyclerView)
        topmovieRecycle.adapter = UserTopMoviesAdapter()
        topmovieRecycle.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.HORIZONTAL, false
        )

        userNowPremieringRecyclerView = findViewById(R.id.userNowPremieringRecyclerView)
        userNowPremieringRecyclerView.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.HORIZONTAL, false)
        userNowPremieringRecyclerView.setHasFixedSize(true)
        userNowPremieringArrayList = arrayListOf<Movie>()
        getMovieData()

        btn_userSearch=findViewById(R.id.btnUserSearchMovie)
        usersearch=findViewById(R.id.txtUserSearchMovie)

        btn_userSearch.setOnClickListener(){
            var myintent = Intent(applicationContext, Search::class.java)
            myintent.putExtra("MovieName", usersearch.getText().toString())
            myintent.putExtra("User", "User")
            startActivity(myintent)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.btm_navigationUserDashboard)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> focusHome()

                R.id.search -> focusSearch()

                R.id.settings -> focusSettings()

                R.id.location -> focusLocation()

                else -> {}
            }
            true
        }


    }

    fun focusHome(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    fun focusSearch(){
        val sm :EditText= findViewById(R.id.txtUserSearchMovie)
        sm.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(sm, InputMethodManager.SHOW_IMPLICIT)
    }

    fun focusSettings(){
        var myintent = Intent(applicationContext, Settings::class.java)
        startActivity(myintent)
    }

    fun focusLocation(){
        var myintent = Intent(applicationContext, Location::class.java)
        startActivity(myintent)
    }
//*******************************************************************************************************************
    private inner class UserTopMoviesAdapter: RecyclerView.Adapter<UserTopMoviesViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTopMoviesViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.topmovie_item, parent, false)

            view.setOnClickListener(){
                var myintent = Intent(applicationContext, Info::class.java)
                myintent.putExtra("MovieID", UserTopMoviesViewHolder(view).movieId.text)
                myintent.putExtra("User", "User")
                startActivity(myintent)
            }

            return UserTopMoviesViewHolder(view)
        }

        override fun onBindViewHolder(holder: UserTopMoviesViewHolder, position: Int) {
            try {
                holder.movieTitle.text = topmovieData.getJSONObject(position).getString("title")
                holder.releaseDate.text =
                    topmovieData.getJSONObject(position).getString("release_date")

                holder.movieId.text = topmovieData.getJSONObject(position).getString("id")

                var poster_path = topmovieData.getJSONObject(position).getString("poster_path")
                var iconUrl: String = "https://image.tmdb.org/t/p/original" + poster_path

                val uiHandler = Handler(Looper.getMainLooper())
                uiHandler.post(Runnable {
                    Picasso.get()
                        .load(iconUrl)
                        .into(holder.movieThumb)
                })

                holder.bookTicket.setOnClickListener(){

                    var myintent = Intent(holder.itemView.context,CheckDB::class.java)
                    myintent.putExtra("MovID", UserTopMoviesViewHolder(holder.itemView).movieId.text)
                    holder.itemView.context.startActivity(myintent)
                }

            } catch (e: JSONException) {
                Toast.makeText(applicationContext, "JSONException!", Toast.LENGTH_LONG).show()
            } catch (e: SQLiteException) {
                Toast.makeText(applicationContext, "SQLiteException!", Toast.LENGTH_LONG).show()
            }
        }

        override fun getItemCount(): Int {
            return topmovieData.length()
        }
    }

    private inner class UserTopMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieTitle: TextView = itemView.findViewById(R.id.txtMovieName)
        var releaseDate: TextView = itemView.findViewById(R.id.txtIMDB)
        var movieThumb: ImageView = itemView.findViewById(R.id.topMoviesImageView)
        var movieId: TextView = itemView.findViewById(R.id.txtIdNotVisible)
        var bookTicket: Button = itemView.findViewById(R.id.btn_bookTicket)
    }

    fun loadMovie() {
        var request = JsonObjectRequest(
            Request.Method.GET,
            "https://api.themoviedb.org/4/list/1?page=1&api_key=552d2cc1c293befeb2042aa156f6dcf1",
            JSONObject(),
            { res ->
                try {
                    topmovieData = res.getJSONArray("results")
                    topmovieRecycle.adapter?.notifyDataSetChanged()            //? = making this optional
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, "JSONException!", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("Error", error.toString())
            })

        Volley.newRequestQueue(applicationContext).add(request)
    }
    //*******************************************************************************************************************


    private fun getMovieData() {

        dbref = FirebaseDatabase.getInstance().getReference("Movie")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (movieSnapshot in snapshot.children){
                        val movie = movieSnapshot.getValue(Movie::class.java)
                        userNowPremieringArrayList.add(movie!!)
                    }
                    userNowPremieringRecyclerView.adapter = MyAdapterNPremiering(userNowPremieringArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    //*******************************************************************************************************************

    fun getUserData(){

        var lastName: String = ""
        var helloText:TextView = findViewById(R.id.hello)

        var userDetailsIntent = intent
        var id = userDetailsIntent.getStringExtra("UserID")

        dbref = FirebaseDatabase.getInstance().getReference("Users")
        var query: Query = dbref.orderByChild("nic").equalTo("$id")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (snapshot in snapshot.children) {
                        var firstName = snapshot.child("firstName").value.toString()
                        helloText.setText("Hello, ${firstName.toString()}")
                        getUserImage(id.toString())
                    }
                }
                else{
                    Toast.makeText(applicationContext,"Unfortunate!", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    fun getUserImage(nic:String){
        var userProfileIcon:ShapeableImageView = findViewById(R.id.userProfileIcon)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference.child("Users$nic")

        val localfile = File.createTempFile("tempImage","png")
        storageReference.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            userProfileIcon.setImageBitmap(bitmap)

        }.addOnFailureListener {
            Toast.makeText(applicationContext,"No user image found!", Toast.LENGTH_SHORT).show()
        }

    }
}