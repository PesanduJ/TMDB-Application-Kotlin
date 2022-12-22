package com.example.tmdbapplication

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapterNP(private val movieList: ArrayList<Movie>) :
    RecyclerView.Adapter<MyAdapterNP.MyViewHolderNP>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNP {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.nowplaying_item, parent, false)
        return MyAdapterNP.MyViewHolderNP(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderNP, position: Int) {
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

    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class MyViewHolderNP(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView = itemView.findViewById(R.id.txtId11)
        var time: TextView = itemView.findViewById(R.id.txtReleaseDate1)
        var name: TextView = itemView.findViewById(R.id.txtMovieName1)
        var poster: ImageView = itemView.findViewById(R.id.nowPlayingImageView)
        var rating: TextView = itemView.findViewById(R.id.txtRating1)
    }
}

