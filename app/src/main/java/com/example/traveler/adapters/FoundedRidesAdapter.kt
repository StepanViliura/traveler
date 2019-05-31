package com.example.traveler.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.traveler.MainActivity
import com.example.traveler.R
import com.example.traveler.activities.ride_info
import com.example.traveler.data.OwnRide
import com.example.traveler.data.Ride
import com.example.traveler.data.RideInfo
import com.example.traveler.database.SignedUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.ZoneId


class FoundedRidesAdapter(private val context: Context,
                          private val dataSource: ArrayList<Ride>) : BaseAdapter() {
    val database = FirebaseFirestore.getInstance()
    val ride = database.document("rides/${SignedUser.getInstance().userInfo?.id}").collection("own")
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_founded_rides, parent, false)
        // Get title element
        val fromToMsg = rowView.findViewById(R.id.from_to_message) as TextView

        val timeLine = rowView.findViewById(R.id.time_line) as TextView

        val availableSeats = rowView.findViewById(R.id.available_seats_tv) as TextView

        val bookRide = rowView.findViewById<Button>(R.id.book_ride) as Button

        val rideItem = getItem(position) as Ride

// 2
        fromToMsg.text = rideItem.from + " - " + rideItem.to
        var passagersCount :Int = 0
        if (rideItem.passangers != null){
            passagersCount = rideItem.passangers!!.size
        }
        timeLine.text = Instant.ofEpochSecond(rideItem.date!!).atZone(ZoneId.systemDefault()).toLocalDateTime().toString()
        val seats = rideItem.numberOfPassangers.toString().toInt() -  passagersCount
        availableSeats.text = seats.toString()
        rowView.tag = position
        bookRide.tag = rideItem.id
        bookRide.setOnClickListener { view ->
            bookRide(view.tag.toString(), SignedUser.getInstance().userInfo?.id)
        }
        return rowView
    }

    private fun bookRide(rideId:String?, userId:String?){
        database.document("rides/${rideId}").update("passangers", FieldValue.arrayUnion(userId)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                ride.add(RideInfo(rideId, "passanger")).addOnSuccessListener {
                    Toast.makeText(context, "Ride was booked sucessfully :)", Toast.LENGTH_LONG).show()
                    context.startActivity(MainActivity.getLaunchIntent(context))
                }
            }
        }
    }
}