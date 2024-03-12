package com.example.parcial

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gender = findViewById<Spinner>(R.id.gender)
        val btnCamera = findViewById<ImageButton>(R.id.btnCamera)
        val btnGallery = findViewById<ImageButton>(R.id.btnGallery)
        val btnGo = findViewById<Button>(R.id.btnGo)

        //SPINNER
        gender.onItemSelectedListener = this

        //PERMISO
        permisoCamara()

        //CAMERA
        btnCamera.setOnClickListener { takePic() }

        //GALLERY
        btnGallery.setOnClickListener {  }

        //GO
        btnGo.setOnClickListener { goToPanel(gender) }

    }

    //SPINNER
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


    //PERMISSION
    fun permisoCamara(){

        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "Puede usar la camara", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.CAMERA) -> {
                // In an educational UI, explain to the user why your app requires this permission
                Toast.makeText(this, "Se requiere camara", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    Permission.MY_PERMISSION_REQUEST_CAMERA)
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    Permission.MY_PERMISSION_REQUEST_CAMERA)
            }
        }


    }

    fun permisoGaleria(){
        val pickImage = Intent(Intent.ACTION_PICK)
        pickImage.type = "image/*"
        startActivityForResult(pickImage, Permission.IMAGE_PICKER_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Permission.IMAGE_PICKER_REQUEST ->{
                if(resultCode == Activity.RESULT_OK){
                    try {
                        //Logica de seleccion de imagen
                    } catch (e: FileNotFoundException){
                        e.printStackTrace()
                    }
                }
            }
            Permission.REQUEST_IMAGE_CAPTURE ->{
                if (requestCode == Permission.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val imgProfile = findViewById<ImageView>(R.id.imgCamera)
                    imgProfile.setImageBitmap(imageBitmap)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Permission.MY_PERMISSION_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Toast.makeText(this, "Gracias", Toast.LENGTH_SHORT).show()
                } else {
                    // Explain to the user that the feature is unavailable
                    Toast.makeText(this, "No puede tomar fotos", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun takePic(){
        val permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val takePictureIntent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, Permission.REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(this, "No hay una cámara disponible", Toast.LENGTH_SHORT).show();
            }

        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA),
                Permission.MY_PERMISSION_REQUEST_CAMERA)
        }
    }

    //ACTIVITY
    fun goToPanel(spinner: Spinner){
        val name = findViewById<EditText>(R.id.name)
        val birthdate = findViewById<EditText>(R.id.birthdate)
        val gender = spinner.selectedItem.toString()
        val intent = Intent(this, PanelActivity::class.java)
        val bundle = Bundle()
        bundle.putString("name", name.text.toString())
        bundle.putString("gender", gender)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

}