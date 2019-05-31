package com.example.traveler.adapters

import android.widget.BaseAdapter
import android.view.View
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.traveler.R
import com.example.traveler.activities.ride_info
import com.example.traveler.data.OwnRide
import com.example.traveler.data.Ride
import com.example.traveler.data.UserInfo
import kotlinx.android.synthetic.main.list_item_current_ride.view.*
import java.time.Instant
import java.time.ZoneId
import kotlin.collections.ArrayList

class CurrentRidesAdapter(private val context: Context,
                          private val dataSource: ArrayList<OwnRide>
) : BaseAdapter() {

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
        val rowView = inflater.inflate(R.layout.list_item_current_ride, parent, false)
        // Get title element
        val fromToMsg = rowView.findViewById(R.id.from_to_message) as TextView

        val timeLine = rowView.findViewById(R.id.time_line) as TextView

        val role = rowView.findViewById(R.id.role_tv) as TextView

        val availableSeats = rowView.findViewById(R.id.available_seats_tv) as TextView

        val rideItem = getItem(position) as OwnRide

// 2
        fromToMsg.text = rideItem.ride?.from + " - " + rideItem.ride?.to
        timeLine.text = Instant.ofEpochSecond(rideItem.ride?.date!!).atZone(ZoneId.systemDefault()).toLocalDateTime().toString()
        role.text = rideItem.role
        val seats = rideItem.ride?.numberOfPassangers.toString().toInt() -  rideItem.ride!!.passangers!!.size
        availableSeats.text = seats.toString()
        rowView.tag = position
        role.tag = rideItem.ride?.id.toString()
        rowView.setOnClickListener { view ->
            val intent = Intent(context, ride_info::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            intent.putExtra("rideId", view.role_tv.tag.toString())
            context.startActivity(intent)
        }
        return rowView
    }
}