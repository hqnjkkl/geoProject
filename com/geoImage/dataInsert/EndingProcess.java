package com.geoImage.dataInsert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.hibernate.Transaction;

import com.geoImage.dao.GeotaggedData;
import com.geoImage.dao.GeotaggedDataDAO;
import com.geoImage.dao.HibernateSessionFactory;

public class EndingProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EndingProcess ep = new EndingProcess();
//		ep.test1();
		ep.insertLastData();
	}
	
	/**
	 * make the errorAnalyzedText Correct
	 */
	public void insertLastData()
	{
		BufferedReader bReader = null;
		
		GeotaggedDataDAO gdd = new GeotaggedDataDAO();
		GeotaggedData gd = null;
		BufferedWriter bWriter2 = null;
		BufferedWriter bWriter3 = null;
		String logger1 = "E:\\optics\\exp\\optics\\AnalysisErrorLogger.txt";
		String logger2 = "E:\\optics\\exp\\optics\\AnalysisRight.txt";
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
		String aLine = null;
			try {
				bReader = new BufferedReader(new FileReader(new File(logger1)));
				bWriter2 = new BufferedWriter(new FileWriter(new File(logger2))); //解析错误记录
				while ((aLine=bReader.readLine())!=null) {
					String firString = aLine.replaceAll("[(]", "'");
					String secString = firString.replaceAll("[)]", "',");
					secString = secString.replace("18 October 2008", "2008-10-18 00:00:00");
					secString = "insert into geotaggedData VALUES("+secString+");";
					bWriter2.write(secString);
					bWriter2.newLine();
					bWriter2.flush();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bReader.close();
					bWriter2.flush();
					bWriter2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	/**
	 * 得出正确的提取方式
	 */
	public void test1()
	{
		String testString = "(15117242)(04) 251梅500 Visitors,GOOGLE HEART PHOTOS,USA - N.Y.C.," +
				"USA agosto (august) 1995,)(ilnani)(18 October 2008)(-74.037379999999999)(155)" +
				"(240)(USA august 1995 - N.Y.C. Statue of Liberty from Hudson River photo 1)" +
				"(40.693004000000002)(http://www.panoramio.com/user/707263)(707263)" +
				"(http://mw2.google.com/mw-panoramio/photos/small/15117242.jpg)" +
				"(http://www.panoramio.com/photo/15117242)-";
		String firString = testString.replaceAll("[(]", "'");
//		System.out.println(firString);
		String secString = firString.replaceAll("[)]", "',");
//		System.out.println(secString);
	}

}
