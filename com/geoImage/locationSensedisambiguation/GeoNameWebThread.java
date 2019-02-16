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

import org.hibernate.SQLQuery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.LocationSenseDuplicate;
import com.geoImage.dao.LocationSenseDuplicateDAO;
import com.geoImage.dao.LocationSenseDuplicateId;
import com.geoImage.dao.MyLocationElement;
import com.geoImage.dao.MyLocationElementId;
import com.geoImage.dao.MyLocationSense;
import com.geoImage.dao.MyLocationSenseDAO;
import com.geoImage.dao.MyLocationSenseId;
import com.geoImage.tools.IOTools;

/**
 * 这个是爬geoNames上的数据，主要是获取My_Location_Sense， 对于LocationSenseId,
 * 0表示查询有结果，但是没有全匹配的；
 * -1表示没有查询到任何结果；-2表示发生网络错误，之后会需要重新查过；
 * -3表示在同一篇文档中和之前的重复，记录在duplicatelocationSense中;
 * -4表示这篇文档没有name，也就是无法有locationSense;
 * 
 * @author hqn
 * @description
 * @version
 * @update 2014-4-4 下午3:24:41
 */
public class GeoNameWebThread implements Runnable {

	private int id;
	private int flag;
	private Object[] wrongObjs;

	public GeoNameWebThread(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @param id
	 * @param flag
	 *            0表示跑正常的，1表示跑错误的;默认是0
	 */
	public GeoNameWebThread(int id, int flag) {
		this.id = id;
		this.flag = flag;
	}

	/**
	 * 
	 * @param wrongObjs
	 * @param flag
	 *            0表示跑正常的，1表示跑错误的;默认是0
	 */
	public GeoNameWebThread(Object[] wrongObjs, int flag) {
		this.flag = flag;
		this.wrongObjs = wrongObjs;
	}

	public GeoNameWebThread() {

	}

	// 多线程跑每个id
	@Override
	public void run() {
		if (flag == 0) {
			getLocationSenseFromWeb(id);
		} else {
			processWriongLocationSense(wrongObjs);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeoNameWebThread gnwt = new GeoNameWebThread();
		// gnwt.deleteWrongResult(28089, 7);
		// 28089
	}

	/**
	 * 第一个是 doc_id,第二个是word_id,第三个是locationOriginalText
	 * @param wrongObj
	 */
	public void processWriongLocationSense(Object[] wrongObj) {
		List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		Map<String, MyLocationSense> senseTime = null;
		String name = "Rainbow Peak";
		String stateName = "Alabama";
		List<Integer> ids = null;
		int i, finalCount = 0;
		List<Object[]> infoList = null;
		Object[] infos = null;
		Map<String, MyLocationElementId> duplicateMap = null;
		MyLocationSenseDAO mlsd = new MyLocationSenseDAO();
		// MyLocationElementDAO mled = new MyLocationElementDAO();
		LocationSenseDuplicateDAO lsdd = new LocationSenseDuplicateDAO();
		ids = new ArrayList<Integer>();
		ids.add(id);
		MyLocationElementId meId = null;

		name = (String) wrongObj[2];
		meId = new MyLocationElementId((Integer) wrongObj[0],
				(Integer) wrongObj[1]);

		senseTime = new HashMap<String, MyLocationSense>();
		duplicateMap = new HashMap<String, MyLocationElementId>();

		// 查询结果为零，则senseId用-1表示。查询有结果，但是匹配的为零，则senseId用0表示
		// 发生网络错误的，用-2来表示
		// 如果在同一篇文档中和之前的重复,senseId用-3来表示，并记入location_sense_duplicate表
		System.out
				.println("processWriongLocationSense-geoNamestart---------search for:"
						+ name);
		MyLocationElementId mlIdDup = null;
		// 对于重复的处理
		if ((mlIdDup = duplicateMap.get(name)) == null) {
			duplicateMap.put(name, meId);
		} else {
			LocationSenseDuplicate lsd = new LocationSenseDuplicate(
					new LocationSenseDuplicateId(meId.getDocId(),
							meId.getWordId()), new MyLocationElement(meId),
					mlIdDup.getWordId(),mlIdDup.getDocId(),name);
			// mled.attachDirty(lsd.getMyLocationElement());
			lsdd.save(lsd);
			// 存取的是其本来的名字
			MyLocationSense mlsTmp = new MyLocationSense(new MyLocationSenseId(
					meId.getDocId(), meId.getWordId(), -3),
					new MyLocationElement(meId));
			mlsTmp.setLocationSenseName(name);
			// mled.attachDirty(mlsTmp.getMyLocationElement());
			mlsd.save(mlsTmp);
			return;
		}
		int b = geoNameSearch(name, senseList, senseTime, meId);
		System.out.println("name:" + name + ",b:" + b);
		if (b == 1) {
			// java.util.Vector<MyLocationSense> v = new
			// java.util.Vector<MyLocationSense>();

			for (Iterator<MyLocationSense> iterator = senseTime.values()
					.iterator(); iterator.hasNext();) {
				MyLocationSense myLocationSense = (MyLocationSense) iterator
						.next();
				senseList.add(myLocationSense);
			}

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
			senseList.add(new MyLocationSense(new MyLocationSenseId(meId
					.getDocId(), meId.getWordId(), -2), new MyLocationElement(
					meId)));
		}
		// 删除掉错误的
		// mlsd.delete(new MyLocationSense(new
		// MyLocationSenseId(meId.getDocId(),
		// meId.getWordId(), -2), new MyLocationElement(meId)));
		deleteWrongResult(meId.getDocId(), meId.getWordId());
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery("");
		for (i = 0; i < senseList.size(); i++) {
			mlsd.save(senseList.get(i));
		}
		System.out.println("processWriongLocationSense-finalResultSize:"
				+ senseList.size());
		senseList.removeAll(senseList);
		senseTime = null;

	}

	/**
	 * 从网络上，对指定id的doc在www.geonames.org上进行查询，并得到结果。
	 * 
	 * @param id
	 *            一个doc_id
	 */
	public void getLocationSenseFromWeb(int id) {
		List<MyLocationSense> senseList = new ArrayList<MyLocationSense>();
		Map<String, MyLocationSense> senseTime = null;
		String name = "Rainbow Peak";
		String stateName = "Alabama";
		List<Integer> ids = null;
		int i, finalCount = 0;
		List<Object[]> infoList = null;
		Object[] infos = null;
		Map<String, MyLocationElementId> duplicateMap = null;
		MyLocationSenseDAO mlsd = new MyLocationSenseDAO();
		LocationSenseDuplicateDAO lsdd = new LocationSenseDuplicateDAO();
		// MyLocationElementDAO mled = new MyLocationElementDAO();
		// ids = IOTools.readIdFromFile(filePath);

		ids = new ArrayList<Integer>();
		ids.add(id);
		MyLocationElementId meId = null;
		for (int j = 0; j < ids.size(); j++) {// 获取docList的大小
			infoList = IOTools.getTravelogueGeoinfoDocIdWordIDLocationText(ids
					.get(j));
			// 获取每个doc有多少个Word_id
			duplicateMap = new HashMap<String, MyLocationElementId>();
			for (int k = 0; k < infoList.size(); k++) {

				infos = infoList.get(k);

				name = (String) infos[3];
				if (name == null) {
					System.out
							.println("getLocationSenseFromWeb-no name in this travelogue:"
									+ name);
					MyLocationSense mlsTmp = new MyLocationSense(
							new MyLocationSenseId((Integer) infos[1],
									(Integer) infos[2], -4),
							new MyLocationElement(new MyLocationElementId(
									(Integer) infos[1], (Integer) infos[2])));
					mlsTmp.setLocationSenseName(name);
					// mled.attachDirty(mlsTmp.getMyLocationElement());
					mlsd.save(mlsTmp);
					continue;
				}
				stateName = getState((String) infos[0]);
				meId = new MyLocationElementId((Integer) infos[1],
						(Integer) infos[2]);
				senseTime = new HashMap<String, MyLocationSense>();
				// 查询结果为零，则senseId用-1表示。查询有结果，但是匹配的为零，则senseId用0表示
				// 发生网络错误的，用-2来表示
				// 如果在同一篇文档中和之前的重复,senseId用-3来表示，并记入location_sense_duplicate表
				System.out
						.println("getLocationSenseFromWeb-geoNamestart---------search for:"
								+ name);
				MyLocationElementId mlIdDup = null;
				// 对于重复的处理
				if ((mlIdDup = duplicateMap.get(name)) == null) {
					duplicateMap.put(name, meId);
				} else {
					LocationSenseDuplicate lsd = new LocationSenseDuplicate(
							new LocationSenseDuplicateId(meId.getDocId(),
									meId.getWordId()), new MyLocationElement(
									meId), mlIdDup.getWordId(),mlIdDup.getDocId(), name);
					// mled.attachDirty(lsd.getMyLocationElement());
					lsdd.save(lsd);
					// 存取的是其本来的名字
					MyLocationSense mlsTmp = new MyLocationSense(
							new MyLocationSenseId(meId.getDocId(),
									meId.getWordId(), -3),
							new MyLocationElement(new MyLocationElementId(
									(Integer) infos[1], (Integer) infos[2])));
					mlsTmp.setLocationSenseName(name);
					// mled.attachDirty(mlsTmp.getMyLocationElement());
					mlsd.save(mlsTmp);
					continue;
				}
				int b = geoNameSearch(name, senseList, senseTime, meId);
				System.out.println("name:" + name + ",b:" + b);
				if (b == 1) {
					// java.util.Vector<MyLocationSense> v = new
					// java.util.Vector<MyLocationSense>();

					for (Iterator<MyLocationSense> iterator = senseTime
							.values().iterator(); iterator.hasNext();) {
						MyLocationSense myLocationSense = (MyLocationSense) iterator
								.next();
						senseList.add(myLocationSense);
					}

					if (senseList.size() > 10) {
						MyLocationSenseComparator comparator = new MyLocationSenseComparator(
								stateName);
						Collections.sort(senseList, comparator);
						int last = 0;
						while ((last = senseList.size()) > 10) {
							senseList.remove(last - 1);
						}
					} else if (senseList.size() == 0) {
						senseList.add(new MyLocationSense(
								new MyLocationSenseId(meId.getDocId(), meId
										.getWordId(), 0),
								new MyLocationElement(meId)));
					}
					finalCount = 0;
					Integer st = 0;
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
					if (finalCount > 0) {
						for (i = 0; i < senseList.size(); i++) {
							MyLocationSense mls = senseList.get(i);
							Double time = 0.0;
							try {
								time = mls.getLocationSenseTime().doubleValue();
							} catch (Exception e) {
								e.printStackTrace();
							}
							mls.setLocationSensePriorProbability(time
									/ finalCount);
						}
					}
				} else if (b == 0) {
					senseList.add(new MyLocationSense(new MyLocationSenseId(
							meId.getDocId(), meId.getWordId(), -1),
							new MyLocationElement(meId)));
				} else {
					senseList.add(new MyLocationSense(new MyLocationSenseId(
							meId.getDocId(), meId.getWordId(), -2),
							new MyLocationElement(meId)));
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
		}
	}

	/**
	 * 获取www.geonames.org/search.html网站上的对应地名地名实体搜索结果。 结果的位置在<div
	 * id="search">标签当中，标签中第一个是 <form name=searchForm />,接着是查询结果.
	 * 结果又如下三种情况：1，从geoNames数据库中能够查询，并且数量小于50个 location sense的地名，比如Rainbow
	 * Peak,5条记录，从restable下，<tbody></tbody> 标签。在<tbody>下，第一个
	 * <tr>
	 * 表示有多少个记录,第二个表示表格的头列，第三行 开始，就是数据了，一直到
	 * <tr class="tfooter">
	 * 标签。
	 * 
	 * 2.数据库中没有记录，wikipedia也没有记录。显示 We have found no places with the name 'hqn';
	 * 
	 * 3.数据库中没有记录，wikipedia有记录，显示： no records found in geonames database,
	 * showing wikipedia results
	 * 
	 * 1.1情况1，记录很多时>50,只取前50条Name匹配的记录(其中匹配加入完全匹配以及有如下四个单词
	 * city,county,town,township的都算完全匹配)，取到满为止。由于一页只能显示50条记录，所以需 要对网页进行翻页
	 * 对于得到的结果，如果唯一的locationSense
	 * 的种类超过10个，则只取10.取10个的优先级别，先取包含有geoInfo州名的，然后再取不包含州名的。
	 * 对于每种唯一的实体，要计算（1）其坐标的平均值，也就是算术平均。（2）其出现的次数
	 * （3）其出现的概率，概率计算公式:出现的次数/结果中总的个数，总的个数就是每种实体个数的累加和。
	 * 
	 * 1.2情况2，记录<=50，取完为止，然后再对结果进行1.1的筛选 相同的记录， 加州的限定。如果有结果，一直取前面相同的
	 * 
	 * 
	 * 
	 * @param word
	 * @return true if the search is normal, false if there are some problem in
	 *         this search 0表示查询结果只有零个,1表示查询结果正常， -1表示查询异常(四次查询都超时，网站服务器出现异常)
	 * 
	 */

	public int geoNameSearch(String word, List<MyLocationSense> senseList,
			Map<String, MyLocationSense> senseTime, MyLocationElementId meID) {
		// if (time == 4) {
		// return -1;
		// }
		
		//去掉首尾空格
		word = word.replaceAll("(^\\s{1,})|(\\s{1,}$)", "");
		//合并空格
		word = word.replaceAll("[\\s,.]+"," ");
		
		String name = word;
		String firstName = "";
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
			htmlRes = IOTools.readIO2(conn.getInputStream());
			if (htmlRes == null) {
				System.out.println("geonames.org-no httpPage");
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
								.println("geoNameSearch-geonames.org-no results in geoname.org");
						return 0;
					} else {
						System.out
								.println("geoNameSearch-some error of in wikiTag string:"
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
						int listCount = 0;
						if (results <= 0) {
							return 0;
						} else {
							if (results <= 50) {
								getResultList(trElements, firstName, senseList,
										senseTime, results, readCount, meID);
								return 1;
							} else // >50
							{
								while (true) {
									readCount = getResultList(trElements,
											firstName, senseList, senseTime,
											results, readCount, meID);
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
								.println("geoNameSearch-cann't find result table"
										+ " table size is zero: " + matchString);
						return -1;
					}
				}
			} else {
				System.out.println("geonames.org-no searchElement");
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		// return 1;
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
	 * 获取一个table当中每一行的数据
	 * 
	 * @param trElements
	 *            包含每行数据的trElements
	 * @param name
	 *            提取地名词第一个逗号前的名词，无则是原词
	 * @param senseList
	 * @param senseTime
	 * @param results
	 * @param count
	 * @param meID
	 * @return
	 */
	public int getResultList(org.jsoup.select.Elements trElements, String name,
			List<MyLocationSense> senseList,
			Map<String, MyLocationSense> senseTime, int results, int count,
			MyLocationElementId meID) {
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
		double la1 = 0.0, lo1 = 0.0, la2 = 0.0, lo2 = 0.0;
		for (int i = 2; i < trElements.size() - 1; i++) {

			tdElements = trElements.get(i).select("td");
			org.jsoup.nodes.Element tdElement = null;
			tdElement = tdElements.get(1);
			// 名字
			tdText = tdElement.select("a[href]").get(0).text();
			// 全匹配，包含county,city,town的都算在内
			//"county","city"也要区分大小写
			if (name.equalsIgnoreCase(tdText)
					|| (tdText.contains(name) && (tdText.contains("county")
							|| tdText.contains("city")
							|| tdText.contains("town")
							|| tdText.contains("township")
							|| tdText.contains("city of") || tdText
								.contains("town of")))) {
				count++; // 有符合匹配条件的，都加起来
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
						locationSense = locationSense + "," + name;
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
							1.0 / count, featureClass, featureDetails);
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
				if (count == 50) {
					break;
				}
			}
		}

		// for (Iterator iterator = senseTime.values().iterator(); iterator
		// .hasNext();) {
		// MyLocationSense myLocationSense = (MyLocationSense) iterator.next();
		// int times = myLocationSense.getLocationSenseTime();
		// myLocationSense.setLocationSensePriorProbability((double) times
		// / count);
		// senseList.add(myLocationSense);
		// }

		return count;
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

	public void deleteWrongResult(int doc_id, int word_id) {
		String sql = "delete from my_location_sense where my_location_sense.doc_id = "
				+ doc_id
				+ " and my_location_sense.word_id="
				+ word_id
				+ " and my_location_sense.locationSenseId=-2";
		SQLQuery sqlQuery = HibernateSessionFactory.getSession()
				.createSQLQuery(sql);
		sqlQuery.executeUpdate();
		return;
	}
}
