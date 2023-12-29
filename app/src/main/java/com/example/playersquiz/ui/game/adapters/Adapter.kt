package com.example.playersquiz.ui.game.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.playersquiz.databinding.SquadItemBinding

class Adapter(private val uriList: MutableList<String>, private val yearList: MutableList<String>, private val context: Context) : BaseAdapter() {

    private lateinit var binding: SquadItemBinding

    override fun getCount(): Int {
        return yearList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("CheckResult", "ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        binding = SquadItemBinding.inflate(LayoutInflater.from(context), parent, false)

        //set data
        binding.year.text = yearList[position]

        //set Image
        Glide.with(context)
            .load(uriList[position])
            .into(binding.imageView)

        return binding.root
    }

}