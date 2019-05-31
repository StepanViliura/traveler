package com.example.traveler

import android.widget.BaseAdapter
import android.view.View
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import `in`.com.example.traveler.model.Ride
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CurrentRidesAdapter(private val context: Context,
                          private val dataSource: ArrayList<Ride>) : BaseAdapter() {

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

        val ride = getItem(position) as Ride

// 2
        fromToMsg.text = ride.from + " - " + ride.to
        timeLine.text = ride.date
        rowView.tag = position
        return rowView
    }
}