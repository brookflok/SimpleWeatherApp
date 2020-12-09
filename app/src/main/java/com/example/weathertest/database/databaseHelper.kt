package com.example.weathertest.database


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.weathertest.data.WeatherResponse

val DATABASENAME = "Weather Database"
val TABLENAME = "Weather"
val COL_CITYNAME = "Cityname"

val COL_TEMPERATURE = "Temperature"
val COL_CITYID = "cityID"
val COL_ISCHECKED = "isChecked"
val COL_ID = "id"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLENAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COL_CITYID +" INTEGER," +
                COL_CITYNAME + " VARCHAR(256),"+
                COL_ISCHECKED + " INTEGER," +
                COL_TEMPERATURE + " LONG)"
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }

    //Insert one data to database
    fun insertData(weatherResponse: WeatherResponse) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_CITYNAME, weatherResponse.cityName)
        contentValues.put(COL_CITYID, weatherResponse.cityID)
        contentValues.put(COL_TEMPERATURE, weatherResponse.forecast.temperature)
        contentValues.put(COL_ISCHECKED, 1)
        val result = database.insert(TABLENAME, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    //Get One by name from Database
    fun getOneName(name: String): WeatherResponse? {
        val db = this.writableDatabase
        val selectQuery = "SELECT  * FROM $TABLENAME WHERE $COL_CITYNAME = ?"
        db.rawQuery(selectQuery, arrayOf(name)).use { // .use requires API 16
            if (it.moveToFirst()) {
                val result = WeatherResponse()
                result.weatherid = it.getInt(it.getColumnIndex(COL_ID))
                result.cityName = it.getString(it.getColumnIndex(COL_CITYNAME))
                result.isChecked = it.getInt(it.getColumnIndex(COL_ISCHECKED))>=1
                return result
            }
        }
        return null
    }

    //Delete One by name from Database
    fun deleteOneName(name: String): WeatherResponse? {
        val db = this.writableDatabase
        val selectQuery = "DELETE FROM $TABLENAME WHERE $COL_CITYNAME = ?"
        db.rawQuery(selectQuery, arrayOf(name)).use { // .use requires API 16
            if (it.moveToFirst()) {
                val result = WeatherResponse()
                result.weatherid = it.getInt(it.getColumnIndex(COL_ID))
                result.cityName = it.getString(it.getColumnIndex(COL_CITYNAME))
                result.isChecked = it.getInt(it.getColumnIndex(COL_ISCHECKED))>=1
                return result
            }
        }
        return null
    }

    //Return List of cityID
    fun readID() : ArrayList<Int>{
        val list = arrayListOf<Int>()
        val db = this.readableDatabase
        val query = "Select cityID from $TABLENAME"
        val result = db.rawQuery(query, null)
        while (result.moveToNext()){
            list.add(result.getInt(result.getColumnIndex(COL_CITYID)))
        }
        return list
    }
}