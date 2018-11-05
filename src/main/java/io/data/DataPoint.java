package io.data;

import java.time.LocalDateTime;

public class DataPoint {
	private int tripId;
	private int duration;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int startStationID;
	private double startStationLat;
	private double startStationLong;
	private int endStationID;
	private double endStationLat;
	private double endStationLong;
	private int bikeID;
	private int planDuration;
	private String tripRouteCategory;
	private String passholderType;
	private double distance;
	
	/*
	 * The data in the CSV isn't perfect, sometimes the stats are missing values
	 * When there is a missing value, I set it to MAX_VALUE
	 * This makes it easy to know when doing certain calculations is impossible
	 */
	public DataPoint(String[] csvLine) {
		this.tripId = Integer.parseInt(csvLine[0]);
		this.duration = Integer.parseInt(csvLine[1]);
		this.startTime = LocalDateTime.parse(csvLine[2]);
		this.endTime = LocalDateTime.parse(csvLine[3]);
		try {
			this.startStationID = Integer.parseInt(csvLine[4]);
		} catch (NumberFormatException e) {
			this.startStationID = Integer.MAX_VALUE;
		}
		try {
			this.startStationLat = Double.parseDouble(csvLine[5]);
		} catch (NumberFormatException e) {
			this.startStationLat = Double.MAX_VALUE;
		}
		try {
			this.startStationLong = Double.parseDouble(csvLine[6]);
		} catch (NumberFormatException e) {
			this.startStationLong = Double.MAX_VALUE;
		}
		try {
			this.endStationID = Integer.parseInt(csvLine[7]);
		} catch (NumberFormatException e) {
			this.endStationID = Integer.MAX_VALUE;
		}
		try {
			this.endStationLat = Double.parseDouble(csvLine[8]);
		} catch (NumberFormatException e) {
			this.endStationLat = Double.MAX_VALUE;
		}
		try {
			this.endStationLong = Double.parseDouble(csvLine[9]);
		} catch (NumberFormatException e) {
			this.endStationLong = Double.MAX_VALUE;
		}
		try {
			this.bikeID = Integer.parseInt(csvLine[10]);
		} catch (NumberFormatException e) {
			this.bikeID = Integer.MAX_VALUE;
		}
		try {
			this.planDuration = Integer.parseInt(csvLine[11]);
		} catch (NumberFormatException e) {
			this.planDuration = Integer.MAX_VALUE;
		}
		this.tripRouteCategory = csvLine[12];
		this.passholderType = csvLine[13];
		if ((this.startStationLat != Double.MAX_VALUE) &&
				(this.startStationLong != Double.MAX_VALUE) &&
				(this.endStationLat != Double.MAX_VALUE) &&
				(this.endStationLong != Double.MAX_VALUE)) {
			this.distance = distance(this.startStationLat, this.startStationLong, this.endStationLat, this.endStationLong);
		}
		else {
			distance = Double.MAX_VALUE;
		}
	}
	
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		//Section that deals with roundtrips and 0.0 for long and lat data)
		if ((dist == 0) || (dist > 7000)) {
			/*
			 * Round trips will be calculated using an average urban bike speed of 0.002666667 miles per second.
			 * Routes above 600 seconds total round trip time will be curved down, as travel time above
			 * that is unreasonable for urban biking.
			 */
			if (duration > 600) {
				return (600 + Math.sqrt(Math.sqrt(duration - 600))) * 0.002666667;
			}
			return duration * 0.002666667;
		}
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public int getTripId() {
		return tripId;
	}

	public int getDuration() {
		return duration;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public int getStartStationID() {
		return startStationID;
	}

	public double getStartStationLat() {
		return startStationLat;
	}

	public double getStartStationLong() {
		return startStationLong;
	}

	public int getEndStationID() {
		return endStationID;
	}

	public double getEndStationLat() {
		return endStationLat;
	}

	public double getEndStationLong() {
		return endStationLong;
	}

	public int getPlanDuration() {
		return planDuration;
	}

	public String getTripRouteCategory() {
		return tripRouteCategory;
	}

	public String getPassholderType() {
		return passholderType;
	}
	
	public int getBikeID() {
		return bikeID;
	}
	
	public double getDistance() {
		return distance;
	}
}
