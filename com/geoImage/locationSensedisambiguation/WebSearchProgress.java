package com.geoImage.locationSensedisambiguation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.geoImage.dao.MyGeoInfo;
import com.geoImage.dao.MyGeoInfoDAO;
import com.geoImage.dao.MyLocationElement;
import com.geoImage.dao.MyLocationElementId;
import com.geoImage.dao.MyLocationSense;
import com.geoImage.dao.MyLocationSenseDAO;
import com.geoImage.dao.MyLocationSenseId;
import com.geoImage.tools.IOTools;

/**
 * 纯粹在网络上抓取数据
 * 
 * @author hqn
 * @description
 * @version
 * @update 2014-4-9 下午3:47:14
 */
public class WebSearchProgress {

	public WebSearchProgress() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// testSaveWebSearchResult();
	}

	/**
	 * 把MyLocationElement的某个数据在网络上查询，得到其MyLocationSense的结果,
	 * 如果网络查询为0，或者能够匹配的为零，就进行如下判断
	 * 如果在这个geoInfo下这个name出现了3次或者以上，就把geoInfo的信息移植到这个单词的信息当中
	 * 
	 * @param name
	 *            MyLocationElement的名字
	 * @param meId
	 *            MyLocationElement的Id
	 * @param geoInfo
	 *            MyLocationElement对应的geoInfo
	 * @param duplicate
	 *            在geoInfo下有几个相同的
	 */
	public void getSearchResult2(String name, MyLocationElementId meId,
			String geoInfo, int duplicate) {
		Map<String, MyLocationSense> senseTime = new HashMap<String, MyLocationSense>();
		List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		MyLocationSenseDAO mlsd = new MyLocationSenseDAO();
		MyGeoInfo mgi = null;
		MyGeoInfoDAO mgid = new MyGeoInfoDAO();
		String stateName;
		int finalCount = 0;
		int i;

		// name = (String) infos[3];
		// 名词为空的，统一处理

		stateName = getState(geoInfo);
		// meId = new MyLocationElementId((Integer) infos[1],
		// (Integer) infos[2]);
		senseTime = new HashMap<String, MyLocationSense>();
		// 查询结果为零，则senseId用-1表示。查询有结果，但是匹配的为零，则senseId用0表示
		// 发生网络错误的，用-2来表示
		// 如果在同一篇文档中和之前的重复,senseId用-3来表示，并记入location_sense_duplicate表
		System.out
				.println("getLocationSenseFromWeb-geoNamestart---------search for:"
						+ name);
		// MyLocationElementId mlIdDup = null;

		int b = searchNameOnGeoNames(name, senseTime, meId);

		System.out.println("name:" + name + ",b:" + b);
		if (b == 1) {
			// 把元素插入list当中
			for (Iterator<MyLocationSense> iterator = senseTime.values()
					.iterator(); iterator.hasNext();) {
				MyLocationSense myLocationSense = (MyLocationSense) iterator
						.next();
				senseList.add(myLocationSense);
			}
			// 删除大于10的元素
			if (senseList.size() > 10) {
				MyLocationSenseComparator comparator = new MyLocationSenseComparator(
						stateName);
				Collections.sort(senseList, comparator);
				int last = 0;
				while ((last = senseList.size()) > 10) {
					senseList.remove(last - 1);
				}
			} else if (senseList.size() == 0) {
				if (duplicate > 2) {// 把geoInfo的信息移植到这个locationSense上来,他也是唯一的一个
					mgi = mgid.findById(geoInfo);
					// 概率还要另外算,通过来在文档中出现的次数进行计算
					senseList.add(new MyLocationSense(new MyLocationSenseId(
							meId.getDocId(), meId.getWordId(), 1),
							new MyLocationElement(meId), mgi.getGeoInfoSense(),
							mgi.getLatitude(), mgi.getLongitude(), 1, 1.0, mgi
									.getGeoInfoFeature(), mgi
									.getGeoInfoFeatureDetails()));
					
				} else {
					senseList.add(new MyLocationSense(new MyLocationSenseId(
							meId.getDocId(), meId.getWordId(), 0),
							new MyLocationElement(meId)));
				}

			}
			finalCount = 0;
			Integer st = 0;
			// 计算经纬度的平均值
			for (i = 0; i < senseList.size(); i++) {
				MyLocationSense mls = senseList.get(i);
				st = mls.getLocationSenseTime();
				if (st == null) {
					// 对于0个的，除了Id和外键关联对象，其他都是null.把null赋值给基本数据类型，会报错
					continue;
				}
				mls.setLatitude(mls.getLatitude() / st);
				mls.setLongitude(mls.getLongitude() / st);
				finalCount += mls.getLocationSenseTime();
			}
			// 计算概率
			if (finalCount > 0) {
				for (i = 0; i < senseList.size(); i++) {
					MyLocationSense mls = senseList.get(i);
					Double time = 0.0;
					try {
						time = mls.getLocationSenseTime().doubleValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					mls.setLocationSensePriorProbability(time / finalCount);
				}
			}
		} else if (b == 0) {
			if (duplicate > 2) {
				// 把geoInfo的信息移植到这个locationSense上来,他也是唯一的一个
				mgi = mgid.findById(geoInfo);
				// 概率还要另外算,通过来在文档中出现的次数进行计算
				senseList.add(new MyLocationSense(new MyLocationSenseId(meId
						.getDocId(), meId.getWordId(), 1),
						new MyLocationElement(meId), mgi.getGeoInfoSense(), mgi
								.getLatitude(), mgi.getLongitude(), 1, 1.0, mgi
								.getGeoInfoFeature(), mgi
								.getGeoInfoFeatureDetails()));
			} else {
				senseList.add(new MyLocationSense(new MyLocationSenseId(meId
						.getDocId(), meId.getWordId(), -1),
						new MyLocationElement(meId)));
			}
		} else {
			// -2发生错误了
			senseList.add(new MyLocationSense(new MyLocationSenseId(meId
					.getDocId(), meId.getWordId(), -2), new MyLocationElement(
					meId)));
		}
		
		for (i = 0; i < senseList.size(); i++) {
			// mled.attachDirty(senseList.get(i).getMyLocationElement());
			mlsd.save(senseList.get(i));
		}
		System.out.println("getLocationSenseFromWeb-finalResultSize:"
				+ senseList.size());
		senseList.removeAll(senseList);
		senseTime = null;
	}

	/**
	 * 把MyLocationElement的某个数据在网络上查询，得到其MyLocationSense的结果 然后把结果存入数据库
	 * 
	 * @param name
	 *            MyLocationElement的名字
	 * @param meId
	 *            MyLocationElement的Id
	 * @param geoInfo
	 *            MyLocationElement对应的geoInfo
	 */
	public void getSearchResult(String name, MyLocationElementId meId,
			String geoInfo) {
		Map<String, MyLocationSense> senseTime = new HashMap<String, MyLocationSense>();
		List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		MyLocationSenseDAO mlsd = new MyLocationSenseDAO();
		String stateName;
		int finalCount = 0;
		int i;

		// name = (String) infos[3];
		// 名词为空的，统一处理

		stateName = getState(geoInfo);
		// meId = new MyLocationElementId((Integer) infos[1],
		// (Integer) infos[2]);
		senseTime = new HashMap<String, MyLocationSense>();
		// 查询结果为零，则senseId用-1表示。查询有结果，但是匹配的为零，则senseId用0表示
		// 发生网络错误的，用-2来表示
		// 如果在同一篇文档中和之前的重复,senseId用-3来表示，并记入location_sense_duplicate表
		System.out
				.println("getLocationSenseFromWeb-geoNamestart---------search for:"
						+ name);
		// MyLocationElementId mlIdDup = null;

		int b = searchNameOnGeoNames(name, senseTime, meId);
		System.out.println("name:" + name + ",b:" + b);
		if (b == 1) {
			// 把元素插入list当中
			for (Iterator<MyLocationSense> iterator = senseTime.values()
					.iterator(); iterator.hasNext();) {
				MyLocationSense myLocationSense = (MyLocationSense) iterator
						.next();
				senseList.add(myLocationSense);
			}
			// 删除大于10的元素
			if (senseList.size() > 10) {
				MyLocationSenseComparator comparator = new MyLocationSenseComparator(
						stateName);
				Collections.sort(senseList, comparator);
				int last = 0;
				while ((last = senseList.size()) > 10) {
					senseList.remove(last - 1);
				}
			} else if (senseList.size() == 0) {
				senseList.add(new MyLocationSense(new MyLocationSenseId(meId
						.getDocId(), meId.getWordId(), 0),
						new MyLocationElement(meId)));
			}
			finalCount = 0;
			Integer st = 0;
			// 计算经纬度的平均值
			for (i = 0; i < senseList.size(); i++) {
				MyLocationSense mls = senseList.get(i);
				st = mls.getLocationSenseTime();
				if (st == null) {
					// 对于0个的，除了Id和外键关联对象，其他都是null.把null赋值给基本数据类型，会报错
					continue;
				}
				mls.setLatitude(mls.getLatitude() / st);
				mls.setLongitude(mls.getLongitude() / st);
				finalCount += mls.getLocationSenseTime();
			}
			// 计算概率
			if (finalCount > 0) {
				for (i = 0; i < senseList.size(); i++) {
					MyLocationSense mls = senseList.get(i);
					Double time = 0.0;
					try {
						time = mls.getLocationSenseTime().doubleValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					mls.setLocationSensePriorProbability(time / finalCount);
				}
			}
		} else if (b == 0) {
			senseList.add(new MyLocationSense(new MyLocationSenseId(meId
					.getDocId(), meId.getWordId(), -1), new MyLocationElement(
					meId)));
		} else {
			// -2发生错误了
			senseList.add(new MyLocationSense(new MyLocationSenseId(meId
					.getDocId(), meId.getWordId(), -2), new MyLocationElement(
					meId)));
		}
		for (i = 0; i < senseList.size(); i++) {
			// mled.attachDirty(senseList.get(i).getMyLocationElement());
			mlsd.save(senseList.get(i));
		}
		System.out.println("getLocationSenseFromWeb-finalResultSize:"
				+ senseList.size());
		senseList.removeAll(senseList);
		senseTime = null;
	}

	/**
	 * 根据word在http://www.geonames.org/网站上进行地名查询
	 * 
	 * @param word
	 * @param senseTime
	 * @param meID
	 * @return
	 */
	public int searchNameOnGeoNames(String word,
			Map<String, MyLocationSense> senseTime, MyLocationElementId meID) {
		// if (time == 4) {
		// return -1;
		// }
		String name = word;
		String firstName = "";
		// 之后会把firstName加入地名实体当中
		if (name.contains(",")) {
			firstName = getFirstName(name);
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
			word = URLEncoder.encode(firstName, "UTF-8");
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
						int readCount = 0;
						int listCount = 0;
						if (results <= 0) {
							return 0;
						} else {
							if (results <= 50) {
								getResultList(trElements, firstName, senseTime,
										results, readCount, meID);
								return 1;
							} else // >50
							{
								while (true) {
									readCount = getResultList(trElements,
											firstName, senseTime, results,
											readCount, meID);
									System.out
											.println("geoNameSearch-readCount:"
													+ readCount + ",pageCount:"
													+ listCount);
									// 实体个数超过10个，readCount>=50就不继续读下去了
									listCount += 50;
									// 之前小于pageCount +50的已经读过了
									if (listCount >= results || readCount >= 50
											|| listCount > 1000) {
										break;
									}
									String startRow = "&startRow=" + listCount;
									trElements = getTrElementsOfGeoName(urlPath
											+ startRow);
									if (trElements == null) {
										return -1;
									}
								}
								return 1;
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
	 * @return 已经获取的结果数
	 */
	public int getGeoInfoResultList(org.jsoup.select.Elements trElements,
			String firstName, Map<String, MyLocationSense> senseTime,
			int results, int readCount, MyLocationElementId meID) {
		String tdText = null;
		org.jsoup.select.Elements tdElements = null;
		String locationSense = null;
		// List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		// senseTime = new HashMap<String, MyLocationSense>();

		System.out.println("trElements:" + trElements.size());
		int j = senseTime.size(); // j是LocationSense的id
		int featureNodesSize = 0;
		String latitudeString = null;
		String longitudeString = null;
		String featureClass = null;
		String featureDetails = null;
		Element fD = null;
		String nameCopy = null;
		String tdTextCopy = null;

		double la1 = 0.0, lo1 = 0.0, la2 = 0.0, lo2 = 0.0;
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
			if (nameCopy.equals(tdTextCopy)
					|| (tdTextCopy.contains(firstName) && (tdTextCopy
							.contains("county")
							|| tdTextCopy.contains("city")
							|| tdTextCopy.contains("town")
							|| tdTextCopy.contains("township")
							|| tdTextCopy.contains("city of") || tdTextCopy
								.contains("town of")))) {
				readCount++; // 有符合匹配条件的，都加起来
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
				}

				if (senseTime.get(locationSense) == null) {
					j++;// 第几个不同的
					MyLocationSense mls = new MyLocationSense(
							new MyLocationSenseId(meID.getDocId(),
									meID.getWordId(), j),
							new MyLocationElement(meID), locationSense,
							Double.parseDouble(latitudeString),
							Double.parseDouble(longitudeString), 1,
							1.0 / readCount, featureClass, featureDetails);
					senseTime.put(locationSense, mls);

				} else {
					MyLocationSense mls = senseTime.get(locationSense);
					int mlsTime = mls.getLocationSenseTime() + 1;
					// 经纬度累加，最后再求平均
					la1 = Double.parseDouble(latitudeString);
					lo1 = Double.parseDouble(longitudeString);
					la2 = mls.getLatitude();
					lo2 = mls.getLongitude();
					mls.setLocationSenseTime(mlsTime);
					mls.setLatitude(la1 + la2);
					mls.setLongitude(lo1 + lo2);
					// mls.setLocationSensePriorProbability((double) mlsTime
					// / count);
				}
				if (readCount == 50) {
					break;
				}
			}
		}

		return readCount;
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
	 * @return 已经获取的结果数
	 */
	public int getResultList(org.jsoup.select.Elements trElements,
			String firstName, Map<String, MyLocationSense> senseTime,
			int results, int readCount, MyLocationElementId meID) {
		String tdText = null;
		org.jsoup.select.Elements tdElements = null;
		String locationSense = null;
		// List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		// senseTime = new HashMap<String, MyLocationSense>();

		System.out.println("trElements:" + trElements.size());
		int j = senseTime.size(); // j是LocationSense的id
		int featureNodesSize = 0;
		String latitudeString = null;
		String longitudeString = null;
		String featureClass = null;
		String featureDetails = null;
		Element fD = null;
		String nameCopy = null;
		String tdTextCopy = null;

		double la1 = 0.0, lo1 = 0.0, la2 = 0.0, lo2 = 0.0;
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
			if (nameCopy.equals(tdTextCopy)
					|| (tdTextCopy.contains(firstName) && (tdTextCopy
							.contains("county")
							|| tdTextCopy.contains("city")
							|| tdTextCopy.contains("town")
							|| tdTextCopy.contains("township")
							|| tdTextCopy.contains("city of") || tdTextCopy
								.contains("town of")))) {
				readCount++; // 有符合匹配条件的，都加起来
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
				}

				if (senseTime.get(locationSense) == null) {
					j++;// 第几个不同的
					MyLocationSense mls = new MyLocationSense(
							new MyLocationSenseId(meID.getDocId(),
									meID.getWordId(), j),
							new MyLocationElement(meID), locationSense,
							Double.parseDouble(latitudeString),
							Double.parseDouble(longitudeString), 1,
							1.0 / readCount, featureClass, featureDetails);
					senseTime.put(locationSense, mls);

				} else {
					MyLocationSense mls = senseTime.get(locationSense);
					int mlsTime = mls.getLocationSenseTime() + 1;
					// 经纬度累加，最后再求平均
					la1 = Double.parseDouble(latitudeString);
					lo1 = Double.parseDouble(longitudeString);
					la2 = mls.getLatitude();
					lo2 = mls.getLongitude();
					mls.setLocationSenseTime(mlsTime);
					mls.setLatitude(la1 + la2);
					mls.setLongitude(lo1 + lo2);
					// mls.setLocationSensePriorProbability((double) mlsTime
					// / count);
				}
				if (readCount == 50) {
					break;
				}
			}
		}

		return readCount;
	}

	public org.jsoup.select.Elements getTrElementsOfGeoName(String urlPath) {
		URL url = null;
		HttpURLConnection conn = null;
		int reconnect = 0;
		StringBuilder htmlRes = null;
		org.jsoup.select.Elements trElements = null;
		try {
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
					System.out.println("geonames.org-respond is not correct:"
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

			BufferedReader bReader = null;
			String aline = null;
			StringBuilder result = new StringBuilder();
			bReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()), 2 * 1024 * 1024);
			// int count = 0;
			while ((aline = bReader.readLine()) != null) {
				result.append(aline);
				// count++;
			}
			// System.out.println(count);
			IOTools.closeIO(bReader, null);
			htmlRes = result;

			// 可能超过5000就没法继续查看了
			org.jsoup.nodes.Document doc = Jsoup.parse(htmlRes.toString());
			org.jsoup.select.Elements divSearchs = doc.select("div#search");
			if (divSearchs.size() > 0) {
				org.jsoup.nodes.Element searchElement = divSearchs.get(0);
				org.jsoup.select.Elements tableElements = searchElement
						.select("table.restable");
				if (tableElements.size() > 0) {
					trElements = tableElements.get(0).select("tr");
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getTrElementsOfGeoName-get next page Error");
			return null;
		}
		return trElements;
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

	/**
	 * 把地理信息中的州名提取出来，输入是MyLocationElement中的geoInfo的格式 倒数第一个逗号到倒数第二个逗号之间的名词，是一个州名
	 * Dauphin Island, Alabama, United States 提取Alabama
	 * 
	 * @param geoInfo
	 * @return 返回州名
	 */
	public String getState(String geoInfo) {
		int l1, l2;
		l2 = geoInfo.lastIndexOf(",");
		l1 = geoInfo.lastIndexOf(",", l2 - 1);
		String subString = geoInfo.substring(l1 + 1, l2);
		return subString;
	}

}
