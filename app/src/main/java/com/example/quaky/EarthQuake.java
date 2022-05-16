package com.example.quaky;
import java.net.URL;

public class EarthQuake {

	private String magnitude;
	private String offsetlocation;
	private String mainlocation;
	private String year;
	private String time;
	private String link;

	public EarthQuake(String magnitude, String offsetlocation, String mainlocation, String time, String year, String link) {
		this.magnitude = magnitude;
		this.offsetlocation = offsetlocation;
		this.mainlocation = mainlocation;
		this.time = time;
		this.year = year;
		this.link = link;
	}

	public String getMagnitude() {
		return magnitude;
	}

	public String getOffsetlocation() {
		return offsetlocation;
	}

	public String getMainlocation() {
		return mainlocation;
	}

	public String getYear() {
		return year;
	}

	public String getTime() {
		return time;
	}

	public String getLink() {
		return link;
	}
}
