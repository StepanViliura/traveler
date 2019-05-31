package com.example.traveler.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import com.example.traveler.R
import com.example.traveler.adapters.FoundedRidesAdapter
import com.example.traveler.data.Ride
import com.example.traveler.database.SignedUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.founded_rides_content.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class FoundedRides : AppCompatActivity() {

    val database = FirebaseFirestore.getInstance()
    val ride = database.document("rides/${SignedUser.getInstance().userInfo?.id}").collection("own")
    var foundedRides = arrayListOf<Ride>()
    lateinit var  listOfFoundedRides : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_founded_rides)
        val bundle = intent?.getBundleExtra("from")
        val from = intent.getStringExtra("from")
        val to = intent.getStringExtra("to")
        foundRides(from, to)
        val smth =""
    }

    private fun foundRides(from: String?, to :String?) {
        val listOfFoundedRides = findViewById(R.id.list_of_founded_rides) as ListView

        val unix = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        getRides(Ride(from, to, unix))
        thread(start=true) {
            Thread.sleep(8000)
            runOnUiThread(
                object : Runnable {
                    override fun run() {
                        Log.d("UITHREAD", "runOnUiThread")

                        if (foundedRides.size > 0) {
                            no_rides_founded.visibility = View.INVISIBLE
                            listOfFoundedRides.visibility = View.VISIBLE
                            listOfFoundedRides.adapter = FoundedRidesAdapter(applicationContext, foundedRides)
                        }
                        else {
                            listOfFoundedRides.visibility = View.INVISIBLE
                            no_rides_founded.visibility = View.VISIBLE
                            no_rides_founded.text = "No rides founded"
                        }
                    }
                }
            )
        }

        listOfFoundedRides.visibility = View.INVISIBLE
        no_rides_founded.visibility = View.VISIBLE
        no_rides_founded.text = "Loading"

    }

    private fun getRides(ride:Ride){
        val ref = database.collection("rides")
        ref.whereGreaterThan("date", ride.date!!)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty){
                    for(doc in documents) {
                        val foundedRide = Ride(
                            doc.get("from").toString(),
                            doc.get("to").toString(),
                            doc.get("date") as Long?,
                            doc.get("numberOfPassangers").toString(),
                            doc.get("riderId").toString(),
                            doc.get("passangers") as ArrayList<String?>,
                            doc.id
                        )
                        if (ride.from!! == foundedRide.from!!
                            && ride.to!! == foundedRide.to!!
                            && SignedUser.getInstance().userInfo?.id!! != foundedRide.riderId!!
                            && !isAllreadyBooked(ride.passangers)) {
                            foundedRides.add(foundedRide)
                        }
                    }
                }
        }
    }

    private fun isAllreadyBooked(passangers: ArrayList<String?>):Boolean {
        for(id in passangers){
            if(id == SignedUser.getInstance().userInfo?.id){
                return false
            }
        }
        return false
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, FoundedRides::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
