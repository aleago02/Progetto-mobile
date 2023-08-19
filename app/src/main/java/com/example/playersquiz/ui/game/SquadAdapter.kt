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
import java.io.InputStream
import java.net.URL


class SquadAdapter(private val yearList: MutableList<String>, private val uriList: MutableList<String>): RecyclerView.Adapter<SquadAdapter.SquadViewHolder>() {

    class SquadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val squadyearview: TextView = itemView.findViewById(R.id.year)
        private val squadimgview: ImageView = itemView.findViewById(R.id.imageView)

        private fun getBitmapFromURL(src: String?): Bitmap? {
            val inputStream: InputStream
            val bitmap: Bitmap
            return try {
                Log.e("src", src!!)
                inputStream = URL(src).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
                Log.e("Bitmap", "returned")
                bitmap
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Exception", e.message!!)
                null
            }
        }

        fun bind(year: String, url: String) {
            squadyearview.text = year
            squadimgview.setImageBitmap(getBitmapFromURL(url))
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