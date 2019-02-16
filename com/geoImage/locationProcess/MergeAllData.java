package com.geoImage.locationProcess;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;

/**
 * 把除了五大州（California，NewYork，Florida，
 * Washington，Hawaii）的其他州的地名，也进行地名词提取
 * 
 * @author hqn
 * @description   
 * @version
 * @update 2014-4-21 上午9:41:47
 */
public class MergeAllData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MergeAllData mad = new MergeAllData();
		mad.testMultiple();
	}
	
	/**
	 * 把数据库中的doc_id分成若干小段，每一段用多线程跑数据，并且真个流程重复20次，以便
	 * 处理错误的情况
	 */
	public void testMultiple() {
		/**
		 * select count(*) from travelogue_notmp  tnt where tnt.doc_id<25000; //hqn
		select count(*) from travelogue_notmp  tnt where tnt.doc_id>=25000 and tnt.doc_id<50000;//zz
 		select count(*) from travelogue_notmp  tnt where tnt.doc_id>=50000;//wzj
		 */
		/**
		 * select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id <10000;

select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=10000 and tnt.doc_id <20000;

select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=20000 and tnt.doc_id <25000;



select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=25000 and tnt.doc_id <35000;

select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=35000 and tnt.doc_id <45000;

select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=45000 and tnt.doc_id <50000;



select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=50000 and tnt.doc_id <60000;

select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=60000 and tnt.doc_id <70000;

select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in
(select mle.doc_id from my_location_element mle) and tnt.doc_id >=70000;
		 */
		
		String[] sqlStrings = {
				 "select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in"+
				 "(select mle.doc_id from my_location_element mle) and tnt.doc_id <10000",
				 "select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in"+
				 "(select mle.doc_id from my_location_element mle) and tnt.doc_id >=10000 and tnt.doc_id <20000",
				"select tnt.doc_id from travelogue_notmp tnt where tnt.doc_id not in"+
				 "(select mle.doc_id from my_location_element mle) and tnt.doc_id >=20000 and tnt.doc_id <25000"};
		int count2 = 0;
		while (true) {
			count2++;
			for (int i = 0; i < sqlStrings.length; i++) {
					SQLQuery sqlQuery = HibernateSessionFactory.getSession()
							.createSQLQuery(sqlStrings[i]);
					List<Integer> idIntegers = sqlQuery.list();
					if (idIntegers.size() > 0) {
						
						testAllThreadPool(idIntegers);
					} else {
						System.out.println("idIntegers.size:"+idIntegers.size());
						continue;
					}
			}
			System.out.println("mutiple thread count:"+count2);
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count2 > 20) {
				break;
			}

		}

	}
	
	/**
	 * 整个程序多线程，跑，也是主程序
	 */
	public void testAllThreadPool(List<Integer> idIntegers) {
		int i, j = 0, size;
		ExecutorService pool = null;
		//CachedThreadPool会重用之前的线程，并且线程停止60秒后，就会回收这个线程。
		//pool = Executors.newCachedThreadPool();
		//pool = Executors.newFixedThreadPool(nThreads)
		//只会保证一个线程
//		pool = Executors.newSingleThre
		pool = Executors.newFixedThreadPool(10);

		// 收尾处理

		i = 0;
		// int j = 0;
		size = idIntegers.size();
		System.out.println(size);
		int large = 0;
		for (i = 0; i < idIntegers.size(); i++) {
			Integer id = idIntegers.get(i);
			MergeAlgorithm ma = new MergeAlgorithm(id);
			Thread thread = new Thread(ma);
			pool.execute(ma);
			// newYork 3000个,一次20个,150次,一次10分钟,1500分钟
			System.out.println("processed " + i);
			if ((i + 1) % 3 == 0) {
				j++;
				//不允许加入再execute新的线程，当所有任务执行完之后，尽快退出。
				pool.shutdown();
				while (true) {
					//判断是否所有任务都执行完了
					if (pool.isTerminated()) {
						break;
					}
					// 没有完全执行完，就睡眠
					try {
						Thread.currentThread().sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (j % 5 == 0) {
					HibernateSessionFactory.getSession().flush();
					HibernateSessionFactory.getSession().clear();
				}
				pool = Executors.newFixedThreadPool(10);
			}
		}
		pool.shutdown();
	}

}
