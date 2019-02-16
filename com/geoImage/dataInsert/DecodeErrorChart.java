package com.geoImage.dataInsert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URLDecoder;

import com.geoImage.dao.GeotaggedData;
import com.geoImage.dao.GeotaggedDataDAO;

public class DecodeErrorChart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DecodeErrorChart dec = new DecodeErrorChart();
//		dec.decode();
		dec.aboutContinue();
	}
	
	/**
	 * test about continue
	 * continue 后面的语句都不执行了，进入下一个循环
	 */
	public void aboutContinue()
	{
		for (int i = -100; i < 100; i++) {
			if(i>0){
				if(i%10==0){
					System.out.println(i);
				}
				if(i>10){
					continue;
				}
			}
			
			if(i<20){
				System.out.println(" it is a number!"+i);
			}
		}
		
	}
	
	public void decode()
	{
		BufferedReader bReader = null;
		GeotaggedDataDAO gdd = new GeotaggedDataDAO();
		GeotaggedData gd = null;
		BufferedWriter bWriter = null;
		String fileName = "E:\\optics\\exp\\optics\\InformationCity.txt";
		String aLine = null;
		try {
			bReader = new BufferedReader(new FileReader(new File(fileName)));
			bWriter = new BufferedWriter(new FileWriter(new File("E:\\optics\\exp\\optics\\logger.txt")));
//			
//			while((aLine = bReader.readLine())!=null){
				System.out.println(URLDecoder.decode("(1474472)(Humor,New York,鈾モ櫏鈾?                </a>,)","UTF-8"));
//				System.out.println(URLDecoder.decode("This*string*has*asterisks","UTF-8"));
//			}

		}catch (Exception e) {
			
		}
	}

}
