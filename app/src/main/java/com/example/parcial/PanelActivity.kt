package com.example.parcial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        val bundle = intent.getBundleExtra("bundle")
        val nameWritten = bundle?.getString("name")
        val genderWritten = bundle?.getString("gender")

        val name = findViewById<TextView>(R.id.name)
        val gender = findViewById<TextView>(R.id.gender)
        val btnContacts = findViewById<Button>(R.id.btnContacts)
        val btnExplorer = findViewById<Button>(R.id.btnExplorer)
        val btnFavs = findViewById<Button>(R.id.btnFavs)

        name.text = nameWritten
        gender.text = genderWritten

        btnContacts.setOnClickListener {
            val intent = Intent(this, ContactsActivity::class.java)
            startActivity(intent)
        }

        btnExplorer.setOnClickListener {
            val intent = Intent(this, ExploreActivity::class.java)
            startActivity(intent)
        }

        btnFavs.setOnClickListener {
            val intent = Intent(this, FavsActivity::class.java)
            startActivity(intent)
        }

    }
}