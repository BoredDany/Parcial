package com.example.parcial

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.parcial.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap ?= null
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mSensorManager: SensorManager
    private lateinit var mLiteSensor: Sensor
    private lateinit var mLightSensorEventListener: SensorEventListener
    private lateinit var mGeocoder: Geocoder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mLiteSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!

        mLightSensorEventListener = object : SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
                if (mMap != null) {
                    if (event!!.values[0] < 5000) {
                        Log.i("MAPS", "DARK MAP " + event.values[0])
                        mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_style_silver))
                    } else {
                        Log.i("MAPS", "LIGHT MAP " + event.values[0])
                        mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_style_default))
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

        //buscar direccion
        mGeocoder = Geocoder(baseContext)
        val text = findViewById<EditText>(R.id.buscaTxt)
        val addressString = text.toString()

        text.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEND) {
                if (addressString.isNotEmpty()) {
                    try {
                        val addresses = mGeocoder.getFromLocationName(addressString, 2)
                        if (addresses != null && addresses.isNotEmpty()) {
                            val addressResult = addresses[0]
                            val position = LatLng(addressResult.latitude, addressResult.longitude)
                            if (mMap != null) {
                                //Agregar Marcador al mapa
                                mMap!!.moveCamera(CameraUpdateFactory.zoomTo(15F))
                                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(position))
                                mMap!!.addMarker(MarkerOptions().position(position).title("PGEOCODER").snippet("GEOCODER").alpha(1F));

                            } else {
                                Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(this, "La dirección esta vacía", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //mMap!!.uiSettings.isZoomGesturesEnabled = true

        mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_silver))





        // Add a marker in Sydney and move the camera
        val bolivar = LatLng(4.59813470563, -74.0760469437)
        val museo = LatLng(4.60209158756, -74.0718841553)
        val m1 = LatLng(4.70209158756, -74.0658841553)
        val m2 = LatLng(4.90209158756, -74.0608841553)
        val m3 = LatLng(4.10009158756, -74.0508841553)

        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(10F))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(bolivar))
        mMap!!.addMarker(MarkerOptions().position(bolivar).title("Plaza de bolivar").snippet("plaza").alpha(0.5F));
        mMap!!.addMarker(MarkerOptions().position(museo).title("Museo del oro").snippet("cultura").alpha(1F));
        mMap!!.addMarker(MarkerOptions().position(m1).title("m1").snippet("otro m1").alpha(1F));
        mMap!!.addMarker(MarkerOptions().position(m2).title("m2").snippet("orr").alpha(1F));
        mMap!!.addMarker(MarkerOptions().position(m3).title("m3").snippet("m3").alpha(1F));

        //eliminar marcadroes

        //limpiar mapa
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(mLightSensorEventListener, mLiteSensor,
            SensorManager.SENSOR_DELAY_NORMAL)
    }
    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(mLightSensorEventListener)
    }
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable: Drawable? = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}