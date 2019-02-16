package com.geoImage.locationProcess;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.SQLQuery;
import org.jsoup.Jsoup;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.tools.IOTools;
import com.geoImage.tools.MyConfigure;

/**
 * 
 * @author hqn
 * @description merge the place extraction result of wwj and placeMaker.In this
 *              We also add our own algorithm to recognize location word. We
 *              want to increase the precision and recall to at least 80
 *              percents;
 * @version
 * @update 2014-2-25 上午9:14:21
 */

public class MergeAlgorithm implements Runnable {

	List<LocationElement> wwjElements = null; // wwj的一个文本的Location
	List<LocationElement> pmElements = null;// PlaceMaker一个文本的Locaiton
	List<LocationElement> finalList = null; // 最后合并的这个文本的Location
	String textString = null; // 这个文本实体
	int DOCID;
	public Logger logForGoogle = Logger.getLogger(MergeAlgorithm.class
			.getClass());
	// 初始化localTermPostfixMap
	static Map<String, Integer> localTermPostfixMap = null;

	// 初始化integrity的Map
	static Map<String, Integer> integrityPostfixMap = null;
	static Map<String, Integer> integrityPrefixMap = null;
	static Map<String, Integer> integrityTrimMap = null;
	static Map<String, Integer> pResultPrefixMap = null;
	static Map<String, Integer> stateMap = null;

	static boolean isStringInitial; // initial value is false
//	static String googleWordFileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/googleGazetter.txt";
//	static String wikipediaWordFileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/wikipediaGazetter.txt";
//	
	static WebSearchIO webSearchIO = null;
	
	// 存储有ip代理的链接池子
	static List<String[]> ipPools = null;
	static int ipIndex = 0;
//	
//	Map<String, Boolean> wordPairForGoogleMap = null;// 其中存放google的单词对
//	Map<String, Boolean> wordPairForWikiMap = null;// 其中存放wikipedia的单词对
	int googleSearchError = 0;
	int wikipediaSearchError = 0;
	
	static String errorLogFileName = null;
	static String xmlPathFileName = null;
	static String htmlPathFileName = null;
	
	public MergeAlgorithm() {
		// inalize for log4j
		BasicConfigurator.configure();
		PropertyConfigurator.configure("src/log4j.properties");
		finalList = new ArrayList<LocationElement>();
		if (!isStringInitial) {
			initial();
		}
//		initialWordPairMap();
	}

	public MergeAlgorithm(int processDocId) {
		this.DOCID = processDocId;
		BasicConfigurator.configure();
		PropertyConfigurator.configure("src/log4j.properties");
		finalList = new ArrayList<LocationElement>();
		if (!isStringInitial) {
			initial();
		}
//		initialWordPairMap();
	}

	public void initial() {
		webSearchIO = new WebSearchIO();
		initialPResultPrefix();
		initialLocalTerm();
		initialIntegerityMap();
		initialUSStateOrCountry();
//		initialIpPool();
		initialFilePathProperties();
		isStringInitial = true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		MergeAlgorithm ma = new MergeAlgorithm();
		String sqlString = "insert into my_location_element values("+1+","+0+",null,null,null,null,"+
				0+","+0+",null,null)";
		System.out.println(sqlString);
	}

//	public void initialWordPairMap() {
//		this.wordPairForGoogleMap = IOTools.getMapFromFile(googleWordFileName);
//		this.wordPairForWikiMap = IOTools.getMapFromFile(wikipediaWordFileName);
//	}
	
	public void initialFilePathProperties()
	{
		 MyConfigure rc = new MyConfigure("src/myConfigure.properties");//相对路径
		 errorLogFileName = rc.getValue("webSearchErrorLog");//以下读取properties文件的值
	     xmlPathFileName = rc.getValue("xmlFilePath");
	     htmlPathFileName = rc.getValue("htmlFilePath");
	     /*
	        String ip = rc.getValue("xmlFilePath");
	        String host = rc.getValue("webSearchErrorLog");
	        String tab = rc.getValue("tab");
		*/
	}

	public void initialUSStateOrCountry() {
		String[] stateOrCountryStrings = { "alabama", "alaska", "arizona",
				"arkansas", "california", "colorado", "connecticut",
				"delaware", "florida", "georgia", "hawaii", "idaho",
				"illinois", "indiana", "iowa", "kansas", "kentucky",
				"louisiana", "maine", "maryland", "massachusetts", "michigan",
				"minnesota", "mississippi", "missouri", "montana", "nebraska",
				"nevada", "new hampshire", "new jersey", "new mexico",
				"new york", "north carolina", "north dakota", "ohio",
				"oklahoma", "oregon", "pennsylvania", "rhode island",
				"south carolina", "south dakota", "tennessee", "texas", "utah",
				"vermont", "virginia", "washington", "west virginia",
				"wisconsin", "wyoming", /* next is country */"us", "american",
				"usa", };
		stateMap = new HashMap<String, Integer>();
		for (int i = 0; i < stateOrCountryStrings.length; i++) {
			String state = stateOrCountryStrings[i];
			stateMap.put(state, 0);
		}
	}

	public void initialIntegerityMap() {
		// "[\\p{Punct}]{1,}"//各种标点符号,至少1次
		String[] prefixs = { "a", "t", "d", "s", "m", "ve", "ll", "re",
				"about", "above", "after", "against", "any", "are", "around",
				"as", "at", "be", "because", "been", "before", "being",
				"below", "between", "both", "but", "by", "cannot", "could",
				"did", "do", "does", "doing", "down", "during", "each", "few",
				"for", "from", "further", "had", "has", "have", "having", "he",
				"her", "here", "hers", "herself", "him", "himself", "his",
				"how", "i", "if", "in", "into", "is", "it", "its", "itself",
				"me", "my", "myself", "no", "nor", "not", "off", "on", "onto",
				"once", "only", "other", "ought", "our", "ours", "ourselves",
				"out", "over", "own", "same", "should", "so", "some", "such",
				"than", "that", "their", "theirs", "them", "themselves",
				"then", "there", "these", "they", "this", "those", "through",
				"to", "too", "under", "until", "up", "very", "was", "we",
				"were", "what", "when", "where", "which", "while", "who",
				"whom", "why", "with", "without", "would", "you", "your",
				"yours", "yourself", "yourselves", "the", "like", "leave",
				"left", "of", "called", "named", "and", "call", "toward",
				"towards" };
		String[] postfixs = { "are", "as", "be", "been", "being", "but", "by",
				"can", "cannot", "could", "did", "do", "does", "doing", "down",
				"during", "each", "few", "had", "has", "have", "having", "he",
				"her", "here", "hers", "herself", "him", "himself", "his",
				"how", "i", "if", "into", "is", "it", "its", "itself", "me",
				"my", "myself", "no", "nor", "not", "off", "on", "onto",
				"once", "only", "other", "ought", "our", "ours", "ourselves",
				"out", "over", "own", "same", "should", "so", "some", "such",
				"than", "that", "their", "theirs", "them", "themselves",
				"then", "there", "these", "they", "this", "those", "through",
				"to", "too", "under", "until", "up", "very", "was", "we",
				"were", "what", "when", "where", "which", "while", "who",
				"why", "with", "without", "would", "your", "your", "yourself",
				"yourselves", "couldn", "didn", "doesn", "don", "hadn", "hasn",
				"haven", "isn", "mustn", "shan", "shouldn", "that", "they",
				"there", "wasn", "we", "where", "why", "weren", "what", "when",
				"who", "whom", "wouldn", "called", "call", "tomorrow", "take",
				"takes", "took", "go", "went", "gone", "give", "gave", "in" };
		String[] trimWords = { "a", "about", "above", "after", "against", "an",
				"and", "any", "are", "as", "at", "be", "because", "been",
				"before", "being", "below", "between", "both", "but", "by",
				"com", "came", "cannot", "for", "from", "further", "i", "in",
				"more", "most", "of", "off", "let", "or", "out", "over", "she",
				"take", "takes", "took", "the", "why", "with", "you", "go",
				"went", "gone", "give", "gave" };
		integrityPostfixMap = new HashMap<String, Integer>();
		integrityPrefixMap = new HashMap<String, Integer>();
		integrityTrimMap = new HashMap<String, Integer>();
		for (int j = 0; j < postfixs.length; j++) {
			String postfix = postfixs[j];
			integrityPostfixMap.put(postfix, 0);
		}
		// 初始化prefixMap
		for (int j = 0; j < prefixs.length; j++) {
			String prefix = prefixs[j];
			integrityPrefixMap.put(prefix, 0);
		}
		for (int i = 0; i < trimWords.length; i++) {
			String trimWord = trimWords[i];
			integrityTrimMap.put(trimWord, 0);
		}
		return;
	}

	public void initialLocalTerm() {
		String[] localTerm = { "campground", "club", "hill", "cliff",
				"mountain", "river", "spring", "pool", "pond", "lake", "sea",
				"ocean", "basin", "bay", "beach", "coast", "dam", "island",
				"coastline", "valley", "ridge", "peak", "forest", "canyon",
				"dale", "desert", "sand", "oasis", "flat", "cave", "cove",
				"creek", "harbor", "glacier", "veld", "veldt", "road",
				"avenue", "street", "highway", "country", "town", "village",
				"zone", "park", "ground", "land", "field", "court", "yard",
				"courtyard", "deck", "campus", "camp", "site", "lawn", "area",
				"garden", "orchard", "castle", "roof", "room", "tower",
				"bridge", "museum", "monument", "gallery", "art", "gallery",
				"library", "theatre", "cinema", "plaza", "palace", "building",
				"hall", "center", "company", "house", "church", "university",
				"school", /* this is the separate point */
				"campgrounds", "clubs", "hills", "cliffs", "mountains",
				"rivers", "springs", "pools", "ponds", "lakes", "seas",
				"oceans", "basins", "bays", "beaches", "coasts", "dams",
				"islands", "coastlines", "valleys", "ridges", "peaks",
				"forests", "canyons", "dales", "deserts", "sands", "oases",
				"flats", "caves", "coves", "creeks", "harbors", "glaciers",
				"veld", "veldt", "roads", "avenues", "streets", "highways",
				"countries", "towns", "villages", "zones", "parks", "grounds",
				"lands", "fields", "courts", "yards", "courtyards", "decks",
				"campuses", "camps", "sites", "lawns", "areas", "gardens",
				"orchards", "castles", "roofs", "rooms", "towers", "bridges",
				"museums", "monuments", "galleries", "arts", "galleries",
				"libraries", "theatres", "cinemas", "plazas", "palaces",
				"buildings", "halls", "centers", "companies", "houses",
				"churches", "universities", "schools" };// min:3;max:12
		localTermPostfixMap = new HashMap<String, Integer>();
		for (int i = 0; i < localTerm.length; i++) {
			String word = localTerm[i];
			localTermPostfixMap.put(word, 0);
		}
	}

	public void initialPResultPrefix() {
		String pResultPrefix[] = { "in", "at", "go to", "went to", "onto",
				"into", "back to", "town of", "views of", "drive to", "to" };
		pResultPrefixMap = new HashMap<String, Integer>();
		for (int i = 0; i < pResultPrefix.length; i++) {
			String prefix = pResultPrefix[i];
			pResultPrefixMap.put(prefix, 0);
		}
		return;
	}

	public void initialIpPool() {
		String ipFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/ipPassForGoogle1.txt";
		Map<String, Integer> ips = IOTools.readIPPair(ipFile);
		ipPools = new ArrayList<String[]>();
		for (Iterator iterator = ips.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			ipPools.add(type.split(","));
		}
	}
	
	public synchronized String[] getNextIp()
	{
		ipIndex++;
		ipIndex = ipIndex/ipPools.size();
		return ipPools.get(ipIndex);
	}

	/**
	 * 对简单的几个例子进行融合算法处理，查看结果 对wwjElements，textString赋初始值，
	 * 
	 * @param docId
	 */
	public boolean getDataStructure(int docId) {
		this.DOCID = docId;
		String fileName = htmlPathFileName + docId
				+ ".html";
		String xmlFile = xmlPathFileName+"placemakerxml" + docId
				+ ".xml";
		ExtractLocation el = new ExtractLocation();
		// LocationTest lt = new LocationTest();
		// textString = lt.getWwjText(docId);
		textString = IOTools.getTravelogueText(DOCID);
		// 需要修改，让只调用parsewwjHTML一次************* getDataStructure
		// if (el.parsewwjHTML2(fileName) == 0) {

		wwjElements = el.parsewwjHTML3(fileName);
		pmElements = el.parsePMXML(xmlFile);
		return true;
		// } else {
		// System.out.println("error:parsessjHTML" + docId);
		// return false;
		// }

	}

	/**
	 * 获取当前finalist的最后一个元素的词的末尾边界
	 * 
	 * @return 正常的边界，-2表示finalist当中没有值
	 */
	public int getFinalistEnd() {
		if (finalList.size() > 0) {
			return finalList.get(finalList.size() - 1).getEnd();
		} else {
			return -2;
		}
	}

	/**
	 * 去掉一个le元素末尾的标点符号
	 * 
	 * @param le
	 * @return
	 */
	public LocationElement trimStop(LocationElement le) {
		int start = le.getStart();
		int end = le.getEnd();
		String orgString = le.getLocationOriginalText();
		int orgLength = orgString.length();
		while (orgString.substring(orgLength - 1, orgLength).matches(
				"[\\p{Punct}]")) {
			le = new LocationElement(orgString.substring(0, orgLength - 1),
					start, end - 1, le.getDocId());
			orgString = le.getLocationOriginalText();
			orgLength = orgString.length();
			start = le.getStart();
			end = le.getEnd();
		}

		return le;
	}

	/**
	 * 合并后的使用粉红色背景RGB:FFB6C1 <span style = 'background-color:FFB6C1'></span>
	 * wwj的对字体使用中蓝海，<FONT COLOR=3cb371> placeMaker使用下划线U，</FONT>
	 * groundTruth使用：style ='font-style:italic;font-weight:bold;'
	 */
	public void mergeTwoData() {
		int i = 0, j = 0;
		int wStart, wEnd, pmStart, pmEnd;
		int finalListEnd = -2;
		LocationElement newLe = null;
		// String newTerm = new String();
		// STEP1 ********************************************
		while (i < wwjElements.size() && j < pmElements.size()) {
			// if(i==4)
			// {
			// i =i+1;
			// i = i-1;
			// }
			wStart = wwjElements.get(i).getStart();
			wEnd = wwjElements.get(i).getEnd();
			pmStart = pmElements.get(j).getStart();
			pmEnd = pmElements.get(j).getEnd();
			finalListEnd = getFinalistEnd();

			// System.out.println("***********************************************************"+finalList.size());
			// if(finalList.size()==6)
			// {
			// System.out.println("***********************************************************");
			// int kk = 1;
			// kk = kk-1;
			// }
			// newLe = null;
			if (pmStart < wStart && pmEnd < wStart) {
				if (pmStart < finalListEnd) {// 判断pmLocation是否和前一个有交界,单词之间有空格，所以就不算等于的情况
					j++;
					continue;
				}
				finalList.add(trimStop(pmElements.get(j)));
				j++;
				continue;
			}// p检测出,p多余的情况,w没检测出的情况
			else if (wStart < pmStart && wEnd < pmStart) {
				// WResultTest还可处理T3、T5错误
				if (wStart < finalListEnd) {// 判断wwjLocation是否和前一个有交界,单词之间有空格，所以就不算等于的情况
					i++;
					continue;
				}
				newLe = WResultTest(wwjElements.get(i), i);
				if (newLe != null) {
					// newLE = trimComma(newLE);//去掉末尾逗号处理
					finalList.add(trimStop(newLe));
				}
				i++;
				continue;
			}// W检测出来P没检测出来的情况，对应T2.4
			else if (wStart == pmStart && wEnd == pmEnd) {
				// newTerm = wwjElements.get(i).getLocationOriginalText();
				if (wStart < finalListEnd) {// 判断wwjLocation是否和前一个有交界,单词之间有空格，所以就不算等于的情况
					i++;
					j++;
					continue;
				}
				// 去掉先进行gazetterSearch的部分
				// if(gazetterSearch2(wwjElements.get(i).getLocationOriginalText()))
				// {
				// finalList.add(wwjElements.get(i));
				// }else
				// {
				newLe = integrityTest(wwjElements.get(i), i, 0);
				finalList.add(trimStop(newLe));
				// }
				i++;
				j++;
				continue;
			}// W和P的检测结果重合的情况，对应T1
			else {
				if (wStart < finalListEnd) {// 判断wwjLocation是否和前一个有交界,单词之间有空格，所以就不算等于的情况
					i++;
					continue;
				}
				if (pmStart <= finalListEnd) {// 判断pmLocation是否和前一个有交界,单词之间有空格，所以就不算等于的情况
					j++;
					continue;
				}
				newLe = concatenate(wwjElements.get(i), pmElements.get(j));
				if (poiTest(newLe.getLocationOriginalText())) {
					newLe = integrityTest(newLe, i, 0);
					finalList.add(trimStop(newLe));
				} else {
					if (wwjElements.get(i).getLocationOriginalText().length() > pmElements
							.get(j).getLocationOriginalText().length()) {
						finalList.add(trimStop(wwjElements.get(i)));
					} else {
						finalList.add(pmElements.get(j));
					}
				}
				i++;
				j++;
				continue;
			}// 可以处理3种情况：W检测出的是P的一部分，对应T2.1;
				// P检测出的是W的一部分，对应T2.2；两个都不完全正确的情况，对应T3
		}
		// STEP2*******************************************
		newLe = null;
		while (i < wwjElements.size()) {
			newLe = WResultTest(wwjElements.get(i), i);
			if (newLe != null) {
				finalList.add(wwjElements.get(i));
			}
			newLe = null;
			i++;
		}
		while (j < pmElements.size()) {
			// newLe = PResultTest(pmElements.get(j), j);
			// if (newLe != null) {
			finalList.add(pmElements.get(j));
			j++;
			// }
			newLe = null;
		}
		// STEP3********************************************
		/**
		 * 重新扫描一遍文本，处理两人都没检测出（对应T4）的情况调用TextRescan()函数;对text中的每一个word，
		 * 调用PatternMatchforTextRescan（word）函数，看是否属于TextRescan所定义的Pattern列表，
		 * 若结果为真就按对应的类型处理
		 */
		/**
		 * ※STEP3重新扫描文本TextRescan所定义的地名Pattern  Postfix Pattern词列表：hill, hills,
		 * cliff, mountain, river, spring, pool, pond, lake, sea, ocean, basin,
		 * bay, beach, coast, dam, island, coastline, valley, ridge, peak,
		 * forest, canyon, dale, desert, sand, oasis, flats, cave, cove, creek,
		 * harbor, glacier, veld, veldt, road, avenue, street, highway, country,
		 * town, village, zone, park, ground, land, field, court, yard,
		 * courtyard, deck, campus,camp, site, lawn, area, garden, orchard,
		 * castle, roof, room, tower, bridge, museum, monument, gallery,art
		 * gallery, library, theatre, cinema, plaza, palace, building, hall,
		 * center, company, house, church, university, school... is where 
		 * Prefix Pattern词列表： university of, university, arrive in, arrive at,
		 * back to, here in, day in, stop in, stop at, live in, back in, park
		 * in, town of, views of, drive to
		 * 
		 * 
		 */
		return;
	}

	/**
	 * 单词前有
	 * 
	 * @param locationElement
	 * @param j
	 * @return
	 */
	public LocationElement PResultTest(LocationElement locationElement, int j) {
		// 检测P中的某个结果是否正确，是否需要更改，或是应被舍弃
		/*
		 * IF P[j]的前面的词是in, at, go to, went to, onto, into, back to, town of,
		 * views of, drive to Return P[j]; ELSEw Return EmptyString;
		 */
		int leftIndex = locationElement.getStart();
		// System.out.println(leftIndex+",");
		String next = null;
		// String textString = locationElement.getLocationOriginalText();
		String preResults[] = new String[2];// 0:左边，1：左边二
		String actual = "";
		int state = 0;
		// LocationElement[] lElements = new LocationElement[2];
		// int flTail,flHead,slTail,slHead;
		int flTail = leftIndex, flHead = 0, slTail = 0, slHead = 0;
		int i = 0;
		int finaListLastEnd = getFinalistEnd();
		// 向左判断两个单词
		while (leftIndex > 0) {
			next = textString.substring(leftIndex - 1, leftIndex);
			if (state == 0) {
				if (next.matches("\\s")) // 空格
				{
					leftIndex--;
				} else if (next.matches("\\w")) // 单词字母
				{
					state = 1;
					flTail = leftIndex; // 包头不包尾
					if (leftIndex == 1) // 到头了
					{
						// 不需要判断逗号，逗号就停止延伸
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						preResults[i] = textString.substring(leftIndex - 1,
								flTail);
						i++;
						break;
					}
					leftIndex--;
				} else if (next.matches("[\\p{Punct}]")) // 标点符号
				{
					break;
				}
			} else if (state == 1) {
				if (next.matches("\\s")) // 空格
				{
					state = 2;
					flHead = leftIndex;
					if (leftIndex < finaListLastEnd) { // 小于finalList最后一个的end，也不需要延伸
						break;
					}
					preResults[i] = textString.substring(flHead, flTail);
					i++;
					leftIndex--;
				} else if (next.matches("\\w")) // 单词字母
				{
					if (leftIndex == 1) {
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						preResults[i] = textString.substring(leftIndex - 1,
								flTail);
						i++;
						break;
					}
					leftIndex--;
				} else if (next.matches("[\\p{Punct}]")) // 标点符号
				{
					if ((leftIndex) < finaListLastEnd) {
						// 小于finalList最后一个的end，也不需要这个单词
						break;
					}
					preResults[i] = textString.substring(leftIndex, flTail);
					i++;
					break;
				}
			} else if (state == 2) {
				if (next.matches("\\s")) // 空格
				{
					leftIndex--;
				} else if (next.matches("\\w")) // 单词字母
				{
					slTail = leftIndex;
					state = 3;
					if (leftIndex == 1) {
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						preResults[i] = textString.substring(leftIndex - 1,
								leftIndex);
						i++;
						break;
					}
					leftIndex--;
				} else if (next.matches("[\\p{Punct}]")) // 标点符号
				{
					break;
				}
			} else if (state == 3) {
				if (next.matches("\\s")) // 空格
				{
					state = 4;
					slHead = leftIndex;
					if ((leftIndex) < finaListLastEnd) {
						// 小于finalList最后一个的end，也不需要这个单词
						break;
					}
					preResults[i] = textString.substring(slHead, slTail);
					i++;
					break;
				} else if (next.matches("\\w")) // 单词字母
				{
					if (leftIndex == 1) {
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						preResults[i] = textString.substring(leftIndex - 1,
								slTail);
						i++;
						break;
					}
					leftIndex--;
				} else if (next.matches("[\\p{Punct}]")) // 标点符号
				{
					if ((leftIndex) < finaListLastEnd) {
						// 小于finalList最后一个的end，也不需要这个单词
						break;
					}
					preResults[i] = textString.substring(leftIndex, slTail);
					i++;
					break;
				}
			}
		}

		// for (int k = 0; k < preResults.length; k++) {
		// String locationElement2 = preResults[k];
		// if (locationElement2 == null) {
		// logForGoogle.debug("-pResultTest-preResults:" + "null");
		// } else {
		// logForGoogle.debug("-pResultTest-preResults:" + preResults[k]);
		// }
		// }

		// i = 1;
		if (i == 0) {
			return null;
		}
		i = 0;
		while (i <= 1) {
			if (preResults[i] != null) {
				actual = preResults[i] + " " + actual;
			}
			actual = actual.replaceAll("(^[\\s]{1,})|([\\s]{1,}$)", "");
			actual = actual.replaceAll("[\\s]+", " ");
			// logForGoogle.debug("-pResultTest-"+i+"-:"+actual);
			if (pResultPrefixMap.get(actual) != null) {
				return locationElement;
			}
			i++;
		}
		return null;
	}

	private boolean singleWordLocationNameTest(LocationElement le, int i) {
		// 和PResultTest()方法一样；
		if (PResultTest(le, i) == null) {
			return false;
		} else {

			return true;
		}
	}

	/**
	 * 去掉一个le元素末尾的逗号
	 * 
	 * @param le
	 * @return
	 */
	public LocationElement trimComma(LocationElement le) {
		int start = le.getStart();
		int end = le.getEnd();
		String orgString = le.getLocationOriginalText();
		int orgLength = orgString.length();
		if (orgString.substring(orgLength - 1, orgLength).equals(",")) {
			LocationElement newLe = new LocationElement(orgString.substring(0,
					orgLength - 1), start, end - 1, le.getDocId());
			return newLe;
		}
		return le;
	}

	// 判断该词属于前缀pattern或后缀p attern或未知
	// 前缀pattern无须向前延伸，判断是否后缀pattern，若是则不用扩充，若不是则向后延伸，遇结束符（标点，后缀结束词）或长度达到2停止，一个个调用POITest()
	// 后缀pattern无须向后延伸，判断是否前缀pattern，若是则不用扩充，若不是则向前延伸，遇结束符（标点，前缀结束词）或长度达到2停止，一个个去调用POITest()
	// 若无法判断属于前缀pattern还是后缀pattern，则要向左边延伸、右边延伸、及同时向左右延伸

	/**
	 * ※Integrity Test所用的前后缀结束词列表 Prefix Ending Flag词列表 The..., into..., at...,
	 * on..., to ..., here..., (各种标点)..., of.., for..., called..., named... 
	 * Postfix Ending Flag词列表 ...is/was, ...had/has/have, ...to, ...on, ...for,
	 * ...that, ...where, ...(各种标点) 暂时不考虑越界到另外一个单词的情况
	 * 
	 * @param neLe
	 *            一个Location元素
	 * @param i
	 *            neLe在该算法中所处的位置
	 * @param flag
	 *            0：wwjElement;1:pmElements
	 * @return 进行lrm延伸后，最长的单词。
	 */
	public LocationElement integrityTest(LocationElement neLe, int i, int flag) {
		LocationElement[] lrElements = new LocationElement[4];// 0:l1;1:l2;2:r1;3:r2
		List<LocationElement> poiList = new ArrayList<LocationElement>();
		poiList.add(neLe);
		String next = null;
		int leftIndex = neLe.getStart();
		int rightIndex = neLe.getEnd();
		int flTail = leftIndex, flHead = 0, slTail = 0, slHead = 0;
		int rHead = 0;
		int textLength = textString.length();
		int orgLength = neLe.getLocationOriginalText().length();
		int state = 0;
		boolean commaFlag = false;
		if (neLe.getLocationOriginalText().charAt(orgLength - 1) == ',') {
			commaFlag = true;
		}

		String punctuationOrDigit = "[\\p{Punct}]|[\\d]";// 标点符号或者数字，终止符号
		String alph = "\\p{Alpha}"; // 字母字符：[\p{Lower}\p{Upper}]
		// 上一个单词的结尾
		int finaListLastEnd = getFinalistEnd();
		// 向左判断两个单词
		while (leftIndex > 0) {
			next = textString.substring(leftIndex - 1, leftIndex);
			if (state == 0) {
				if (next.matches("\\s")) // 空格
				{
					leftIndex--;
				} else if (next.matches(alph)) // 单词字母
				{
					state = 1;
					flTail = leftIndex; // 包头不包尾
					if (leftIndex == 1) // 到头了
					{
						// 不需要判断逗号，逗号就停止延伸
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						lrElements[0] = new LocationElement(
								textString.substring(leftIndex - 1, flTail),
								leftIndex - 1, flTail, DOCID);
						break;
					}
					leftIndex--;
				} else if (next.matches(punctuationOrDigit)) // 标点符号
				{
					break;
				}
			} else if (state == 1) {
				if (next.matches("\\s")) // 空格
				{
					state = 2;
					flHead = leftIndex;
					if (leftIndex < finaListLastEnd) { // 小于finalList最后一个的end，也不需要延伸
						break;
					}
					lrElements[0] = new LocationElement(textString.substring(
							flHead, flTail), flHead, flTail, DOCID);
					leftIndex--;
				} else if (next.matches(alph)) // 单词字母
				{
					if (leftIndex == 1) {
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						lrElements[0] = new LocationElement(
								textString.substring(leftIndex - 1, flTail),
								leftIndex - 1, flTail, DOCID);
						break;
					}
					leftIndex--;
				} else if (next.matches(punctuationOrDigit)) // 标点符号
				{
					if ((leftIndex) < finaListLastEnd) {
						// 小于finalList最后一个的end，也不需要这个单词
						break;
					}
					if (next.matches("\\d")) {
						if (leftIndex + 1 >= flTail) {
							break;
						}
						lrElements[0] = new LocationElement(
								textString.substring(leftIndex + 1, flTail),
								leftIndex + 1, flTail, DOCID);
						break;
					}
					lrElements[0] = new LocationElement(textString.substring(
							leftIndex, flTail), leftIndex, flTail, DOCID);
					break;
				}
			} else if (state == 2) {
				if (next.matches("\\s")) // 空格
				{
					leftIndex--;
				} else if (next.matches(alph)) // 单词字母
				{
					slTail = leftIndex;
					state = 3;
					if (leftIndex == 1) {
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						lrElements[1] = new LocationElement(
								textString.substring(leftIndex - 1, leftIndex),
								leftIndex - 1, leftIndex, DOCID);
						break;
					}
					leftIndex--;
				} else if (next.matches(punctuationOrDigit)) // 标点符号
				{
					break;
				}
			} else if (state == 3) {
				if (next.matches("\\s")) // 空格
				{
					state = 4;
					slHead = leftIndex;
					if ((leftIndex) < finaListLastEnd) {
						// 小于finalList最后一个的end，也不需要这个单词
						break;
					}
					lrElements[1] = new LocationElement(textString.substring(
							slHead, slTail), slHead, slTail, DOCID);
					break;
				} else if (next.matches(alph)) // 单词字母
				{
					if (leftIndex == 1) {
						if ((leftIndex - 1) < finaListLastEnd) {
							// 小于finalList最后一个的end，也不需要这个单词
							break;
						}
						lrElements[1] = new LocationElement(
								textString.substring(leftIndex - 1, slTail),
								leftIndex - 1, slTail, DOCID);
						break;
					}
					leftIndex--;
				} else if (next.matches(punctuationOrDigit)) // 标点符号
				{
					if ((leftIndex) < finaListLastEnd) {
						// 小于finalList最后一个的end，也不需要这个单词
						break;
					}
					if (next.matches("\\d")) {
						if (leftIndex + 1 >= flTail) {
							break;
						}
						lrElements[1] = new LocationElement(
								textString.substring(leftIndex + 1, flTail),
								leftIndex + 1, flTail, DOCID);
						break;
					}
					lrElements[1] = new LocationElement(textString.substring(
							leftIndex, slTail), leftIndex, slTail, DOCID);
					break;
				}
			}
		}
		// 向右判断单词,向右不用考虑边界延伸出界
		state = 0;
		int outflag = 0;
		int rightWord = 2;
		// if (neLe.getLocationOriginalText().equalsIgnoreCase("Railroad")) {
		// state = 0;
		// }
		while (rightIndex < textLength) {
			next = textString.substring(rightIndex, rightIndex + 1);
			switch (state) {
			case 0:
				if (next.matches("\\s")) // 空格
				{
					rightIndex++;
				} else if (next.matches(alph)) // 单词字母
				{
					// 遇到第一个单词就是结尾字符
					if ((rightIndex + 1) == textLength) {

						lrElements[rightWord] = new LocationElement(
								textString
										.substring(rightIndex, rightIndex + 1),
								rightIndex, rightIndex + 1, DOCID);
						state = 2;
						break;
					}
					state = 1;
					rHead = rightIndex; // 包头不包尾
					rightIndex++;
				} else if (next.matches(punctuationOrDigit)) // 标点符号
				{
					state = 2;
					break;
				}
				// else {
				// rightIndex++;
				// }
				break;
			case 1:
				if (next.matches("\\s")) // 空格
				{
					state = 0;
					lrElements[rightWord] = new LocationElement(
							textString.substring(rHead, rightIndex), rHead,
							rightIndex, DOCID);
					rightWord++;
					rightIndex++;
					if (rightWord == 4) {
						state = 2;
						break;
					}
				} else if (next.matches("\\w")) // 单词字母
				{
					if ((rightIndex + 1) == textLength) {
						lrElements[rightWord] = new LocationElement(
								textString.substring(rHead, rightIndex + 1),
								rHead, rightIndex + 1, DOCID);
						state = 2;
						break;
					}
					rightIndex++;
				} else if (next.matches(punctuationOrDigit)) // 标点符号
				{
					if (next.matches("\\d")) {
						if (rightIndex - 1 <= rHead) {
							break;
						}
						lrElements[rightWord] = new LocationElement(
								textString.substring(rHead, rightIndex - 1),
								rHead, rightIndex - 1, DOCID);
						break;
					}
					lrElements[rightWord] = new LocationElement(
							textString.substring(rHead, rightIndex), rHead,
							rightIndex, DOCID);
					state = 2;
					break;
				}
				break;
			// else {
			// leftIndex--;
			// }
			case 2:
				outflag = 1;
				break;
			default:
				break;
			}
			if (outflag == 1) {
				break;
			}
		}
		// test---------------------------------------------------------------------------
		// -------------------------------------------------------------------------------
		// logForGoogle.debug("begin of a word-integrityTest:"
		// + neLe.getLocationOriginalText());
		// for (int j = 0; j < lrElements.length; j++) {
		// LocationElement locationElement = lrElements[j];
		// if (locationElement == null) {
		// logForGoogle.debug("-integrityTest:" + "null");
		// } else {
		// logForGoogle.debug("-integrityTest:"
		// + locationElement.getLocationOriginalText());
		// }
		// }

		// 左边，右边两次进行延伸
		// 0:l1;1:l2;2:r1;3:r2
		Integer isExist = null;
		int leftEnd = 0;// 记录左边有几个合法的单词
		for (int j = 0; j < 4; j++) {
			if (lrElements[j] != null) {
				String nowText = lrElements[j].getLocationOriginalText()
						.toLowerCase();
				if (j == 0) {
					isExist = integrityPrefixMap.get(nowText);
					// logForGoogle.debug("integrityTest- prefix:" + isExist);
					if (isExist == null) {
						LocationElement concateElement = concatenate(
								lrElements[j], neLe);
						boolean isPoi = poiTest(concateElement
								.getLocationOriginalText());
						if (isPoi) {
							poiList.add(concateElement);
						}
						leftEnd = 1;
					} else // 是前缀
					{
						leftEnd = 0;
						j = 1;
					}
				} else if (j == 1) {
					isExist = integrityPrefixMap.get(nowText);
					// logForGoogle.debug("integrityTest- prefix:" + isExist);
					if (isExist == null) {
						LocationElement concateElement = concatenate(
								concatenate(lrElements[j], lrElements[j - 1]),
								neLe);
						boolean isPoi = poiTest(concateElement
								.getLocationOriginalText());
						if (isPoi) {
							poiList.add(concateElement);
						}
						leftEnd = 2;
					} else// 跳到右边
					{
						leftEnd = 1;
						continue;
					}
				} else if (j == 2) {
					isExist = integrityPostfixMap.get(nowText);// 判断是否是后缀
					// logForGoogle.debug("integrityTest- postfix:" + isExist);
					if (isExist == null) {
						LocationElement concateElement = concatenate(neLe,
								lrElements[j]);
						boolean isPoi = poiTest(concateElement
								.getLocationOriginalText());
						if (isPoi) {
							poiList.add(concateElement);
						}
						// else
						// {
						// if(commaFlag && gazetterSearch2(nowText))
						// {//逗号判断，是不属于.当中的后缀
						// poiList.add(concateElement);
						// }
						// }
						for (int k = 0; k < leftEnd; k++) {
							concateElement = concatenate(concateElement,
									lrElements[k]);
							isPoi = poiTest(concateElement
									.getLocationOriginalText());
							if (isPoi) {
								poiList.add(concateElement);
							}
						}
					} else // 不用再向右边延伸了
					{
						break;
					}
				} else if (j == 3) {
					isExist = integrityPostfixMap.get(nowText);
					// logForGoogle.debug("integrityTest- postfix:" + isExist);
					if (isExist == null) {
						LocationElement concateElement = concatenate(neLe,
								concatenate(lrElements[j - 1], lrElements[j]));
						boolean isPoi = poiTest(concateElement
								.getLocationOriginalText());
						if (isPoi) {
							poiList.add(concateElement);
						}
						for (int k = 0; k < leftEnd; k++) // 加入左边的判断
						{
							concateElement = concatenate(concateElement,
									lrElements[k]);
							isPoi = poiTest(concateElement
									.getLocationOriginalText());
							if (isPoi) {
								poiList.add(concateElement);
							}
						}
					} else // 不用再向右边延伸了
					{
						break;
					}
				}
			} else {
				if (j == 0) {
					leftEnd = 0;
					j = 1;// 跳过j=1的情况
				} else if (j == 1) {
					leftEnd = 1;
				} else {
					break;
				}
			}
		}

		// test---------------------------------------------------------------------------
		// -------------------------------------------------------------------------------
		// logForGoogle
		// .debug("the size of integrityTest resultList -integrityTest:"
		// + poiList.size());
		// for (Iterator<LocationElement> iterator = poiList.iterator();
		// iterator
		// .hasNext();) {
		// LocationElement locationElement = (LocationElement) iterator.next();
		// logForGoogle.debug("-integrityTest:"
		// + locationElement.getLocationOriginalText());
		// }

		if (poiList.size() == 0) {
			return neLe;
		}
		LocationElement result = null;
		if (poiList.size() == 1) {
			result = poiList.get(0);
			result = trimRedundantWords(result, neLe);
			if (commaFlag) {
				if (result.getEnd() > neLe.getEnd()) {
					String commaTailString = textString.substring(
							neLe.getEnd(), result.getEnd());
					// 判断逗号后面的，是否在地点名词中
					if (!gazetterSearch2(searchPreprocess(commaTailString))) {
						return neLe;
					}
				}
			}
			result = trimRedundantWords(result, neLe);
			return result;
		}

		Collections.sort(poiList, new Comparator<LocationElement>() {
			@Override
			public int compare(LocationElement o1, LocationElement o2) {
				int length1 = o1.getEnd() - o1.getStart();
				int length2 = o2.getEnd() - o2.getStart();
				// 大的排在前面
				if (length1 > length2) {
					return -1;
				} else if (length1 < length2) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		result = poiList.get(0);
		if (commaFlag) // 去末尾逗号，留到WResult做
		{
			if (result.getEnd() > neLe.getEnd()) {
				String commaTailString = textString.substring(neLe.getEnd(),
						result.getEnd());
				if (!gazetterSearch2(searchPreprocess(commaTailString))) {
					return neLe;
				}
			}
		}
		result = trimRedundantWords(result, neLe);
		return result;
	}

	/**
	 * 去掉右边和左边多余的单词
	 * 
	 * @param extendLE
	 * @param orgLE
	 * @return
	 */
	public LocationElement trimRedundantWords(LocationElement extendLE,
			LocationElement orgLE) {
		int orgStart = orgLE.getStart();
		int orgEnd = orgLE.getEnd();
		String orgString = orgLE.getLocationOriginalText();
		int orgLength = orgString.length();

		if (extendLE.getEnd() > orgEnd) { // 右边有多的
			String tail = textString.substring(orgEnd, extendLE.getEnd());
			tail = searchPreprocess(tail);
			if (integrityTrimMap.get(tail) != null) {// 右边的是多余的，去掉右边
				extendLE = new LocationElement(textString.substring(
						extendLE.getStart(), orgEnd), extendLE.getStart(),
						orgEnd, orgLE.getDocId());
			}
		}
		if (extendLE.getStart() < orgStart) {// 左边有多的
			String head = textString.substring(extendLE.getStart(), orgStart);
			head = searchPreprocess(head);
			if (integrityTrimMap.get(head) != null) {// 左边的是多余的，去掉左边
				extendLE = new LocationElement(textString.substring(orgStart,
						extendLE.getEnd()), orgStart, extendLE.getEnd(),
						orgLE.getDocId());
			}
		}
		return extendLE;

	}

	public void textRescan() {
		/*
		 * For each word in Text IF PatternMatchforTextRescan(word) is
		 * TR_POSTFIX get the front words(at most 4) of current word, and do
		 * POITest() for each candidate; ELSE IF PatternMatchforTextRescan(word)
		 * is TR_PREFIX get the back words(at most 4) of current word, and do
		 * POITest() for each candidate; ELSE;
		 */
	}

	public void patternMatchforTextRescan(String word) {

	}

	/**
	 * hill, cliff, ,mountain, ,river, rivers,spring, pool,pools,pond, lake,
	 * sea, ocean, basin, bay, beach, coast, dam, island, coastline, valley,
	 * ridge, peak, forest, canyon, dale, desert, sand, oasis, flats, cave,
	 * cove, creek, harbor, glacier, veld, veldt, road, avenue, street, highway,
	 * country, town, village, zone, park, ground, land, field, court, yard,
	 * courtyard, deck, campus, camp, site, lawn, area, garden, orchard, castle,
	 * roof, room, tower, bridge, museum, monument, gallery, art gallery,
	 * library, theatre, cinema, plaza, palace, building, hall, center, company,
	 * house, church, university, school hills,cliffs,mountains 查看是否是后缀
	 */
	public boolean isLocalTerm(String newTerm) {
		Integer isPostfix = null;
		String subPost = null;
		String[] newTerms = newTerm.split(" ");
		subPost = newTerms[newTerms.length - 1].toLowerCase();
		isPostfix = localTermPostfixMap.get(subPost);
		if (isPostfix != null) {
			// System.out.println("isLocalTerm-newTerm:" + newTerm + ":"
			// + isPostfix);
			return true;
		}
		// 然后看它是否属于典型的地名Pattern（postfix pattern，从hill到school）
		// 同样加入复数形式
		// 不一样的只有一个后缀(is Where)
		// System.out.println("isLocalTerm-newTerm:" + newTerm + ":" +
		// isPostfix);
		return false;
	}

	/**
	 * 将term到Wikipedia上进行query，获取前3条结果的title中高亮的词
	 * 看是否有跟term一致的，若有则认为该term是一个合法POI.3条中有其一就行。 或者term直接由词条 //只要在稳重出现的，都算搜索到 //
	 * 有结果
	 * <ul class="mw-search-results">
	 * //结果元素<span class="searchmatch">Park</span> //直接词条
	 * <h1 id="firstHeading" class="firstHeading" lang="en">
	 * <span dir="auto">Birmingham, Alabama</span></h1>
	 * //没有结果
	 * <p class="mw-search-nonefound">
	 * //There were no results matching the query.
	 * </p>
	 * 
	 * @param newTerm
	 * @return
	 */
	public boolean wikipediaSearch(String newTerm, int time) {
		if (time == 3)//重连接三次
		{
			wikipediaSearchError++;			
			return false;
		}
		StringBuilder sBuilder = null;
		// comment: "\\"is \,so "\\s" is "\s","\s" is the kind of blank
		// character,like"\n\t..."
		// "\\s,",is "\s" or ","
		String matchString = newTerm;// 把标点变成1个空格，合并连续的空白符
		// ^表示行开头;X(1,),表示X至少1次;$表示行结尾;"\\s"表示空白符
		// matchString = matchString.replaceAll("(^\\s{1,})|(\\s{1,}$)", "");//
		// 去掉行开头和结尾的空格
		// 判断wordPairMap中有木有这个词
//		Boolean preSearchResult = wordPairForWikiMap.get(newTerm);
		Boolean preSearchResult = webSearchIO.getWikipediaSearchResult(newTerm);
		if (preSearchResult != null) {

			if (preSearchResult) {
				// logForGoogle
				// .debug("wikipediaSearch-mapResult match:" + newTerm);
				// logForGoogle.debug("wikipediaSearch-mapResult googleResult:"
				// + preSearchResult);
				return true;
			} else {
				// logForGoogle.debug("wikipediaSearch-mapResult not match:"
				// + newTerm);
				// logForGoogle.debug("wikipediaSearch-mapResult googleResult:"
				// + preSearchResult);
				return false;
			}

		}
		String wikipediaResult = new String();
		StringBuilder wikipediaTmp = new StringBuilder();
		try {
			newTerm = URLEncoder.encode(newTerm, "UTF-8"); // google中的q参数需要对参数进行编码，
			// 否则会出现错误
			// System.out.println("SearchTerm"+newTerm);
			String urlpath = "http://en.wikipedia.org/w/index.php?" + "search="
					+ newTerm + "&title=Special%3ASearch&go=Go";
			URL url = new URL(urlpath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
			conn.setRequestMethod("GET");
			conn.setReadTimeout(6 * 10000);
			int responseCode = 200;
			int reConnect = 0;
			if (conn.getInputStream().available() == 0) {
				responseCode = -1;// 表示没有数据
			} else {
				responseCode = conn.getResponseCode();
			}
				// 设置循环来处理302的错误
				if (responseCode != 200) {
					logForGoogle.debug(DOCID+"-googleSearch2-respond is not correct:"
							+ responseCode + ":" + newTerm);
					while (responseCode !=200) {
						if (reConnect == 4) {
							throw new Exception("connect to google up to 4 times");
						}
						logForGoogle.debug(DOCID+"-googleSearch2-" + newTerm
								+ ",reconnect" + reConnect+",responseCode:"+responseCode);
						String repeatUrl = conn.getHeaderField("location");
						url = new URL(repeatUrl);
						conn = (HttpURLConnection) url.openConnection();
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
								throw new Exception("connect to wikipedia up to 4 times");
							}
							continue;
						}
						responseCode = conn.getResponseCode();
						reConnect++;
					}
				}

			sBuilder = IOTools.readIO(conn.getInputStream());
			if(sBuilder==null)
			{
				//网路内容为空,作为错误处理
				wikipediaSearchError++;
				return false;
			}
			// 开始解析
			org.jsoup.nodes.Document doc = Jsoup.parse(sBuilder.toString());
			org.jsoup.select.Elements noneFoundElements = doc
					.select("p.mw-search-nonefound");
			if (noneFoundElements == null || noneFoundElements.size() == 0) // 没有nonefound
			{
				org.jsoup.select.Elements ulElements = doc
						.select("ul.mw-search-results");
				if (ulElements == null || ulElements.size() == 0)// 无选择列表
				{
					org.jsoup.nodes.Element headElement = doc
							.getElementById("firstHeading");
					if (headElement != null)// 有firstHeading
					{
						org.jsoup.select.Elements dirResults = headElement
								.select("span[dir]");
						if (dirResults == null || dirResults.size() == 0) {// 找不到内容
							Map<String,Boolean> map = new HashMap<String,Boolean>();
							map.put(matchString, false);
							webSearchIO.insertWikipediaSearchResult(map);
//							wordPairForWikiMap.put(matchString, false);
							return false;
						} else {
							org.jsoup.nodes.Element resultElement = dirResults
									.get(0);
							String tmpString = resultElement.text();
							// 去掉标点符号，合并多余空格，去掉头尾空格
							wikipediaResult = tmpString.replaceAll("[\\s,.]+",
									" ");
							wikipediaResult = wikipediaResult.replaceAll(
									"(^[\\s]{1,})|([\\s]{1,}$)", "");
							if (wikipediaResult.equalsIgnoreCase(matchString)) { // 找到内容，但是不匹配
								// logForGoogle
								// .debug("wikipediaSearch-successful match:"
								// + matchString);
//								wordPairForWikiMap.put(matchString, true);
								Map<String,Boolean> map = new HashMap<String,Boolean>();
								map.put(matchString, true);
								webSearchIO.insertWikipediaSearchResult(map);
								return true;
							} else// ********
							{
								// logForGoogle
								// .debug("wikipediaSearch-fail to match:"
								// + wikipediaResult);
								// logForGoogle
								// .debug("wikipediaSearch-fail to match:"
								// + matchString);
//								wordPairForWikiMap.put(matchString, false);
								Map<String,Boolean> map = new HashMap<String,Boolean>();
								map.put(matchString, false);
								webSearchIO.insertWikipediaSearchResult(map);
								return false;
							}
						}
					}// 跳出，没有头元素
				} else// 有选择列表
				{
					org.jsoup.nodes.Element ulElement = ulElements.get(0);
					org.jsoup.select.Elements liElements = ulElement
							.getElementsByTag("li");
					if (liElements != null && liElements.size() > 0) {
						// 有li列表
						for (int i = 0; i < liElements.size(); i++) {
							org.jsoup.select.Elements spanResultElements = liElements
									.get(i).select("span.searchmatch");
							wikipediaTmp = new StringBuilder();
							for (int j = 0; j < spanResultElements.size(); j++) {
								String tmpString = spanResultElements.get(j)
										.text();
								// System.out.println(tmpString);
								if (tmpString.replaceAll("[\\s,.]+", " ")
										.equalsIgnoreCase(matchString)) {// goole其中的一个em元素匹配成功
									// logForGoogle
									// .debug("wikipediaSearch-successful match:"
									// + wikipediaResult
									// + ";"
									// + tmpString);
//									wordPairForWikiMap.put(matchString, true);
									Map<String,Boolean> map = new HashMap<String,Boolean>();
									map.put(matchString, true);
									webSearchIO.insertWikipediaSearchResult(map);
									return true;
								}
								wikipediaTmp.append(" " + tmpString);
							}
							// System.out.println();
							wikipediaResult = wikipediaTmp.toString()
									.replaceAll("[\\s,.]+", " ");
							wikipediaResult = wikipediaResult.replaceAll(
									"(^[\\s]{1,})|([\\s]{1,}$)", "");
							if (wikipediaResult.equalsIgnoreCase(matchString)
									|| wikipediaResult.contains(matchString)) {
								// logForGoogle
								// .debug("wikipediaSearch-successful match:"
								// + wikipediaResult
								// + ";"
								// + matchString);
//								wordPairForWikiMap.put(matchString, true);
								Map<String,Boolean> map = new HashMap<String,Boolean>();
								map.put(matchString, true);
								webSearchIO.insertWikipediaSearchResult(map);
								return true;
							}
							if (i == 2) {
								break;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logForGoogle.error(e.getStackTrace());
			try {
				Thread.currentThread().sleep(500 * (time + 1));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				
			}
			return wikipediaSearch(newTerm, time + 1);
		}
		// logForGoogle.debug("wikipediaSearch-the wikipediaResult:"
		// + wikipediaResult);
		// logForGoogle.debug("wikipediaSearch-the matcher:" + matchString);
		// liElements当中没有元素 or 前三个list当中没有完全匹配的
		// 匹配的规则是按照高亮的词，把其em用空格拼接起来，去掉标点
//		wordPairForWikiMap.put(matchString, false);
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		map.put(matchString, false);
		webSearchIO.insertWikipediaSearchResult(map);
		return false;
	}

	public int wordCount(LocationElement le) {
		String orgString = le.getLocationOriginalText();
		String[] words = orgString.split("[[\\p{Punct}]|[\\s]]{1,}");
		return words.length;
	}

	/**
	 * 一个LocationElement的star和结尾是包头不包尾
	 * 
	 * @param le
	 * @param i
	 * @return
	 */
	public LocationElement WResultTest(LocationElement le, int i) {
		LocationElement newLE = le;
		String newTerm = le.getLocationOriginalText();// 初始化
		int wwjNextStart = -1;
		String wwjNextTerm = null;
		String wwjNTFTerm = null;
		if (i < wwjElements.size() - 1) {// 可以向后扩展
			wwjNextStart = wwjElements.get(i + 1).getStart();
			wwjNextTerm = wwjElements.get(i + 1).getLocationOriginalText();
			wwjNTFTerm = wwjNextTerm.split("[[\\p{Punct}][\\s]]{1,}")[0];// [[\\p{Punct}][\\s]]{1,}

			// 合并前后两个LocationElement
			// wwj自身只相隔一个字符，可以合并
			if (le.getEnd() + 1 == wwjNextStart) {
				int newStart = le.getStart();
				int newEnd = wwjElements.get(i + 1).getEnd();
				newTerm = textString.substring(newStart, newEnd);// 生成新的类
				if (poiTest(newTerm)) {
					newLE = concatenate(wwjElements.get(i),
							wwjElements.get(i + 1));
				} else // newTerm，newLE还原
				{
					newTerm = le.getLocationOriginalText();
					newLE = le;
				}
				// 满足距离是二，并且第一个是“，”，只在这里加了逗号判断,并且判断是否属于州名
				// }
				// else if ((le.getEnd() + 2 == wwjNextStart)
				// && textString.substring(le.getEnd(), le.getEnd() + 1)
				// .equals(",") && stateMap.get(wwjNTFTerm) != null) {
				// int newStart = le.getStart();
				// int newEnd = wwjElements.get(i + 1).getEnd();
				// newTerm = textString.substring(newStart, newEnd);// 生成新的类
				// if (poiTest(newTerm)) {
				// newLE = concatenate(wwjElements.get(i),
				// wwjElements.get(i + 1));
				// } else // newTerm，newLE还原
				// {
				// newTerm = le.getLocationOriginalText();
				// newLE = le;
				// }

			} else // 不用拼接
			{
				newTerm = le.getLocationOriginalText();
				newLE = le;
			}
		}
		// 再判断是否完整，需要扩充？
		newLE = integrityTest(newLE, i, 0);
		// 再判断是否正确
		if (wordCount(newLE) > 1) {
			return newLE;
		} else {
			if (singleWordLocationNameTest(newLE, i)) {
				return newLE;
			} else {
				return null;
			}
		}
		// return newLE;// 要修改
	}

	public LocationElement concatenate(LocationElement locationElement,
			LocationElement locationElement2) {
		int newStart1 = locationElement.getStart();
		int newStart2 = locationElement2.getStart();
		int newEnd1 = locationElement.getEnd();
		int newEnd2 = locationElement2.getEnd();
		newStart1 = newStart1 < newStart2 ? newStart1 : newStart2;
		newEnd1 = newEnd1 > newEnd2 ? newEnd1 : newEnd2;
		LocationElement newLe = new LocationElement(textString.substring(
				newStart1, newEnd1), newStart1, newEnd1, DOCID);
		return newLe;
	}

	/**
	 * 对字符串进行合并连续空格，合并逗号，句号为空格，去掉头尾空格处理
	 * 
	 * @param newTerm
	 *            输入的字符串
	 * @return 返回处理过的字符串
	 */
	public String searchPreprocess(String newTerm) {
		// comment: "\\"is \,so "\\s" is "\s","\s" is the kind of blank
		// character,like"\n\t..."
		// "\\s,",is "\s" or ","
		newTerm = newTerm.replaceAll("[\\s,.]+", " ");// 把标点变成1个空格，合并连续的空白符
		// ^表示行开头;X(1,),表示X至少1次;$表示行结尾;"\\s"表示空白符
		newTerm = newTerm.replaceAll("(^\\s{1,})|(\\s{1,}$)", "");// 去掉行开头和结尾的空格
		return newTerm;
	}

	public boolean poiTest(String newTerm) {
		newTerm = searchPreprocess(newTerm);
		if (gazetterSearch2(newTerm)) {
			// logForGoogle.debug("gazetterSearch2: true:" + newTerm);
			return true;
		}
		// logForGoogle.debug("gazetterSearch2: false:" + newTerm);
		if (isLocalTerm(newTerm)) {
			if (googleSearch2(newTerm, 0)) {
				return true;
			}
			return false;
		} else {
			if (googleSearch2(newTerm, 0) && wikipediaSearch(newTerm, 0)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * 在gazetter当中查询是否有这个词存在
	 * 
	 * @param newTerm
	 * @return true 存在gazetter中，false表示不存在
	 */
	public boolean gazetterSearch2(String newTerm) {

		return IOTools.searchDataBase(newTerm.replaceAll("'", "''"));
	}

	/**
	 * //将term到Google上进行query，获取前3条结果的title中用红色高亮的词
	 * 看是否有跟term一致的，若有则认为该term是一个合法POI.3条中有其一就行。
	 * 
	 * @param newTerm
	 * @param time
	 *            用来统计为同一个newTerm递归调用google的次数
	 * @return
	 */
	public boolean googleSearch2(String newTerm, int time) {
//	public synchronized boolean googleSearch2(String newTerm, int time) {

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
//		Boolean preSearchResult = wordPairForGoogleMap.get(newTerm);
		Boolean preSearchResult = webSearchIO.getGoogleSearchResult(newTerm);
		if (preSearchResult != null) {
			if (preSearchResult) {
				return true;
			} else {
				return false;
			}
		}
		try {
			Thread.currentThread().sleep(200);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
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
			// String urlpath = "http://www.google.com/search?q=" + newTerm
						// + "&hl=en&oe=utf-8&safe=strict";
			//上面两个url可以做为参考,不行时可以交替使用
			String urlpath = "http://www.google.com/#newwindow=1&q=" + newTerm
					+ "&hl=en&oe=utf-8";  //可以更换其他域名
			
//			String ip[] = getNextIp();
//			Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(ip[0],Integer.parseInt(ip[1])));
			URL url = new URL(urlpath);			
//			 conn = (HttpURLConnection) url.openConnection(proxy);
			conn = (HttpURLConnection) url.openConnection();

			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
			conn.setRequestMethod("GET");
			conn.setReadTimeout(6 * 1000);
			int responseCode = 200;
			if (conn.getInputStream().available() == 0) {
				responseCode = -1;// 表示没有数据
			} else {
				responseCode = conn.getResponseCode();
			}
			// 设置循环来处理302的错误
			if (responseCode != 200) {
				logForGoogle.debug(DOCID+"-googleSearch2-respond is not correct:"
						+ responseCode + ":" + newTerm);
				int reConnect = 0;
				while (responseCode !=200) {
					if (reConnect == 4) {
						throw new Exception("connect to google up to 4 times");
					}
					logForGoogle.debug(DOCID+"-googleSearch2-" + newTerm
							+ ",reconnect" + reConnect+",responseCode:"+responseCode);
					String repeatUrl = conn.getHeaderField("location");
					url = new URL(repeatUrl);
					conn = (HttpURLConnection) url.openConnection();
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
							throw new Exception("connect to google up to 4 times");
						}
						continue;
					}
					responseCode = conn.getResponseCode();
					reConnect++;
				}
			}

			sBuilder = IOTools.readIO(conn.getInputStream());

			if(sBuilder==null)
				return false;
			org.jsoup.nodes.Document doc = Jsoup.parse(sBuilder.toString());
			/**
			 * ol id="rso" li class = g <em></em>
			 */
			org.jsoup.nodes.Element rsoElement = doc.getElementById("rso");
			if (rsoElement == null)// 没有查询结果
			{
//				wordPairForGoogleMap.put(matchString, false);
				Map<String,Boolean> map = new HashMap<String,Boolean>();
				map.put(matchString, false);
				webSearchIO.insertGoogleSearchResult(map);
				return false;
			}
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
//								wordPairForGoogleMap.put(matchString, true);
								Map<String,Boolean> map = new HashMap<String,Boolean>();
								map.put(matchString, true);
								webSearchIO.insertGoogleSearchResult(map);
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
//							wordPairForGoogleMap.put(matchString, true);
							Map<String,Boolean> map = new HashMap<String,Boolean>();
							map.put(matchString, true);
							webSearchIO.insertGoogleSearchResult(map);
							return true;
						}
					}
					// logForGoogle.debug("the googleResult:"+googleResult);
					// logForGoogle.debug("the matcher:"+matchString);
					if (liCount == 3) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logForGoogle.error(e.toString() + ";\n" + newTerm);
			conn.disconnect();
			try {

				Thread.currentThread().sleep(500 * (time + 1));
			} catch (InterruptedException e1) {
				System.out.print("docid"+DOCID+"---");
				e1.printStackTrace();
			}
			
			return googleSearch2(newTerm, time + 1);
		}
		// logForGoogle.debug("googleSearch2-no match the googleResult:"
		// + googleResult);
		// logForGoogle.debug("googleSearch2-the matcher:" + matchString);
		// liElements当中没有元素 or 前三个list当中没有完全匹配的
		// 匹配的规则是按照高亮的词，把其em用空格拼接起来，去掉标点
//		wordPairForGoogleMap.put(matchString, false);
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		map.put(matchString, false);
		webSearchIO.insertGoogleSearchResult(map);
		return false;
	}

	public List<LocationElement> getWwjElements() {
		return wwjElements;
	}

	public void setWwjElements(List<LocationElement> wwjElements) {
		this.wwjElements = wwjElements;
	}

	public List<LocationElement> getPmElements() {
		return pmElements;
	}

	public void setPmElements(List<LocationElement> pmElements) {
		this.pmElements = pmElements;
	}

	public List<LocationElement> getFinalList() {
		return finalList;
	}

	public String getTextString() {
		return textString;
	}
	@Override
	public void run() {
		LocationElement integrityResult = null;
		Date date = new Date();
		long time1 = date.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// MergeAlgorithm ma = new MergeAlgorithm();
		String time = df.format(date);
		int i = 0;
		System.out.println(DOCID + " start:" + time);
		getDataStructure(DOCID);
		try {			
			mergeTwoData();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
			//新的文件
			IOTools.writeToFile(new Integer(DOCID).toString(), errorLogFileName);
			return ;
		}
		// String googleWordFileName =
		// "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/googleGazetter.txt";
		// String wikipediaWordFileName =
		// "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/wikipediaGazetter.txt";
		// 全部插入数据库
		// 检查查找错误的
		String sqlString = null;
		if (googleSearchError >=1 || wikipediaSearchError>=1) {
			IOTools.writeToFile(new Integer(DOCID).toString(), errorLogFileName);
		} else if(finalList.size()==0)
		{
			sqlString = "insert into my_location_element values("+DOCID+","+0+",null,null,null,null,"+
					0+","+0+",null,null,null)";
			SQLQuery query = HibernateSessionFactory.getSession()
					.createSQLQuery(sqlString);
			query.executeUpdate();
		}else {
			//把结果写入文件
			
			
			for (i = 0; i < finalList.size(); i++) {
				LocationElement le = finalList.get(i);
				String orgString = le.getLocationOriginalText().replaceAll("'",
						"''");
				sqlString = "insert into my_location_element values("
						+ le.getDocId() + "," + (i + 1) + ",null,'" + orgString
						+ "',null,null," + le.getStart() + "," + le.getEnd()
						+ ",null,null,null)";
				
				/*
				 * 判断是否有查询过，不是很合理 String sqlString2 =
				 * "select doc_id from my_location_element where doc_id=" +
				 * le.getDocId() + " and word_id=" + (i + 1); SQLQuery query2 =
				 * HibernateSessionFactory.getSession()
				 * .createSQLQuery(sqlString2); Integer temp_docid = (Integer)
				 * query2.uniqueResult(); if (temp_docid != null) continue;
				 */
				// System.out.println(sqlString);
				SQLQuery query = HibernateSessionFactory.getSession()
						.createSQLQuery(sqlString);
				query.executeUpdate();
			}
		}
//		Map<String, Boolean> beGoogleMap = IOTools
//				.getMapFromFile(googleWordFileName);
//		Map<String, Boolean> beWikipediaMap = IOTools
//				.getMapFromFile(wikipediaWordFileName);
//		IOTools.writeMapToFile(wordPairForGoogleMap, beGoogleMap,
//				googleWordFileName);
//		IOTools.writeMapToFile(wordPairForWikiMap, beWikipediaMap,
//				wikipediaWordFileName);
		date = new Date();
		long time2 = date.getTime();
		time = df.format(date);
		System.out.println(DOCID + " stop;listSize:" + finalList.size() + ";"
				+ time + ";used Time:" + (time2 - time1));
		
	}
}
