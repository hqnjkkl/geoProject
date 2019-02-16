package com.geoImage.optics;

import com.geoImage.dao.GeotaggedData;
import com.geoImage.dao.GeotaggedDataDAO;
import com.geoImage.dao.OrderedFile;
import com.geoImage.dao.OrderedFileDAO;
import com.geoImage.dao.OrderedFileId;

/**
 * 
 * @author hqn
 * @description 把记录写入数据库的OrderedFile当中
 * @version
 * @update 2013-11-26 上午10:20:41
 */

public class OrderFileWriter {
	
	OrderedFileDAO ofd = new OrderedFileDAO();
	
	int count = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		OrderFileWriter ofw = new OrderFileWriter();
		
		ofw.writeDataPoint(104);
	}
	
	/**
	 * 把一个数据点的数据写入数据库  // 需呀测试
	 * @param dp
	 */
	public void writeDataPoint(DataPoint dp)
	{
//		GeotaggedDataDAO gdd = new GeotaggedDataDAO();
//		GeotaggedData gd = gdd.findById(dp.getPhotoId());
		OrderedFileId ofid = new OrderedFileId(dp.getPhotoId(),DataPoint.opticsOrder);
		OrderedFile orderedFile = new OrderedFile(ofid,
				new GeotaggedData(dp.getPhotoId()),
				dp.getCoreDistance(),
				dp.getReachabilityDistance());
		
		ofd.save(orderedFile);
		count++;
		if(count==1000)
		{
			ofd.getSession().flush();
			ofd.getSession().clear();
			count = 0 ;
		}
//		tx.commit();
//		System.out.println(ofd.findAll());
	}

	public void writeDataPoint(int id)
	{
		
		OrderedFileDAO ofd = new OrderedFileDAO();

		GeotaggedDataDAO gdd = new GeotaggedDataDAO();
		GeotaggedData gd = gdd.findById(id);
//		
//		OrderedFile orderedFile = new OrderedFile(id,
//				gd,DataPoint.opticsOrder);
		OrderedFileId ofid = new OrderedFileId(id,DataPoint.opticsOrder);
		OrderedFile orderedFile = new OrderedFile(ofid,
				new GeotaggedData(id));
		ofd.save(orderedFile);
		ofd.getSession().flush();
		
//		tx.commit();
//		System.out.println(ofd.findAll());
	}
}
