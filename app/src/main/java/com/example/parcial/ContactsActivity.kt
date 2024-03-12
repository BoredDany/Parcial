package com.example.parcial

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException

class ContactsActivity : AppCompatActivity() {
    var mProjection: Array<String>? = null
    var mCursor: Cursor? = null
    var mContactsAdapter: ContactAdapter? = null
    var mlista: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        permisoContactos()

        val mlista = findViewById<ListView>(R.id.contacts)
        mProjection = arrayOf(ContactsContract.Profile._ID, ContactsContract.Profile.DISPLAY_NAME_PRIMARY)
        mContactsAdapter = ContactAdapter(this, null, 0)
        mlista?.adapter = mContactsAdapter

        initView()

    }

    //PERMISSION
    fun permisoContactos(){

        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "Puede ver contactos", Toast.LENGTH_SHORT).show()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.READ_CONTACTS) -> {
                // In an educational UI, explain to the user why your app requires this permission
                Toast.makeText(this, "Se requiere contactos", Toast.LENGTH_SHORT).show()
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    Permission.REQUEST_READ_CONTACTS)
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    Permission.REQUEST_READ_CONTACTS)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Permission.REQUEST_READ_CONTACTS ->{
                if (requestCode == Permission.REQUEST_READ_CONTACTS && resultCode == RESULT_OK) {
                    //ok
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Permission.REQUEST_READ_CONTACTS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Toast.makeText(this, "Gracias contactos", Toast.LENGTH_SHORT).show()
                } else {
                    // Explain to the user that the feature is unavailable
                    Toast.makeText(this, "No tengo acceso a contactos", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun initView() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            mCursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, null
            )
            mContactsAdapter?.changeCursor(mCursor)
        }
    }
}