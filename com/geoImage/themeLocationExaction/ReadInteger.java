package com.geoImage.themeLocationExaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReadInteger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReadInteger ri = new ReadInteger();
		String fileString = "e:/hqn/placeMakerXMLFile/error.txt";
		File directory = new File(fileString);
		System.out.println(directory.getName());
		System.out.println(directory.getName().substring(0));
		System.out.println("placemakerxml80382.xml".substring(13));
		System.out.println("80382.xml".substring(0,"80382.xml".indexOf(".")));;
		ri.listFile("e:/hqn/placeMakerXMLFile");
//		String s[] = "80382.xml";
		
//		System.out.println(s.length);
		//ri.readInteger(fileString);
	}
	
	/**
	 * 从文件中提取文件的信息，获得数字序列中缺失的文件名字
	 * @param file
	 */
	public void listFile(String file)
	{
		File directory = new File(file);
		File[] files = directory.listFiles();
		int i;
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(i=0;i<80398;i++)
		{
			map.put(i, "");
		}
		for(i=0;i<files.length;i++)
		{
			String fileName = files[i].getName();
			if(i%300==0)
				System.out.println(map.size()+","+i);
			if(fileName.endsWith(".xml"))
			{
				fileName = fileName.substring(0,fileName.indexOf("."));
				Integer numInteger = Integer.parseInt(fileName.substring(13));
				map.remove(numInteger);
			}
		}
		Iterator it = map.keySet().iterator();
		System.out.println(map.size());
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
		return ;
	}
	
	public void readInteger(String file)
	{
		BufferedReader bReader = null;
		Integer num = null;
		ObjectInputStream ois = null;
		FileInputStream frs = null;
		try {
		//	FileReader fr = new FileReader(file);
			frs =new FileInputStream(file);			
			while((num = ois.readInt())!=null)
			{
				System.out.println(num);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try {
//				bReader.close();
				ois.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return ;
	}

}
