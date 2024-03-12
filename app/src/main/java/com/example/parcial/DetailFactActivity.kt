package com.example.parcial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class DetailFactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_fact)

        val nameI = intent.getStringExtra("name")
        val name = findViewById<TextView>(R.id.name)
        val category = findViewById<TextView>(R.id.category)
        val description = findViewById<TextView>(R.id.description)
        val imgBack = findViewById<ImageView>(R.id.imgBack)
        val fact = FactManager.instance.getFactByName(nameI.toString())
        val factsList = findViewById<ListView>(R.id.factsList)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, FactManager.instance.buildFactosAdapter(nameI.toString()))
        factsList.adapter = adapter
        if(fact != null){
            name.text = fact.name
            category.text = fact.category
            description.text = fact.description
            Picasso.get().load(fact.image_url).into(imgBack)
        }

    }
}