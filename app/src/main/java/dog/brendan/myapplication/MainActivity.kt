package dog.brendan.myapplication

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onInfo
import org.jetbrains.anko.sdk25.coroutines.onItemClick

class MainActivity : AppCompatActivity() {

	private val locationManager = LocationManager()

	private var currentLocation = locationManager.commerce

	private var currentUUID = ""

	private var prevUUID = currentLocation.gym

	private lateinit var progress: ProgressBar

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
			true
		}

		vidView.onInfo { mp, what, extra ->
			when (what) {
				MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> progress.visibility = View.GONE
				MediaPlayer.MEDIA_INFO_BUFFERING_START -> progress.visibility = View.VISIBLE
				MediaPlayer.MEDIA_INFO_BUFFERING_END -> progress.visibility = View.VISIBLE
			}
		}

		vidView.setupRoom(getURL(currentLocation.gym))
		val title = findViewById<TextView>(R.id.camera_title)
		title.text = title.text.toString().replace("NAME", currentLocation.name)
		val gymButton = findViewById<Button>(R.id.gym)
		gymButton.onClick { vidView.changeRoom(currentLocation.gym) }
		val romperButton = findViewById<Button>(R.id.romper)
		romperButton.onClick { vidView.changeRoom(currentLocation.romper) }
		val toyboxButton = findViewById<Button>(R.id.toybox)
		toyboxButton.onClick { vidView.changeRoom(currentLocation.toybox) }
		val outdoorButton = findViewById<Button>(R.id.outside)
		outdoorButton.onClick { vidView.changeRoom(currentLocation.outside) }

		drawerList.onItemClick { p0, p1, p2, p3 ->
			currentLocation = locationManager.locations[p2]
			title.text = resources.getString(R.string.cam_title).replace("NAME", currentLocation.name)
			vidView.changeRoom(currentLocation.gym)
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
}