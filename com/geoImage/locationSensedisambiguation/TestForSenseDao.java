package com.geoImage.locationSensedisambiguation;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.MyLocationSenseDAO;

public class TestForSenseDao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestForSenseDao tfsd = new TestForSenseDao();
//		tfsd.testForDelete();
		tfsd.testForSimpleDoc(9994);
//		tfsd.testForWrongProcess();
//		tfsd.testFoSelectHql();
//		tfsd.testForSelectSql();
//		tfsd.testForInsertSql(9958, 9);
//		tfsd.testForDeleteSql(9958, 9);
	}
	
	public void testForSimpleDoc(int doc_id)
	{
		GeoNameWebSearch gnwt = new GeoNameWebSearch();
		gnwt.getLocationSenseFromWeb(doc_id);
		
	}
	
	public void testFoSelectHql()
	{
		String hql = "select new MyLocationSense(id,myLocationElement) " +
				"from MyLocationSense mls where mls.id.docId=9958 and " +
				" mls.id.wordId=9";
		Query query = HibernateSessionFactory.getSession().createQuery(hql);
		List list = query.list();
		System.out.println(list.size());
//		HibernateSessionFactory.getSession().flush();
//		HibernateSessionFactory.getSession().clear();
	}
	
	public void testForSelectSql()
	{
		String sql = "select * from my_location_sense where "+
	"doc_id = 9958 and word_id = 9 and locationSenseId=-3";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sql);
		List list = sqlQuery.list();
		System.out.println(list.size());
//		System.out.println(list.get(0));
	}
	
	public void testForInsertSql(int doc_id,int word_id)
	{
		//一次错误执行 insert语句，把insert写成,insert into my_location_sense where
		// doc_id = 9985 and word_id=9 and locationSenseId=-3;
//		Session session =  HibernateSessionFactory.getSession();

//		Transaction tx = session.beginTransaction();
		
		String sql = "insert into my_location_sense(doc_id,word_id,locationSenseId) " +
				"values ("+doc_id+","+word_id+",-3)";
		
		
		SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sql);
		int i = sqlQuery.executeUpdate();
		System.out.println("executed lines:"+i);
//		HibernateSessionFactory.getSession().getTransaction().commit();
//		
//		tx.commit();
//		session.close();
	}
	
	
	public void testForWrongProcess()
	{
		GeoNameWebSearch gnwt = new GeoNameWebSearch();
		gnwt.processWriongLocationSense(new Object[]{9958,9,"Pearl Harbor"});
	}
	
	public void testForDeleteSql(int doc_id,int word_id)
	{
		
		MyLocationSenseDAO mlsd = new MyLocationSenseDAO();
		SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery("delete from my_location_sense" +
				" where my_location_sense.doc_id = "+doc_id+" and my_location_sense.word_id="
				+word_id+" and my_location_sense.locationSenseId="+(-3));
		int i = sqlQuery.executeUpdate();
		System.out.println("executed lines:"+i);
	}

}
