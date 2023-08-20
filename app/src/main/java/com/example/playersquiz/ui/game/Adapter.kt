package com.example.playersquiz.ui.game

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.playersquiz.R

class Adapter(private val uriList: MutableList<String>, private val yearList: MutableList<String>, private val context: Context) : BaseAdapter() {


    override fun getCount(): Int {
        return yearList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("CheckResult")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = LayoutInflater.from(context).inflate(R.layout.squad_item, null)

        //find view
        val squadyearview: TextView = view.findViewById(R.id.year)
        val squadimgview: ImageView = view.findViewById(R.id.imageView)

        Log.d("getView", "yearList : ${yearList} uriList ${uriList} ")
        //set data
        squadyearview.text = yearList[position]

        //set Image
        Glide.with(context)
            .load(uriList)
            .into(squadimgview)

        return view
    }

}