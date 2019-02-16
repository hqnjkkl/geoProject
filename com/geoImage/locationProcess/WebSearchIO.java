package com.geoImage.locationProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.tools.IOTools;

public class WebSearchIO {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		WebSearchIO wsio = new WebSearchIO();
//		Boolean result = wsio.getGoogleSearchResult("Latin America with a");
//		System.out.println(result);
		
//		Map a = (Map) new HashMap().put(new Object(),new Object());
//		
//		WebSearchIO wsio = new WebSearchIO();
//		wsio.getWikipediaSearchResult("Wickham Whackers");
//		Boolean result = wsio.getWikipediaSearchResult("Wickham Whackers");
//		System.out.println(result);
		
//		WebSearchIO wsio = new WebSearchIO();
//		Map<String,Boolean> map = snew HashMap<String, Boolean>();
//		map.put("hqn haha", true);
//		wsio.insertGoogleSearchResult(map);
//		wsio.insertWikipediaSearchResult(map);
//		wsio.insertAll();
	}
	
	/**
	 * 把搜索结果中的文本都放在数据库当中
	 */
	public void insertAll()
	{
		 String googleWordFileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/googleGazetter.txt";
		 String wikipediaWordFileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/wikipediaGazetter.txt";
		 BufferedReader br = null;
			String aline = null;
			String ss[] = new String[2]; 
			int i = 0;
			try {
				br = new BufferedReader(new FileReader(new File(wikipediaWordFileName)));
				while((aline=br.readLine())!=null)
				{
					ss = aline.split("\t");
					i++;
					if(getWikipediaSearchResult(ss[0])==null)
					{
						
						Map<String, Boolean> map = new HashMap<String, Boolean>();
						map.put(ss[0], Boolean.parseBoolean(ss[1]));
						if(i%100==0)
						{
						System.out.println("i:"+i+","+ss[0]+","+ss[1]);
						}
						insertWikipediaSearchResult(map);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally
			{
				
				IOTools.closeIO(br, null);
			}
	}
	
	/**
	 * 查找googleSearchTerm
	 * @param term
	 * @return
	 */
	public Boolean getGoogleSearchResult(String term)
	{
		
		String sql = "select result from googleSearchResult where googleSearchResult.word='"+term.replaceAll("'", "''")+"'";
		Session session = HibernateSessionFactory.getSession();
		Boolean result = null;
		try {			
			SQLQuery query = session.createSQLQuery(sql);
			Object r = query.uniqueResult();
			if(r==null)
			{
				return null;
			}
			result = Boolean.parseBoolean(r.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 把google查询结果插入数据库
	 * @param terms
	 */
	public void insertGoogleSearchResult(Map<String, Boolean> terms)
	{
		String sql = "insert into googleSearchResult(word,result) values ('";
		
		for (Iterator iterator = terms.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			String value = terms.get(type).toString();
			try{				
				SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sql+type.replaceAll("'", "''")+"','"+value+"')");
				sqlQuery.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println(sql+type.replaceAll("'", "''")+"','"+value+"')");
		}
	}
	
	/**
	 * 查找wikipedia的查询结果
	 * @param term
	 * @return
	 */
	public Boolean getWikipediaSearchResult(String term)
	{
		
		String sql = "select result from wikipediaSearchResult where wikipediaSearchResult.word='"+term.replaceAll("'", "''")+"'";
		Session session = HibernateSessionFactory.getSession();
		Boolean result = null;
		try{
		SQLQuery query = session.createSQLQuery(sql);
		Object r = query.uniqueResult();
		if(r==null)
		{
			return null;
		}
		result = Boolean.parseBoolean(r.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(result.toString());
		return result;
	}
	
	/**
	 * 把wikipedia的查询结果放入数据库
	 * @param terms
	 */
	public void insertWikipediaSearchResult(Map<String, Boolean> terms)
	{
		
		String sql = "insert into wikipediaSearchResult (word,result) values('";
		Session session = HibernateSessionFactory.getSession();
		for (Iterator iterator = terms.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			String value = terms.get(type).toString();			
//			System.out.println(sql+type.replaceAll("'", "''")+"','"+value+"')");
			try {				
				SQLQuery query = session.createSQLQuery(sql+type.replaceAll("'", "''")+"','"+value+"')");
				query.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return;
	}

}
