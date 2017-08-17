package com.dogtopia.app.data

import android.os.AsyncTask
import com.dogtopia.app.location.LocationLoader
import org.jsoup.Jsoup

/**
 * Extracts various information from a location's website.
 *
 * TODO: Naptime hours - this actually isn't consistently listed on the different locations' websites
 */
object ExtendedLocationInfo {

	/**
	 * Returns the open hours for today.
	 * In most cases, this will return "7:00 AM - 7:00 PM"
	 */
	class HoursTodayTask(val location: LocationLoader.LocationInfo) : AsyncTask<Void, Void, String>() {
		override fun doInBackground(vararg p0: Void?): String {
			val doc = Jsoup.connect(location.website).get()
			return doc.getElementById("today-hours").text().trim().substring(13)
		}
	}

	/**
	 * Returns the hours for each day of the week.
	 */
	class HoursWeekTask(val location: LocationLoader.LocationInfo) : AsyncTask<Void, Void, List<String>>() {
		override fun doInBackground(vararg p0: Void?): List<String> {
			val doc = Jsoup.connect(location.website).get()
			val table = doc.select("table")[0]
			val rows = table.select("tr")
			val out = rows.map { it.select("td") }.map { it.text() }
			return out
		}
	}
}