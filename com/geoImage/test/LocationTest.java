package com.geoImage.test;

import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;

public class LocationTest {

	String text;

	public LocationTest() {
		// String sqlString2 =
		// "select doc_id,text from travelogue where travelogue.doc_id=10";
		// // Session session =
		// SQLQuery sqlQuery =
		// HibernateSessionFactory.getSession().createSQLQuery(sqlString2);
		// text = (String)((Object[])sqlQuery.uniqueResult())[1];
		// System.out.println(text);
	}

	public LocationTest(int docId) {
		String sqlString2 = "select doc_id,text from travelogue where travelogue.doc_id="
				+ docId;
		// Session session =
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString2);
		Object uniqueResult = sqlQuery.uniqueResult();
		if (uniqueResult instanceof Object[]) {
			text = (String) ((Object[]) uniqueResult)[1];
		} else if (uniqueResult instanceof String) {
			text = (String) uniqueResult;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocationTest lt = new LocationTest();
		System.out.println(lt.getWwjText(1));
		// new LocationElement(null,2536,2823,1);
		// lt.locationIndexTest();
		// LocationTest lt = new LocationTest(2);
		// lt.testForStringReplace();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 获取数据库中travelogue的文本
	 * 
	 * @param docId
	 *            数据库中travelogue表对应的docId
	 * @return
	 */
	public String getWwjText(int docId) {
		String sqlString2 = "select text from travelogue where travelogue.doc_id="
				+ docId;
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString2);
		String docText = null;
		Object uniqueResult = sqlQuery.uniqueResult();
		// System.out.println(uniqueResult);
		if (uniqueResult instanceof Object[]) {
			docText = (String) ((Object[]) uniqueResult)[0];
		} else if (uniqueResult instanceof String) {
			// System.out.println("String");
			docText = (String) uniqueResult;
		}
		// String docText = (String)((Object [])sqlQuery.uniqueResult())[0];
		return docText;
	}

	public String locationIndexTest(int start, int end) {

		// contain the start and end of the location name
		// 包头不包尾巴
		// System.out.println(text);

		return text.substring(start, end);
	}
}
