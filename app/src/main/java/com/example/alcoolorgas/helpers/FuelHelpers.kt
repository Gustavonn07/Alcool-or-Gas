package com.example.alcoolorgas.data

import android.content.Context
import com.example.alcoolorgas.models.FuelStation
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit

class FuelHelpers(private val context: Context) {

    private val prefs = context.getSharedPreferences("stations_db", Context.MODE_PRIVATE)

    private fun readJSON(): JSONArray {
        val json = prefs.getString("stations", "[]") ?: "[]"
        return JSONArray(json)
    }

    private fun saveJSON(array: JSONArray) {
        prefs.edit { putString("stations", array.toString()) }
    }

    private fun toJSON(st: FuelStation): JSONObject {
        return JSONObject().apply {
            put("id", st.id)
            put("name", st.name)
            put("alcool", st.alcool)
            put("gasolina", st.gasolina)
            put("latitude", st.latitude)
            put("longitude", st.longitude)
            put("date", st.date)
            put("percent", st.percent)
        }
    }

    private fun fromJSON(obj: JSONObject): FuelStation {

        return FuelStation(
            id = obj.getString("id"),
            name = obj.getString("name"),
            alcool = obj.getDouble("alcool"),
            gasolina = obj.getDouble("gasolina"),
            latitude = if (obj.isNull("latitude")) null else obj.getDouble("latitude"),
            longitude = if (obj.isNull("longitude")) null else obj.getDouble("longitude"),
            date = obj.getLong("date"),
            percent = obj.getInt("percent")

        )
    }

    fun clearAll() {
        saveJSON(JSONArray())
    }

    fun getStations(): MutableList<FuelStation> {
        val array = readJSON()
        val list = mutableListOf<FuelStation>()

        for (i in 0 until array.length()) {
            list.add(fromJSON(array.getJSONObject(i)))
        }

        return list
    }

    fun addStation(st: FuelStation) {
        val array = readJSON()
        array.put(toJSON(st))
        saveJSON(array)
    }

    fun updateStation(st: FuelStation) {
        val array = readJSON()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            if (obj.getString("id") == st.id) {
                array.put(i, toJSON(st))
                break
            }
        }

        saveJSON(array)
    }

    fun deleteStation(id: String) {
        val array = readJSON()
        val newArray = JSONArray()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            if (obj.getString("id") != id) {
                newArray.put(obj)
            }
        }

        saveJSON(newArray)
    }
}
