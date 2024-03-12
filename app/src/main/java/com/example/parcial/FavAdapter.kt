package com.example.parcial

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class FavAdapter (private val context: Context?, private val facts: List<Fact>) : BaseAdapter(){

    override fun getCount(): Int {
        return facts.size
    }

    override fun getItem(position: Int): Fact {
        return facts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // You can use a unique ID if available
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.view_fav, parent, false)

        val fact = view.findViewById<TextView>(R.id.fact)
        val imgCategory = view.findViewById<ImageView>(R.id.imgCategory)

        val factObject = getItem(position) // Get Fact object at position
        fact?.text = factObject.name
        // ... (logic for image based on category) ...

        return view
    }
}