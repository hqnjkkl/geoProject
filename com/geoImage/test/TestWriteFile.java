package com.geoImage.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.geoImage.tools.IOTools;

public class TestWriteFile {

	public static void main(String[] args) {
		writeToFile();
	}
	
	public static void writeToFile()
	{
		Map<String, Boolean> boolTestMap = new HashMap<String, Boolean>();
		boolTestMap.put("test", true);
 		BufferedWriter bw = null;
 		String fileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/testBool.txt";
		 try {
			bw = new BufferedWriter(new FileWriter(new File(fileName),true));//加在后面
			 for (Iterator iterator = boolTestMap.keySet().iterator(); iterator.hasNext();) {
				 
				String key = (String) iterator.next();
					bw.write(key+"\t"+boolTestMap.get(key));
					bw.newLine();
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}finally
		{
			IOTools.closeIO(null, bw);
		}
	}
}
