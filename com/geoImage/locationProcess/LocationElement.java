package com.geoImage.locationProcess;

/**
 * 
 * @author hqn
 * @description the location information of a Location
 * @version
 * @param <T>
 * @update 2014-2-20 下午4:10:07
 */
public class LocationElement implements Comparable<Object> {

	/**
	 * @param args
	 */
	private String locationName;
	private String locationOriginalText;
	private String type;
	private int confidence;
	private int start;
	private int end;
	private double latitude;
	private double longitude;
	private int docId;

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public LocationElement(String locationOriginalText, int start, int end,
			int docid) {
		super();
		this.locationOriginalText = locationOriginalText;
		this.start = start;
		this.end = end;
		this.docId = docid;
	}

	public LocationElement() {

	}

	public static void main(String[] args) {

	}

	public void showLocation() {
		System.out.print("location information:");
		System.out.println(";docId:" + docId + ";text:" + locationOriginalText
				+ ";start:" + start + ";end:" + end + ";latitude:" + latitude
				+ ";longitude:" + longitude);
		return;
	}

	@Override
	public boolean equals(Object obj) {
		LocationElement le = (LocationElement) obj;
		if (this.start == le.getStart()
				&& this.end == le.getEnd()
				&& this.locationOriginalText.equals(le
						.getLocationOriginalText())) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Object o) {
		// System.out.println("调用了 Comparable 的compareTo??");
		if (o instanceof LocationElement) {
			if (((LocationElement) o).getStart() > this.getStart()) {
				return -1;
			} else if (((LocationElement) o).getStart() < this.getStart()) {
				return 1;
			} else {

				return 0;
			}
		} else {
			try {
				throw new Exception(
						"the object is not a type of LocationElement");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -100;
		}

	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationOriginalText() {
		return locationOriginalText;
	}

	public void setLocationOriginalText(String locationOriginalText) {
		this.locationOriginalText = locationOriginalText;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getConfidence() {
		return confidence;
	}

	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
