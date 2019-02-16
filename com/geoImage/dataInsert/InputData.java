package com.geoImage.dataInsert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.geoImage.dao.GeotaggedData;
import com.geoImage.dao.GeotaggedDataDAO;
import com.geoImage.dao.HibernateSessionFactory;

public class InputData {

	String[] aLineStrings = null;
	String aLine = null;
	DealCharacter dc = new DealCharacter();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		InputData iData = new InputData();
		// iData.test2();
		String fileName = new String(
				"E:\\optics\\exp\\optics\\InformationCity.txt");
		iData.insertAllData(fileName);

		// System.out.println("\\");
		// iData.insertAllData(fileName);
		// String dateString = "2008-8-10 00:00:00.000";
		// try {
		// System.out.println(iData.string2Time(dateString));
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/**
	 * �������ı����е���ݲ�����ݿ⵱��
	 * 
	 * @param fileName
	 */
	public void insertAllData(String fileName) {
		BufferedReader bReader = null;
		GeotaggedDataDAO gdd = new GeotaggedDataDAO();
		GeotaggedData gd = null;
		BufferedWriter bWriter = null;
		BufferedWriter bWriter2 = null;
		BufferedWriter bWriter3 = null;
		String logger1 = "E:\\optics\\exp\\optics\\AnalysisErrorLogger.txt";
		String logger2 = "E:\\optics\\exp\\optics\\CharacterErrorLogger.txt";
		String logger3 = "E:\\optics\\exp\\optics\\TagAndTitleEmptyLogger.txt";
		int cal = 0;// ����ѭ���Ĵ�����������С�β������

		int sta = 0;// һ���ַ���ʼλ��
		int end = 0;// һ���ַ����λ��
		int left = 0;// �����ŵĸ���
		int right = 0;// �����ŵĸ���
		int j = 0;// �ڼ������
		Integer errors[] = new Integer[1000];
		String errorStrings[] = new String[200];
		int eIndex = 0;
		Transaction tx = HibernateSessionFactory.getSession()
				.beginTransaction();
		try {
			bReader = new BufferedReader(new FileReader(new File(fileName)));
			bWriter = new BufferedWriter(new FileWriter(new File(logger1))); //解析错误记录
			bWriter2 = new BufferedWriter(new FileWriter(new File(logger2)));//字符去除记录
			bWriter3 = new BufferedWriter(new FileWriter(new File(logger3)));//tag和title为空记录
			// String aLine =
			// "(14380!@#$%^&*4546~_907)()(Tom Farris)(24 September 2008)(-77.529315999999994)(160)(240)(black forest trail 3)(41.515903999999999)(http://www.panoramio.com/user/2263509)(2263509)(http://mw2.google.com/mw-panoramio/photos/small/14380907.jpg)"+
			// "(http://www.panoramio.com/photo/14380907)-";
			while ((aLine = bReader.readLine()) != null) {

				// System.out.println(aLine);

				aLineStrings = new String[13];

				j = 0;// calculate the order of line
				// sta = 0;
				// end = 0;
				left = 0; // make the start and end is empty
				right = 0;// make the start and end is empty
				for (int i = 0; i < aLine.length(); i++) {
					if (aLine.charAt(i) == '(') {
						left++;
						if (left == 1) {
							sta = i;
						}
					} else if (aLine.charAt(i) == ')') {
						right++;
						end = i;
						if (left == right) {
							aLineStrings[j] = aLine.substring(sta + 1, end);
							j++;
							left = 0;
							right = 0;
						}
					}
				}

				// aLine.split("[)]",0);//������ʽ��ƥ����"("��ͷ��")"��β���м��������ַ�(���ˡ�����������)���ַ�

				// System.out.println(aLineStrings.length);
				// for (int i = 0; i < aLineStrings.length; i++) {
				// System.out.println(aLineStrings[i]);
				// }

				// (PhotoID)(photoTags)(ownerName)(uploadDate)(Longitude)(height)
				// (width)(photoTile)(Latitude)(ownerUrl)(ownerId)
				// (photoFileUrl)(photoUrl)

				// if(aLineStrings[t].equals(""))

				gd = null;// initial it as null every time
				try {
					gd = analysisGeotaggedData(aLineStrings);
				} catch (Exception e) {
//					e.printStackTrace();
					System.out.println("first analysis is error,start second analysis");
					// the second time analysis
					try {
						aLineStrings = secondAnalysis(aLine);
						gd = analysisGeotaggedData(aLineStrings);
					} catch (Exception e2) {
						bWriter.write(aLine);
						bWriter.flush();
						bWriter.newLine();
						System.out
								.println("It is second analysized wrong recorder"
										+ cal);
						System.out.println("the wrong order:" + eIndex);
						// record the error data
						eIndex++;
						// errors[eIndex] = gd.getPhotoId();
						// System.out.println(errors[eIndex]);
					}
				}

				if (gd != null) {
					gd.setPhotoTags(gd.getPhotoTags().replaceAll(
							"[^a-zA-Z0-9,]", " "));// 除去非以上三种编码符号
					gd.setPhotoTitle(gd.getPhotoTitle().replaceAll(
							"[^a-zA-Z0-9,]", " "));
					if (!(aLineStrings[1].equals(gd.getPhotoTags())
							&& aLineStrings[7].equals(gd.getPhotoTitle()))){
						bWriter2.write(aLine);//去除符号前后文本不一致
						bWriter2.flush();
						bWriter2.newLine();
					}
					gd.setPhotoTags(dc.trimBlank(gd.getPhotoTags()));
					gd.setPhotoTitle(dc.trimBlank(gd.getPhotoTitle()));
					//tag and title both are empty
					if(gd.getPhotoTags().equals("")&&gd.getPhotoTitle().equals(""))
					{
						bWriter3.write(aLine);
						bWriter3.flush();
						bWriter3.newLine();
						cal++;
						if (cal % 1000 == 0)
							System.out.println("the recorder inserted come to " + cal
									+ ";");
						continue;
					}
					
					gdd.save(gd);
					
				}
				// System.out.println(gd.toString());
				// System.out.println(aLineStrings[]);
				// HibernateSessionFactory.getSession().flush();
				cal++;// ��������С�β������
				if (cal % 1000 == 0)
				{
					HibernateSessionFactory.getSession().flush();
					HibernateSessionFactory.getSession().clear();
					System.out.println("the recorder inserted come to " + cal
							+ ";");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(cal);
			System.out.println(aLine+"---"+aLine.length());
			for (int i = 0; i < aLineStrings.length; i++) {
				System.out.println(i + "----" + aLineStrings[i]);
			}
		} finally {
			HibernateSessionFactory.getSession().flush();
			HibernateSessionFactory.getSession().clear();
			try {
				bReader.close();
				
				bWriter.flush();
				bWriter.close();
				
				bWriter2.flush();
				bWriter2.close();
				
				bWriter3.flush();
				bWriter3.close();
				tx.rollback();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(cal);
				System.out.println("the aLine at the end"+aLine+"---");
				for (int i = 0; i < aLineStrings.length; i++) {
					System.out.println(i + "----" + aLineStrings[i]);
				}
			}

		}
	}

	/**
	 * get a new GeotaggedData Object from the analyzied String array
	 * 
	 * @param resultString
	 *            the result of the text analysis
	 * @return
	 * @throws ParseException
	 */
	public GeotaggedData analysisGeotaggedData(String[] resultString)
			throws ParseException {

		GeotaggedData gd = new GeotaggedData();

		gd.setPhotoId(Integer.parseInt(aLineStrings[0]));
		// System.out.println(aLineStrings[1]);
		// String tagString = aLineStrings[1];

		gd.setPhotoTags(aLineStrings[1]);
		gd.setOwnerName(aLineStrings[2]);

		Timestamp ts = string2Time(tranverseString(aLineStrings[3]));
		gd.setUploadDate(ts);

		gd.setLongitude(Double.parseDouble(aLineStrings[4]));
		gd.setHeight(Double.parseDouble(aLineStrings[5]));
		gd.setWidth(Double.parseDouble(aLineStrings[6]));
		gd.setPhotoTitle(aLineStrings[7]);
		gd.setLatitude(Double.parseDouble(aLineStrings[8]));
		gd.setOwnerUrl(aLineStrings[9]);
		gd.setOwnerId(Integer.parseInt(aLineStrings[10]));
		gd.setPhotoFileUrl(aLineStrings[11]);
		gd.setPhotoUrl(aLineStrings[12]);

		return gd;
	}

	/**
	 * the second analysis of the text
	 * 
	 * @param firstString
	 *            读入的哪一行文本
	 * @return
	 */
	public String[] secondAnalysis(String firstString) {

		String[] resultString = new String[13];
//		System.out.println(firstString);
		int left = 0, left2, right = 0;// record the place of "(" and ")"
		int ls = 0, rs = 0;// record the number of "(" and ")" at one time
		int cal = 0;// calculate the order of line
		// 拆分字符串
		for (int i = 0; i < firstString.length(); i++) {
			if (firstString.charAt(i) == '(') {
				left2 = left;
				left = i;
				ls++;
				if (rs > 0) {
					resultString[cal] = firstString.substring(left2 + 1, right);// 分出字符串
					cal++;
					ls = 1;
					rs = 0;
				}
			} else if (firstString.charAt(i) == ')') {
				right = i;
				rs++;
			} else if (i == (firstString.length() - 1)) {
				resultString[cal] = firstString.substring(left + 1, right);
				cal++;
				ls = 0;
				rs = 0;
			}
		}
//		for (int i = 0; i < resultString.length; i++) {
//			System.out.println(resultString[i]);
//		}
		return resultString;
	}

	/**
	 * �Ѹ�ʽ�硰25 August 2009��������ת��Ϊ��ʽΪ"2008-8-10 00:00:00.000"������
	 * 
	 * @param timeString
	 * @return ��ʽΪ"2008-8-10 00:00:00.000"�������ַ�
	 */
	public String tranverseString(String timeString) {
		// 25 August 2009 to "2008-8-10 00:00:00.000"
		String[] ss = null;
		
			ss = timeString.split(" ");
	
		String tmp = null;
		if (ss[1].startsWith("Jan")) {
			tmp = "1";
		} else if (ss[1].startsWith("Feb")) {
			tmp = "2";
		} else if (ss[1].startsWith("Mar")) {
			tmp = "3";
		} else if (ss[1].startsWith("April")) {
			tmp = "4";
		} else if (ss[1].startsWith("May")) {
			tmp = "5";
		} else if (ss[1].startsWith("June")) {
			tmp = "6";
		} else if (ss[1].startsWith("July")) {
			tmp = "7";
		} else if (ss[1].startsWith("Augu")) {
			tmp = "8";
		} else if (ss[1].startsWith("Sept")) {
			tmp = "9";
		} else if (ss[1].startsWith("Octo")) {
			tmp = "10";
		} else if (ss[1].startsWith("Nove")) {
			tmp = "11";
		} else if (ss[1].startsWith("Dece")) {
			tmp = "12";
		}
		String result = ss[2] + "-" + tmp + "-" + ss[0] + " 00:00:00.000";

		return result;
	}

	/**
	 * 把字符串当中非英文、数字、逗号、空格的
	 * 
	 * @param inputString
	 * @return
	 */
	public String dealCharacter(String inputString) {

		return null;
	}

	/**
	 * method ���ַ����͵�����ת��Ϊһ��timestamp��ʱ�����java.sql.Timestamp��
	 * dateString ��Ҫת��Ϊtimestamp���ַ� dataTime timestamp
	 */
	public java.sql.Timestamp string2Time(String dateString)
			throws java.text.ParseException {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS",
				Locale.ENGLISH);// �趨��ʽ
		// dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
		// Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);// util����
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp����,timeDate.getTime()����һ��long��
		return dateTime;
	}

	public void test2() {
		GeotaggedDataDAO gdd = new GeotaggedDataDAO();
		GeotaggedData gd = new GeotaggedData();
		gd.setPhotoId(1);
		gd.setOwnerName("huqiaonan");
		gdd.save(gd);
		HibernateSessionFactory.getSession().flush();
	}

	public void test1() {
		GeotaggedDataDAO gdd = new GeotaggedDataDAO();
		Session session = HibernateSessionFactory.getSession();
		Transaction tx = session.beginTransaction();
		GeotaggedData gd = new GeotaggedData();
		gd.setPhotoId(1);
		gd.setOwnerName("huqiaonan");

		try {
			// session.delete(gd);
			// session.save(gd);
			// gdd.save(gd);
			gdd.delete(gd);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
