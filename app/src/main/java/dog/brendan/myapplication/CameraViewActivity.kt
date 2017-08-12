package dog.brendan.myapplication

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import org.jetbrains.anko.sdk25.coroutines.onInfo
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import org.jetbrains.anko.toast


class CameraViewActivity : AppCompatActivity() {

    private val locationManager = LocationManager()

    private var currentLocation = LocationLoader.locations[0]

    private var currentUUID = ""

    private var prevUUID = currentLocation.getCamera("Gym")

    private lateinit var progress: ProgressBar

    private var loadTarget: com.squareup.picasso.Target? = null

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

        if (intent != null) {
            val extra = intent.extras.get("location") as String
            LocationLoader.locations
                    .filter { it.name == extra }
                    .forEach { currentLocation = it }
        }
        setupCameraView()
    }

    fun setupCameraView() {
        setContentView(R.layout.activity_main)
        progress = findViewById<ProgressBar>(R.id.progressBar)
        val drawerList = findViewById<ListView>(R.id.navList)
        val adapt = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationManager.locationNames)
        drawerList.adapter = adapt

        val vidView = findViewById<VideoView>(R.id.camera)

        vidView.setOnErrorListener { _, _, _ ->
            if (prevUUID != currentUUID && currentUUID != "") {
                vidView.changeRoom(prevUUID);
            }
            toast("Failed to load camera.")
            progress.visibility = View.GONE
            true
        }
        setupSpinner()

//        loadBitmap(currentUUID)

        vidView.onInfo { mp, what, extra ->
            when (what) {
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> progress.visibility = View.GONE
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> progress.visibility = View.VISIBLE
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> progress.visibility = View.VISIBLE
            }
        }

        val title = findViewById<TextView>(R.id.camera_title)
        title.text = title.text.toString().replace("NAME", currentLocation.name)
//        val gymButton = findViewById<Button>(R.id.gym)
//        gymButton.onClick {
//            vidView.changeRoom(currentLocation.getCamera("Gym"))
//        }
//        val romperButton = findViewById<Button>(R.id.romper)
//        romperButton.onClick {
//            vidView.changeRoom(currentLocation.getCamera("Romper"))
//        }
//        val toyboxButton = findViewById<Button>(R.id.toybox)
//        toyboxButton.onClick {
//            vidView.changeRoom(currentLocation.getCamera("Toybox"))
//        }
//        val outdoorButton = findViewById<Button>(R.id.outside)
//        outdoorButton.onClick {
//            vidView.changeRoom(currentLocation.getCamera("Outside"))
//        }
    }

    override fun onResume() {
        super.onResume()
        val vidView = findViewById<VideoView>(R.id.camera)
        vidView.start()
    }

    private fun setupSpinner() {
        val spinner = findViewById<Spinner>(R.id.camera_selector)
        val options = currentLocation.camera.map { it.name }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val vidView = findViewById<VideoView>(R.id.camera)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                vidView.changeRoom(currentLocation.getCamera(options[position]))
                println(currentLocation.getCamera(options[position]))
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }

    /**
     * Changes the room on the [VideoView] given the [room] UUID.
     */
    fun VideoView.changeRoom(room: String) {
        if (room != currentUUID) {
            progress.visibility = View.VISIBLE
            stopPlayback()
            change(getURL(room), room)
            start()
        }
    }

    /**
     * Calls [VideoView.setVideoURI] while also storing the previous/current URLs in case the
     * video fails to load.
     */
    fun VideoView.change(uri: String, uid: String) {
        prevUUID = currentUUID
        setVideoURI(Uri.parse(uri))
        currentUUID = uid
    }

    /**
     * Provides the direct m3u8 stream URL for the given [room] UUID.
     */
    private fun getURL(room: String): String = "https://stream-alfa.dropcam.com:443/nexus_aac/$room/playlist.m3u8"
    private fun getThumbURL(room: String): String = "https://video.nest.com/api/get_image?uuid=$room&width=560"

    /**
     * Sets up the [VideoView] for the first-time launch.
     */
    fun VideoView.setupRoom(url: String): VideoView {
        setVideoURI(Uri.parse(url));
        requestFocus()
        setOnPreparedListener {
            start()
        }
        return this
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}