package com.example.playersquiz.ui.game



import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playersquiz.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class SquadAdapter(val yearList: MutableList<String>, val uriList: MutableList<String>): RecyclerView.Adapter<SquadAdapter.SquadViewHolder>() {

    class SquadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val SquadyearView: TextView = itemView.findViewById(R.id.year)
        private val SquadimgView: ImageView = itemView.findViewById(R.id.imageView)

        fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                Log.e("src", src!!)
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val myBitmap = BitmapFactory.decodeStream(input)
                Log.e("Bitmap", "returned")
                return myBitmap
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Exception", e.message!!)
                return null
            }
        }

        fun bind(year: String, url: String) {
            SquadyearView.text = year
            SquadimgView.setImageBitmap(getBitmapFromURL(url));
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.squad_item, parent, false)

        return SquadViewHolder(view)
    }

    override fun onBindViewHolder(holder: SquadViewHolder, position: Int) {
        holder.bind(yearList[position], uriList[position])
    }

    override fun getItemCount(): Int {
        return yearList.size
    }


}