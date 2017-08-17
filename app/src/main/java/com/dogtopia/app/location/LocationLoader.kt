package com.dogtopia.app.location

import android.app.Activity
import com.dogtopia.app.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * Since there are many different locations, it wouldn't be wise to store them all manually via
 * code.  Instead, we will be storing them in a json file, which is then parsed by this class.
 *
 * Structure for the location json file is more or less a list of [State]s, which contain
 * a list of [LocationInfo]s, which contain basic information about various locations, such
 * as their name, address, phone number, website, email, and a list of [Camera]s.
 */
object LocationLoader {

	/**
	 * All of the loaded [LocationInfo] instances
	 */
	val locations = arrayListOf<LocationInfo>()

	/**
	 * A map containing a list of state names with a list of their locations.  The reason the state
	 * is represented with a String rather than a [State] object is because this is mainly
	 * used for the location spinner on the starting screen.
	 */
	val stateLocationMap = hashMapOf<String, List<String>>()

	/**
	 * A list of all of the loaded state names for the state spinner.
	 */
	val stateNames = arrayListOf<String>()

	fun load(activity: Activity) {
		val gson = Gson()

		val states = arrayListOf<State>()
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

	/**
	 * Adds the specified [state] to the [stateLocationMap].
	 */
	private fun addStateMap(state: State) {
		if (!stateLocationMap.containsKey(state.name)) {
			val locationNames = arrayListOf<String>()
			for ((name) in state.location) {
				locationNames += name
			}
			Collections.sort(locationNames)
			//The first entry is *always* "None"
			locationNames.add(0, "None")
			stateLocationMap.put(state.name, locationNames)
		}
	}

	data class State(val name: String, val shortname: String, val location: List<LocationInfo>)

	data class LocationInfo(val name: String, val address: String,
							val phone: String, val website: String, val email: String,
							val camera: ArrayList<Camera>) {
		fun getCamera(camName: String): String {
			for ((name1, id) in camera) {
				if (name1 == camName) {
					return id
				}
			}
			return ""
		}
	}

	data class Camera(val name: String, val id: String)
}