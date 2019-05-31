package com.example.traveler

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import `in`.com.example.traveler.model.Ride
import kotlinx.android.synthetic.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.sql.Time
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var listOfRides : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fillCurrentRides()

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fillCurrentRides() {
        listOfRides = findViewById<ListView>(R.id.list_of_rides)



        var rides = arrayListOf(
            Ride("Lviv", "Kyiv", "2019-05-18T 3:24:00"),
            Ride("Krakow", "Kyiv", "2019-05-18 3:24:00"),
            Ride("Wroclaw", "Kyiv", "2019-05-18 3:24:00"),
            Ride("Kyiv", "Lviv", "2019-05-18 3:24:00")
        )


        if (rides.size > 0) {
            listOfRides.visibility = View.VISIBLE
            listOfRides.adapter = CurrentRidesAdapter(applicationContext, rides)
            listOfRides.setOnItemClickListener {
                    parent, view, position, id ->
                    val rideId = view.tag as Int
                    val intent = Intent(this@MainActivity, ride_info::class.java)
                    intent.putExtra("rideId", rideId)
                    startActivity(intent)
            }
            main_activity_no_rides.visibility = View.INVISIBLE
        }
        else {
            listOfRides.visibility = View.INVISIBLE
            main_activity_no_rides.visibility = View.VISIBLE
        }
    }

    public fun openSearchActivity(view: View) {
        val intent = Intent(this@MainActivity, SearchRide::class.java)
        startActivity(intent)
    }

    public fun openCreateRideActivity(view: View) {
        val intent = Intent(this@MainActivity, CreateRide::class.java)
        startActivity(intent)
    }

    public fun viewFullRideInfo(view: View) {
        val rideId = view.tag as Int
        val intent = Intent(this@MainActivity, ride_info::class.java)
        intent.putExtra("rideId", rideId)
        startActivity(intent)
    }
}
