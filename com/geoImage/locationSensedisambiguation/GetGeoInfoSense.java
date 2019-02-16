package com.geoImage.locationSensedisambiguation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.MyGeoInfo;

/**
 * 
 * @author hqn zz
 * @description
 * @version
 * @update 2014-4-10 下午4:15:01
 */
public class GetGeoInfoSense {

	private String countryName = "";
	private String stateName = "";
	private String tableName = "";

	/**
	 * 在distinctGeoInfo中保存修改后的geo_info
	 */
	public Map<String, String> distinctGeoInfo;
	/**
	 * 遍历distinctGeoInfo，然后把GeoInfo_Modified进行拆分放到geoWordMap里
	 */
	public Map<String, MyGeoInfo> geoWordMap;

	private WebSearchForGeoInfo webSearch;

	// 构造函数
	public GetGeoInfoSense(String state_name, String country_name) {
		this.countryName = country_name;
		this.stateName = state_name;
		this.tableName = "travelogue_" + state_name;
		distinctGeoInfo = new HashMap<String, String>();
		geoWordMap = new HashMap<String, MyGeoInfo>();
		this.webSearch = new WebSearchForGeoInfo();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GetGeoInfoSense ggis = new GetGeoInfoSense("Hawaii", "United States");

		ggis.modifyGeoInfo();
	}

	/**
	 * @return 键值类型都为String的map,key是geoInfo在数据库中的名称
	 *         value是对geoInfo进行一系列修改过后的符合规范的geoInfoName
	 */
	public void modifyGeoInfo() {
		GetGeoInfoText();
		GeoInfoFormatRegularizer();
		geoWordSplit();
	}

	/**
	 * 为geoWordMap里的每个词获取实体信息
	 */
	public void GeoInfoSenseRetrieving() {
		
		Iterator<String> words = geoWordMap.keySet().iterator();
		while (words.hasNext()) {
			String geoWord = words.next();
			Map<String, Object[]> senseTime = new HashMap<String, Object[]>();
			int webSR = 0;
			webSR = webSearch.searchGeoInfoOnGeoNames(geoWord + "," + stateName,
					senseTime);
			if (webSR == 0) {
				// 再在数据库中查找
				String sql = "SELECT count(doc_id) numCount, geo_info from travelogue_Hawaii where"
						+ "geo_info LIKE '"
						+ geoWord
						+ "%' GROUP BY geo_info order by numCount desc" + "limit 1";
				SQLQuery sqlQuery = HibernateSessionFactory.getSession()
						.createSQLQuery(sql);
				Object[] result = (Object[]) sqlQuery.uniqueResult();
				if(result==null)
				{
					geoWordMap.put(geoWord,null);
				}
				String geoInfoSense = (String) result[1];
				// value = new MyGeoInfo(, geoInfoSense, latitude, longitude,
				// geoInfoFeature, geoInfoFeatureDetails)
			} else if (webSR == 1) {
				// locationSense,latitudeString,longitudeString,featureClass,featureDetails
				
			} else {
				ArrayList<Integer> res = new ArrayList<Integer>();
				res.add(null);
			}
		}
		

		return;
	}

	/**
	 * 把一条geoInfo里几个geoWord的geoInfoSense进行合并
	 * 
	 * @param senses
	 * @return
	 */
	public Object[] geoInfoSenseMerge(List<Object> senses) {
		Object[] result = null;
		
		return result;
	}

	/**
	 * 在GeoWordMap中查找key为geoWord的item，并返回其value
	 * 
	 * @param geoWord
	 * @return
	 */
	public MyGeoInfo FindByGeoWordMap(String geoWord) {
		MyGeoInfo value = null;

		value = geoWordMap.get(geoWord);

		return value;
	}

	/**
	 * 从表中读取geo_info数据，并存到Map<geoInfo_OriginalName, geoInfo_ModifiedName>里
	 */
	public void GetGeoInfoText() {
		// 读取数据
		String sqlQuery = "select distinct geo_info from " + tableName
				+ " order by geo_info";
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(sqlQuery);
		List<String> qResults = query.list();

		// 存到Map<geoInfo_OriginalName, geoInfo_ModifiedName>里
		for (String qResult : qResults) {
			distinctGeoInfo.put(qResult, "");
		}
		tx.commit();
		session.close();
		System.out.println(distinctGeoInfo.size()
				+ " distinct geo_info items are retrieved.");
	}

	/**
	 * 将geo_info中的一些异常格式规范化
	 */
	public void GeoInfoFormatRegularizer() {
		int errorTermCount = 0, totalCount = 0;
		String curTerm = "";
		String newTerm = "";
		String[] replacePattern = { "/", " and ", " to ", " from ", " - " };
		String[] removePattern = { "\'", "‘" };

		// 对每条geo_info进行处理
		Set<String> geoInfoItems = distinctGeoInfo.keySet();
		for (String geoInfoItem : geoInfoItems) {
			curTerm = geoInfoItem;
			newTerm = curTerm;
			// 括号处理
			if (curTerm.contains("(") && curTerm.contains(")")) {
				// 拆分每个括号并处理
				while (newTerm.contains("(")) {
					int lParen = newTerm.indexOf("(");
					int rParen = newTerm.indexOf(")");
					String content = newTerm.substring(lParen + 1, rParen);
					// 拼接新字符串
					String tmp = newTerm.substring(0, lParen);
					tmp += ", " + content;
					tmp += newTerm.substring(rParen + 1, newTerm.length());
					newTerm = tmp;
				}
			}
			// 需要去除处理的pattern：各种单引号
			for (int j = 0; j < removePattern.length; j++) {
				if (curTerm.contains(removePattern[j]))
					newTerm = this.RemoveAllPattern(newTerm, removePattern[j]);
			}
			// 需要替换成逗号处理的pattern：斜杠 , and, to, from, 破折号
			for (int j = 0; j < replacePattern.length; j++) {
				if (curTerm.contains(replacePattern[j]))
					newTerm = this
							.ReplaceAllPattern(newTerm, replacePattern[j]);
			}
			// 多余州名，国家名处理
			newTerm = RemoveRedundancy(newTerm, this.stateName);
			newTerm = RemoveRedundancy(newTerm, this.countryName);
			// 在distinctGeoInfo中保存修改后的geo_info
			if (newTerm.compareTo(curTerm) != 0) {
				distinctGeoInfo.put(curTerm, newTerm);
				System.out.println("geo_info \'" + curTerm
						+ " \' has been updated as \'" + newTerm + "\'.");
				errorTermCount++;
			} else {
				System.out.println("geo_info \'" + curTerm
						+ "\' do not need update!");
			}
			totalCount++;
		}
		System.out.println(totalCount + " items has been scaned, and "
				+ errorTermCount + " items has been normalized!");
	}

	/**
	 * 将规范化后的geo_info以逗号为单元进行拆分
	 */
	public void geoWordSplit() {
		Iterator iter = distinctGeoInfo.entrySet().iterator();
		// 遍历distinctGeoInfo，然后把GeoInfo_Modified进行拆分放到geoWordMap里
		while (iter.hasNext()) {
			Map.Entry<String, String> mapEn = (Map.Entry<String, String>) iter
					.next();
			String geoInfoTerm = mapEn.getValue();
			if (geoInfoTerm.isEmpty())
				geoInfoTerm = mapEn.getKey();
			String[] geoWords = geoInfoTerm.split(",");
			for (int i = 0; i < geoWords.length; i++) {
				String geoWord = geoWords[i].trim();
				if (!geoWord.isEmpty() && !geoWordMap.containsKey(geoWord))
					geoWordMap.put(geoWord, null);
			}
		}
		// 打印统计信息
		System.out
				.println(geoWordMap.size() + " geo words has been extracted!");
		int count = 0;
		Set<String> keys = geoWordMap.keySet();
		for (String key : keys) {
			count++;
			if (count % 10 == 0)
				System.out.print(key + "\n");
			else
				System.out.print(key + ",\t");
		}
		System.out.println();
		return;
	}

	/**
	 * 从字符串中去除第一个符合pattern定义的子串
	 */
	private String RemoveOnePattern(String objStr, String regX) {
		String newTerm = "";

		if (objStr.contains(regX)) {
			// 拼接
			int index = objStr.indexOf(regX);
			String formerStr = objStr.substring(0, index - 1).trim();
			String latterStr = objStr.substring(index + 1, objStr.length())
					.trim();
			newTerm = formerStr + latterStr;
			return newTerm;
		} else {
			return objStr;
		}
	}

	/**
	 * 从字符创中去除所有符合pattern定义的子串
	 */
	private String RemoveAllPattern(String objStr, String regX) {
		String newTerm = "";

		if (objStr.contains(regX)) {
			String[] fragments = objStr.split(regX);
			// 根据pattern进行拆分，trim space后合并
			for (int i = 0; i < fragments.length; i++) {
				String tmp = fragments[i].trim();
				if (!tmp.isEmpty())
					newTerm += tmp;
			}
			return newTerm;
		} else {
			return objStr;
		}
	}

	/**
	 * 用逗号替代字符串中所有pattern出现的位置
	 */
	private String ReplaceAllPattern(String objStr, String regX) {
		String newTerm = "";

		if (objStr.contains(regX)) {
			String[] fragments = objStr.split(regX);
			for (int i = 0; i < fragments.length; i++) {
				String tmp = fragments[i].trim();
				if (i == 0 && !tmp.isEmpty())
					newTerm += tmp;
				else if (!tmp.isEmpty())
					newTerm += ", " + tmp;
				else
					;
			}
			return newTerm;
		} else {
			return objStr;
		}
	}

	/**
	 * 判断一个字符串中是否含有字母
	 * 
	 * @param s
	 * @return
	 */
	private boolean ValidWord(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (Character.isLetter(s.charAt(i)))
				return true;
		}
		return false;
	}

	/**
	 * 去除字符串中的冗余子串，只保留最后一处子串
	 */
	private String RemoveRedundancy(String objStr, String regX) {
		String newTerm = "";

		if (objStr.contains("Kona"))
			System.out.print("O");
		if (objStr.lastIndexOf(regX) > objStr.indexOf(regX)) {
			String[] fragments = objStr.split(regX);
			int size = fragments.length;
			for (int i = 0; i < fragments.length; i++) {
				if (ValidWord(fragments[i]))
					newTerm += fragments[i];
				if (i == (size - 2) || size == 1)
					newTerm += regX;
			}
			return newTerm;
		}
		return objStr;
	}
}
