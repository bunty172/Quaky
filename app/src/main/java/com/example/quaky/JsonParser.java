package com.example.quaky;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class JsonParser {

	private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
	private static String JSON_RESPONSE = null;
	public static final String SEPARATOR = "of";

	public static ArrayList<EarthQuake> retrieveFromHTTPURL() {
		URL url = createURLfromString(USGS_URL);
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;

		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.connect();

			if (httpURLConnection.getResponseCode() == 200) {
				inputStream = httpURLConnection.getInputStream();
				JSON_RESPONSE = getJSONResponsefromInputstream(inputStream);
			}
		} catch (IOException e) {
			Log.e("IOexception occured", "Error occurred while conncetion with web server with the given URL");
		} finally {
			if (httpURLConnection != null)
				httpURLConnection.disconnect();
			else
				JSON_RESPONSE = null;

		};

		return extractEarthquakeDatafromJSONresponse();
	}

	private static String getJSONResponsefromInputstream(InputStream inputStream) {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder finalJSONResponseString = new StringBuilder();

		try {
			String aline = bufferedReader.readLine();
			while (aline != null) {
				finalJSONResponseString.append(aline);
				aline = bufferedReader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return finalJSONResponseString.toString();
	}

	public static ArrayList<EarthQuake> extractEarthquakeDatafromJSONresponse() {

		ArrayList<EarthQuake> earthQuakeArrayList = new ArrayList<>();
		Date dateobject;

		try {
			if (JSON_RESPONSE != null) {
				JSONObject response = new JSONObject(JSON_RESPONSE);
				JSONArray features_array = response.getJSONArray("features");
				for (int i = 0; i < features_array.length(); i++) {
					JSONObject currentEarthquake = features_array.getJSONObject(i);
					JSONObject properties = currentEarthquake.getJSONObject("properties");
					Double currentEarthquake_givenmagnitude = properties.getDouble("mag");
					String currentEarthquake_fulllocation = properties.getString("place");
					long currentEarthquake_time_the_epoch = properties.getLong("time");
					dateobject = new Date(currentEarthquake_time_the_epoch);
					String currentEarthquake_url = properties.getString("url");

					String currentEarthquake_magnitude = correctmagnitude(currentEarthquake_givenmagnitude);
					String currentEarthquake_offsetlocation = correctoffsetlocation(currentEarthquake_fulllocation);
					String currentEarthquake_mainlocation = correctmainlocation(currentEarthquake_fulllocation);
					String currentEarthquake_year = correctyearformat(dateobject);
					String currentEarthquake_time = correcttimeformat(dateobject);


					earthQuakeArrayList.add(new EarthQuake(currentEarthquake_magnitude, currentEarthquake_offsetlocation, currentEarthquake_mainlocation, currentEarthquake_time, currentEarthquake_year, currentEarthquake_url));
				}
			} else
				earthQuakeArrayList = null;
		} catch (JSONException e) {
			Log.e("exception occurred", "not a valid JSON respose : error while parsing");
		}

		return earthQuakeArrayList;
	}


	public static String correctmagnitude(double mag) {
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		String magnitude = decimalFormat.format(mag);
		return magnitude;
	}

	public static String correctoffsetlocation(String fulllocation) {
		if (fulllocation.contains(SEPARATOR)) {
			String[] array = fulllocation.split(SEPARATOR);
			return array[0] + SEPARATOR;
		} else
			return "Near of";
	}

	public static String correctmainlocation(String fulllocation) {
		if (fulllocation.contains(SEPARATOR)) {
			String[] array = fulllocation.split(SEPARATOR);
			return array[1];
		} else
			return fulllocation;
	}

	public static String correctyearformat(Date d) {
		SimpleDateFormat yearobject;
		yearobject = new SimpleDateFormat("MMM DD,YYYY");
		String year = yearobject.format(d);
		return year;
	}

	public static String correcttimeformat(Date d) {
		SimpleDateFormat timeobject;
		timeobject = new SimpleDateFormat("h:mm a");
		String time = timeobject.format(d);
		return time;
	}

	private static URL createURLfromString(String usgsUrl) {
		URL url = null;
		try {
			url = new URL(usgsUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return url;
	}
}
