package com.geoImage.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.MyLocationElement;
import com.geoImage.dao.UscountryArea;
import com.geoImage.locationProcess.MergeAlgorithm;
import com.geoImage.locationSensedisambiguation.LocationSenseScore;

/**
 * 
 * @author hqn
 * @description the IO process of File,Web,DataBase
 * @version
 * @update 2014-2-25 下午7:30:18
 */
public class IOTools {

	static Logger logForIOTools = Logger.getLogger(IOTools.class.getClass());
	static Map<String, String> searchMap = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(UscountryArea.class.getName());
		// writeDataBase(string);
		// String fileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/US.txt";
		// writeDataBaseFromFile(fileName);
		// System.out.println(searchDataBase("Sawmill Creek Road"));

		// //test for getTravelogueGeoinfoText
		// Object[] obj = getTravelogueGeoinfoText(1);
		// System.out.println(obj[0]+"----------------------hqn "+obj[1]);

		// test for getMyLocationElements
		getMyLocationElements(553);

	}

	/**
	 * 
	 * @param fileName
	 *            E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/googleWikiGazetter.txt
	 * @return
	 */
	public static Map<String, Boolean> getMapFromFile(String fileName) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		BufferedReader br = null;
		String aline = null;
		String ss[] = new String[3];
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			while ((aline = br.readLine()) != null) {
				// ---------------------------------------------------------横线中间省略就可以用做读模板
				ss = aline.split("\t");
				map.put(ss[0], Boolean.parseBoolean(ss[1])); // s[0]代表单词，s[1]代表true
																// or false
				// ----------------------------------------------------------------------
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return map;
	}

	public static Map<String, String> readIPProxyFile(String proxyFile) {
		Map<String, String> ipMap = new HashMap<String, String>();

		BufferedReader br = null;
		String aline = null;
		String ss[] = new String[2];
		try {
			br = new BufferedReader(new FileReader(new File(proxyFile)));
			while ((aline = br.readLine()) != null) {
				ss = aline.split("@");
				if (ss.length == 1) {
					ipMap.put(ss[0], null);
				} else {
					ipMap.put(ss[0], ss[1]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return ipMap;
	}

	// 读取文件中的ip和port，ip和port用,分开
	public static List<String> readTerm(String termFile) {
		BufferedReader br = null;
		String aline = null;
		String ss[] = new String[2];
		List<String> result = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(new File(termFile)));
			while ((aline = br.readLine()) != null) {
				result.add(aline.split("\t")[0]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return result;
	}

	// 读取文件中的ip和port，ip和port用,分开
	public static List<String[]> readAllIp(String proxyFile) {
		BufferedReader br = null;
		String aline = null;
		String ss[] = new String[2];
		List<String[]> result = new ArrayList<String[]>();
		try {
			br = new BufferedReader(new FileReader(new File(proxyFile)));
			while ((aline = br.readLine()) != null) {
				result.add(aline.split(","));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return result;
	}

	/**
	 * 
	 * @param ipFile
	 *            ip文件的名字
	 * @return Map,key为[ip,port]对，value为1
	 */

	/**
	 * 读取文件中的ip和对应的数值(location-sense的id)，文件当中ip和数值按照id分开
	 * 
	 * @param ipFile
	 *            文件的名字
	 * @return 字符串的ip和id对
	 */
	public static Map<String, String> readPureMap(String ipFile) {
		BufferedReader br = null;
		String aline = null;
		String ss[] = new String[2];
		// List<String[]> result = new ArrayList<String[]>();
		Map<String, String> result = new HashMap<String, String>();
		try {
			br = new BufferedReader(new FileReader(new File(ipFile)));
			while ((aline = br.readLine()) != null) {
				if (result.get(aline) != null) {
					System.out.println(aline);
				} else {
					ss = aline.split("\t");
					result.put(ss[0], ss[1]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return result;
	}

	/**
	 * 
	 * @param ipFile
	 *            ip文件的名字
	 * @return Map,key为[ip,port]对，value为1
	 */
	public static Map<String, Integer> readIPPair(String ipFile) {
		BufferedReader br = null;
		String aline = null;
		String ss[] = new String[2];
		// List<String[]> result = new ArrayList<String[]>();
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			br = new BufferedReader(new FileReader(new File(ipFile)));
			while ((aline = br.readLine()) != null) {
				if (result.get(aline) != null) {
					System.out.println(aline);
				} else {
					result.put(aline, 1);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return result;
	}

	public static void readFromFileModel(String fileName) {
		BufferedReader br = null;
		String aline = null;
		String ss[] = new String[2];
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			while ((aline = br.readLine()) != null) {

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return;
	}

	/**
	 * 文件当中存储着很多整形ID，每个ID占一行,为geoName网站做测试的
	 * 
	 * @param fileName
	 *            文件名所在的路径
	 * @return 包含有整数的List
	 */
	public static List<Integer> readIdFromFile(String fileName) {
		BufferedReader br = null;
		String aline = null;
		List<Integer> ids = new ArrayList<Integer>();
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			while ((aline = br.readLine()) != null) {
				ids.add(Integer.parseInt(aline));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(br, null);
		}
		return ids;
	}

	/**
	 * 读取inputStream当中的数据
	 * 
	 * @param is
	 *            输入流
	 * @return
	 */
	public static StringBuilder readIO(InputStream is) {
		BufferedReader bReader = null;
		String aline = null;
		StringBuilder result = new StringBuilder();
		bReader = new BufferedReader(new InputStreamReader(is), 5 * 1024 * 1024);
		// int count = 0;
		try {
			while ((aline = bReader.readLine()) != null) {
				result.append(aline);
				// count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// System.out.println(count);
			IOTools.closeIO(bReader, null);
		}
		return result;
	}

	/**
	 * 读取inputStream当中的数据
	 * 
	 * @param is
	 *            输入流
	 * @return
	 * @throws IOException 
	 */
	public static StringBuilder readIO2(InputStream is) throws IOException {
		BufferedReader bReader = null;
		String aline = null;
		StringBuilder result = new StringBuilder();
		bReader = new BufferedReader(new InputStreamReader(is), 5 * 1024 * 1024);
		// int count = 0;
			while ((aline = bReader.readLine()) != null) {
				result.append(aline);
				// count++;
			}
			// System.out.println(count);
			IOTools.closeIO(bReader, null);
		return result;
	}
	/**
	 * 获取travelogue中的一条记录的Text文本字段
	 * 
	 * @param docId
	 *            travelogue表中的doc_id
	 * @return 获取的文本，如果没有，为空
	 */
	/**
	 * 获取location_sense中senseId为-2的记录
	 * 
	 * @param sql
	 * @return 一个List,List中是Object数组,数组是doc_id,word_id
	 */
	public static List<Object[]> getWrongSenseId(String sql) {
		List<Object[]> resultList = null;
		try {
			SQLQuery sqlQuery = HibernateSessionFactory.getSession()
					.createSQLQuery(sql);
			String docText = null;
			resultList = sqlQuery.list();
		} catch (Exception e) {
			return null;
		}
		return resultList;
	}

	/**
	 * 获取travelogue中的一条记录的Text文本字段
	 * 
	 * @param docId
	 *            travelogue表中的doc_id
	 * @return 获取的文本，如果没有，为空
	 */
	public static String getTravelogueText(int docId) {
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

	/**
	 * 获取my_location_sense中还未查询的doc_id，查询范围是从[id-5000,id)
	 * 
	 * @param distributedId
	 *            分配的id，
	 * @return
	 */
	public static List<Integer> getNotProcessedDoc(int distributedId) {
		String sqlString = "select DISTINCT mle.doc_id from my_location_element "
				+ "mle where mle.doc_id <"
				+ distributedId
				+ " and mle.doc_id>="
				+ (distributedId - LocationSenseScore.dataJump)
				+ " and mle.doc_id not in (select DISTINCT mls.doc_id "
				+ "from my_location_sense mls where mls.doc_id <"
				+ distributedId
				+ " and mls.doc_id>="
				+ (distributedId - LocationSenseScore.dataJump)
				+ ")";

		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString);
		List<Integer> ids = sqlQuery.list();
		return ids;
	}
	
	public static List<Object[]> getDocIdAndText()
	{
		String sqlString3 = "select doc_id,originalName from my_location_sense";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sqlString3);
		List<Object[]> list = sqlQuery.list();
		return list;
	}
	/**
	 * 获取表my_location_element中和表travelogue中
	 * doc_id对应的geo_Info,doc_id,word_id,locationOriginalText数据
	 * 
	 * @param docId
	 *            和doc_Id对应的结果列表
	 * @return 结果列表
	 */
	public static List<Object[]> getTravelogueGeoinfoDocIdWordIDLocationText(
			int docId) {
		String sqlString2 = "select t.geo_info,m.doc_id,m.word_id,m.locationOriginalText from travelogue as t ,my_location_element as m "
				+ "WHERE t.doc_id = m.doc_id and m.doc_id =" + docId;
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString2);
		List<Object[]> list = sqlQuery.list();
		// System.out.println(uniqueResult);

		// String docText = (String)((Object [])sqlQuery.uniqueResult())[0];
		return list;
	}

	/**
	 * 获取travelogue中的一条记录的geoInfo和Text
	 * 
	 * @param docId
	 *            travelogue表中的doc_id 测试结束之后，把查询语句中的信息改为travelogue_tmp
	 * @return 获取的文本，如果没有，为空
	 * 
	 */
	public static Object[] getTravelogueGeoinfoText(int docId) {
		String sqlString2 = "select geo_info,text from travelogue where travelogue.doc_id="
				+ docId;
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString2);
		Object uniqueResult = sqlQuery.uniqueResult();
		// System.out.println(uniqueResult);

		// String docText = (String)((Object [])sqlQuery.uniqueResult())[0];
		return ((Object[]) uniqueResult);
	}

	/**
	 * 获取my_location_element中的 doc_id,word_id,locationOriginalText,start,end元素
	 * 并封装在MyLocationElement类中，其中以word_id降序排序
	 * 
	 * @param doc_id
	 * @return List<MyLocationElement>
	 */
	public static List<MyLocationElement> getMyLocationElements(int doc_id) {
		// String sqlString =
		// "select doc_id,word_id,locationOriginalText,start,end " +
		// "from my_location_element where doc_id = "+doc_id+" ORDER BY word_id ASC";
		String hqlString = "select new MyLocationElement(me.id,me.locationOriginalText,me.start,me.end) from MyLocationElement me"
				+ " where me.id.docId="
				+ doc_id
				+ "  order by me.id.wordId asc";
		Session session = HibernateSessionFactory.getSession();
		List<MyLocationElement> list = session.createQuery(hqlString).list();
		return list;
	}

	/**
	 * 查询数据库中是否有相匹配的字符串,for USCountryArea
	 * 
	 * @param newTerm
	 *            需要匹配的字符串
	 * @return
	 */
	public static boolean searchDataBase(String newTerm) {
		// String sqlString = "select count(*) from USCountryArea " +
		// "where USCountryArea.`name` LIKE '%"+newTerm+" %' OR " +
		// "USCountryArea.`asciiname` LIKE '%"+newTerm+"%' OR " +
		// "USCountryArea.`alternatename` LIKE '%"+newTerm+"';";
		String sqlString = "select count(*) from USCountryArea where USCountryArea.`name` = '"
				+ newTerm + "' limit 1";
		// System.out.println(sqlString);
		Session session = HibernateSessionFactory.getSession();
		SQLQuery query = session.createSQLQuery(sqlString);
		BigInteger object = (BigInteger) query.uniqueResult();

		if (object.equals(new BigInteger("0"))) {
			return false;
		}
		// System.out.println(object);
		return true;
	}

	public void deleteSampleLocationElements() {
		String sqlString = "delete from my_location_element where doc_id ";
	}

	public static void writeToFile(String data, String fileName) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName), true));// 加在后面
			bw.write(data);
			// bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
		}
	}

	/**
	 * 把map中的key和value写入文件,中间中逗号间隔
	 * 
	 * @param map
	 *            存有数据的map
	 * @param fileName
	 *            写入数据的文件
	 */
	public static void writeIPMapToFile(Map map, String fileName) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName)));// 加在后面
			for (Iterator iterator = map.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				bw.write(key + "," + map.get(key));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
		}
	}

	/**
	 * 把map写入文件当中，map没有泛型，直接当做Object，中间用/t分割
	 * 
	 * @param map
	 *            需要写入的数据
	 * @param fileName
	 *            写入的文件
	 */
	public static void writePureMapToFile(Map map, String fileName) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName),true));// 加在后面
			for (Iterator iterator = map.keySet().iterator(); iterator
					.hasNext();) {
				Object key = iterator.next();
				bw.write(iterator.next() + "\t" + map.get(key));
				bw.flush();
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
		}
	}

	/**
	 * 只把key写入文件，key是[ip,port]对.
	 * 
	 * @param map
	 *            存储[ip,port]对的map
	 * @param fileName
	 *            需要写入的文件的名字，无则重新建立，有则覆盖内容。
	 */
	public static void writeIpToFile(Map map, String fileName) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName)));// 加在后面
			for (Iterator iterator = map.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				bw.write(key);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
		}
	}

	public static void writeMapToFile(Map<String, Boolean> map,
			Map<String, Boolean> beforemap, String fileName) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName), true));// 加在后面
			for (Iterator iterator = map.keySet().iterator(); iterator
					.hasNext();) {

				String key = (String) iterator.next();
				if (beforemap.get(key) == null) {
					bw.write(key + "\t" + map.get(key));
					bw.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
		}
	}

	/**
	 * 把获得的Ip和已经分配的id写入文件
	 * 
	 * @param map
	 * @param fileName
	 */
	public static void writeMapToFile(Map<String, Integer> map, String fileName) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName)));// 加在后面
			for (Iterator<String> iterator = map.keySet().iterator(); iterator
					.hasNext();) {

				String key = (String) iterator.next();
				bw.write(key + "/t" + map.get(key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
		}
	}

	/**
	 * 执行写数据库的操作,针对USCountryArea
	 * 
	 * @param args
	 */
	public static void writeDataBase(String args[]) {
		/*
		 * 0,4,5,14,15,16是数字，要转化成Integer类型
		 */
		if (args[4].equals("")) {
			args[4] = "NULL";
		}
		if (args[5].equals("")) {
			args[5] = "NULL";
		}
		if (args[14].equals("")) {
			args[14] = "NULL";
		}
		if (args[15].equals("")) {
			args[15] = "NULL";
		}
		if (args[16].equals("")) {
			args[16] = "NULL";
		}

		// String sqlString =
		// "insert into USCountryArea (geonameid,`name`,`asciiname`,`alternatename`,`latitude`,"+
		// "`longitude`,`feature class`,`feature code`,`country code`,`cc2`,`admin1 code`,"+
		// "`admin2 code`,`admin3 code`,`admin4 code`,`population`,`elevation`,`dem`,"+
		// "`timezone`,`modification date`) vaLues("+args[0]+",'"+args[1]+"','"+args[2]+
		// "','"+args[3]+"',"+args[4]+","+args[5]+",'"+args[6]+"','"+args[7]+"','"+args[8]+"','"+
		// args[9]+"','"+args[10]+"','"+args[11]+"','"+args[12]+"','"+args[13]+"',"+args[14]+","+args[15]+","+args[16]+",'"+args[17]+"','"+args[18]+"')";

		// System.out.println(sqlString);
		// SQLQuery sqlQuery =session.createSQLQuery(sqlString);

		// int executResult = sqlQuery.executeUpdate();
		// sqlQuery.executeUpdate();
		// tx.commit();
		return;
	}

	/**
	 * 
	 * @param fileName
	 *            读取US.txt，然后写入数据库
	 */
	public static void writeDataBaseFromFile(String fileName) {
		BufferedReader bReader = null;
		String text = null;
		// StringBuilder sb = new StringBuilder();
		Session session = HibernateSessionFactory.getSession();
		int count = 0;
		try {
			bReader = new BufferedReader(new FileReader(fileName));
			while ((text = bReader.readLine()) != null) {
				count++;
				if (count % 1000 == 0) {
					System.out.println("count:" + count);
					session.flush();
					session.clear();
				}
				// 对String当中的单引号进行"'"
				text = text.replaceAll("'", "''");
				String args[] = text.split("\t");
				try {

					// 写入数据库
					writeDataBase(args);
				} catch (Exception e) {
					e.printStackTrace();
					logForIOTools.error("insert to database error: id:"
							+ args[0] + "\n" + text);
					// return ;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeIO(bReader, null);
		}
		return;
	}

	public static void writeSearchResultFromFile(String fileName) {
		BufferedReader bReader = null;
		String text = null;
		// StringBuilder sb = new StringBuilder();
		Session session = HibernateSessionFactory.getSession();
		int count = 0;
		try {
			bReader = new BufferedReader(new FileReader(fileName));
			while ((text = bReader.readLine()) != null) {
				count++;
				if (count % 1000 == 0) {
					System.out.println("count:" + count);
					session.flush();
					session.clear();
				}
				// 对String当中的单引号进行"'"
				text = text.replaceAll("'", "''");
				String args[] = text.split("\t");
				try {

					// 写入数据库
					// String sqlString =
					// "insert into googleSearchResult (word,result)values ('"+args[0]+"','"+args[1]+"')";
					String sqlString2 = "insert into wikipediaSearchResult (word,result)values ('"
							+ args[0] + "','" + args[1] + "')";

					SQLQuery sqlQuery = session.createSQLQuery(sqlString2);

					int executResult = sqlQuery.executeUpdate();
					System.out.println(executResult);
					// System.out.println(executResult);
				} catch (Exception e) {
					e.printStackTrace();
					logForIOTools.error("insert to database error: id:"
							+ args[0] + "\n" + text);
					// return ;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeIO(bReader, null);
		}
		return;
	}

	public void testReflect() {
		Class[] argtype = new Class[] { String.class };// 代表构造方法的参数类型数组
		Object[] argparam = new Object[] { "张三" };// 代表构造方法的参数值数组
		try {
			Class classType = Class.forName(UscountryArea.class.getName());
			Constructor constructor = classType.getDeclaredConstructor(argtype); // 获得构造方法，argtype是参数类型数组，我们这里代表的是参数只有一个String类型
			constructor.setAccessible(true);// 访问私有构造函数,Spring可以配置私有的属性和方法，其实就是用到的这里
			Object accpTeacher2 = constructor.newInstance(argparam);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return;
	}

	public static String readIOReturnString(InputStream is) {
		return readIO(is).toString();
	}

	/**
	 * 关闭对应的读入流和输出流
	 * 
	 * @param reader
	 * @param writer
	 */
	public static void closeIO(Reader reader, Writer writer) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (writer != null) {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	public static void processWordPair(MergeAlgorithm ma) {
		String googleWordFileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/googleGazetter.txt";
		String wikipediaWordFileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/wikipediaGazetter.txt";
		Map<String, Boolean> beGoogleMap = IOTools
				.getMapFromFile(googleWordFileName);
		Map<String, Boolean> beWikipediaMap = IOTools
				.getMapFromFile(wikipediaWordFileName);
		// IOTools.writeMapToFile(ma.getWordPairForGoogleMap(),
		// beGoogleMap,googleWordFileName);
		// IOTools.writeMapToFile(ma.getWordPairForWikiMap(),
		// beWikipediaMap,wikipediaWordFileName);
	}

	/**
	 * 需要调整 jvm内存大小 java -Xmx1000m -Xms1000m -Xmn800m -Xss128k
	 * 调整了也报内存溢出错误，这个方法无法使用，使用数据库的方法
	 * 
	 * @param newTerm
	 * @return
	 */
	public boolean gazetterSearch(String newTerm) {
		String gazetterFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/US.txt";
		try {
			System.out.println("before read:"
					+ new Date(System.currentTimeMillis()));
			String gazetter = IOTools.readIOReturnString(new FileInputStream(
					new File(gazetterFile)));
			System.out.println("after read:"
					+ new Date(System.currentTimeMillis()));
			gazetter.contains(newTerm);
			System.out.println("after search:"
					+ new Date(System.currentTimeMillis()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}
}
