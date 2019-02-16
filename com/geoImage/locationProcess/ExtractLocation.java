package com.geoImage.locationProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.SQLQuery;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.test.LocationTest;
import com.geoImage.tools.IOTools;

/**
 * 
 * @author hqn
 * @description extract the location information from the xml file from Yahoo
 *              PlaceMaker and the location information from html file of wwj.
 *              also delete the disagree element of PlaceMaker and wwj results;
 * @version
 * @update 2014-2-21 上午9:34:58
 */
public class ExtractLocation {
	DocumentBuilderFactory builderFactory = DocumentBuilderFactory
			.newInstance();

//	Map map = new HashedMap();
	// Load and parse XML file into DOM
	public Document parse(String filePath) {
		Document document = null;
		try {
			// DOM parser instance
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			// parse an XML file into a DOM tree
			document = builder.parse(new File(filePath));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static void main(String[] args) {
		ExtractLocation el = new ExtractLocation();
//		el.parsePMXML("E:/hqn/placeMakerXMLFile/placemakerxml1.xml");
		// try {
		// Parser htmlParser =
		// el.getParser("E:/hqn/新项目初识/图片地理位置研究/地理位置数据/showhtml/1.html",
		// "utf-8");
		// el.parsewwjHTML(htmlParser);
		// } catch (ParserException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
//		try {
			
		
//		for(int i=10;i<40000;i+=20)
//		{			
//			el.parsewwjHTML2("E:/hqn/新项目初识/图片地理位置研究/地理位置数据/showhtml/"+i+".html");
//		}
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	Iterator iterator = el.map.keySet().iterator();
//	System.out.println(el.map.keySet().size());
//	while(iterator.hasNext())
//	{
//		System.out.println(iterator.next());
//	}
//		el.parsewwjHTML2("E:/hqn/新项目初识/图片地理位置研究/地理位置数据/showhtml/"+1810+".html");
		el.deleteElement();
		return;
	}
	
	

	/**
	 * extract the place information from the xml file of Yahoo PlaceMaker
	 * 
	 * @param fileName
	 *            the name of xml file for example:
	 *            parsePMXML("E:/hqn/placeMakerXMLFile/placemakerxml1.xml");
	 */
	public List<LocationElement> parsePMXML(String fileName) {
		List<LocationElement> locationList = new ArrayList<LocationElement>();
		int docId = Integer.parseInt(fileName.substring(
				fileName.indexOf("xml") + 3, fileName.indexOf(".xml")));
		Document document = parse(fileName);
		// get root element
		Element rootElement = document.getDocumentElement();
		NodeList placeReferences = rootElement
				.getElementsByTagName("reference");// 参考的个数

		for (int i = 0; i < placeReferences.getLength(); i++) {
			Element prElement = (Element) placeReferences.item(i);
			// NodeList prNodeList = prElement.getChildNodes();

			NodeList startNodeList = prElement.getElementsByTagName("start");
			NodeList endNodeList = prElement.getElementsByTagName("end");
			NodeList textNodeList = prElement.getElementsByTagName("text");

			LocationElement locationElement = new LocationElement(textNodeList
					.item(0).getTextContent(), Integer.parseInt(startNodeList
					.item(0).getTextContent()), Integer.parseInt(endNodeList
					.item(0).getTextContent()), docId);
			locationList.add(locationElement);
//			locationElement.showLocation();
			// locationElement.showLocation();
			// break;
		}
		Collections.sort(locationList);
		return locationList;
	}

	/**
	 * 
	 * @param url
	 *            可以是网络上的url,也可以是本地的文件
	 * @param encoding
	 * @return
	 * @throws ParserException
	 * @throws IOException
	 */
	public Parser getParser(String url, String encoding)
			throws ParserException, IOException {

		Parser parser = new Parser(url);
		parser.setEncoding(encoding);
		return parser;
	}
	
	/**
	 * 可以返回单个wwj数据的类
	 * @param fileName
	 * @return
	 */
	public List<LocationElement> parsewwjHTML3(String fileName)
	{
		
		File file = new File(fileName);
		List<LocationElement> locationList = new ArrayList<LocationElement>();
		int docId = Integer.parseInt(fileName.substring(
				fileName.indexOf("html/") + 5, fileName.indexOf(".html")));
		int a, b, c,d,count;//num to count wwj's htmlfile
		LocationElement le = null;
		StringBuilder text = readFile(file);
//		StringBuilder aSpan = null;
		text = new StringBuilder(text.substring(text
				.indexOf("text: <br/><div>") + 16));
		count = 0;
		while ((a = text.indexOf("<span ")) != -1) {
			// a = text.indexOf("<span ");
			b = text.indexOf("'>");
			c = text.indexOf("</span>");
			d = text.indexOf("<br/>");
			//System.out.println("d:"+d+";text:"+text.length());
			//去除<br/>的影响
			if(d!=-1 && d<a)
			{
				text = new StringBuilder(text.replace(d,d+"<br/>".length(),""));
				count ++;
				continue;
			}
			String name = null;
			try {				
				name = text.substring(b+2,c);
			} catch (Exception e) {
				e.printStackTrace();
//				map.put(docId, "1");
				System.out.println("hqn-------------------------------------------------------");
				System.out.println("text:"+text.length()+";docId:"+docId+";a:"+a+"b:"+b+"c:"+c+"d:"+d);
				continue;
				//System.out.println(text.substring(0,b+1));
			}
//			System.out.println(name);
			int nameLenth;
//			LocationTest lt = new LocationTest(docId);
			if(name==null)
			{
				nameLenth = 0;
			}else
			{
				nameLenth = name.length();
			}
			le = new LocationElement(name,a+count,a+nameLenth+count,docId);
			le = trimStop(le);
			text = new StringBuilder(text.replace(a, c+"</span>".length(),""));
//			System.out.println(text);
			count += c-b-2;
//			System.out.println(text);
//			le.showLocation();
//			String subString = null;
//			System.out.println(count+"****************");
			
			locationList.add(le);
//			icount++;
		}
		return locationList;
	}

	/**
	 * 去掉一个le元素末尾的标点符号，或者逗号
	 * @param le
	 * @return
	 */
	public LocationElement trimStop(LocationElement le) {
		int start = le.getStart();
		int end = le.getEnd();
		String orgString = le.getLocationOriginalText();
		int orgLength = orgString.length();
		while(orgString.substring(orgLength-1, orgLength) .matches("[\"#$%&'()*+-./:;<=>?@]"))
		{
			le = new LocationElement(orgString.substring(0, orgLength-1), start, end - 1,le.getDocId());
			orgString = le.getLocationOriginalText();
			orgLength = orgString.length();
			start = le.getStart();
			end = le.getEnd();
		}

		return le;
	}
	
	/**
	 * 把travelogue_notmp中的数据，其中HTML和xml不一致的去除掉
	 * 
	 */
	public void deleteElement()
	{
		//删除travelogue_tmp中的语句
//		String sqlString2 = "select doc_id from travelogue_tmp";
//		String sqlSting3 = "delete from travelogue_tmp where travelogue_tmp.doc_id=";
		//删除travelogue_notmp中的语句
		String sqlString2 = "select doc_id from travelogue_notmp";
		String sqlSting3 = "delete from travelogue_notmp where travelogue_notmp.doc_id=";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString2);
		BigInteger doc_Id = null;
//		sqlQuery.uniqueResult();
//		Object uniqueResult = sqlQuery.uniqueResult();
		List<Integer> list = sqlQuery.list();
		System.out.println(list.size());
		int res=0;
		int i=0;
		String fileName = null;
		// System.out.println(uniqueResult);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Integer bigInteger = (Integer) iterator.next();
			i++;
			res = bigInteger.intValue();
			fileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/showhtml/"+res+".html";
//			System.out.println(fileName);
			if(parsewwjHTML2(fileName)==-1)
			{
				sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sqlSting3+res);
				System.out.println(res+":"+i);
				sqlQuery.executeUpdate();
			}
			
		}
		return;
	}
	
	/**
	 * 把travelogue_tmp中的数据，其中HTML和xml不一致的去除掉
	 */
	public void deleteElement2()
	{
		String sqlString2 = "select doc_id from travelogue_Washington ";
		String sqlSting3 = "delete from travelogue_Washington where travelogue_Washington.doc_id=";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sqlString2);
		BigInteger doc_Id = null;
//		sqlQuery.uniqueResult();
//		Object uniqueResult = sqlQuery.uniqueResult();
		List<Integer> list = sqlQuery.list();
		System.out.println(list.size());
		int res=0;
		int i=0;
		String fileName = null;
		// System.out.println(uniqueResult);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Integer bigInteger = (Integer) iterator.next();
			i++;
			res = bigInteger.intValue();
			fileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/showhtml/"+res+".html";
//			System.out.println(fileName);
			if(parsewwjHTML2(fileName)==-1)
			{
				sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sqlSting3+res);
				System.out.println(res+":"+i);
				sqlQuery.executeUpdate();
			}
			
		}
//		if (uniqueResult instanceof Object[]) {
//			BigInteger result[] = (BigInteger[])uniqueResult;
//			for(int i=0;i<result.length;i++)
//			{
//				res = result[i].intValue();
//				System.out.println(fileName);
//				parsewwjHTML2(fileName);
//				System.out.println(res);
				//sqlQuery = HibernateSessionFactory.getSession().createSQLQuery(sqlSting3+res);
//			}
//		} else if (uniqueResult instanceof BigInteger) {
//			// System.out.println("String");
//			doc_Id = (BigInteger) uniqueResult;
//		}
		// String docText = (String)((Object [])sqlQuery.uniqueResult())[0];
		return;
	}

	/**
	 * 测试了这个解析算法在wwj的html当中，1.html可以和数据库中的travelogue数据同步
	 * 测试了
	 * 0代表wwjHtml的内容和数据库中的travelogue当中一致，-1代表不一致
	 * @param fileName
	 */
	public int parsewwjHTML2(String fileName) {
		File file = new File(fileName);
		List<LocationElement> locationList = new ArrayList<LocationElement>();
		int docId = Integer.parseInt(fileName.substring(
				fileName.indexOf("html/") + 5, fileName.indexOf(".html")));

		int a, b, c,d,count;
		LocationElement le = null;
		StringBuilder text = readFile(file);
//		StringBuilder aSpan = null;
		text = new StringBuilder(text.substring(text
				.indexOf("text: <br/><div>") + 16));
//		System.out.println(text);
		// 从0开始计数
		count = 0;
//		int icount = 0;
		//复杂的wwj的程序统计终于做出来了，并且还和数据库中的Text对上了
		LocationTest lt = new LocationTest(docId);
		while ((a = text.indexOf("<span ")) != -1) {
			// a = text.indexOf("<span ");
			b = text.indexOf("'>");
			c = text.indexOf("</span>");
			d = text.indexOf("<br/>");
			//System.out.println("d:"+d+";text:"+text.length());
			//去除<br/>的影响
			if(d!=-1 && d<a)
			{
				text = new StringBuilder(text.replace(d,d+"<br/>".length(),""));
				count ++;
				continue;
			}
//			System.out.println(text);
		
			String name = null;
			try {				
				name = text.substring(b+2,c);
			} catch (Exception e) {
				e.printStackTrace();
//				map.put(docId, "1");
				System.out.println("html Error: hqn-------------------------------------------------------");
//				System.out.println("a:"+a+"b:"+b+"c:"+c+"d:"+d+";text:"+text.length());
//				System.out.println(text.substring(0,b+1));
				return -1;
			}
//			System.out.println(name);
			int nameLenth;
//			LocationTest lt = new LocationTest(docId);
			if(name==null)
			{
				nameLenth = 0;
			}else
			{
				nameLenth = name.length();
			}
			le = new LocationElement(name,a+count,a+nameLenth+count,docId);
			
//			text = new StringBuilder(name).append(text.replace(a, c+"</span>".length(), ""));
			//下面不用减
			text = new StringBuilder(text.replace(a, c+"</span>".length(),""));
//			System.out.println(text);
			count += c-b-2;
//			System.out.println(text);
//			le.showLocation();
			String subString = null;
			try{
				subString = lt.locationIndexTest(le.getStart(), le.getEnd());	
				
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("continue;");
				return -1;
			}
			if(subString!=null&&!subString.equals(name))
			{
				System.out.println("hqn-------------------------------------------------------");
				System.out.println("a:"+a+"b:"+b+"c:"+c+"d:"+d+";text:"+text.length());
//				map.put(docId, "1");
				return -1;
			}
//			System.out.println(count+"****************");
			locationList.add(le);
//			icount++;
		}
		// le.set
		// System.out.println(a);
		return 0;
	}

	public StringBuilder readFile(File file) {
		BufferedReader bReader = null;
		String text = null;

		StringBuilder sb = new StringBuilder();

		try {
			bReader = new BufferedReader(new FileReader(file));
			while ((text = bReader.readLine()) != null) {
				sb.append(text);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(bReader, null);
		}
		return sb;
	}


	public void parsewwjHTML(Parser parser) throws ParserException { // 获得网页<body></body>标签中的内容，
																		// 保存在body中
		TagNameFilter spanTagFilter = new TagNameFilter("span");

		// NodeFilter bodyFilter=new NodeClassFilter();
		org.htmlparser.util.NodeList nodeList = parser
				.extractAllNodesThatMatch(spanTagFilter);
		Tag tag = null;
		LocationElement le = null;
		System.out.println(nodeList.size());
		for (int i = 0; i < nodeList.size(); i++) {

			tag = (Tag) nodeList.elementAt(i);
			le = new LocationElement();
			le.setLocationOriginalText(tag.toPlainTextString());

//			String titleString = tag.getAttribute("title");

			System.out.println();
			System.out.println(tag.toPlainTextString());
		}

	}

}
