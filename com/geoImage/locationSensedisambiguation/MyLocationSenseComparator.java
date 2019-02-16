package com.geoImage.locationSensedisambiguation;

import java.util.Comparator;

import com.geoImage.dao.MyLocationSense;

public class MyLocationSenseComparator implements Comparator<MyLocationSense> {

	String stateName = null;
	public MyLocationSenseComparator(String name)
	{
		this.stateName = name;
	}

	@Override
	public int compare(MyLocationSense o1, MyLocationSense o2) {
		if (o1.getLocationSenseName().contains(stateName) && !o2.getLocationSenseName().contains(stateName)) {
			return -1;
		}else if (!o1.getLocationSenseName().contains(stateName) && o2.getLocationSenseName().contains(stateName)){
			return 1;
		}else {
			if (o1.getId().getLocationSenseId()>o2.getId().getLocationSenseId()) {
				return 1;
			}else if(o1.getId().getLocationSenseId()<o2.getId().getLocationSenseId())
			{
				return -1;
			}else {
				return 0;
			}
		}
	}

}
