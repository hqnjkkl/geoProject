package com.geoImage.locationProcess;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;

import com.geoImage.tools.IOTools;

/**
 * 从ip地址列表中，获取能够被google接受的较好的代理ip，port列表
 * 
 * @author hqn
 * @description
 * @version
 * @update 2014-3-11 下午6:49:30
 */
public class GetIpForGoogle implements Runnable{

	static String ipPassFor163 = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForSelf.txt";
	static String ipNotForGoogle = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipNotForGoogle.txt";
	static String filePathString = "E:/hqn/新项目初识/图片地理位置研究/第七次会议(2014-2-17)/allIP.txt";
	
	static String ipPassForGoogle = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForGoogle.txt";
	
	static List<String[]> ipList = null;
	static List<String[]> ipResultList = null;
	String[] ip = null;
	static int ipIndex = 0;
	
	String termString = null;
	
	int googleSearchError = 0;
	
	public GetIpForGoogle()
	{
		if(ipList==null)
		{
			ipList = IOTools.readAllIp(filePathString);
		}
		
	}
	
	public GetIpForGoogle(String term)
	{
		if(ipList==null)
		{
			ipList = IOTools.readAllIp(filePathString);
		}
		if(ipResultList==null)
		{
			ipResultList = new ArrayList<String[]>();
		}
		this.termString = term;
		ip = getNextIp();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		GetIpForGoogle gifg = new GetIpForGoogle();
//		gifg.test2();
//		test2();
//		String file1 = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForSelf2.txt";
//		String file2 = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForSelf3.txt";
//		duplicateRemoval(file1, file2);
		
//		String file1 = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForSelf2.txt";
		String googleFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForGoogle1.txt";
		String selfIPFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForSelf3.txt";
		String resultFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/mergeResult.txt";
		mergeGoogleAndSelfIp(googleFile,selfIPFile,resultFile);
	}
	/**
	 * 合并google和selefIp测试当中获得通过的ip，重复的打印出来查看结果
	 * @param googleFile 通过google的ip测试文件
	 * @param selfIpFile 通过自我ip测试的文件
	 * @param resultFile 合并后结果存入的文件
	 */
	public static void mergeGoogleAndSelfIp(String googleFile,String selfIpFile,String resultFile)
	{
		Map<String, Integer> map1 = IOTools.readIPPair(googleFile);
		Map<String, Integer> map2 = IOTools.readIPPair(selfIpFile);
		System.out.println("size1:"+map1.size()+";size2:"+map2.size());
		//结果放在map2中
		for (Iterator iterator = map2.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			if(map2.get(type)==null)
			{
				map2.put(type,1);
			}else {
				System.out.println("duplicate:"+type);
			}
		}
		System.out.println("size3:"+map2.size());
		IOTools.writeIpToFile(map2, resultFile);
	}
	
	/**
	 * 去掉一篇文章中相同的数据
	 * @param file1 存有源数据文件
	 * @param file2 去重后的数据文件
	 */
	public static void duplicateRemoval(String file1,String file2)
	{
		Map<String, Integer> map1 = IOTools.readIPPair(file1);
		IOTools.writeIpToFile(map1, file2);
	}
	
	public static void test2()
	{
		
		Map<String, Integer> map1 = IOTools.readIPPair(ipPassForGoogle);
		String fileName1 = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForGoogle1.txt";
		IOTools.writeIpToFile(map1, fileName1);
		
		System.out.println("not for google\n");
		Map<String, Integer> map2 = IOTools.readIPPair(ipNotForGoogle);
		String fileName2 = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipNotForGoogle1.txt";
		IOTools.writeIpToFile(map2, fileName2);
	}
	
	public void test1()
	{
		String termFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/googleGazetter.txt";
		List<String> termsList = IOTools.readTerm(termFile);
		ExecutorService pool = null;
		pool = Executors.newFixedThreadPool(10);
		GetIpForGoogle gifg = new GetIpForGoogle();
		System.out.println("termsListSize:"+termsList.size());
		for (int i = 0; i < termsList.size(); i++) {
			GetIpForGoogle gifg1 = new GetIpForGoogle(termsList.get(i));
			Thread t1 = new Thread(gifg1);
			pool.execute(t1);
			if((i+1)%10==0)
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
				pool = Executors.newFixedThreadPool(10);
			}
		}
	}
	
	public  synchronized String[] getNextIp()
	{
		int size = ipList.size();
		++ipIndex;
		ipIndex = ipIndex%size;
		System.out.println("size:"+size+";ipIndex"+ipIndex);
		return ipList.get(ipIndex);
	}
	
	public synchronized void addNewIp(String[] ip)
	{
		
		ipResultList.add(ip);
	}
	

	public void writeIpList() {
		ExecutorService pool = null;
		pool = Executors.newFixedThreadPool(6);
		if(googleSearch2(this.termString,0))
		{
			IOTools.writeToFile(ip[0]+","+ip[1],ipPassForGoogle);
		}else
		{
			IOTools.writeToFile(ip[0]+","+ip[1], ipNotForGoogle);
		}
	}
	
	@Override
	public void run() {
		writeIpList();
	}

	/**
	 * 
	 * @param newTerm
	 * @param time
	 * @param index 
	 * @return
	 */
	public boolean googleSearch2(String newTerm, int time) {

		if (time == 3) {
			// 表示这次查询经过了3次异常，表示无法查询
			googleSearchError++;
			return false;
		}
		StringBuilder sBuilder = null;
		// comment: "\\"is \,so "\\s" is "\s","\s" is the kind of blank
		// character,like"\n\t..."
		// "\\s,",is "\s" or ","
		String matchString = newTerm;// 把标点变成1个空格，合并连续的空白符
		// ^表示行开头;X(1,),表示X至少1次;$表示行结尾;"\\s"表示空白符
		// matchString = matchString.replaceAll("(^\\s{1,})|(\\s{1,}$)",
		// "");//去掉行开头和结尾的空格
		String googleResult = new String();
		StringBuilder googleTmp = new StringBuilder();
		HttpURLConnection conn = null;
		int liCount = 0;
		try {
			newTerm = URLEncoder.encode(newTerm, "UTF-8"); // google中的q参数需要对参数进行编码，
			// 否则会出现错误
			// System.out.println(newTerm);
			// String urlpath = "http://www.google.com.hk/search?q=" + newTerm
			// + "&hl=en&oe=utf-8&safe=strict";
			String urlpath = "http://www.google.com/search?q=" + newTerm
					+ "&hl=en&oe=utf-8&safe=strict";

			URL url = new URL(urlpath);
			
			Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(ip[0],Integer.parseInt(ip[1])));
			 conn = (HttpURLConnection) url.openConnection(proxy);
			
			//conn = (HttpURLConnection) url.openConnection();

			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
			conn.setRequestMethod("GET");
			conn.setReadTimeout(6 * 10000);
			int responseCode = 200;
			if (conn.getInputStream().available() == 0) {
				responseCode = -1;// 表示没有数据
			} else {
				responseCode = conn.getResponseCode();
			}
			// 设置循环来处理302的错误
			if (responseCode != 200) {
				System.out.println("testGoogleIp-respond is not correct:"
						+ responseCode + ":" + newTerm);
				int reConnect = 0;
				while (responseCode == 302 || responseCode == 301) {
					System.out.println("testGoogleIp-" + newTerm
							+ ",reconnect" + reConnect);
					String repeatUrl = conn.getHeaderField("location");
					url = new URL(repeatUrl);
					 conn = (HttpURLConnection) url.openConnection(proxy);
					conn.setInstanceFollowRedirects(false);
					conn.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
					conn.setRequestMethod("GET");
					conn.setReadTimeout(6 * 10000);
					if (conn.getInputStream().available() == 0) // 看是否可以链接
					{
						reConnect++;
						if (reConnect == 4) {
							// conn.connect();
							return false;
						}
						continue;
					}
					responseCode = conn.getResponseCode();
					reConnect++;
					if (reConnect == 4) {
						// conn.connect();
						return false;
					}
				}
			}

			sBuilder = IOTools.readIO(conn.getInputStream());

			org.jsoup.nodes.Document doc = Jsoup.parse(sBuilder.toString());
			/**
			 * ol id="rso" li class = g <em></em>
			 */
			org.jsoup.nodes.Element rsoElement = doc.getElementById("rso");
			org.jsoup.select.Elements liElements = rsoElement.select("li.g");
			// 根据li表单来抽取google搜索到的单词
			liCount = 0;// liCount计算到第三个li标签时，完成结果
			if (liElements != null) {
				for (int i = 0; i < liElements.size(); i++) {
					liCount++;
					org.jsoup.nodes.Element liElement = liElements.get(i);
					// logForGoogle.debug("googleSearch2-at the began li:" +
					// liElement.text());
					org.jsoup.select.Elements emElements = liElement
							.getElementsByTag("em");
					// System.out.println("emSize:" + emElements.size());

					if (emElements != null) {
						googleTmp = new StringBuilder();
						for (int j = 0; j < emElements.size(); j++) {
							org.jsoup.nodes.Element emElement = emElements
									.get(j);
							String tmpString = emElement.text();

							// logForGoogle
							// .debug("get one tmpString:" + tmpString);
							if (tmpString.replaceAll("[\\s,.]+", " ")
									.equalsIgnoreCase(matchString)) {// goole其中的一个em元素匹配成功
								// logForGoogle
								// .debug("googleSearch2-successful match:"
								// + matchString);
								// logForGoogle
								// .debug("googleSearch2-sucessful googleResult:"
								// + tmpString);
								return true;
							}
							googleTmp.append(" " + tmpString);
							// System.out.println(emElement.text());
						}

						googleResult = googleTmp.toString().replaceAll(
								"[\\s,.]+", " ");
						googleResult = googleResult.replaceAll(
								"(^[\\s]{1,})|([\\s]{1,}$)", "");
						// 去掉开头结尾空格
						if (googleResult.equalsIgnoreCase(matchString)
								|| googleResult.contains(matchString)) {
							// logForGoogle
							// .debug("googleSearch2-successful match:"
							// + matchString);
							// logForGoogle
							// .debug("googleSearch2-sucessful googleResult:"
							// + googleResult);
							// wordPairForGoogleMap.put(matchString, true);
							return true;
						}
					}
					// logForGoogle.debug("the googleResult:"+googleResult);
					// logForGoogle.debug("the matcher:"+matchString);
					if (liCount == 3) {
						return true;
//						break;s
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(termString+":"+ip[0]+":"+ip[1]+",time"+time);
			// logForGoogle.error(e.toString() + ";" + newTerm);
			conn.disconnect();
			try {

				Thread.currentThread().sleep(500 * (time + 1));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return googleSearch2(newTerm, time + 1);
		}
		// logForGoogle.debug("googleSearch2-no match the googleResult:"
		// + googleResult);
		// logForGoogle.debug("googleSearch2-the matcher:" + matchString);
		// liElements当中没有元素 or 前三个list当中没有完全匹配的
		// 匹配的规则是按照高亮的词，把其em用空格拼接起来，去掉标点
		// wordPairForGoogleMap.put(matchString, false);
		System.out.println("can you execute this?????????????????????????????????????????");
		return true;
	}

	
}
