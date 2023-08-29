package com.example.playersquiz.ui.game.Adapters

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
import com.example.playersquiz.remote.models.RootMetadataSupportResponseRemoteModel

class Adapter(private val transfersList: List<RootMetadataSupportResponseRemoteModel>, private val context: Context) : BaseAdapter() {


    override fun getCount(): Int {
        return transfersList.size
    }

    override fun getItem(p0: Int): Any? {
        return transfersList[p0]
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

        Log.d("getView", "yearList : ${transfersList}")
        //set data
        val transfer = transfersList[position]
        squadyearview.text = transfer.player?.name ?: ""

        //set Image
        val imageUrl  = transfer.player?.id ?: ""
        Glide.with(context)
            .load(imageUrl)
            .into(squadimgview)

        return view
    }

}