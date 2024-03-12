package com.example.parcial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class FavsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favs)

        val adapter = FavAdapter(this, FactManager.instance.factsList)
        val listView = findViewById<ListView>(R.id.favsList)
        listView.adapter = adapter
    }
}