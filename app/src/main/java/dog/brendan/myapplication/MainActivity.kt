package dog.brendan.myapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.view.animation.AnimationUtils
import android.widget.TextView


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
        fadeIn(findViewById<TextView>(R.id.welcome), 250L)
        fadeIn(findViewById<TextView>(R.id.subtitle), 500L)
        fadeIn(findViewById<TextView>(R.id.spinner), 500L)
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

    private fun fadeIn(view: View, offset: Long) {
        val anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        anim.reset()
        view.clearAnimation()
        anim.startOffset = offset
        view.startAnimation(anim)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun displayCameraActivity(location: String) {
        val intent = Intent(this, CameraViewActivity::class.java)
        intent.putExtra("location", location)
        startActivity(intent)
    }

}