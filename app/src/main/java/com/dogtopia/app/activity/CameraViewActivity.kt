package com.dogtopia.app.activity

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dogtopia.app.location.LocationLoader
import com.dogtopia.app.R
import com.dogtopia.app.data.ExtendedLocationInfo
import com.dogtopia.app.fadeIn
import com.dogtopia.app.fadeOutIn
import com.dogtopia.app.onSelect
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onInfo
import org.jetbrains.anko.toast

class CameraViewActivity : AppCompatActivity() {

	private var currentLocation = LocationLoader.locations[0]

	private var currentUUID = ""

	private var prevUUID = currentLocation.getCamera("Gym")

	/**
	 * TODO:
	 * Display error image instead of black screen when cameras are off.
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

	private fun setupCameraView() {
		setContentView(R.layout.activity_main)
		camera.setOnErrorListener { _, _, _ ->
			if (prevUUID != currentUUID && currentUUID != "") {
				camera.changeRoom(prevUUID);
			}
			toast("Failed to load camera.")
			progressBar.visibility = View.GONE
			true
		}
		setupSpinner()
		setupHours()

		camera.onInfo { mp, what, extra ->
			progressBar.visibility = when (what) {
				MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> View.GONE
				MediaPlayer.MEDIA_INFO_BUFFERING_START -> View.VISIBLE
				MediaPlayer.MEDIA_INFO_BUFFERING_END -> View.GONE
				MediaPlayer.MEDIA_INFO_AUDIO_NOT_PLAYING -> View.GONE
				else -> View.GONE
			}
		}

		camera_title.text = camera_title.text.toString().replace("NAME", currentLocation.name)
		fadeOutIn(camera_controls)
	}

	private fun setupHours() {
		val task = ExtendedLocationInfo.HoursTodayTask(currentLocation).execute()
		cameraHours.text = cameraHours.text.toString().replace("TEXT", task.get())
	}

	override fun onResume() {
		super.onResume()
		camera.start()
	}

	private fun setupSpinner() {
		val options = currentLocation.camera.map { it.name }
		val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options)
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		camera_selector.adapter = adapter
		camera_selector.onSelect { position ->
			camera.changeRoom(currentLocation.getCamera(options[position]))
		}
	}

	/**
	 * Changes the room on the [VideoView] given the [room] UUID.
	 */
	private fun VideoView.changeRoom(room: String) {
		if (room != currentUUID) {
			progressBar.visibility = View.VISIBLE
			stopPlayback()
			change(getURL(room), room)
			start()
		}
	}

	/**
	 * Calls [VideoView.setVideoURI] while also storing the previous/current URLs in case the
	 * video fails to load.
	 */
	private fun VideoView.change(uri: String, uid: String) {
		prevUUID = currentUUID
		setVideoURI(Uri.parse(uri))
		currentUUID = uid
	}

	/**
	 * Provides the direct m3u8 stream URL for the given [room] UUID.
	 */
	private fun getURL(room: String): String = "https://stream-alfa.dropcam.com:443/nexus_aac/$room/playlist.m3u8"

	private fun getThumbURL(room: String): String = "https://video.nest.com/api/get_image?uuid=$room&width=560"

	override fun onPause() {
		super.onPause()
	}
}