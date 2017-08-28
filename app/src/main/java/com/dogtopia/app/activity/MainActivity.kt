package com.dogtopia.app.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.dogtopia.app.*
import com.dogtopia.app.location.LocationLoader
import kotlinx.android.synthetic.main.activity_start.*
import android.content.SharedPreferences



class MainActivity : AppCompatActivity() {

	/**
	 * When the activity is started, the spinners' onItemSelected method is invoked.  These two
	 * dummy booleans are used for the two spinners to avoid any changes from happening when the view
	 * is initially loaded.
	 */
	private var dummy = false
	private var dummy2 = false

	/**
	 * TODO:
	 * Check if it's naptime.  If so, display a different message and don't show cameras.
	 * Data-saving mode that only loads thumbnails
	 *
	 * Support for the other types of cameras (Maybe urgent? Will have to ask corporate)
	 */

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Load the locations
		LocationLoader.load(this)

		setContentView(R.layout.activity_start)

		if (Global.justStarted) {
			val prefs = getSharedPreferences("Dogtopia", Context.MODE_PRIVATE)
			if (prefs.getString("lastLocation", "") != "") {
				val s = prefs.getString("lastLocation", "")
				displayCameraActivity(s)
				Global.justStarted = false
			}
		}

		// Fade in the first few elements
		fadeIn(logo, 250L)
		fadeIn(stateHint, 500L)
		fadeIn(stateSpinner, 500L)

		// Set the contents of the state spinner to the list of state names
		val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LocationLoader.stateNames)
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		stateSpinner.adapter = arrayAdapter

		/*
			Fades the Location spinner in, as well as updates its contents.
			I made a fadeOutIn method for these because I want there to be some visual feedback
			after the choice is initially selected.
		 */
		stateSpinner.onSelect {
			if (!dummy) {
				dummy = true
			} else {
				if (stateSpinner.selectedItem.toString() != "None") {
					fadeOutIn(locationHint, 500L)
					fadeOutIn(locationSpinner, 500L)
					updateLocations(stateSpinner.selectedItem.toString())
				}
			}
		}
	}

	override fun onStart() {
		super.onStart()
	}

	private fun updateLocations(state: String) {
		dummy2 = false
		val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LocationLoader.stateLocationMap[state])

		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		locationSpinner.adapter = arrayAdapter

		locationSpinner.onSelect {
			if (!dummy2) {
				dummy2 = true
			} else {
				displayCameraActivity(locationSpinner.selectedItem.toString())
			}
		}
	}

	override fun onPause() {
		super.onPause()
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
		/*
		When the user goes back to this activity and tries to open the previous location again,
		it would not handle the input.  So the selected location is reset when the view is opened.
		 */
		val stateSpinner = findViewById<Spinner>(R.id.stateSpinner)
		if (stateSpinner.selectedItem.toString() != "None") {
			updateLocations(stateSpinner.selectedItem.toString())
		}
		val prefs = getSharedPreferences("Dogto pia", Context.MODE_PRIVATE)
		val editor = prefs.edit()
		editor.putString("lastLocation", "")
		editor.apply()
	}

	/**
	 * Starts the [CameraViewActivity], targeting the specified [location].
	 *
	 * The location is a string, since the [Intent.putExtra] method doesn't allow objects
	 * to be used.  The location name provided is then converted to a [LocationInfo] object
	 * from the [LocationLoader.locations] list.
	 */
	private fun displayCameraActivity(location: String) {
		val intent = Intent(this, CameraViewActivity::class.java)
		intent.putExtra("location", location)
		startActivity(intent)
	}
}