package com.geoImage.locationSensedisambiguation;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.geoImage.tools.IOTools;

public class WebSearchForGeoInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WebSearchForGeoInfo wspfgi =  new WebSearchForGeoInfo();
		wspfgi.testSearchGeoInfoOnGeoNames();
	}
	
	
	public void testSearchGeoInfoOnGeoNames()
	{
		String word = " Honolulu,    Hawaii";
		int res =0;
		//去掉首尾空格
		word = word.replaceAll("(^\\s{1,})|(\\s{1,}$)", "");
		//合并空格
		word = word.replaceAll("[\\s]+"," ");
		System.out.println(word);
		Map<String, Object[]> senseTime = new HashMap<String, Object[]>();
		res = searchGeoInfoOnGeoNames(word, senseTime);
		System.out.println("senseSize:"+senseTime.size());
		System.out.println(res);
	}
	
	
	/**
	 * 根据word在http://www.geonames.org/网站上进行地名查询
	 * 这个是针对geoInfo中拆分出来的单词，在http://www.geonames.org/网站上查询，不需要
	 * 进行翻页,只在第一页，也就是前50个，找到第一个完全匹配的就返回结果
	 * 
	 * @param word 这个word是geoInfo拆分之后的地名加上对应的州名
	 * @param senseTime 
	 *  key是String,是单个的单词
	 *  value是objects,顺序如下：locationSense,latitudeString,longitudeString,featureClass,featureDetails
	 * @return 0：没找到。1找到匹配，-1有错误;
	 */
	public int searchGeoInfoOnGeoNames(String word,
			Map<String, Object[]> senseTime) {
		// if (time == 4) {
		// return -1;
		// }
		String name = word;
		String firstName = "";
		// 之后会把firstName加入地名实体当中
		if (name.contains(",")) {
			firstName = getFirstName(name);
			System.out.println("have comma:" + word);
		} else {
			firstName = name;
		}
		String matchString = new String(word);
		String urlPath = null;
		String inWikiTag = null;
		// List<MyLocationSense> senseList = null;

		int results = 0;
		// String result = null;
		StringBuilder htmlRes = null;
		int reconnect = 0;
		URL url = null;
		HttpURLConnection conn = null;
		try {
			// 用逗号前的单词来查询
			word = URLEncoder.encode(word, "UTF-8");
			// while(true)
			// {
			//
			// }
			urlPath = "http://www.geonames.org/search.html?q=" + word
					+ "&country=US";
			url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11 QIHU 360EE");
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("GET");
			conn.setReadTimeout(6 * 1000);
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				while (responseCode != 200) {
					reconnect++;
					System.out
							.println("searchNameOnGeoNames-respond is not correct:"
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
			htmlRes = IOTools.readIO2(conn.getInputStream());
			if (htmlRes == null) {
				System.out.println("searchNameOnGeoNames-no httpPage");
				return -1;
			}
			org.jsoup.nodes.Document doc = Jsoup.parse(htmlRes.toString());
			org.jsoup.nodes.Element searchElement = doc.select("div#search")
					.get(0);
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
								.println("searchNameOnGeoNames-geonames.org-no results in geoname.org");
						return 0;
					} else {
						System.out
								.println("searchNameOnGeoNames-some error of in wikiTag string:"
										+ inWikiTag);
						return -1;
					}
				} else {
					// 处理没有结果或者有自己的结果情况
					org.jsoup.select.Elements tableElements = searchElement
							.select("table.restable");
					if (tableElements.size() > 0) {
						System.out
								.println("searchNameOnGeoNames-search table size is bigger than zero: "
										+ matchString
										+ ",size:"
										+ tableElements.size());
						org.jsoup.select.Elements trElements = tableElements
								.get(0).select("tr");
						String resLines = trElements.get(0).select("small")
								.text();
						System.out.println("searchNameOnGeoNames-resLines:"
								+ resLines);
						results = Integer.parseInt(resLines.split(" ")[0]);
						if (results <= 0) {
							return 0;
						} else {
							int res = getGeoInfoResultList(trElements,
									firstName, senseTime);
							if(res==1)
							{
								return 1;
							}else {
								return 0;
							}
						}

					} else {
						// 错误判断
						Element formElement = searchElement
								.select("form[name]").get(0);
						Node nonNode = formElement.nextSibling();
						String nonString = nonNode.toString();
						if (nonString.contains("no places with")) {
							return 0;
						}
						System.out
								.println("searchNameOnGeoNames-cann't find result table"
										+ " table size is zero: " + matchString);
						return -1;
					}
				}
			} else {
				System.out.println("searchNameOnGeoNames-no searchElement");
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 获取一个网页中的结果
	 * 
	 * @param trElements
	 * @param firstName
	 * @param senseTime
	 * @param results
	 * @param readCount
	 * @param meID
	 * @return 是否获取结果：0,没有匹配结果；1，有结果；
	 */
	public int getGeoInfoResultList(org.jsoup.select.Elements trElements,
			String firstName, Map<String, Object[]> senseTime) {
		String tdText = null;
		org.jsoup.select.Elements tdElements = null;
		String locationSense = null;
		// List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		// senseTime = new HashMap<String, MyLocationSense>();

		System.out.println("trElements:" + trElements.size());
		int featureNodesSize = 0;
		String latitudeString = null;
		String longitudeString = null;
		String featureClass = null;
		String featureDetails = null;
		Element fD = null;
		String nameCopy = null;
		String tdTextCopy = null;

		for (int i = 2; i < trElements.size() - 1; i++) {

			tdElements = trElements.get(i).select("td");
			org.jsoup.nodes.Element tdElement = null;
			tdElement = tdElements.get(1);
			// 名字
			tdText = tdElement.select("a[href]").get(0).text();
			// 全匹配，包含county,city,town的都算在内
			// "county","city"也要区分大小写
			nameCopy = firstName.toLowerCase();
			// 去除网页中的单引号
			tdTextCopy = tdText.replaceAll("'", "").toLowerCase();
			
			//一旦有匹配的就
			if (nameCopy.equals(tdTextCopy)
					|| (tdTextCopy.contains(firstName) && (tdTextCopy
							.contains("county")
							|| tdTextCopy.contains("city")
							|| tdTextCopy.contains("town")
							|| tdTextCopy.contains("township")
							|| tdTextCopy.contains("city of") || tdTextCopy
								.contains("town of")))) {
				// 经度纬度
				latitudeString = tdElement.select("span.latitude").get(0)
						.text();
				longitudeString = tdElement.select("span.longitude").get(0)
						.text();

				tdElement = tdElements.get(2);
				Elements aHrefs = tdElement.select("a[href]");
				if (aHrefs.size() > 0) {
					Element hrefElement = aHrefs.get(0);
					locationSense = hrefElement.text();
					// 实体名称
					Element nextSub = hrefElement.nextElementSibling();
					if (nextSub != null) {
						locationSense = locationSense
								+ hrefElement.nextSibling().toString();
					}
					org.jsoup.select.Elements smalls = tdElement
							.select("small");
					if (smalls.size() > 0) {
						String small = tdElement.select("small").get(0).text();
						if (small != null && small != "") {
							locationSense = locationSense + "," + small;
						}
						locationSense = locationSense + "," + firstName;
					} else {
						locationSense = tdElement.text();
					}
					
				}

					tdElement = tdElements.get(3);
					List<org.jsoup.nodes.Node> featureNodes = tdElement
							.childNodes();
					// System.out.println("feature size:"+featureNodes.size());
					/**
					 * <td>administrative division <br>
					 * <small>population 180,105, elevation 172m</small></td>
					 */

					// for (int k = 0; k < featureNodes.size(); k++) {
					// System.out.println(featureNodes.get(k).toString());
					// }
					featureNodesSize = featureNodes.size();
					// 有些feature没有featureDetails，加入判断
					if (0 < featureNodesSize) {
						featureClass = featureNodes.get(0).toString();
						if (featureClass.equals("<br />")) {
							featureClass = null;
						}
					}
					if (2 < featureNodesSize) {
						fD = (Element) featureNodes.get(2);
						featureDetails = fD.text();
					}					
						Object[] objects = new Object[]{locationSense,latitudeString,longitudeString,featureClass,featureDetails};
						senseTime.put(firstName, objects);
				return 1;
			}
		}

		return 0;
	}

	
	
	/**
	 * 获取名字的第一个词，通常是最小的那个词
	 * 
	 * @param geoInfo
	 * @return
	 */
	public String getFirstName(String geoInfo) {
		int l1;
		l1 = geoInfo.indexOf(",");
		String subString = geoInfo.substring(0, l1);
		return subString;
	}

}
