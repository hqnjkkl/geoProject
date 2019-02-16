package com.geoImage.locationSensedisambiguation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.MyLocationElementId;
import com.geoImage.tools.IOTools;

/**
 * 负责管理GeoNames上的抓取程序，负责对网络词汇抓取的多线程管理，
 * 对错误处理的多线程管理
 * @author hqn
 * @description   
 * @version
 * @update 2014-4-8 上午11:13:48
 */
public class LocationSenseScore {

	public static int dataJump = 10000;
	public static int poolSize = 20;
	String filePath = "E:/hqn/新项目初识/图片地理位置研究/第十次会议(2014-3-28)/Waikiki beachid_doc_list.txt";
	GeoNameDistributeClient gdc = null;
	int distributeId;

	public LocationSenseScore() {
		gdc = new GeoNameDistributeClient();
		distributeId = gdc.getIds();
	}

	public static void main(String[] args) {
		LocationSenseScore lss = new LocationSenseScore();
		// lss.getLocationSenseFromWeb();
		// lss.testForList();
		// lss.multipleThredForGeoNames();
		// lss.distributeProcess();
		// System.out.println(IOTools.getNotProcessedDoc(5000).size());
		lss.distributeProcess();
	}

	public void testForList() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(3);
		System.out.println(list.size());
		list.removeAll(list);
		System.out.println(list.size());
		MyLocationElementId meId = new MyLocationElementId(1, 2);
		System.out.println(meId);
	}

	public void distributeProcess() {
		multipleThredForGeoNames();
		wrongMultipleThread();
	}

	/**
	 * 负责处理locationSenseId为-2的单词，分10个多线程跑，每个线程处理一个单词
	 * 所有逻辑总共循环十遍
	 */
	public void wrongMultipleThread() {
		List<Object[]> wrongList = null;
		ExecutorService pool = null;
		int i, j = 0;
		pool = Executors.newFixedThreadPool(LocationSenseScore.poolSize);
		int count = 0;
		while (true) {
			//需要加入一个geoInfo也可以仿照webSearch
			String wrongSense = "select mls.doc_id,mls.word_id,mle.locationOriginalText from"
					+ " my_location_sense mls,my_location_element mle where mls.locationSenseId = -2 "
					+ "and mls.word_id=mle.word_id and mls.doc_id = mle.doc_id and "
					+ "mls.doc_id <"
					+ distributeId
					+ " and mls.doc_id >="
					+ (distributeId - dataJump);

			wrongList = IOTools.getWrongSenseId(wrongSense);

			if (wrongList == null) {
				return;
			} else {
				if (wrongList.size() == 0) {
					break;
				}
				System.out.println(wrongList.size());
				for (i = 0; i < wrongList.size(); i++) {
					GeoNameWebSearch gnwt = new GeoNameWebSearch(
							wrongList.get(i), 1);
					pool.execute(gnwt);
					System.out.println("processed: wrong: " + i);
					if (i % LocationSenseScore.poolSize == 0) {
						pool.shutdown();
						while (true) {
							if (pool.isTerminated()) {
								break;
							}
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						j++;
						if (j % 5 == 0) {
							HibernateSessionFactory.getSession().flush();
							HibernateSessionFactory.getSession().clear();
						}
						System.out.println("wrongMultipleThread-clear hibernate session!");
						pool = Executors.newFixedThreadPool(LocationSenseScore.poolSize);
					}
					// gnwt.processWriongLocationSense(wrongList.get(i));
				}
				count++;
				if (count >= 10) {
					break;
				}
			}
			count++;
			if (count >= 15) {
				break;
			}
		}
	}

	/**
	 * 根据所分配的id，在[id-5000,id)范围内，循环跑数据，如果跑完了就好，或者超过10次就退出
	 */
	public void multipleThredForGeoNames() {
		ExecutorService pool = null;
		int i, j = 0;
		pool = Executors.newFixedThreadPool(LocationSenseScore.poolSize);
		List<Integer> ids = null;
		System.out.println("test for multiple thread-multipleThredForGeoNames");
		// while(true)
		// {
		int disCount = 0;
		while (true) {
			disCount++;
			if (disCount >= 10) {
				break;
			}
			ids = IOTools.getNotProcessedDoc(distributeId);
			if (ids.size() == 0) {
				break;
			}
			System.out.println("count:" + disCount + ",size:" + ids.size());
			for (i = 0; i < ids.size(); i++) {
				Integer id = ids.get(i);
				GeoNameWebSearch gnwt = new GeoNameWebSearch(id);
				Thread thread = new Thread(gnwt);
				pool.execute(thread);
				// newYork 3000个,一次20个,150次,一次10分钟,1500分钟
				System.out.println("processed " + i);
				if ((i + 1) % LocationSenseScore.poolSize == 0) {
					j++;
					pool.shutdown();
					while (true) {
						if (pool.isTerminated()) {
							break;
						}
						// 没有完全执行完，就睡眠
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (j % 5 == 0) {
						HibernateSessionFactory.getSession().flush();
						HibernateSessionFactory.getSession().clear();
					}
					System.out.println("multipleThredForGeoNames-clear hibernate session!");
					pool = Executors.newFixedThreadPool(LocationSenseScore.poolSize);
				}
			}
			pool.shutdown();
		}
	}
	// }
}
