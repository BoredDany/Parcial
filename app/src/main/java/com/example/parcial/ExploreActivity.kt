package com.example.parcial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class ExploreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        val listFavs = findViewById<ListView>(R.id.listFavs)
        FactManager.instance.facts = FactManager.instance.getFacts(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, FactManager.instance.buildAdapter( this))
        listFavs.adapter = adapter

        listFavs.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val intent = Intent(baseContext, DetailFactActivity::class.java)
                intent.putExtra("name", listFavs.getItemAtPosition(position).toString())
                startActivity(intent)
            }
        })
    }
}