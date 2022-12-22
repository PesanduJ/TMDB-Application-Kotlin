package com.example.tmdbapplication

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MyAdapterNPremiering(private val movieList: ArrayList<Movie>) :RecyclerView.Adapter<MyAdapterNPremiering.MyViewHolderNPremiering>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderNPremiering {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.nowpremiering_item, parent, false)

        itemView.setOnClickListener(){
            var myintent = Intent(itemView.context, Info::class.java)
            myintent.putExtra("MovieID", MyViewHolderNPremiering(itemView).id.text.toString())
            myintent.putExtra("User", "User")
            itemView.context.startActivity(myintent)
        }
        return MyAdapterNPremiering.MyViewHolderNPremiering(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderNPremiering, position: Int) {
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

        //        holder.removeShow.setOnClickListener(){
//            var inte = Intent(holder.itemView.context,Movies::class.java)
//            holder.itemView.context.startActivity(inte)
//            Toast.makeText(holder.itemView.context,"${holder.id.text}",Toast.LENGTH_SHORT).show()
//        }

        holder.bookTicket.setOnClickListener(){

            var myintent = Intent(holder.itemView.context,CheckDB::class.java)
            myintent.putExtra("MovID", MyViewHolderNPremiering(holder.itemView).id.text)
            holder.itemView.context.startActivity(myintent)
        }

    }

    override fun getItemCount(): Int {
        return movieList.size
    }


    class MyViewHolderNPremiering(itemView: View) : RecyclerView.ViewHolder(itemView){
        var id: TextView = itemView.findViewById(R.id.txtIdNotVisibleNP)
        var time: TextView = itemView.findViewById(R.id.txtTimeNP)
        var name: TextView = itemView.findViewById(R.id.txtMovieNameNP)
        var poster: ImageView = itemView.findViewById(R.id.topMoviesImageViewNPN)
        var rating: TextView = itemView.findViewById(R.id.txtIMDBNP)
        var bookTicket: Button = itemView.findViewById(R.id.btn_bookTicketNP)
    }

}
