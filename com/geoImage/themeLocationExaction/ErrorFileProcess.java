package com.geoImage.themeLocationExaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;

public class ErrorFileProcess {

	/**
	 * @param args
	 */

	/**
	 * 对file中的id进行处理
	 * @param file
	 */
	public void readFile(String file)
	{
		int i=0,j = 0;
		BufferedReader bReader = null;
		String num = null;
		try {
			bReader = new BufferedReader(new FileReader(file));
			while((num = bReader.readLine())!=null)
			{
				Integer tmpInteger = Integer.parseInt(num);
				String sqlString = "select doc_id,text from travelogue where travelogue.doc_id="
				+tmpInteger;
				SQLQuery sqlQuery =  HibernateSessionFactory.getSession().createSQLQuery(sqlString);
				Object[] obj = (Object[]) sqlQuery.uniqueResult();
				try{
					processError((Integer) obj[0],(String)obj[1]);
					}catch(Exception e1)
					{
						System.out.println("error process failed:"+(++i));
						continue;
					}
				System.out.println(++j+"successfule "+(Integer) obj[0]+","+(String)obj[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try {
				bReader.close();
//				ois.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String[] args) {
		ErrorFileProcess efp = new ErrorFileProcess();
		//efp.readFile("e:/hqn/placeMakerXMLFile/error.txt");
		try {
//			efp.processError(3, "Ontario");
			efp.processError(3, "我去中国");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void processError(int index,String content) throws Exception
	{
		String url = "http://wherein.yahooapis.com/v1/document";
		
		String[] secrets = {"RjKvIevV34FCwqPSzLqt4pstbOaRxGGFvYZu91duAZ3UVo6DsRaXbBQUAdY2yTM-",
				"BMgfizjV34E0ojw_qm0c3TcEB7Pls4xgSftAV.dGHKqpz5fCsJSExxykaQWW",
				"PbXUT7HV34Fq2KhMd68qS.CRZY9RWjW_dEQLgINMwG.eNxu2hf84BTkvHNttEg4-",
				""};
		
		//String textContent="I am in China";
		
		BufferedWriter writer = null; //写进xml文件的写对象
		URL obj;
		String inputLine;
//		try {
			obj = new URL(url);
	
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		//add reuqest header
		con.setRequestMethod("POST");
		//这样子可以传递参数给雅虎，获得结果
		//有些content为空会报错，所以加入了“ ”，至少有一个空格来处理content，减少报错数量
		String urlParameters = "documentContent="+" "+content+"&appid="+secrets[0]+
				"&documentType=text/plain&outputType=xml";
 
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		if(index%100==0)
		System.out.println("\n"+index+"Sending 'POST' request to URL : " + url);
	//	System.out.println("Post parameters : " + urlParameters);
	//	System.out.println(index+": Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		
		while((inputLine=in.readLine())!=null)
		{
			System.out.println(inputLine);
		}
		
		File file = new File("e:/hqn/placeMakerXMLFile/placemakerxml"+index+".xml");//./表示当前目录
		
		if(file.createNewFile())//创建了文件
		{
			writer = new BufferedWriter(new FileWriter(file));
			while((inputLine=in.readLine())!=null)
			{
				writer.write(inputLine);
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
			in.close();
			
		}else
		{
			in.close();
			throw new Exception(file.getAbsolutePath()+" is not created!");
		}
		
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
	}


	}

