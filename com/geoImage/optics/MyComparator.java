package com.geoImage.optics;

import java.util.Comparator;

public class MyComparator implements Comparator<DataPoint> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compare(DataPoint o1, DataPoint o2) {
		if (o1.getTempleDistanceToCenter()<o2.getTempleDistanceToCenter())
		{
			return -1;
		}else if(o1.getTempleDistanceToCenter()>o2.getTempleDistanceToCenter())
		{
			return 1;
		}else
		{			
			return 0;
		}
	}

}
