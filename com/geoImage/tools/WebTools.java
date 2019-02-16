package com.geoImage.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;

import com.geoImage.dao.MyLocationElementId;
import com.geoImage.dao.MyLocationSense;

public class WebTools {
	public Logger logForWebTools = Logger.getLogger(WebTools.class.getClass());

	public WebTools() {
		BasicConfigurator.configure();
		PropertyConfigurator.configure("src/log4j.properties");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WebTools wts = new WebTools();

		// System.out.println(wts.wikipediaSearch("Sawmill Creek Road"));
		// System.out.println(wts.wikipediaSearch("Silver Cove"));
		// System.out.println(wts.wikipediaSearch("Birmingham, Alabama"));
		// System.out.println(wts.wikipediaSearch("Southeast AlaskanIndian Cultural Center"));
		// wts.searchPluralOfWord();
		// wts.beginIcibaSearch();
	}
	
	public void testForGeoName()
	{
		List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		Map<String, MyLocationSense> senseTimeMap = new HashMap<String, MyLocationSense>();
		MyLocationElementId meId = new MyLocationElementId(1,1);
		int b =0;
//		b = WebTools.geoNameSearch("Rainbow Peak", senseList,
//				senseTimeMap,meId);
		System.out.println(b);
		if (b == 1) {
			for (Iterator iterator = senseList.iterator(); iterator.hasNext();) {
				MyLocationSense mls = (MyLocationSense) iterator.next();
				System.out.println(mls.getLocationSenseName() + ":::"
						+ mls.getLocationSensePriorProbability() + ":::"
						+ mls.getLocationSenseTime() + ":::"
						+ mls.getSenseFeature() + ":::"
						+ mls.getSenseFeatureDetails() + ":::"
						+ mls.getLatitude() + ":::" + mls.getLongitude());
			}
		} else if (b == -1) {

		}
	}

	public void searchPluralOfWord() {
		String string = "hill, cliff, mountain, river, spring, pool, pond, lake, "
				+ "sea, ocean, basin, bay, beach, coast, dam, island, coastline, valley, ridge, "
				+ "peak, forest, canyon, dale, desert,  sand, oasis, flats, cave, cove, creek, harbor,"
				+ " glacier, veld, veldt, road, avenue, street, highway, country, town, village, zone, "
				+ "park, ground, land, field, court, yard, courtyard, deck, campus, camp, site, lawn, "
				+ "area, garden, orchard, castle, roof, room, tower, bridge, museum, monument, gallery, "
				+ "art gallery, library, theatre,  cinema, plaza, palace, building, "
				+ "hall, center, company, house, church, university, school";
		String s2 = string.replaceAll("[\\s,.]+", " ");
		String[] ss = s2.split(" ");
		for (int i = 0; i < ss.length; i++) {
			System.out.print("\"" + ss[i] + "\"" + ",");
		}
		System.out.println();
	}

	public void beginIcibaSearch() {
		String[] words = { "hill", "cliff", "mountain", "river", "spring",
				"pool", "pond", "lake", "sea", "ocean", "basin", "bay",
				"beach", "coast", "dam", "island", "coastline", "valley",
				"ridge", "peak", "forest", "canyon", "dale", "desert", "sand",
				"oasis", "flat", "cave", "cove", "creek", "harbor", "glacier",
				"veld", "veldt", "road", "avenue", "street", "highway",
				"country", "town", "village", "zone", "park", "ground", "land",
				"field", "court", "yard", "courtyard", "deck", "campus",
				"camp", "site", "lawn", "area", "garden", "orchard", "castle",
				"roof", "room", "tower", "bridge", "museum", "monument",
				"gallery", "art", "gallery", "library", "theatre", "cinema",
				"plaza", "palace", "building", "hall", "center", "company",
				"house", "church", "university", "school" };
		String pluralWords[] = { "hills", "cliffs", "mountains", "rivers",
				"springs", "pools", "ponds", "lakes", "seas", "oceans",
				"basins", "bays", "beaches", "coasts", "dams", "islands",
				"coastlines", "valleys", "ridges", "peaks", "forests",
				"canyons", "dales", "deserts", "sands", "oases", "flats",
				"caves", "coves", "creeks", "harbors", "glaciers", "veld",
				"veldt", "roads", "avenues", "streets", "highways",
				"countries", "towns", "villages", "zones", "parks", "grounds",
				"lands", "fields", "courts", "yards", "courtyards", "decks",
				"campuses", "camps", "sites", "lawns", "areas", "gardens",
				"orchards", "castles", "roofs", "rooms", "towers", "bridges",
				"museums", "monuments", "galleries", "arts", "galleries",
				"libraries", "theatres", "cinemas", "plazas", "palaces",
				"buildings", "halls", "centers", "companies", "houses",
				"churches", "universities", "schools"

		};
		System.out.println(words.length);
		System.out.println(pluralWords.length);
		for (int i = pluralWords.length - 1; i >= 0; i--) {
			String string = pluralWords[i];
			System.out.print("\"" + pluralWords[i] + "\"" + ",");
		}
		System.out.println();

		String[] plurals = new String[200];
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			plurals[i] = icibaSearch(word);

		}
		for (int j = words.length - 1; j >= 0; j--) {
			System.out.print("\"" + plurals[j] + "\"" + ",");
		}
		System.out.println();
	}


	/**
	 * 
	 * @param word
	 * @param geoInfo
	 * @param time
	 * @return 0表示查询结果只有零个,1表示查询结果正常，-1表示查询异常
	 */
	
	/*
	public static int geoNameSearch2(String word, String geoInfo, int time,
			List<MyLocationSense> senseList,
			Map<String, MyLocationSense> senseTime) {
		if (time == 4) {
			return -1;
		}
		String name = word;
		String state = "California";
		String matchString = new String(word);
		String urlPath = null;
		String inWikiTag = null;
		int results = 0;
		// List<MyLocationSense> senseList = null;
		// String result = null;
		StringBuilder htmlRes = null;
		int reconnect = 0;
		URL url = null;
		HttpURLConnection conn = null;
		try {
			word = URLEncoder.encode(word + "," + state, "UTF-8");
			urlPath = "http://www.geonames.org/search.html?q=" + word
					+ "&country=US";
			url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("GET");
			conn.setReadTimeout(3 * 1000);
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				while (responseCode != 200) {
					reconnect++;
					System.out
							.println("geoNameSearch2--respond is not correct:"
									+ responseCode + ",reConnect" + reconnect);
					if (reconnect == 4) {
						throw new Exception(
								"connect to geonames.org up to 4 times");
					}
					String repeatUrl = conn.getHeaderField("location");
					url = new URL(repeatUrl);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
					conn.setRequestMethod("GET");
					conn.setReadTimeout(6 * 1000);
					responseCode = conn.getResponseCode();
				}
			}
			htmlRes = IOTools.readIO(conn.getInputStream());
			if (htmlRes == null) {
				System.out.println("geoNameSearch2--no httpPage");
				return -1;
			}
			org.jsoup.nodes.Document doc = Jsoup.parse(htmlRes.toString());
			org.jsoup.nodes.Element searchElement = doc.select("div#search")
					.get(0);
			// 是否有我所看到的正常的结果
			if (searchElement != null) {
				// if no elements of that tag exists,it will return a not null
				// elements,
				// but the size of this elements is zero
				org.jsoup.select.Elements inWikiElements = searchElement
						.select("font[color]");
				if (inWikiElements.size() > 0) {
					// 处理全部是wikipedia结果的情况
					inWikiTag = inWikiElements.get(0).getElementsByTag("small")
							.get(0).text();
					if (inWikiTag
							.equals("no records found in geonames database, showing wikipedia results")) {
						System.out
								.println("geoNameSearch2-geonames.org-no results in geoname.org");
						return 0;
					} else {
						// 异常结果
						System.out
								.println("geoNameSearch2-some error of in wikiTag string:"
										+ inWikiTag);
						return -1;
					}
				} else {
					// 处理没有结果或者有自己的结果情况
					org.jsoup.select.Elements tableElements = searchElement
							.select("table.restable");
					if (tableElements.size() > 0) {
						System.out
								.println("search table size is bigger than zero: "
										+ matchString
										+ ",size:"
										+ tableElements.size());
						org.jsoup.select.Elements trElements = tableElements
								.get(0).select("tr");
						String resLines = trElements.get(0).select("small")
								.text();
						System.out.println("resLines:" + resLines);
						results = Integer.parseInt(resLines.split(" ")[0]);
						int readCount = 0;
						if (results > 0) {
							getResultList(trElements, name, senseList,
									senseTime, results, readCount);
							if (senseList.size() == 0) {
								return 0;
							} else {
								return 1;
							}
						} else {
							return 0;
						}

					} else {
						System.out.println("search table size is zero: "
								+ matchString);
						return 0;
					}
				}
				// org.jsoup.select.Elements locationSenseElements =
				// searchElement.getElementsByTag("tr");

				// System.out.println(locationSenseElements.size());
			} else {
				System.out.println("geoNameSearch2-no searchElement");
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		// return 1;
	}
	*/

	public String icibaSearch(String word) {
		StringBuilder sBuilder = null;

		String urlpath = "http://www.iciba.com/" + word;
		String resultString = new String();
		try {
			URL url = new URL(urlpath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
			conn.setRequestMethod("GET");
			conn.setReadTimeout(6 * 1000);
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				System.out.println("respond is not correct:" + responseCode);
			}
			sBuilder = IOTools.readIO(conn.getInputStream());

			org.jsoup.nodes.Document doc = Jsoup.parse(sBuilder.toString());
			org.jsoup.select.Elements groupDiv = doc.select("div.group_inf");
			if (groupDiv != null && groupDiv.size() > 0) {
				org.jsoup.select.Elements explainElements = groupDiv.get(0)
						.select("a.explain");
				if (explainElements != null && explainElements.size() > 0) {
					resultString = explainElements.get(0).text();
					return resultString;
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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
	public boolean wikipediaSearch(String newTerm) {
		StringBuilder sBuilder = null;
		// comment: "\\"is \,so "\\s" is "\s","\s" is the kind of blank
		// character,like"\n\t..."
		// "\\s,",is "\s" or ","
		String matchString = newTerm.replaceAll("[\\s,.]+", " ");// 把标点变成1个空格，合并连续的空白符
		// ^表示行开头;X(1,),表示X至少1次;$表示行结尾;"\\s"表示空白符
		matchString = matchString.replaceAll("(^\\s{1,})|(\\s{1,}$)", "");// 去掉行开头和结尾的空格
		String wikipediaResult = new String();
		StringBuilder wikipediaTmp = new StringBuilder();
		int liCount = 0;
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
			conn.setReadTimeout(6 * 1000);
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				System.out.println("respond is not correct:" + responseCode);
			}

			sBuilder = IOTools.readIO(conn.getInputStream());
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
								logForWebTools.info("successful match:"
										+ matchString);
								return true;
							} else// ********
							{
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
									logForWebTools.info("successful match:"
											+ wikipediaResult + ";"
											+ matchString);
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
								logForWebTools.info("successful match:"
										+ wikipediaResult + ";" + matchString);
								return true;
							}
							if (i == 2) {
								break;
							}
						}
					}
				}
			} else// noneFound
			{
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logForWebTools.debug("the wikipediaResult:" + wikipediaResult);
		logForWebTools.debug("the matcher:" + matchString);
		// liElements当中没有元素 or 前三个list当中没有完全匹配的
		// 匹配的规则是按照高亮的词，把其em用空格拼接起来，去掉标点
		return false;
	}

	/**
	 * test whether the search works,just generate a html file by the search
	 * 
	 * @param newTerm
	 * @return
	 */
	public boolean googleSearch(String newTerm) {
		BufferedReader bReader = null;
		BufferedWriter bWriter = null;
		String aline = null;
		File outputFile = new File(
				"E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data test/testGoogle.html");
		// goole请求只能使用Get,Post都失败了
		try {
			newTerm = URLEncoder.encode(newTerm, "UTF-8"); // google中的q参数需要对参数进行编码，
			// 否则会出现错误
			String urlpath = "http://www.google.com/search?q=" + newTerm
					+ "&hl=en&&oe=utf-8";

			// String urlParameters = "#newwindow=1&q=" + newTerm +
			// "&safe=strict";
			// https://www.google.com.hk/#newwindow=1&q=Grove+Hills%2C+Alabama&safe=strict
			URL url = new URL(urlpath);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
			// conn.setConnectTimeout(6 * 1000); // �������ӳ�ʱʱ��6s

			conn.setRequestMethod("GET");
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				throw new IOException("respond is not correct:" + responseCode);
			}
			bReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			bWriter = new BufferedWriter(new FileWriter(outputFile));
			while ((aline = bReader.readLine()) != null) {
				bWriter.write(aline);
				bWriter.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			IOTools.closeIO(bReader, bWriter);
		}
		return true;
	}

}
