package dog.brendan.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*


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

        // Fade in the first few elements
        fadeIn(findViewById<ImageView>(R.id.logo), 250L)
        fadeIn(findViewById<TextView>(R.id.stateHint), 500L)
        fadeIn(findViewById<TextView>(R.id.stateSpinner), 500L)

        val stateSpinner = findViewById<Spinner>(R.id.stateSpinner)

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
                    fadeOutIn(findViewById<TextView>(R.id.locationHint))
                    fadeOutIn(findViewById<TextView>(R.id.locationSpinner))
                    updateLocations(stateSpinner.selectedItem.toString())
                }
            }
        }
    }

    private fun updateLocations(state: String) {
        dummy2 = false
        val locationSpinner =  findViewById<Spinner>(R.id.locationSpinner)
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