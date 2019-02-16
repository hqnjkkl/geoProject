package com.geoImage.themeLocationExaction;

import java.math.BigInteger;
import java.util.Arrays;

import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;

public class getTheNumberOfState {

	String states[] = {"Alabama","Alaska","Arizona","Arkansas",
			"California","Colorado","Connecticut","Delaware","Florida","Georgia",
			"Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas",
			"Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan",
			"Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada",
			"New hampshire","New jersey","New mexico","New York","North Carolina","North Dakota",
			"Ohio","Oklahoma","Oregon","Pennsylvania","Rhode island","South carolina",
			"South dakota","Tennessee","Texas","Utah","Vermont","Virginia",
			"Washington","West Virginia","Wisconsin","Wyoming"};
	BigInteger stateNum[] = new BigInteger[50];
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getTheNumberOfState gtnos = new getTheNumberOfState();
		String sqlString =  "SELECT count(*) FROM `travelogue` where travelogue.geo_info like '%"+"Virginia"+", United States%'";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sqlString);
		System.out.println(sqlQuery.uniqueResult());
		//		gtnos.getNums();
//		gtnos.getNewSQL();
		//80397
	}
	
	/**
	 * 获取一个复杂的SQL查询语句
	 */
	public void getNewSQL()
	{
		/*SELECT count(*) FROM `travelogue` where (
				 travelogue.geo_info not like '%, Alabama, United States' and 
				travelogue.geo_info  not like '%, Alaska, United States');
				*/
		int i;
		String sqlString =  "SELECT count(*) FROM `travelogue` where " +
				"(";
		// not in ('%, Alabama, United States')
		for(i=0;i<49;i++)
		{
			sqlString += "travelogue.geo_info not like '%"+states[i]+", United States%' and ";
		}
		
		sqlString += "travelogue.geo_info not like '%"+states[i]+", United States%')";
				
		System.out.println(sqlString);
		
		return ;
	}
	
	public void getNums()
	{
		
		//TravelogueDAO tdo = new TravelogueDAO();

		System.out.println(states.length);
		int i = 0;
//		String sqlString = "SELECT count(*) FROM `travelogue` where travelogue.geo_info like '%, "+
//		states[i]+", United States'"; 
		BigInteger n = new BigInteger("0");
		BigInteger min = new BigInteger("99999");
		BigInteger max = new BigInteger("0");
		for(i=0;i<50;i++)
		{
			String sqlString =  "SELECT count(*) FROM `travelogue` where travelogue.geo_info like '%"+states[i]+", United States%'";
			SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sqlString);
			stateNum[i] = (BigInteger)sqlQuery.uniqueResult();
			System.out.println(i+":"+states[i]+":"+stateNum[i]);
			max = max.max(stateNum[i]);
			min = min.min(stateNum[i]);
			n = n.add(stateNum[i]);
			//System.out.println("n:"+n);
		}
		Arrays.sort(stateNum);
		
		for(i=0;i<50;i++)
		{
			System.out.println(i+"-"+stateNum[i]+";");
		}
		String sqlString =  "SELECT count(*) FROM `travelogue`";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sqlString);
		
		BigInteger total = (BigInteger)sqlQuery.uniqueResult();
		System.out.println(total +" "+n);
		System.out.println(max+" "+min);
	}

}
