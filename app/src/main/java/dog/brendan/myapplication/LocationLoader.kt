package dog.brendan.myapplication

import android.app.Activity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader


object LocationLoader {

    val states = arrayListOf<State>()
    val locations = arrayListOf<LocationInfo>()
    val locationNames = arrayListOf<String>()

    fun load(activity: Activity) {
        val gson = Gson()
        locationNames.add("None")
        val inputStream = activity.resources.openRawResource(R.raw.locations)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val type = object : TypeToken<List<State>>() {}.type
        val typeList = gson.fromJson<List<State>>(reader, type)
        states += typeList
        reader.close()

        for (state in states) {
            locations += state.location
        }
        for ((name) in locations) {
            locationNames += name
        }
    }
}