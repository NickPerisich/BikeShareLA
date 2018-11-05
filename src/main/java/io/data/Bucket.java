package io.data;

public class Bucket {
	private int stationID;
	private int count;
	
	public Bucket (int stationID) {
		this.stationID = stationID;
		this.count = 1;
	}
	
	public void addCount() {
		this.count = this.count + 1;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public int getStationID () {
		return this.stationID;
	}
}
