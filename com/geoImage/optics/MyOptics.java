package com.geoImage.optics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.geoImage.dao.GeotaggedData;
import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.OrderedFile;
import com.geoImage.dao.OrderedFileDAO;
import com.geoImage.dao.PhotoCluster;
import com.geoImage.dao.PhotoClusterDAO;
import com.geoImage.dao.PhotoClusterId;

/**
 * The main part of the algorithm OPTIC(Ordering Points to identify the
 * clustering structure)
 * 
 * @author hqn
 * 
 */
public class MyOptics {

	/**
	 * e_radius = 0.09, MinPts = 10
	 */

	Hashtable<Integer, DataPoint> setOfObjects;
	private double eRadius = 0.09;
	private int minPts = 10;

	public RTreeTool rtt;

	public OrderSeeds oss;

	OrderFileWriter ofw = null;

	SimpleDateFormat df = null;

	private int clusterOrder = 1;

	public static void main(String[] args) {
		MyOptics mOptics = new MyOptics();
		// mOptics.readAllData();// good result

//		mOptics.startOPTICS();
		mOptics.startCluster();
	}

	/**
	 * the start of Optics algorithm
	 */
	public void startOPTICS() {
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// 设置日期格式
		System.out.println("time of startOPTICS" + df.format(new Date()));// new
																			// Date()为获取当前系统时间
		rtt = new RTreeTool();

		oss = new OrderSeeds();

		ofw = new OrderFileWriter();

		readAllData();
		System.out.println("***set of Objects read:" + setOfObjects.size()
				+ ";" + df.format(new Date()));

		OrderedFileDAO orderedFile = new OrderedFileDAO();

		optics(setOfObjects, eRadius, minPts, orderedFile);
		ofw.ofd.getSession().flush();
		ofw.ofd.getSession().close();
		System.out.println("end of the order algorithm:" + setOfObjects.size()
				+ ";" + df.format(new Date()));
	}

	/**
	 * 遍历所有的对象，如果没有被处理，则进行expandClusterOrder()操作
	 * 
	 * @param setOfObjects
	 * @param eRadius
	 * @param minPts
	 * @param orderedFile
	 */
	public void optics(Hashtable<Integer, DataPoint> setOfObjects,
			double eRadius, int minPts, OrderedFileDAO orderedFile) {
		Set<Integer> keySet = setOfObjects.keySet();
		Iterator<Integer> it = keySet.iterator();
		while (it.hasNext()) {
			Integer integer = it.next();
			DataPoint dPoint = setOfObjects.get(integer);
			if (!dPoint.isProcessed()) {
				expandClusterOrder(setOfObjects, dPoint, eRadius, minPts,
						orderedFile);
			}
		}
		// orderedFile的收尾处理
		orderedFile.getSession().flush();
		orderedFile.getSession().close();

	}

	/**
	 * 进行聚类排序相关的算法操作
	 * 
	 * @param setOfObjects
	 * @param dp
	 * @param eRadius
	 * @param minPts
	 * @param orderedFile
	 */
	public void expandClusterOrder(Hashtable<Integer, DataPoint> setOfObjects,
			DataPoint dp, double eRadius, int minPts, OrderedFileDAO orderedFile) {
		List<DataPoint> neighborPoints = rtt.neighbors(setOfObjects, dp,
				eRadius);
		DataPoint currentPoint = null;

		dp.setProcessed(true);
		dp.setReachabilityDistance(DataPoint.UNDEFINED); // set to undefined
		dp.setCoreDistance(neighborPoints, eRadius, minPts);
		System.out.println(dp.getPhotoId() + ";" + dp.getCoreDistance() + ";"
				+ dp.getReachabilityDistance());
		// return ;
		ofw.writeDataPoint(dp);

		if (dp.getCoreDistance() != DataPoint.UNDEFINED) {

			oss.update(neighborPoints, dp);
			while (!oss.isEmpty()) {
				currentPoint = oss.nextPoint();
				List<DataPoint> neighborPoints2 = rtt.neighbors(setOfObjects,
						currentPoint, eRadius);

				currentPoint.setProcessed(true);
				currentPoint.setCoreDistance(neighborPoints2, eRadius, minPts);
				ofw.writeDataPoint(currentPoint);

				if (currentPoint.getCoreDistance() != DataPoint.UNDEFINED) {
					oss.update(neighborPoints2, currentPoint);
				}
			}
		}
	}

	/**
	 * 从数据库中读入表geotaggedData的数据，包括photoId,longitude,latitude三个属性
	 * 转存为List<DataPoint>类型
	 * 
	 * @return
	 */
	public Hashtable<Integer, DataPoint> readAllData() {
		String hql = "select new GeotaggedData(geoData.photoId,geoData.longitude,geoData.latitude)"
				+ " from GeotaggedData geoData";
		setOfObjects = new Hashtable<Integer, DataPoint>();
		rtt = new RTreeTool();
		// build the RTree
		System.out.println("create the RTree :" + rtt.createRTree());

		Query queryList = HibernateSessionFactory.getSession().createQuery(hql);
		List<GeotaggedData> readList = queryList.list();
		System.out.println("readList:" + readList.size());

		// 读取所有的点，并建立RTree
		for (int i = 0; i < readList.size(); i++) {
			GeotaggedData gdData = readList.get(i);
			setOfObjects.put(gdData.getPhotoId(),
					new DataPoint(gdData.getPhotoId(), gdData.getLongitude(),
							gdData.getLatitude()));
			rtt.SetTreeData(gdData.getPhotoId(), gdData.getLongitude(),
					gdData.getLatitude());
			// System.out.println(gdData.getPhotoId()+"***"+gdData.getLongitude()+"***"+gdData.getLatitude());
		}

		return setOfObjects;
	}

	/**
	 * e_radius' = 0.09° 对应10km 
	 * e_radius' = 0.0449157° 对应5km
	 *  e_radius' = 0.0179663° 对应2km 
	 *  e_radius' = 0.00898315° 对应1km 
	 *  e_radius' = 0.00449157° 对应500m 
	 * e_radius' = 0.00179663° 对应200m 
	 * e_radius' = 0.000898315° 对应100m
	 * e_radius' = 0.000449157° 对应50m
	 * 
	 * 
	 * 
	 * 
	 */
	public void startCluster()
	{
		double eRadius2s[] = {0.09,0.0449157,0.0179663,0.00898315,0.00449157,
				0.00179663,0.000898315,0.000449157};
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
		System.out.println("time of startCluster"+df.format(new Date()));// new Date()为获取当前系统时间
//		String hql = "from OrderedFile orderedFile where orderedFile.id.orderedFilePhotoId <456352";
//		String hql = "select from OrderedFile (orderedFile.id,orderedFile.) OrderedFile orderedFile";
		String hql = "select new OrderedFile(orderedFile.id,orderedFile.coreDistance," +
				"orderedFile.reachabilityDistance) from OrderedFile" +
				" orderedFile";
		//		 "select new GeotaggedData(geoData.photoId,geoData.longitude,geoData.latitude)"
//			+ " from GeotaggedData geoData";
		Query queryList = HibernateSessionFactory.getSession().createQuery(hql);
		List<OrderedFile> clusterOrderedObjs = queryList.list();
		
		System.out.println("read list of OrderFile:"+clusterOrderedObjs.size()+";"+df.format(new Date()));
		for (int i = 1; i < eRadius2s.length; i++) {	
			this.clusterOrder = i+1;
			extractDBScanCluster(clusterOrderedObjs, eRadius2s[i], this.minPts);
			System.out.println("end of a cluster: "+eRadius2s[i]+","+this.minPts+","+df.format(new Date()));// new Date()为获取当前系统时间
		}
		
	}

	/**
	 * 进行聚类算法
	 * 
	 * @param clusterOrderedObjs
	 *            需要聚类的数据点，已经有OPTICS的排序
	 * @param eRadius2
	 *            领域范围
	 * @param minPts
	 *            最小邻居个数
	 */
	public void extractDBScanCluster(List<OrderedFile> clusterOrderedObjs,
			double eRadius2, int minPts) {
		OrderedFile of = null;
		int clusterId = DataPoint.NOISE;
		PhotoCluster pCluster = null;
		PhotoClusterId pci = null;
		PhotoClusterDAO pcd = new PhotoClusterDAO();
		int count = 0;

		for (int i = 0; i < clusterOrderedObjs.size(); i++) {
			int photoId = 0;
			of = clusterOrderedObjs.get(i);
			photoId = of.getId().getOrderedFilePhotoId();
			pci = new PhotoClusterId(photoId, this.clusterOrder);

			pCluster = null;
			if (of.getReachabilityDistance() > eRadius2)// UNDEFINED > eRadius2
			{
				if (of.getCoreDistance() <= eRadius2) {
					clusterId = nextId(clusterId);

					pCluster = new PhotoCluster(pci,
							new GeotaggedData(photoId), eRadius2, clusterId,
							this.minPts);
					pcd.save(pCluster);
					count++;
					// Object.clusterId = clusterId
				} else {
					pCluster = new PhotoCluster(pci,
							new GeotaggedData(photoId), eRadius2,
							DataPoint.NOISE, this.minPts);
					pcd.save(pCluster);
					count++;
					// Object.clusterId = NOISE
				}

			} else { // pCluster.reachabilityDistance <=eRadius2;
				pCluster = new PhotoCluster(pci, new GeotaggedData(photoId),
						eRadius2, clusterId, this.minPts);
				pcd.save(pCluster);
				count++;
				// Object.clusterId = clusterId
			}
			if (count > 2000) {
				count = 0;
				pcd.getSession().flush();
				pcd.getSession().clear();
			}
		}
		pcd.getSession().flush();
		pcd.getSession().clear();
		pcd.getSession().close();
	}

	private int nextId(int clusterId) {
		if (clusterId == DataPoint.NOISE) {
			return 1;
		} else {
			return clusterId + 1;
		}
	}

}
