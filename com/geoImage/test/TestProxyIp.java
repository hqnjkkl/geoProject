package com.geoImage.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.geoImage.tools.IOTools;

public class TestProxyIp implements Runnable{

	
	String ip[] = {"183.157.160.28","80"};
	public TestProxyIp(){}
	
	public TestProxyIp(String[] ip)
	{
			this.ip = ip;
	}
	public static void main(String args[]) {
		TestProxyIp tpi = new TestProxyIp();
//		tpi.getIp();
//		tpi.testYourIp();
//		System.out.println(tpi.getHtml("http://iframe.ip138.com/ic.asp",new String[]{"27.24.158.152","80"}));
//		tpi.getIp2();
		ExecutorService pool = null;
		String filePathString = "E:/hqn/新项目初识/图片地理位置研究/第七次会议(2014-2-17)/allIP.txt";
		
		pool = Executors.newFixedThreadPool(20);
		
		BufferedReader bReader = null;
		String aline = null;
		StringBuilder result= new StringBuilder();
		List<String[]> ipResultTmp = new ArrayList<String[]>();
		//		int count = 0;
		try {
			bReader = new BufferedReader(new FileReader(new File(filePathString)),5*1024*1024);
			while((aline = bReader.readLine())!=null)
			{
				ipResultTmp.add(aline.split(","));
//				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
//			System.out.println(count);
			IOTools.closeIO(bReader, null);
		}
		
		System.out.println("allIPSize______________________________:"+ipResultTmp.size());
		for (int i = 0; i < ipResultTmp.size(); i++) {
		TestProxyIp tpi1 = new TestProxyIp(ipResultTmp.get(i));
			Thread t1 = new Thread(tpi1);
			pool.execute(t1);
			if((i+1)%20==0)
			{
				pool.shutdown();
				while(true)
				{
					if(pool.isTerminated())
					{
						break;
					}
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("i:"+i);
				pool = Executors.newFixedThreadPool(20);
			}
		}
		pool.shutdown();
	}
	

	@Override
	public void run() {
		getIp3();
	}
	
	//解析文章中的ip
	public void getIp() {
		String filePathString = "E:/hqn/新项目初识/图片地理位置研究/第七次会议(2014-2-17)/allIP.txt";
		String proxyFile = "E:/hqn/新项目初识/图片地理位置研究/第七次会议(2014-2-17)/ip代理.txt";
		Map<String, String> ipMap = IOTools.readIPProxyFile(proxyFile);
		Map<String, String> ipMap2 = new HashMap<String, String>();
		for (Iterator iterator = ipMap.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			String[] ips = type.split(":");
			if(ips.length==1)
			{
				System.out.println(ips[0]+"------"+null);
				ipMap2.put(ips[0], null);
			}else {
				
				System.out.println(ips[0]+"------"+ips[1]);
				ipMap2.put(ips[0]+","+ips[1],"");
			}
		}
		IOTools.writeIPMapToFile(ipMap2, filePathString);
	}
	
	public boolean getIp3()
	{
		String address = "http://iframe.ip138.com/ic.asp";
		String ipPass = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForSelf2.txt";
		if(getHtml(address,ip))
		{
			IOTools.writeToFile(ip[0]+","+ip[1], ipPass);
			return true;
		}else
		{
			System.out.println(ip[0]+","+ip[1]);
			return false;
		}
	}
	
	/**
	 * 
	 */
	public void getIp2()
	{
		String filePathString = "E:/hqn/新项目初识/图片地理位置研究/第七次会议(2014-2-17)/allIP.txt";
		String address = "http://iframe.ip138.com/ic.asp";
		String ipPass = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForSelf.txt";
		BufferedReader bReader = null;
		String aline = null;
		StringBuilder result= new StringBuilder();
		List<String[]> ipResultTmp = new ArrayList<String[]>();
		List<String[]> ipResult = new ArrayList<String[]>();
		//		int count = 0;
		try {
			bReader = new BufferedReader(new FileReader(new File(filePathString)),5*1024*1024);
			while((aline = bReader.readLine())!=null)
			{
				ipResultTmp.add(aline.split(","));
//				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
//			System.out.println(count);
			IOTools.closeIO(bReader, null);
		}
		for (int i = 0; i < ipResultTmp.size(); i++) {
			String[] array_element = ipResultTmp.get(i);
			//通过了写入文件
			if(getHtml(address, array_element))
			{
				System.out.println(true);
				ipResult.add(array_element);
				IOTools.writeToFile(array_element[0]+","+array_element[1], ipPass);
			}else
			{
				System.out.println(false);
			}
		}
		
		System.out.println(ipResult.size());
		for (Iterator iterator = ipResult.iterator(); iterator.hasNext();) {
			String[] strings = (String[]) iterator.next();
			System.out.println(strings[0]+","+strings[1]);
		}
	}
	
	

	public void testYourIp() {
		System.getProperties().setProperty("proxySet", "true"); // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
		// 94.20.20.135:3128
		// System.getProperties().setProperty("http.proxyHost", "94.20.20.135");
		// System.getProperties().setProperty("http.proxyPort", "3128");
		// 111.1.32.51:8088
		// 221.130.23.29:80
		// 27.24.158.152,80
		// 222.180.84.61,9999

//		System.getProperties().setProperty("http.proxyHost", "27.24.158.152");
//		System.getProperties().setProperty("http.proxyPort", "80");
		 System.out.println(getHtml("http://iframe.ip138.com/ic.asp",null));
		 
		 //[183.157.160.31
		 //183.157.160.26
		 //183.157.160.8
		 
//		 System.out.println(getHtml("http://www.ip38.com/"));
		// //判断代理是否设置成功
//		System.out.println(getHtml("http://www.whatismyip.com.tw/"));
		//
		// String filePathString =
		// "E:/hqn/新项目初识/图片地理位置研究/第七次会议(2014-2-17)/代理ip列表.csv";
		// try {
		// testIp(new FileInputStream(new File(filePathString)));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
	}

	public static StringBuilder testIp(InputStream is) {
		Map<String, String> hashMap = new HashMap<String, String>();

		BufferedReader bReader = null;
		String aline = null;
		StringBuilder result = new StringBuilder();
		bReader = new BufferedReader(new InputStreamReader(is), 5 * 1024 * 1024);
		// int count = 0;
		String lines[] = null;
		try {
			while ((aline = bReader.readLine()) != null) {
				if (aline.length() > 5) {
					lines = aline.split(",");
					hashMap.put(lines[0], lines[1]);
				}
				// result.append(aline);
				// count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// System.out.println(count);
			IOTools.closeIO(bReader, null);
		}
		for (Iterator iterator = hashMap.keySet().iterator(); iterator
				.hasNext();) {
			String type = (String) iterator.next();
			System.out.println(type + "," + hashMap.get(type));
		}
		return result;
	}

	private boolean getHtml(String address,String ip[]) {
		StringBuffer html = new StringBuffer();
		String result = null;
		try {

			URL url = new URL(address);
			Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(ip[0],Integer.parseInt(ip[1])));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

			
//			HttpURLConnection conn =(HttpURLConnection) url.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
//			conn.setRequestProperty("proxySet", "true");
			 // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
			// 94.20.20.135:3128
//			System.getProperties().setProperty("http.proxyHost", ip[0]);
//			System.getProperties().setProperty("http.proxyPort", ip[1]);
//			conn.setRequestProperty("http.proxyHost", ip[0]);
//			conn.setRequestProperty("http.proxyPort", ip[1]);
//			conn.setRequestProperty(key, value)
			conn.setReadTimeout(6 * 10000);
			conn.getResponseCode();
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String inputLine;
				while((inputLine=in.readLine())!=null)
				{
					html.append(inputLine);
				}
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("html:"+ip[0]+":"+ip[1]);
			}
			finally {
				in.close();
				conn = null;
				url = null;
			}
			result = html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("html:"+ip[0]+":"+ip[1]);
			return false;
		}
		try {
			System.out.println(result.substring(result.indexOf("<center>")));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("html:"+ip[0]+":"+ip[1]);
			return false;
		}
		
		if(result.contains(ip[0]))
//		183.157.160.28
//		if(result.contains("183.157.160.28"))
		{
			return true;
		}else
		{
			return false;
		}
	}
}