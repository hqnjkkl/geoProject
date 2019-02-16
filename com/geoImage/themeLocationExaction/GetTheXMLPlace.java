package com.geoImage.themeLocationExaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;

public class GetTheXMLPlace {

	/**
	 * @param args
	 */
	// static int index = 1;

	public GetTheXMLPlace() {

		return;
	}

	static ArrayList<Object[]> errorList = new ArrayList<Object[]>();

	Map<Integer, String> errorMap = new HashMap<Integer, String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetTheXMLPlace gtxm = new GetTheXMLPlace();
		// gtxm.test2();
		gtxm.addRemainXML();
	}

	public void testXml() {
		GetTheXMLPlace gtxm = new GetTheXMLPlace();
		BufferedWriter errorWriter = null;
		try {

			File file = new File("e:/hqn/placeMakerXMLFile/error.txt");
			file.createNewFile();
			errorWriter = new BufferedWriter(new FileWriter(file));
			gtxm.batchGetXML(errorWriter);
			System.out.println("batch update is successful! error List is "
					+ errorList.size());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				errorWriter.flush();
				errorWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		while (errorList.size() > 0) {
			Object[] objects = errorList.get(0);
			try {
				errorList.remove(0);
				gtxm.sendPostForYQL((Integer) objects[0], (String) objects[1]);
			} catch (Exception e1) {
				e1.printStackTrace();
				errorList.add(objects);
				System.out.println("error process: " + errorList.size());
			}
		}
	}

	public void test2() {
		String existFile = "e:/hqn/placeMakerXMLFile/placemakerxml" + 69093
				+ ".xml";
		File file = new File(existFile);
		System.out.println(file.exists());
	}

	/**
	 * 处理没有查完的XMLfile，如果没法处理，就删除这一条数据
	 */
	public void addRemainXML() {
		int index = 0;
		int i, j;
		String sqlString2 = null;
		for (i = 0; i < 80400; i += 1000)// >0 <=1000;>1000 <=2000
		{
			sqlString2 = "select doc_id,text from travelogue where travelogue.doc_id>"
					+ i + " and travelogue.doc_id<=" + (i + 1000);
			// Session session =
			SQLQuery sqlQuery = HibernateSessionFactory.getSession()
					.createSQLQuery(sqlString2);
			List<Object> list = sqlQuery.list();
			for (j = 0; j < list.size(); j++) {
				Object[] obj = (Object[]) list.get(j);

				String existFile = "e:/hqn/placeMakerXMLFile/placemakerxml"
						+ (Integer) obj[0] + ".xml";
				File file = new File(existFile);
				if (!file.exists()) {
					try {
						System.out.println("sendPost2-will processed!:"
								+ (Integer) obj[0]);
						sendPostForYQL2((Integer) obj[0], (String) obj[1], file);
					} catch (Exception e1) {
						e1.printStackTrace();
						System.out.println("sendPost2:wrongDocId:"
								+ (Integer) obj[0]);
						deleteData((Integer) obj[0]);
					}
				}
			}
			System.out.println(i + "is processed");
			HibernateSessionFactory.getSession().flush();
			HibernateSessionFactory.getSession().clear();
		}
	}

	public void deleteData(int id) {
		String[] sqlStrings = {
				"delete from travelogue where travelogue.doc_id=" + id,
				"delete from travelogue_California where travelogue_California.doc_id=" + id,
				"delete from travelogue_Florida where travelogue_Florida.doc_id=" + id,
				"delete from travelogue_Hawaii where travelogue_Hawaii.doc_id=" + id,
				"delete from travelogue_Washington where travelogue_Washington.doc_id=" + id,
				"delete from travelogue_tmp where travelogue_tmp.doc_id=" + id };
		for (int i = 0; i < sqlStrings.length; i++) {
			String string = sqlStrings[i];			
			SQLQuery sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(string);
			sqlQuery.executeUpdate();
		}
	}

	public void test1() {
		String sqlString2 = "select doc_id,text from travelogue where travelogue.doc_id=123";

		try {
			sendPostForYQL(123, sqlString2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	public void batchGetXML(BufferedWriter errorWriter) {
		int i, j;

		String sqlString = "SELECT doc_id,text FROM travelogue where travelogue.doc_id >0 "
				+ "and travelogue.doc_id<=10";// 一次性查询太多，程序内存会爆掉
		String sqlString2 = null;

		try {

			for (i = 0; i < 80400; i += 1000)// >0 <=1000;>1000 <=2000
			{
				sqlString2 = "select doc_id,text from travelogue where travelogue.doc_id>"
						+ i + " and travelogue.doc_id<=" + (i + 1000);
				// Session session =
				SQLQuery sqlQuery = HibernateSessionFactory.getSession()
						.createSQLQuery(sqlString2);
				List<Object> list = sqlQuery.list();
				for (j = 0; j < list.size(); j++) {
					Object[] obj = (Object[]) list.get(j);
					try {
						sendPostForYQL((Integer) obj[0], (String) obj[1]);
					} catch (Exception e1) {
						e1.printStackTrace();
						errorList.add(obj);
						System.out.println("add: " + obj[0] + ","
								+ errorList.size());
						errorWriter.write((Integer) obj[0]);
						errorWriter.newLine();
					}
				}
				System.out.println(i + "is processed");
				HibernateSessionFactory.getSession().flush();
				HibernateSessionFactory.getSession().clear();
			}
			// List<String> textList = getTravelogueText(sqlString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				errorWriter.flush();
				// errorWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void sendPostForYQL2(int index, String content, File file)
			throws Exception {
		System.out.println();
		String url = "http://wherein.yahooapis.com/v1/document";

		String[] secrets = {
				"RjKvIevV34FCwqPSzLqt4pstbOaRxGGFvYZu91duAZ3UVo6DsRaXbBQUAdY2yTM-",
				"BMgfizjV34E0ojw_qm0c3TcEB7Pls4xgSftAV.dGHKqpz5fCsJSExxykaQWW",
				"" };
		BufferedWriter writer = null; // 写进xml文件的写对象
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// add reuqest header
		con.setRequestMethod("POST");
		// 这样子可以传递参数给雅虎，获得结果
		String urlParameters = "documentContent=" + content + "&appid="
				+ secrets[0] + "&documentType=text/plain&outputType=xml";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		if (index % 100 == 0)
			System.out.println("\n" + index
					+ "Sending 'POST' request to URL : " + url);
		// System.out.println("Post parameters : " + urlParameters);
		// System.out.println(index+": Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;

		if (file.createNewFile())// 创建了文件
		{
			writer = new BufferedWriter(new FileWriter(file));
			while ((inputLine = in.readLine()) != null) {
				writer.write(inputLine);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			in.close();

		} else {
			in.close();
			throw new Exception(file.getAbsolutePath() + " is not created!");
		}
	}

	private void sendPostForYQL(int index, String content) throws Exception {

		String url = "http://wherein.yahooapis.com/v1/document";

		String[] secrets = {
				"RjKvIevV34FCwqPSzLqt4pstbOaRxGGFvYZu91duAZ3UVo6DsRaXbBQUAdY2yTM-",
				"BMgfizjV34E0ojw_qm0c3TcEB7Pls4xgSftAV.dGHKqpz5fCsJSExxykaQWW",
				"" };

		// String textContent="I am in China";

		BufferedWriter writer = null; // 写进xml文件的写对象
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// add reuqest header
		con.setRequestMethod("POST");
		// 这样子可以传递参数给雅虎，获得结果

		String urlParameters = "documentContent=" + content + "&appid="
				+ secrets[0] + "&documentType=text/plain&outputType=xml";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		if (index % 100 == 0)
			System.out.println("\n" + index
					+ "Sending 'POST' request to URL : " + url);
		// System.out.println("Post parameters : " + urlParameters);
		// System.out.println(index+": Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));

		String inputLine;
		// int tmpIndex = index/100 + 1;
		File file = new File("e:/hqn/placeMakerXMLFile/placemakerxml" + index
				+ ".xml");// ./表示当前目录
		// if(!file.exists())
		// {
		// // file.
		// }

		if (file.createNewFile())// 创建了文件
		{
			writer = new BufferedWriter(new FileWriter(file));
			while ((inputLine = in.readLine()) != null) {
				writer.write(inputLine);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			in.close();

		} else {
			in.close();
			throw new Exception(file.getAbsolutePath() + " is not created!");
		}
	}

	/**
	 * 使用sql查询语句，查询出travelogue的text内容。 只需要HibernateSessionFactory就行
	 * 
	 * @return
	 */
	public List<String> getTravelogueText(String sqlString) {
		// SELECT count(*) FROM `travelogue`
		List<String> list = null;

		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString);
		list = sqlQuery.list();

		// System.out.println(list.size()+","+list.get(0));
		return list;
	}
}
