package dog.brendan.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import org.jetbrains.anko.contentView
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    private var dummy = false

    /**
     * TODO:
     * Check if it's naptime.  If so, display a different message and don't show cameras.
     *
     * Hide buttons for locations that don't have romper/outdoor rooms
     *
     * Data-saving mode that only loads thumbnails
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                if (!dummy) {
                    dummy = true
                } else {
                    if (spinner.selectedItem.toString() != "None") {
                        displayCameraActivity(spinner.selectedItem.toString())
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
    }

    private fun displayCameraActivity(location: String) {
        val intent = Intent(this, CameraViewActivity::class.java)
        intent.putExtra("location", location)
        startActivity(intent)
    }

}