package com.exp.clonefieldkonnect.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exp.clonefieldkonnect.R
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions

class CustomerMapActivity : AppCompatActivity()/*, OnMapReadyCallback */{

//    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_map)

//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.mapFragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

//    override fun onMapReady(map: GoogleMap) {
//        googleMap = map
//
//        // Dummy Lat/Lng list
//        val locations = listOf(
//            LatLng(22.7196, 75.8577), // Indore
//            LatLng(28.7041, 77.1025), // Delhi
//            LatLng(19.0760, 72.8777), // Mumbai
//            LatLng(12.9716, 77.5946)  // Bangalore
//        )
//
//        // Add markers for each location
//        for (latLng in locations) {
//            googleMap.addMarker(
//                MarkerOptions().position(latLng).title("Marker at ${latLng.latitude}, ${latLng.longitude}")
//            )
//        }
//
//        // Move camera to first location
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 5f))
//    }
}
