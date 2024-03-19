package com.example.parcial

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.parcial.databinding.ActivityBindingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class BindingActivity : AppCompatActivity() {
    lateinit var binding: ActivityBindingBinding
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binding)

        //inicializar pantalla
        binding = ActivityBindingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        //Al tener permisos
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this permission
                Toast.makeText(this, "Se requiere contactos", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    Permission.MY_PERMISSION_REQUEST_LOCATION)
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    Permission.REQUEST_READ_CONTACTS)
            }
        }

        //acceso directo a los objetos
        binding.locationTxt.text ="hola"
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            Log.i("LOCATION", "onSuccess Location")
            if (location != null) {
                Log.i("LOCATION", "Longitud:" + location.longitude)
                Log.i("LOCATION", "Latitud:" + location.latitude)
                binding.longitude.text = location.longitude.toString()
                binding.latitude.text = location.latitude.toString()
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Permission.MY_PERMISSION_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Toast.makeText(this, "LOCATION", Toast.LENGTH_SHORT).show()
                } else {
                    // Explain to the user that the feature is unavailable
                    Toast.makeText(this, "NO LOCATION", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
}