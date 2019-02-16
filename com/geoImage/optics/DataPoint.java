package com.geoImage.optics;

import java.util.List;

/**
 * It is the processed point in OPTICS
 * 
 * @author hqn
 *
 */
public class DataPoint {
	
	public static final double UNDEFINED = 100000000;
	
	public static final int NOISE = -9999999;
	
	private boolean isCorePoint = false;
	
	private boolean isProcessed = false;
	
	private int photoId;
	
	private double templeDistanceToCenter = UNDEFINED;

	private double coreDistance = UNDEFINED; //默认是负一
	
	private double reachabilityDistance = UNDEFINED;//default is UNDEFINED
	
	private double longitude; //default is 0.0
	
	private double latitude; //default is 0.0
	
	public static final  Integer opticsOrder = 1;
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataPoint dpDataPoint = new DataPoint();
//		System.out.println(dpDataPoint.isProcessed);//the default value of isProcessed is false
//		System.out.println(dpDataPoint.coreDistance);//the default value of coreDistance is 0.0//

	}
	
	
	
	public DataPoint() {

		
	}

	public DataPoint(int photoId,double longitude, double latitude)
	{
		this.longitude = longitude;
		this.latitude = latitude;
		this.photoId = photoId;
	}
	
	

	public boolean isCorePoint() {
		return isCorePoint;
	}



	public void setCorePoint(boolean isCorePoint) {
		this.isCorePoint = isCorePoint;
	}




	

	public DataPoint(boolean isProcessed, double coreDistance,
			double reachabilityDistance, double longitude, double latitude) {
		super();
		this.isProcessed = isProcessed;
		this.coreDistance = coreDistance;
		this.reachabilityDistance = reachabilityDistance;
		this.longitude = longitude;
		this.latitude = latitude;
	}





	public boolean isProcessed() {
		return isProcessed;
	}



	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}



	public double getCoreDistance() {
		return coreDistance;
	}



	public void setCoreDistance(double coreDistance) {
		this.coreDistance = coreDistance;
	}
	
	/**
	 * 如果存在的话，设置本对象的核心距离，
	 * @param neighbors 本对象eRadius内的邻居
	 * @param eRadius
	 * @param MinPts
	 * @return 是否设置了
	 */
    public boolean setCoreDistance(List<DataPoint> neighbors, double eRadius, int MinPts)
    {
    	//MinPts = 2   size = 
//        if (MinPts - 2 > neighbors.size() - 1)
//        {
//            return false;
//        }
    	
    	if (MinPts > neighbors.size())
        {
            return false;
        }
        //neighbors 是否把自己算入内
        DataPoint dp =  neighbors.get(MinPts - 1);
//        Photo minPts_p = (Photo)neighbors[MinPts - 2];
        
        double tmpCoreDistance = Math.sqrt(
                Math.pow(this.getLongitude() -dp.getLongitude(),2.0) +
                Math.pow(this.getLatitude() -dp.getLatitude(),2.0));
        this.setCoreDistance(tmpCoreDistance);
        return true;
    }
    
//    public void setReachabilityDistance(DataPoint centerPoint) {
//    	
//	}



	public double getReachabilityDistance() {
		return reachabilityDistance;
	}



	public void setReachabilityDistance(double reachabilityDistance) {
		this.reachabilityDistance = reachabilityDistance;
	}



	public int getPhotoId() {
		return photoId;
	}



	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}



	public double getLongitude() {
		return longitude;
	}



	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}



	public double getLatitude() {
		return latitude;
	}



	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	
	public double getTempleDistanceToCenter() {
		return templeDistanceToCenter;
	}



	public void setTempleDistanceToCenter(double templeDistanceToCenter) {
		this.templeDistanceToCenter = templeDistanceToCenter;
	}

}
