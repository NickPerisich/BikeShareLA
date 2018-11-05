package io.data;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class DataSet {
	//Holds the entire dataset
	private ArrayList<DataPoint> data;
	private ArrayList<DataPoint> winterData;
	private ArrayList<DataPoint> summerData;
	//Numerous stats that are displayed on the page
	private int mostPopularStart;
	private int leastPopularStart;
	private int mostPopularEnd;
	private int leastPopularEnd;
	private double averageDistance;
	private int regularRiders;
	private int averageTime;
	private String popPassType;
	private String popTripType;
	private int winterDuration;
	private int summerDuration;
	private double winterDistance;
	private double summerDistance;
	private String winterTripType;
	private String summerTripType;

	//Constructor loads data from CSV and calls individual methods to parse data
	public DataSet() {
		data = new ArrayList<DataPoint>();
		winterData = new ArrayList<DataPoint>();
		summerData = new ArrayList<DataPoint>();
		String csvFile = "src/main/java/io/data/metro-bike-share-trip-data.csv";
        CSVReader reader = null;
        try {
        	//Reads the CSV into a string array for each line, creating a new DataPoint
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;
            line = reader.readNext();
            while ((line = reader.readNext()) != null) {
            	DataPoint temp = new DataPoint(line);
                data.add(temp);
                //Parses the data into the winter and summer season
                if ((temp.getStartTime().isAfter(LocalDateTime.parse("2016-06-21T00:00:00"))) && 
                		(temp.getStartTime().isBefore(LocalDateTime.parse("2016-09-23T00:00:00")))){
                	summerData.add(temp);
                }
                else if ((temp.getStartTime().isAfter(LocalDateTime.parse("2016-12-21T00:00:00"))) && 
                		(temp.getStartTime().isBefore(LocalDateTime.parse("2017-03-20T00:00:00")))) {
                	winterData.add(temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * Most of the stat calculations happen here
         * If a method needed to be used on the seasonal dataset, I made the function return a value instead of
         * directly setting the instance data of the DataSet object.
         */
        startStationStats(data);
        endStationStats(data);
        averageDistance = averageDistance(data);
        regularRiders(data);
        averageTime = averageTime(data);
        startStationStats(data);
        popPassType(data);
        popTripType = popTripType(data);
    	winterDuration = averageTime(winterData);
    	summerDuration = averageTime(summerData);
    	winterDistance = averageDistance(winterData);
    	summerDistance = averageDistance(summerData);
    	winterTripType = popTripType(winterData);
    	summerTripType = popTripType(summerData);
	}

	//Calculates how many riders are "regular", aka they have monthly or flex passes
	public void regularRiders(ArrayList<DataPoint> data) {
		this.regularRiders = 0;
		for (DataPoint dp: data) {
			if ((dp.getPlanDuration() == 30) || (dp.getPlanDuration() == 365)) {
				regularRiders++;
			}
		}
	}

	//Calculates the average distance of a users trip
	public double averageDistance(ArrayList<DataPoint> data) {
		double average = 0;
		double dataPoints = 0;
		for (DataPoint dp: data) {
			if ((dp.getDistance() != Double.MAX_VALUE) && (!(Double.isNaN(dp.getDistance())))) {
				average += dp.getDistance();
				dataPoints++;
			}
		}
		average = average / dataPoints;
		return (double) Math.round(average * 100000) / 100000;
	}
	
	//Decides whether round trips or one ways are more popular
	public String popTripType (ArrayList<DataPoint> data) {
		int roundTrip = 0;
		int oneWay = 0;
		//Uses a pretty simple iteration through the data and finds the most common trip type
		for (DataPoint dp: data) {
			if (dp.getPassholderType().equals("One Way")) {
				oneWay++;
			}
			else if (dp.getPassholderType().equals("Round Trip")) {
				roundTrip++;
			}
		}
		if (oneWay > roundTrip) {
			return "One Way";
		}
		else {
			return "Round Trip";
		}
	}
	
	//Finds the most popular type of pass held by riders
	public void popPassType (ArrayList<DataPoint> data) {
		int walkUp = 0;
		int flexPass = 0;
		int monthlyPass = 0;
		//Uses a pretty simple iteration through the data and finds the most common pass type
		for (DataPoint dp: data) {
			if (dp.getPassholderType().equals("Walk-up")) {
				walkUp++;
			}
			else if (dp.getPassholderType().equals("Flex Pass")) {
				flexPass++;
			}
			else if (dp.getPassholderType().equals("Monthly Pass")) {
				monthlyPass++;
			}
		}
		if ((walkUp > flexPass) && (walkUp > monthlyPass)) {
			this.popPassType = "Walk-up";
		}
		else if ((flexPass > walkUp) && (flexPass > monthlyPass)) {
			this.popPassType = "Flex Pass";
		}
		else if ((monthlyPass > flexPass) && (monthlyPass > walkUp)) {
			this.popPassType = "Monthly Pass";
		}
	}
	
	//Calculates how long most people spend with the bike
	public int averageTime(ArrayList<DataPoint> data) {
		int average = 0;
		int dataPoints = 0;
		for (DataPoint dp: data) {
			average += dp.getDuration();
			dataPoints++;
		}
		return average / dataPoints;
	}
	
	//Calculates the most and least popular stations for starting
	public void startStationStats(ArrayList<DataPoint> data) {
		//Uses "buckets" to keep track of how many of each station are visited
		ArrayList<Bucket> stations = new ArrayList<Bucket>();
		boolean newBucket;
		for (DataPoint dp: data) {
			newBucket = true;
			//Iterates through the buckets looking for a match
			for (Bucket b: stations) {
				if (b.getStationID() == dp.getStartStationID()) {
					b.addCount();
					newBucket = false;
				}
			}
			//Creates new bucket if one for station doesn't exist
			if (newBucket) {
				stations.add(new Bucket(dp.getStartStationID()));
			}
		}
		//Calculates min and max for said buckets
		Bucket max = stations.get(0);
		Bucket min = stations.get(0);
		for (Bucket b: stations) {
			if (b.getCount() > max.getCount()) {
				max = b;
			}
			if (b.getCount() < min.getCount()) {
				min = b;
			}
		}
		mostPopularStart = max.getStationID();
		leastPopularStart = min.getStationID();
	}
	
	//Calculates the most and least popular stations for ending
	public void endStationStats(ArrayList<DataPoint> data) {
		//Uses "buckets" to keep track of how many of each station are visited
		ArrayList<Bucket> stations = new ArrayList<Bucket>();
		boolean newBucket;
		for (DataPoint dp: data) {
			newBucket = true;
			//Iterates through the buckets looking for a match
			for (Bucket b: stations) {
				if (b.getStationID() == dp.getEndStationID()) {
					b.addCount();
					newBucket = false;
				}
			}
			//Creates new bucket if one for station doesn't exist
			if (newBucket) {
				stations.add(new Bucket(dp.getEndStationID()));
			}
		}
		//Calculates min and max for said buckets
		Bucket max = stations.get(0);
		Bucket min = stations.get(0);
		for (Bucket b: stations) {
			if (b.getCount() > max.getCount()) {
				max = b;
			}
			if (b.getCount() < min.getCount()) {
				min = b;
			}
		}
		mostPopularEnd = max.getStationID();
		leastPopularEnd = min.getStationID();
	}
	
	public int getMostPopularStart() {
		return mostPopularStart;
	}

	public int getLeastPopularStart() {
		return leastPopularStart;
	}

	public int getMostPopularEnd() {
		return mostPopularEnd;
	}

	public int getLeastPopularEnd() {
		return leastPopularEnd;
	}

	public double getAverageDistance() {
		return averageDistance;
	}

	public int getRegularRiders() {
		return regularRiders;
	}
	
	public int getAverageTime() {
		return averageTime;
	}

	public String getPopPassType() {
		return popPassType;
	}

	public String getPopTripType() {
		return popTripType;
	}
	
	public int getWinterDuration() {
		return winterDuration;
	}

	public int getSummerDuration() {
		return summerDuration;
	}

	public double getWinterDistance() {
		return winterDistance;
	}

	public double getSummerDistance() {
		return summerDistance;
	}

	public String getWinterTripType() {
		return winterTripType;
	}

	public String getSummerTripType() {
		return summerTripType;
	}
}
