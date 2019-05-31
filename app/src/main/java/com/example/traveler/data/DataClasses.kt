package com.example.traveler.data

data class Ride(
    var from: String? = "",
    var to: String? = "",
    var date: Long? = 0,
    var numberOfPassangers: String? = "",
    var riderId:String? = "",
    var passangers: ArrayList<String?> = arrayListOf(),
    var id:String? = ""
)

data class OwnRide (
    var ride: Ride? = Ride(),
    var role: String? = ""
)

data class UserInfo(
    var id:String? = "",
    var email:String? = "",
    var name:String? = "",
    var surname:String? = "",
    var car:String? = ""
)
data class RideInfo (var id:String? = "", var role:String?)