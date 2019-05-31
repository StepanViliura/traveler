package `in`.com.example.traveler.db

import `in`.com.example.traveler.model.Ride
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import `in`.com.example.traveler.model.Users

class RideDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($ID Integer PRIMARY KEY, $RIDE_FROM TEXT, $RIDE_TO TEXT, $DRIVER_ID Integer)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    //Inserting (Creating) data
    fun addRide(user: Ride): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(RIDE_FROM, user.from)
        values.put(RIDE_TO, user.to)
        values.put(DRIVER_ID, 1)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    companion object {
        private val DB_NAME = "RidesDB"
        private val DB_VERSIOM = 1;
        private val TABLE_NAME = "rides"
        private val ID = "id"
        private val RIDE_FROM = "RideFrom"
        private val RIDE_TO = "RideTo"
        private val DRIVER_ID = "DriverID"
    }
}