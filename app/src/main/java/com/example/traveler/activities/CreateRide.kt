package com.example.traveler.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.traveler.MainActivity
import com.example.traveler.R
import com.example.traveler.data.Ride
import com.example.traveler.data.RideInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_ride.*

import com.example.traveler.database.SignedUser
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CreateRide : AppCompatActivity() {
    val database = FirebaseFirestore.getInstance()
    val ride = database.document("rides/${SignedUser.getInstance().userInfo?.id}").collection("own")
    val TAG = "RIDE_CREATING"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ride)
        setupUI()
    }

    private fun setupUI() {
        ceate_ride_btn.setOnClickListener {
            createRide()
        }
    }

    private fun createRide() {
        val unix = LocalDate.parse(start_time_et.text.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")).atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond

        database.collection("rides").add(
            Ride(
                create_ride_from_et.text.toString(),
                create_ride_destination_et.text.toString(),
                unix,
                number_of_passangers_et.text.toString(),
                SignedUser.getInstance().userInfo?.id,
                arrayListOf()
            )
        ).addOnCompleteListener { document ->
            if (document.isSuccessful) {
                ride.add(RideInfo(document.getResult()!!.id, "rider"))
                startActivity(MainActivity.getLaunchIntent(this))
            }
            else {
                Toast.makeText(applicationContext, "Something goes wrong!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
