package com.geoImage.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.MyLocationElement;
import com.geoImage.locationProcess.LocationElement;
import com.geoImage.locationProcess.MergeAlgorithm;
import com.geoImage.tools.IOTools;

public class TestForMergeAlgorithm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Object o;
		// MergeAlgorithm ma1 = new MergeAlgorithm();
		// ma.getDataStructure(1);
		// LocationElement integrityResult = null;
		// ma1.initialIntegerityMap();
		// ma1.initialLocalTerm();
		TestForMergeAlgorithm tfa = new TestForMergeAlgorithm();
		// tfa.testForMultiThread();
		// tfa.testForPResultTest();
		// tfa.testForIntegrity();
		// tfa.testForMultiThread();
		
		// System.getProperties().setProperty("http.proxyHost", "221.130.23.29");
		// System.getProperties().setProperty("http.proxyPort", "80");
		// tfa.testForMergeTwoData();
		// HibernateSessionFactory.getSession().flush();
		// HibernateSessionFactory.getSession().clear();
		// HibernateSessionFactory.getSession().close();
		
		// tfa.testAllThreadPool();
		// tfa.testMultipleTable();
		// tfa.testFoHql();
		// tfa.testSamples();
		tfa.testForVisualization();
		
	}

	public void testFoHql() {
		String hql = "select new MyLocationElement(mle.id,mle.locationOriginalText,"
				+ "mle.start,"
				+ "mle.end) from MyLocationElement mle"
				+ " where mle.id.wordId = 111";
		Query query = HibernateSessionFactory.getSession().createQuery(hql);
		List<MyLocationElement> mleList = query.list();
		System.out.println(mleList.size());

	}

	public void testSamples() {
		// int testList[] = {100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600,
		// 51200 };

		int testList[] = { 1, 2, 3 };
		int i, j = 0;
		ExecutorService pool = null;
		pool = Executors.newFixedThreadPool(4);
		for (i = 0; i < testList.length; i++) {
			Integer id = testList[i];
			MergeAlgorithm ma = new MergeAlgorithm(id);
			Thread thread = new Thread(ma);
			pool.execute(ma);

			// newYork 3000个,一次20个,150次,一次10分钟,1500分钟
			System.out.println("processed " + i);
			if ((i + 1) % 3 == 0) {
				j++;
				pool.shutdown();
				while (true) {
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

	public void testMultipleTable() {
		String[] sqlStrings = {
				"select travelogue_Hawaii.doc_id from travelogue_Hawaii "
						+ "where travelogue_Hawaii.doc_id not in (select doc_id from my_location_element)",
				"select travelogue_California.doc_id from travelogue_California where "
						+ " travelogue_California.doc_id not in (select my_location_element.doc_id from my_location_element)" };

		int count = 0;
		int count2 = 0;
		while (true) {
			count2++;
			for (int i = 0; i < sqlStrings.length; i++) {
				count = 0;
					SQLQuery sqlQuery = HibernateSessionFactory.getSession()
							.createSQLQuery(sqlStrings[i]);
					List<Integer> idIntegers = sqlQuery.list();
					if (idIntegers.size() > 0) {
						count++;
						testAllThreadPool(idIntegers);
					} else {
						break;
					}
			

			}
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
		pool = Executors.newFixedThreadPool(10);

		// String sqlString1 = "select doc_id from travelogue_NewYork";
		// String sqlString1 =
		// "select doc_id from travelogue_NewYork where (doc_id>62302 and doc_id<67001)";
		// String sqlString1 =
		// "select doc_id from travelogue_California";//zz,7826

		/**
		 * number of runned: select count(travelogue_California.doc_id) from
		 * travelogue_California where travelogue_California.doc_id not in
		 * (select my_location_element.doc_id from my_location_element) and ;
		 * 
		 * 
		 */
		// String sqlString1 =
		// "select doc_id from travelogue_Hawaii where travelogue_Hawaii.doc_id>10030";//hqn,2102
		// String sqlString1 =
		// "select doc_id from travelogue_Florida";//wzj,3622
		// String sqlString1 =
		// "select travelogue_Hawaii.doc_id from travelogue_Hawaii " +
		// "where travelogue_Hawaii.doc_id not in (select doc_id from my_location_element)";//hqn,
		// String sqlString1 =
		// "select travelogue_Washington.doc_id from travelogue_Washington " +
		// "where travelogue_Washington.doc_id not in (select doc_id from my_location_element)";//hqn,
		// sqlString1 =
		// "select travelogue_California.doc_id from travelogue_California where doc_id >78000 AND"+
		// " travelogue_California.doc_id not in (select my_location_element.doc_id from my_location_element)";//--
		// 749
		// String sqlString1 =
		// "select travelogue_Florida.doc_id from travelogue_Florida " +
		// "where travelogue_Florida.doc_id not in (select doc_id from my_location_element)";//hqn,

		// String sqlString1 =
		// "select doc_id from travelogue_Washington";//hqn,2083，第四部

		// String sqlString1 =
		// "select travelogue_NewYork.doc_id from travelogue_NewYork where travelogue_NewYork.doc_id "+
		// "not in (select doc_id from my_location_element) and travelogue_NewYork.doc_id<100000;";
		//
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
				pool.shutdown();
				while (true) {
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

	public LocationElement getNextElement(List<LocationElement> finalList, int j) {
		if (j < finalList.size()) {
			return finalList.get(j);
		} else {
			return null;
		}
	}
	
	public MyLocationElement getMyLocationElementNextElement(List<MyLocationElement> finalList, int j) {
		if (j < finalList.size()) {
			return finalList.get(j);
		} else {
			return null;
		}
	}

	public void testForMergeTwoData() {
		int testDoc[] = { 100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600,
				51200 };
		// int testDoc[] = {1,2,3};
		String colors[] = { "<span style = 'background-color:FFB6C1'>",
				"</span>" };
		String textString = "";
		String locationString = null;
		// int colorLength = colors[0].length() + colors[1].length();
		BufferedWriter bw = null;
		MergeAlgorithm ma = null;
		String sizeRecord = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data test/size.txt";
		try {
			for (int i = 0; i < testDoc.length; i++) {
				// if(i==1)break;
				String fileName = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data test/"
						+ testDoc[i] + ".html";
				bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(new File(fileName))));
				int doc_id = testDoc[i];
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				Date date = new Date();
				String time = df.format(date);
				System.out.println("start:" + doc_id + ":" + time);
				ma = new MergeAlgorithm(doc_id);
				if (ma.getDataStructure(doc_id)) {
					ma.mergeTwoData();
					/**
					 * <FONT COLOR=FFB6C1></FONT>
					 * 
					 */
					List<LocationElement> finalList = ma.getFinalList();
					IOTools.writeToFile(doc_id + "," + finalList.size(),
							sizeRecord);
					// if(doc_id==1600)
					// {
					// for (Iterator iterator = finalList.iterator(); iterator
					// .hasNext();) {
					// LocationElement locationElement = (LocationElement)
					// iterator
					// .next();
					// System.out.println(locationElement.getLocationOriginalText());
					// }
					//
					// int ii = 0;
					// ii +=1;
					// ii-=1;
					// }
					textString = ma.getTextString();
					int j = 0;
					bw.write("<html><br>\n<body>\n");
					for (int k = 0; k < textString.length(); k++) { // 字符的索引
						LocationElement le = getNextElement(finalList, j); // j:元素的索引
						// if(k==648)
						// {
						// j+=1;
						// j-=1;
						// }
						if (le != null && k == le.getStart()) { // 有下一个
							String tmp = le.getLocationOriginalText();
							bw.write(colors[0] + tmp + colors[1]);
							k = le.getEnd() - 1;
							j++;
						} else {
							if (textString.charAt(k) == '\n') {
								bw.write("<br/>");
							} else {
								bw.write(textString.charAt(k));
							}
						}
					}
					bw.write("</body>\n</html>\n");
				}
				IOTools.closeIO(null, bw);
				IOTools.processWordPair(ma);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
			IOTools.processWordPair(ma);
		}
	}

	public void testForPResultTest() {
		MergeAlgorithm ma = new MergeAlgorithm(1);
		if (ma.getDataStructure(1)) {
			LocationElement pResultTestResult = null;
			for (int j = 0; j < ma.getPmElements().size(); j++) {
				LocationElement le = ma.getPmElements().get(j);
				System.out.println(le.getLocationOriginalText() + " :" + j);
				pResultTestResult = ma.PResultTest(le, j);
				if (pResultTestResult == null) {
					System.out.println("TestForMergeAlgorithm-" + null);
				} else {
					System.out.println("TestForMergeAlgorithm-"
							+ pResultTestResult.getLocationOriginalText());
				}

				System.out.println();
			}
		}
	}

	public void testForMultiThread() {

		for (int i = 1; i <= 10; i++) {
			MergeAlgorithm ma = new MergeAlgorithm(i);
			Thread thread = new Thread(ma);
			thread.start();
		}
	}

	public void testForIntegrity() {
		LocationElement integrityResult = null;
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		MergeAlgorithm ma = new MergeAlgorithm();
		// for(int i=0;i<20;i++)
		// {
		// if(ma.getDataStructure(i))
		// {
		//
		// }
		// }

		if (ma.getDataStructure(1)) {
			String time = df.format(date);
			System.out.println(time);
			int j = 1;
			// System.out.println(System.currentTimeMillis());
			try {

				for (j = 0; j < ma.getWwjElements().size(); j++) {
					LocationElement le = ma.getWwjElements().get(j);
					System.out.println(le.getLocationOriginalText() + " :" + j);
					if (j == 17) {
						int a = 1;// 测试点
						a = a + 1;
					}
					integrityResult = ma.integrityTest(le, j, 0);
					System.out.println("TestForMergeAlgorithm-"
							+ integrityResult.getLocationOriginalText());
					System.out.println();
				}
				// integrityResult =
				// ma.integrityTest(ma.getWwjElements().get(7), i, 0);
				// System.out.println("TestForMergeAlgorithm-"+integrityResult.getLocationOriginalText());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOTools.processWordPair(ma);
			}
			date = new Date();
			time = df.format(date);
			System.out.println(time);
		}
		return;
	}
	
	//将制定文件的地名提取结果可视化
	public void testForVisualization(){
		
		String docListFileName = "D:/Study/Ideas/annotating images with the aid of travelogues/travelogue mining/Experiments/Semantic Parsing&Relational Validation/Waikiki beachid_doc_list.txt";
		BufferedReader br = null;
		BufferedWriter bw = null;
		String docID = null;
		String colors[] = { "<span style = 'background-color:FFB6C1'>", "</span>" };
		
		try{
			br = new BufferedReader(new FileReader(docListFileName));
			while ((docID = br.readLine()) != null){
				// 从数据库MyLocationElement的表中读Location Elements
				String hqlLocElem = "select new MyLocationElement(le.id, le.locationOriginalText, le.start, le.end)"
					+ " from MyLocationElement le" + " where le.id.docId = " + docID;
				Query queryListLocElem = HibernateSessionFactory.getSession().createQuery(hqlLocElem);
				List<MyLocationElement> readSpotList = queryListLocElem.list();				
				
				//从数据库travelogue_Hawaii的表中读text				
				String sqlTlogText = "select text from travelogue_Hawaii where travelogue_Hawaii.doc_id = "
					+ docID;
				SQLQuery sqlQuery = HibernateSessionFactory.getSession()
						.createSQLQuery(sqlTlogText);			
				Object uniqueResult = sqlQuery.uniqueResult();
				String docText = (String) (uniqueResult);
				
				// 画数据
				String visualFileName = "D:/Study/Ideas/annotating images with the aid of travelogues/travelogue mining/Experiments/Semantic Parsing&Relational Validation/" 
					+ docID + ".html";
				bw = new BufferedWriter(new FileWriter(visualFileName));
				bw.write("<html><br>\n<body>\n");
				for (int j = 0, k = 0; k < docText.length(); k++) { //字符的索引
					MyLocationElement le = getMyLocationElementNextElement(readSpotList, j); //j:元素的索引

					if (le != null && k == le.getStart()) { //有下一个
						String str = le.getLocationOriginalText();
						bw.write(colors[0] + str + colors[1]);
						k = le.getEnd()-1;
						j++;
					} else {
						if(docText.charAt(k)=='\n')
						{
							bw.write("<br/>");
						}else {							
							bw.write(docText.charAt(k));
						}
					}
				}
				bw.close();
			}
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return;
	}
}
