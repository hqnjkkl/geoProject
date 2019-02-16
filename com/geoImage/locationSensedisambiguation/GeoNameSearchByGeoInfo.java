package com.geoImage.locationSensedisambiguation;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.MyGeoInfo;
import com.geoImage.dao.MyGeoInfoDAO;

/**
 * 以geoInfo为分类标准，对相同geoInfo下的单词进行查询，存储到locationsense表当中
 * 
 * @author hqn
 * @description
 * @version
 * @update 2014-4-9 下午2:34:42
 */
public class GeoNameSearchByGeoInfo {

	Session session = HibernateSessionFactory.getSession();
	List<String> geoInfos = null;
	
	public GeoNameSearchByGeoInfo()
	{
		String sql = "select DISTINCT geo_info from travelogue_Hawaii th where th.doc_id in "
				+ "(select mle.doc_id from my_location_element mle)";
		geoInfos = getGeoInfos(sql);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeoNameSearchByGeoInfo gnsbgi = new GeoNameSearchByGeoInfo();
		// gnsbgi.testGetGeoInfos();
		// gnsbgi.testGetNameByGeoInfo();
		// gnsbgi.testGetElementsByName();
//		gnsbgi.testGetMyGeoInfo();
		gnsbgi.testForGetUnporcessedNum();
	}

	public void testGetGeoInfos() {
		String sql = "select DISTINCT geo_info from travelogue_Hawaii th where th.doc_id in "
				+ "(select mle.doc_id from my_location_element mle)";
		List<String> res = getGeoInfos(sql);
		System.out.println(res.size());
	}

	public void testGetNameByGeoInfo() {
		List<String> res = getDistinctNameByGeoInfo("Honolulu, Hawaii, United States");
		System.out.println(res.size());
		for (Iterator<String> iterator = res.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}
	}

	public void testGetElementsByName() {
		List<Object[]> res = getElementsByNameGeoInfo("at the beach",
				"Hawaii Volcanoes National Park, Hawaii, United States");
		System.out.println(res.size());
		for (Iterator iterator = res.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			for (int i = 0; i < objects.length; i++) {
				Object object = objects[i];
				System.out.println(object);
			}
		}
	}

	public void controlWebSearchThread() {
		int unProcessedDocs = 0;
		int webSearchCount = 0;
		String tableName = "travelogue_Hawaii";
		unProcessedDocs = getUnporcessedNum(tableName);
		List<String> namesUnderGeoinfo = null;
		List<Object[]> duplicateElements = null;
		if(unProcessedDocs>0)
		{
			while(true)
			{
				webSearchCount++;
				unProcessedDocs = getUnporcessedNum(tableName);
				//进行处理的逻辑代码
				for (Iterator<String> iterator = geoInfos.iterator(); iterator
						.hasNext();) {
					String geoInfo =  iterator.next();
					namesUnderGeoinfo = getDistinctNameByGeoInfo(geoInfo);
					
					for (Iterator<String> iterator2 = namesUnderGeoinfo.iterator(); iterator2
							.hasNext();) {
						String locationName = (String) iterator2.next();
						duplicateElements = getElementsByNameGeoInfo(locationName, geoInfo);
						
					}
				}
				if(unProcessedDocs==0)
				{
					break;
				}
				if(webSearchCount>5)
				{
					break;
				}
			}
		}
		
		
		
	}
	
	public void testForGetUnporcessedNum()
	{
		int res = getUnporcessedNum("travelogue_Hawaii");
		System.out.println(res);
	}
	
	/**
	 * 获取某个表中，还未处理的doc_id的数量
	 * @param tableName
	 * @return
	 */
	public int getUnporcessedNum(String tableName) {

		String sql = " select count(*) from " + tableName
				+ " tn where tn.doc_id not IN"
				+ "(select doc_id from my_location_sense)";
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		BigInteger res = (BigInteger)sqlQuery.uniqueResult();
		return res.intValue();
	}

	/**
	 * 获取一张表中所有单个的geoInfo信息
	 * 
	 * @param sql
	 *            取出单个的geo_info的语句 select DISTINCT geo_info from
	 *            travelogue_Hawaii th where th.doc_id in (select mle.doc_id
	 *            from my_location_element mle);
	 * @return 元素类型为String的List
	 */
	public List<String> getGeoInfos(String sql) {
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sql);
		List<String> list = sqlQuery.list();
		return list;
	}

	/**
	 * 根据geoInfo获取拥有这个geoInfo的文档的所有单个的单词
	 * 
	 * @param geoInfo
	 * @return
	 */
	public List<String> getDistinctNameByGeoInfo(String geoInfo) {
		if (geoInfo.indexOf("'") >= 0) {
			geoInfo = geoInfo.replaceAll("'", "''");
		}
		String sql = "select DISTINCT locationOriginalText from my_location_element mle where "
				+ "mle.doc_id in (select travelogue_Hawaii.doc_id from travelogue_Hawaii where travelogue_Hawaii.geo_info="
				+ "'" + geoInfo + "') and locationOriginalText is not null";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sql);
		List<String> list = sqlQuery.list();
		return list;
	}

	/**
	 * 获取在geoInfo下拥有这个locationName的所有elements的doc_id和word_id
	 * 
	 * @param locationName
	 *            某个单独的locationName
	 * @param geoInfo
	 *            某一个geoInfo
	 * @return 元素类型为Object[]的List,其中Object[0]是doc_id,Object[1]是word_id
	 */
	public List<Object[]> getElementsByNameGeoInfo(String locationName,
			String geoInfo) {
		// 处理单引号
		if (locationName.indexOf("'") >= 0) {
			locationName = locationName.replaceAll("'", "''");
		}

		if (geoInfo.indexOf("'") >= 0) {
			geoInfo = geoInfo.replaceAll("'", "''");
		}
		String sql = "select my_location_element.doc_id,my_location_element.word_id "
				+ "from my_location_element where my_location_element.locationOriginalText='"
				+ locationName
				+ "' and my_location_element.doc_id in"
				+ " (select doc_id from travelogue_Hawaii where travelogue_Hawaii.geo_info='"
				+ geoInfo + "') order by doc_id , word_id";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sql);
		List<Object[]> elements = sqlQuery.list();
		return elements;
	}

	public void testGetMyGeoInfo() {
		String geoInfoName = "haha it is a test";
		MyGeoInfoDAO mgid = new MyGeoInfoDAO();
		// mgid.save(new MyGeoInfo(geoInfoName));
		HibernateSessionFactory.getSession().flush();
		// getMyGeoInfo(geoInfoName);

		MyGeoInfo mgi = mgid.findById(geoInfoName);
		System.out.println(mgi.getGeoInfoOriginalName());
	}

	/**
	 * 根据查找出MyGeoInfo当中geoInfoName为指定字符串的MyGeoInfo
	 * 
	 * @param geoInfo
	 * @return
	 */
	public MyGeoInfo getMyGeoInfo(String geoInfo) {// public MyGeoInfo(String
													// geoInfoName, String
													// minimalName, Double
													// latitude,
													// / Double longitude,
													// String geoInfoFeature,
													// String
													// geoInfoFeatureDetails) {
		String hql = "select new MyGeoInfo(mgi.geoInfoName,mgi.minimalName,"
				+ "mgi.latitude,mgi.longitude,mgi.geoInfoFeature,mgi.geoInfoFeatureDetails) "
				+ "from MyGeoInfo mgi where mgi.geoInfoName=" + geoInfo;
		Query query = HibernateSessionFactory.getSession().createQuery(hql);
		MyGeoInfo mgi = (MyGeoInfo) query.uniqueResult();
		System.out.println(mgi);
		System.out.println(mgi.getGeoInfoOriginalName());
		return mgi;
	}
}
