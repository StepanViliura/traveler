package `in`.com.example.traveler.model

class Users {
    var id: Int = 0;
    var firstName: String = "";
    var lastName: String = "";
}

data class Ride(
    val from: String,
    val to: String,
    val date: String
)