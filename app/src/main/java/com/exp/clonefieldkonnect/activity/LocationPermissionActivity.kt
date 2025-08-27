package com.exp.clonefieldkonnect.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.exp.clonefieldkonnect.R

class LocationPermissionActivity : AppCompatActivity() {
    lateinit var cardAllow: CardView
    lateinit var imgClose: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_permission)
        initViews()
    }

    private fun initViews() {
        cardAllow = findViewById(R.id.cardAllow)
        imgClose = findViewById(R.id.imgClose)

        cardAllow.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),

                    2
                )
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),

                    2
                )
            }

        }
        imgClose.setOnClickListener {
            finishAffinity()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> {
                val perms = java.util.HashMap<String, Int>()
                // Initial
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] =
                    PackageManager.PERMISSION_GRANTED

                // Fill with results
                for (i in permissions.indices)
                    perms[permissions[i]] = grantResults[i]
                // Check for ACCESS_FINE_LOCATION
                if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED

                ) {
                    Toast.makeText(this,"Allow permission",Toast.LENGTH_LONG).show()
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }// other 'case' lines to check for other
        // permissions this app might request
    }

}