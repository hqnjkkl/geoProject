package com.geoImage.optics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

/**
 * 
 * @author hqn
 * @description   处理有关RTree的工具类
 * @version 0.2
 * @update 2013-11-25 下午8:26:31
 */
public class RTreeTool {
    public double orgX = -1000.0;
    public double orgY = -1000.0;
    
    public RTree rt ;
    
    public int fileCount = 0;
    
//    Hashtable<Integer, DataPoint> setOfObjects ;
    
	public static void main(String[] args) {
		double d = 0.0449157;
		System.out.println(d);
		
	}
	
	
	
    public RTreeTool() {
	}

//    public RTreeTool(Hashtable<Integer, DataPoint> setOfObjects) {
//    	this.setOfObjects = setOfObjects;
//	}
    
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
	 * 插入一个点的数据，数据点存放如一个特定的坐标系当中
	 * @param longitude
	 * @param latitude
	 * @param photoId
	 */
	public void SetTreeData(int photoId,double longitude, double latitude)//a loop
     {
         double longi = longitude * 1500.0 - orgX;//坐标系是如何计算的
         double lati = orgY - latitude * 1500.0;
         Rectangle r = new Rectangle((float)longi, (float)lati, (float)(longi + 1), (float)(lati + 1));
         rt.add(r,photoId);
         //count++;
     }
	
	/**
	 * 返回dPoint的eRadius范围内的邻居，并设置每个对象到dPoint的距离。
	 * 所有的dPoint来自setOfObjects
	 * @param dPoint
	 * @param eRadius
	 * @return
	 */
	public List<DataPoint> neighbors(Hashtable<Integer, DataPoint> setOfObjects,DataPoint dPoint,double eRadius)
	{
		List<DataPoint> neighborPoints = new ArrayList<DataPoint>();
	
		 double leftUpLongi = dPoint.getLongitude() - eRadius;
         double leftUpLati = dPoint.getLatitude() + eRadius;
         double rightDownLongi = dPoint.getLongitude() + eRadius;
         double rightDownLati = dPoint.getLatitude() - eRadius;
         List<Integer> ids = rangeSearch(leftUpLongi, leftUpLati, rightDownLongi, rightDownLati);
     
         for(int i=0;i<ids.size();i++)
         {
        	 
        	 DataPoint neighbor = setOfObjects.get(ids.get(i));
        	try {
        		if (neighbor==null) {
        			throw new Exception("");
        		}				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("the set exist Object not in setOfObjects,please" +
						"check the function RTreeTool.rangeSearch()");
				return null;
			}
        	
        	//delete the point not in the circle 
        	//test about the Math function，it is right
        	double tempDisToCenter = Math.sqrt(
                    Math.pow(dPoint.getLongitude() -neighbor.getLongitude(),2.0) +
                    Math.pow(dPoint.getLatitude() -neighbor.getLatitude(),2.0));
        	
//        	if (i%200==0) {
//        		System.out.println("***test about Math sqrt and Math pow***");
//        		System.out.println(dPoint.getLongitude()+":"+neighbor.getLongitude()+","+(dPoint.getLongitude()-neighbor.getLongitude()));
//        		System.out.println(dPoint.getLatitude()+":"+neighbor.getLatitude()+","+(dPoint.getLatitude() -neighbor.getLatitude()));
//        		System.out.println("pow:longitude"+Math.pow(dPoint.getLongitude() -neighbor.getLongitude(),2.0));
//        		System.out.println("pow:latitude"+Math.pow(dPoint.getLatitude() -neighbor.getLatitude(),2.0));
//        		System.out.println("sqrt"+tempDisToCenter);
//        	}
        	
        	if (tempDisToCenter<= eRadius)//in circle
             {
        		neighbor.setTempleDistanceToCenter(tempDisToCenter);
            	 	neighborPoints.add(neighbor);
             }   
         }
        
//         SortedList sortedList = new SortedList();
         //对neighborPoints进行排序
         Collections.sort(neighborPoints,new MyComparator());
         //test for Collections.sort right!!! from small to large 
//         String fileNameString = "E:\\optics\\exp\\test.file"+fileCount;
//         fileCount++;
//         BufferedWriter bw = null;
//         try {
//			bw = new BufferedWriter(new FileWriter(new File(fileNameString)));
//			for (Iterator iterator = neighborPoints.iterator(); iterator.hasNext();) {
//				DataPoint dataPoint = (DataPoint) iterator.next();
//				bw.write(","+dataPoint.getTempleDistanceToCenter());
//				bw.newLine();
//			}
//			bw.write("end****************************************************************end");
//			bw.newLine();
//			bw.flush();
//         } catch (IOException e) {
//			e.printStackTrace();
//		}finally
//		{
//			try {
//				bw.flush();
//				bw.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
          return neighborPoints;
	}
	
	/**
	 * 进行区域查询，查询者四个参数内的点
	 * @param leftUpLongi
	 * @param leftUpLati
	 * @param rightDownLongi
	 * @param rightDownLati
	 * @return 这个区域内点的id
	 */
   public List<Integer> rangeSearch(double leftUpLongi,double leftUpLati,double rightDownLongi,double rightDownLati)
    {
    double a1 = leftUpLongi * 1500.0 - orgX;
	double a2 = rightDownLongi * 1500.0 - orgX;
	double a3 = orgY - leftUpLati * 1500.0;
	double a4 = orgY - rightDownLati * 1500.0;
	List<Integer> ids= null;
        Rectangle r = new Rectangle((float)a1, (float)a3, (float)a2, (float)a4);
        MyProcedure mp = new MyProcedure();
        rt.contains(r, mp);
       ids =  mp.getIds();

        return ids;
    }

}
