package com.example.traveler.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.traveler.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_ride.*

class SearchRide : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_ride)
        setupUI()
    }

    private fun setupUI() {
        search_ride_btn.setOnClickListener {
            startFinidingOfRides()
        }
    }

    private fun startFinidingOfRides(){
        val intent = Intent(this@SearchRide, FoundedRides::class.java)
        intent.putExtra("from", et_search_ride_from.text.toString())
        intent.putExtra("to",et_search_ride_to.text.toString())
        startActivity(intent)
    }
}
