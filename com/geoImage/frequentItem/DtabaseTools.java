package com.geoImage.frequentItem;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SQLQuery;

import com.geoImage.dao.FreSenseThr5;
import com.geoImage.dao.FreSenseThr5DAO;
import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.HuUser;
import com.geoImage.dao.HuUserDAO;

/**
 * 主要是和数据库操作的一些插入修改函数
 * @author hqn
 * @description   
 * @version
 * @update 2015-1-3 下午2:55:06
 */
public class DtabaseTools {

	/**
	 * 把提取到的频繁项，插入到数据库的表fre_sense_thr5当中
	 * @param f1items
	 *            key是一个个地理位置，值是出现的次数
	 */
	public static void insertFreItems(Map<Set<String>,Integer> freItems,Map<String, Integer> senseIndex)
	{
		FreSenseThr5DAO fst5d = new FreSenseThr5DAO();
		for(Iterator<Set<String>> iterator = freItems.keySet().iterator();iterator.hasNext();)
		{
			Set<String> keySet = iterator.next();
			Integer value = freItems.get(keySet);
			StringBuffer ss = new StringBuffer();
			StringBuffer routeWordIds = new StringBuffer();
			for(Iterator<String> it2 = keySet.iterator();it2.hasNext();)
			{
				String nextString = it2.next();
				ss.append(nextString);
				routeWordIds.append(senseIndex.get(nextString));
				
				if (it2.hasNext()) {
					ss.append("->");
					routeWordIds.append(",");
					}
			}
			//第一个空的是主键，会自动插入，第二个是Travelogue其实和很多个doc相关联，第三个是word_id感觉知道额也没用s
			//如果是hibernate使用increment，并且数据库当中使用的是autoincrement，主键是可以为null的
			FreSenseThr5 fst5 = new FreSenseThr5(null, null,keySet.size(), value, ss.toString(),routeWordIds.toString(),5);	
			fst5d.save(fst5);
		}
		return ;
	}

	/**
	 * 用来做测试的一个函数，测试和数据库fre_sense_thr5能否连通
	 * 
	 * @return
	 */
	public static List<Object[]> getDocIdAndText() {
		String sqlString3 = "select routeId,routeText from fre_sense_thr5";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString3);
		List<Object[]> list = sqlQuery.list();
		return list;
	}
	/**
	 * 查询Hawaii当中用户的种类
	 * @return
	 */
	public static List<Object[]> getUserFromTravelogue() {
		String sqlString3 = "select author from travelogue_Hawaii group by author";
							 //select travelogue_Hawaii.author from travelogue_Hawaii

		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString3);
		List<Object[]> list = sqlQuery.list();
		System.out.println(list.size());
		System.out.println(list.size());
		return list;
	}
	
	/**
	 * 给HuUser表的用户初始化数据
	 */
	public static void insertIntoHuUser()
	{
		HuUserDAO hud = new HuUserDAO();
		List<Object[]> lists = getUserFromTravelogue();
		for(int i=0;i<lists.size();i++)
		{
			System.out.println(lists.get(i)+","+lists.get(i));
			HuUser hu = new HuUser(lists.get(i)+"", null, null);
			hud.save(hu);
		}
	}
	
	public static void main(String[] args) {
		// writeSearchResultFromFile();
		//List<Object[]> res = getDocIdAndText();
		//FreSenseThr5 fst5 = new FreSenseThr5(7, null,3, 4, "a->b", null, 5);
		//writeSearchResultFromFile(fst5);
		//System.out.println(res);
		insertIntoHuUser();
	}

	public static void writeSearchResultFromFile(FreSenseThr5 fst5) {
			FreSenseThr5DAO fst5d = new FreSenseThr5DAO();
			// FreSenseThr5 fst5 = new FreSenseThr5(1, new Travelogue(4), 4,
			// "a->b->c", "3,4,5", 5);
			fst5d.save(fst5);
		return;
	}
}
