package com.geoImage.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.geoImage.dao.GeotaggedData;
import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.optics.DataPoint;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

public class TestForRtree {

    public double orgX = -1000.0;
    public double orgY = -1000.0;
    
    public RTree rt ;
    
    public List<DataPoint> setOfObjects;
    
    /**
     * 初始化Rtree测试类
     * @param beginX
     * @param beginY
     */
    
    public TestForRtree(){}//空构造函数
    
    /**
     * 带初始参数的构造函数
     * @param beginX
     * @param beginY
     */
    public TestForRtree(double beginX,double beginY)
    {
    	  orgX = beginX * 1500.0;
          orgY = beginY * 1500.0;
    }
    
    /**
     * 创建并初始化RTree,创建时，这棵树是空的
     * @return
     */
    public boolean createRTree()
    {	
    	rt = new RTree();
    	System.out.println("***RTree is created");
    	rt.init(null);
    	System.out.println();
    	return true;
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		double d3 = 0.1234567891;
		double d4 = 0.12345678912;
		double d5 = 0.123456789123;
		double d6 = 0.1234567891234;
		double d7 = 0.12345678912345;
		System.out.println(d3+","+d4+","+d5+","+d6+","+d7);
//		TestForRtree tfr = new TestForRtree();
//		tfr.readAllData();
	}

	/**
	 * 从数据库中读入表geotaggedData的数据，包括photoId,longitude,latitude三个属性
	 * 转存为List<DataPoint>类型
	 * @return
	 */
	public List<DataPoint> readAllData()
	{
//		String hql = "select new GeotaggedData(geoData.photoId,geoData.longitude,geoData.latitude)" +
//				" from GeotaggedData geoData";
		String hql = "select new GeotaggedData(geoData.photoId,geoData.longitude,geoData.latitude)"+
		" from GeotaggedData geoData where geoData.photoId<10070907";
		setOfObjects = new ArrayList<DataPoint>();
		
		Query queryList=HibernateSessionFactory.getSession().createQuery(hql);
		List<GeotaggedData> readList = queryList.list();
		System.out.println("readList:"+readList.size());
		
		for (int i = 0; i < readList.size(); i++) {
			GeotaggedData gdData = readList.get(i);
			setOfObjects.add(new DataPoint(gdData.getPhotoId(), gdData.getLongitude(), gdData.getLatitude()));
//			System.out.println(gdData.getPhotoId()+"***"+gdData.getLongitude()+"***"+gdData.getLatitude());
		}
		
	System.out.println("setOfObject size:"+setOfObjects.size());
		return setOfObjects;
	}
	/**
	 * 插入一个点的数据，数据点存放如一个特定的坐标系当中
	 * @param longitude
	 * @param latitude
	 * @param photoId
	 */
	public void SetTreeData(double longitude, double latitude,int photoId)//a loop
     {
         double longi = longitude * 1500.0 - orgX;//坐标系是如何计算的
         double lati = orgY - latitude * 1500.0;
         Rectangle r = new Rectangle((float)longi, (float)lati, (float)(longi + 1), (float)(lati + 1));
         rt.add(r,photoId);
         //count++;
     }
	
	
//	
//	public RTree getSetOfObjects(){
//		RTree setOfObjects = new RTree();
//		
//		setOfObjects.add();
//		
//		return setOfObjects;
//	}

}
