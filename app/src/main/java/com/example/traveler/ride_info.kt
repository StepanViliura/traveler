package com.example.traveler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.TextView


class ride_info : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rideId = intent.getIntExtra("rideId", 0)
        setContentView(R.layout.activity_ride_info)

        val textV = findViewById<TextView>(R.id.ride_id_tv)
        textV.text = "Ride ID = " + rideId.toString()

    }
}
