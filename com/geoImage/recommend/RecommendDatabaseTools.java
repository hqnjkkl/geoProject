package com.geoImage.recommend;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;

public class RecommendDatabaseTools {

	/**
	 * @param args
	 */

	/**
	 * 获取Hawaii当中的所有docId的值，List<Object[]>当中如果只是单个的值的话，先转换成Object，再转换成想要的
	 *  Object ob = list.get(0);
		BigInteger bi = (BigInteger)ob;
	 * 类型
	 * 
	 * @return
	 */
	public static List<Object[]> getHawaiiDocIds() {
		String sql = "select doc_id from travelogue_Hawaii group by author";
		return getListBySql(sql);
	}
	/**
	 * 通过docId获得Hawaii的地名列表
	 * @param docId
	 * @return
	 */
	public static List<Object[]> getRouteByDocId(Integer docId)
	{
		String sql = "select originalName from my_location_sense where doc_id = "+docId;
		return getListBySql(sql);
	}
	
	/**
	 * 通过sql语句，获得想要的List
	 * @param sql
	 * @return
	 */
	public static List<Object[]> getListBySql(String sql) {
							 //select travelogue_Hawaii.author from travelogue_Hawaii
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sql);
		List<Object[]> list = sqlQuery.list();
		//System.out.println("sql List size:"+list.size());
		return list;
	}
	
	/**
	 * 通过Doc_Id得到用户Id
	 * @param docId
	 * @return
	 */
	public static Object getUserIdByDocId(Integer docId)
	{
		String sql1 = "select author from travelogue_Hawaii where travelogue_Hawaii.doc_id = "+docId;
		String author =(String)((Object)getListBySql(sql1).get(0));
		
		String sql2 = "select userId from hu_user where userName= "+"'"+author+"'";
		//System.out.println("author:"+author);
		Integer UserId  = null;
		try {
			
			UserId = (Integer)(Object)getListBySql(sql2).get(0);
		} catch (Exception e) {
			System.out.println(UserId);
		}
		return UserId;
	}
	
	/**
	 * 数据库当中，获得唯一结果，一条数据可能有很多列，所以很多列得用Object[]来表示
	 * @param sql
	 * @return
	 */
	public static List<Object[]> getUniqueResult(String sql)
	{
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sql);
		List<Object[]> uniqueResult = sqlQuery.list();
//		if (uniqueResult instanceof Object[]) {
//			docText = (String) ((Object[]) uniqueResult)[0];
//		} else if (uniqueResult instanceof String) {
//			// System.out.println("String");
//			docText = (String) uniqueResult;
//		}
		return uniqueResult;
	}
	/**
	 * 获得某个表格的大小
	 * @param sql
	 * @return
	 */
	public static Integer getTableSize(String sql)
	{
		List<Object[]> list = getUniqueResult(sql);
		BigInteger res = (BigInteger)((Object)list.get(0));
		return res.intValue();
	}
	public static void main(String[] args) {
		//List<Object> res = (List<Object>)getHawaiiDocIds();
//		System.out.println(getUserIdByDocId(43029));
		new RecommendDatabaseTools().fun();
	}
	void fun()
	{
		byte a[] = new byte[3];
		System.out.println(a[0]);
	}
	
	
}
