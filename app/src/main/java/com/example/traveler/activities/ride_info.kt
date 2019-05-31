package com.example.traveler.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.traveler.R
import com.example.traveler.adapters.CurrentRidesAdapter
import com.example.traveler.adapters.PassangersAdapter
import com.example.traveler.data.Ride
import com.example.traveler.data.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ride_info.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.Instant
import java.time.ZoneId
import kotlin.concurrent.thread


class ride_info : AppCompatActivity() {
    val database = FirebaseFirestore.getInstance()
    var passangers = ArrayList<UserInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rideId = intent.getStringExtra("rideId")

        setContentView(R.layout.activity_ride_info)
        findRideInfo(rideId)
    }
    private fun findRideInfo(id: String?){
        database.document("rides/${id}").get().addOnSuccessListener { document ->
            if(document.exists()) {
                val ride = Ride(
                    document.get("from").toString(),
                    document.get("to").toString(),
                    document.get("date") as Long?,
                    document.get("numberOfPassangers").toString(),
                    document.get("riderId").toString(),
                    document.get("passangers") as ArrayList<String?>,
                    document.id
                )
                viewGeneralInfo(ride)
                getRider(ride.riderId)
                for(passanger in ride.passangers){
                    database.document("user/${passanger}").get().addOnSuccessListener { document->
                        val info = document.get("userInfo") as HashMap<String, Any?>
                        val userInfo = UserInfo(
                            info.get("id").toString(),
                            info.get("email").toString(),
                            info.get("name").toString(),
                            info.get("surname").toString(),
                            info.get("car").toString()
                        )
                        passangers.add(userInfo)
                    }
                }
                viewPassangers()
            }
            else {
                Toast.makeText(applicationContext, "Error ocurred", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun viewGeneralInfo(ride:Ride){
        from_to.text = ride.from + " - " + ride.to
        time.text = Instant.ofEpochSecond(ride.date!!).atZone(ZoneId.systemDefault()).toLocalDateTime().toString()
    }

    private fun viewPassangers(){
        thread(start=true) {
            Thread.sleep(7000)
            runOnUiThread(
                object : Runnable {
                    override fun run() {
                        Log.d("UITHREAD", "runOnUiThread")

                        if (passangers.size > 0) {
                            list_of_passangers.visibility = View.VISIBLE
                            list_of_passangers.adapter = PassangersAdapter(applicationContext, passangers)
                            no_passangers.visibility = View.INVISIBLE
                        }
                        else {
                            list_of_passangers.visibility = View.INVISIBLE
                            no_passangers.visibility = View.VISIBLE
                            no_passangers.text = "No passangers"
                        }
                    }
                }
            )
        }

        no_passangers.text = "Loading"
    }

    private fun getRider(id:String?){
        database.document("user/${id}").get().addOnSuccessListener { document ->
            val info = document.get("userInfo") as HashMap<String, Any?>
            val userInfo = UserInfo(
                info.get("id").toString(),
                info.get("email").toString(),
                info.get("name").toString(),
                info.get("surname").toString(),
                info.get("car").toString()
            )
            passangers.add(userInfo)
        }
    }
}
