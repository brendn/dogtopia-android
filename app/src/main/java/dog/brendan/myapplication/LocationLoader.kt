package dog.brendan.myapplication

import android.app.Activity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object LocationLoader {

    val states = arrayListOf<State>()
    val locations = arrayListOf<LocationInfo>()

    val stateLocationMap = hashMapOf<String, List<String>>()
    val stateNames = arrayListOf<String>()

    fun load(activity: Activity) {
        val gson = Gson()

        stateLocationMap.clear()
        stateNames.clear()
        states.clear()
        locations.clear()

        val inputStream = activity.resources.openRawResource(R.raw.locations)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val type = object : TypeToken<List<State>>() {}.type
        val typeList = gson.fromJson<List<State>>(reader, type)
        states += typeList
        reader.close()

        for ((name, _, location) in states) {
            stateNames += name
            locations += location
        }
        Collections.sort(stateNames)
        stateNames.add(0, "None")
        for (state in states) {
            addStateMap(state)
        }
    }

    private fun addStateMap(state: State) {
        if (!stateLocationMap.containsKey(state.name)) {
            val locationNames = arrayListOf<String>()
            for ((name) in state.location) {
                locationNames += name
            }
            Collections.sort(locationNames)
            locationNames.add(0, "None")
            stateLocationMap.put(state.name, locationNames)
        }
    }
}