package com.geoImage.locNameStatics;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.geoImage.dao.HibernateSessionFactory;


public class geoInfoPreprocessing {

	static int DOC_ID = 0;
	static int GEO_INFO_INDEX = 2;
	static int MAX_UPDATE = 100;
	static String COUNTRY = "united states";
	String regionName = "Hawaii";
	String tableName = "travelogue_" + regionName;

	/* 去除地名尾部（United States之后）的多余字符 */
	public void TailTrim() {
		int count = 0;
		String sqlQuery = "select * from " + tableName;

		// 从数据库读取geo_info
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(sqlQuery);
		ScrollableResults scres = query.scroll();
		
		while (scres.next()) {
			// 读取geo_info属性值
			String geoInfoStr = (String) scres.get(GEO_INFO_INDEX);
			String tmpStr = geoInfoStr.toLowerCase();
			
			// 判断United States之后是否还有词
			if (!tmpStr.endsWith(COUNTRY)) {
				String doc_id = scres.get(DOC_ID).toString();
				int supposedTail = tmpStr.indexOf(COUNTRY) + COUNTRY.length();
				geoInfoStr = geoInfoStr.substring(0, supposedTail);
				// 更新操作
				String sqlUpdate = "update " + tableName
						+ " set geo_info = \"" + geoInfoStr
						+ "\" where doc_id = " + doc_id;
				SQLQuery updateQuery = session.createSQLQuery(sqlUpdate);
				updateQuery.executeUpdate();
				count++;
				if (count % MAX_UPDATE == 0) {
					session.flush();
					session.clear();
				}
			}
		}
		session.flush();
		session.clear();
		tx.commit();
		session.close();
	}

	public void testTailTrim()
	{
		int count = 0;
		String sql = "select * from " + tableName;
		sql += " where doc_id = 9990";
		
		Session session = HibernateSessionFactory.getSession();
		Transaction tr = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(sql);
		
		ScrollableResults scres = query.scroll();
		while(scres.next()){
			//判断United States之后是否还有词
			String geoInfoStr = (String) scres.get(GEO_INFO_INDEX);
			String tmpStr = geoInfoStr.toLowerCase();
			String doc_id = scres.get(DOC_ID).toString();
			if (!tmpStr.endsWith(COUNTRY)){
				int supposedTail = tmpStr.indexOf(COUNTRY) + COUNTRY.length();
				geoInfoStr = geoInfoStr.substring(0, supposedTail);
				System.out.println("Update doc_id=" + doc_id + ", geo_info=" + tmpStr + " as " + geoInfoStr);
				count++;
			}
			else {
				System.out.println("doc_id " + doc_id + " don't need update!");
			}			
		}
		System.out.println("Finally " + count + " docs " + "updated!");
		tr.commit();
		session.close();
	}

	public static void main(String[] args) {
		geoInfoPreprocessing gip = new geoInfoPreprocessing();

		gip.testTailTrim();
		//gip.TailTrim();
	}
}
