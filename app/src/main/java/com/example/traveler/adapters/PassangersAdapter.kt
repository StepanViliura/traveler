package com.example.traveler.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.traveler.MainActivity
import com.example.traveler.R
import com.example.traveler.data.Ride
import com.example.traveler.data.RideInfo
import com.example.traveler.data.UserInfo
import com.example.traveler.database.SignedUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.ZoneId



class PassangersAdapter(private val context: Context,
                          private val dataSource: ArrayList<UserInfo>) : BaseAdapter() {
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
        val rowView = inflater.inflate(R.layout.list_item_passanger, parent, false)
        // Get title element
        val name = rowView.findViewById(R.id.name) as TextView

        val email = rowView.findViewById(R.id.email) as TextView
        val passanger = getItem(position) as UserInfo

// 2
        name.text = passanger.name + " " + passanger.surname
        email.text = passanger.email
        var passagersCount :Int = 0
        return rowView
    }
}