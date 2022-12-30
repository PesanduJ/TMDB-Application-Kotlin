package com.example.tmdbapplication

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso



class MyAdapterNS(private val movieList:ArrayList<Movie>) : RecyclerView.Adapter<MyAdapterNS.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.nowshowing_item,parent,false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = movieList[position]
        holder.id.text = currentitem.id
        holder.name.text = currentitem.name
        holder.time.text = currentitem.time
        holder.rating.text = currentitem.rating

        var posterPath = currentitem.poster.toString()
        var iconUrl: String = "https://image.tmdb.org/t/p/original" + posterPath

        val uiHandler = Handler(Looper.getMainLooper())
        uiHandler.post(Runnable {
            Picasso.get()
                .load(iconUrl)
                .into(holder.poster)
        })

        holder.removeShow.setOnClickListener(){
            removeMovieData(holder.id.text.toString(), holder.itemView)
        }


    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var id:TextView=itemView.findViewById(R.id.txtId1)
        var time:TextView=itemView.findViewById(R.id.txtDate)
        var name:TextView=itemView.findViewById(R.id.txtName)
        var poster:ImageView = itemView.findViewById(R.id.nowShowingImageView)
        var removeShow:Button = itemView.findViewById(R.id.btn_removeShow)
        var rating:TextView = itemView.findViewById(R.id.txtRating)

    }

    private fun removeMovieData(id:String,itemView :View){

        var dbref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Movie")
        dbref.child(id).removeValue().addOnSuccessListener {
            Toast.makeText(itemView.context,"Movie removed!",Toast.LENGTH_SHORT).show()
            (itemView.context as Activity).finish()
            var inte = Intent(itemView.context,Database::class.java)
            itemView.context.startActivity(inte)


        }.addOnFailureListener {
            Toast.makeText(itemView.context,"Error occurred",Toast.LENGTH_SHORT).show()
        }

    }
}
