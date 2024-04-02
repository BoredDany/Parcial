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
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.math.roundToInt

class BindingActivity : AppCompatActivity() {
    lateinit var binding: ActivityBindingBinding
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    val RADIUS_OF_EARTH_KM = 6371.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binding)

        //inicializar pantalla
        binding = ActivityBindingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //updates
        mLocationRequest = createLocationRequest()
        //inicializar callback
        mLocationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                Log.i("LOCATION", "Location update in the callback: $location")
                if (location != null) {
                    binding.elevation.text = location.altitude.toString()
                    binding.latitude.text = location.latitude.toString()
                    binding.longitude.text = location.longitude.toString()
                }
            }
        }

        //Al tener permisos
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                //getLocation()
                startLocationUpdates()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this permission
                Toast.makeText(this, "Se requiere contactos", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
        binding.distance.text ="distance"

        binding.btnmaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


    }

    private fun createLocationRequest(): LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply{
            setMinUpdateIntervalMillis(5000)
        }.build()

    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }

    }
    fun distance(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val latDistance = Math.toRadians(lat1 - lat2)
        val lngDistance = Math.toRadians(long1 - long2)
        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val result = RADIUS_OF_EARTH_KM * c
        return (result * 100.0).roundToInt() / 100.0
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
            }else{
                Log.i("LOCATION", "null")
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
                    startLocationUpdates()
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