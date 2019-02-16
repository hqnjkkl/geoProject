package com.geoImage.textSegmentation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.MyLocationElement;
import com.geoImage.dao.MyLocationElementDAO;
import com.geoImage.dao.MyLocationElementId;
import com.geoImage.dao.TravelogueSnippet;
import com.geoImage.dao.TravelogueSnippetDAO;
import com.geoImage.dao.TravelogueSnippetId;
import com.geoImage.tools.IOTools;

public class TravelogueSentence {

	List<MyLocationElement> myLocationElements = null;
	String geoInfo = null;
	public Logger logForSnippet = Logger.getLogger(TravelogueSentence.class
			.getClass());
	
	public TravelogueSentence()
	{
		BasicConfigurator.configure();
		PropertyConfigurator.configure("src/log4j.properties");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TravelogueSentence ts = new TravelogueSentence();
		// ts.extractSentences(46786);
		// ts.getSnippet();
//		ts.getAllNewYorkDocId();
		try {
			
			ts.getSnippet3();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Integer> getAllNewYorkDocId()
	{
		Session session = HibernateSessionFactory.getSession();
		String sql = "select DISTINCT doc_id from travelogue_NewYork";
		SQLQuery query = session.createSQLQuery(sql);
		List<Integer> list = query.list();
		System.out.println(list.size());
		return list;
	}
	
	

	public void getSnippet() throws IOException {
//		int testList[] = { 1, 2, 3, 100, 200, 400, 800, 1600, 3200, 6400,
//				12800, 25600, 51200 };
		List<TravelogueSnippet> tsList = new ArrayList<TravelogueSnippet>();
		TravelogueSnippet ts = null;
		 int testList[] = { 1 };
		int i, j, k, sSize1, sSize2;
		int snippetId = 0;
		int wordcount = 0;
		int doc_id;
		// String sqlString = "select doc_id from ";// hqn,2102
		// List<Integer> idIntegers = sqlQuery.list();
		List<MySentence> sentences = null;
		boolean isInsert = true;
		String fileName = "E:hqn/dataTestForSnippet/snippetDoc";
		BufferedWriter bw = null;
		TravelogueSnippetDAO tsdDao = new TravelogueSnippetDAO();
		// bw.newLine();
		for (i = 0; i < testList.length; i++) {
			fileName = "E:/hqn/dataTestForSnippet/snippetDoc" + testList[i]
					+ ".txt";
			bw = new BufferedWriter(new FileWriter(new File(fileName), true));// 加在后面
			doc_id = testList[i];
			sentences = extractSentences(doc_id); // 已经设置了geoInfo和myLocations
			sSize1 = sentences.size();
			myLocationElements = IOTools.getMyLocationElements(doc_id);

			isInsert = insertLocationElementToSentence2(doc_id, sentences);
			sSize2 = sentences.size();
			if (isInsert) {
				// 先不出力这种情况
				// if(sSize2<sSize1)
				// {
				// while(sSize2<sSize1)
				// {
				// sSize1 = sentences.size();
				// for(j=0;j<sentences.size();j++)
				// {
				// sentences.get(j).setLocations(null);
				// }
				// isInsert = insertLocationElementToSentence(doc_id,sentences);
				// sSize2 = sentences.size();
				// }
				// }else if(sSize2>sSize1)
				// {
				// System.out.println("error-getSnippet:size1"+sSize1+",sSize2"+sSize2);
				// return ;
				// }

				wordcount = 0;
				snippetId = 1;
				ts = new TravelogueSnippet();
				StringBuilder sbBuilder = null;
				j = 0; // sentence索引
				// 有sentence
				if (sentences.size() > 0) {
					if (sentences.get(j).getLocations() == null) {
						sbBuilder = new StringBuilder();
						ts.setStart(sentences.get(j).getStart());
						while (sentences.get(j).getLocations() == null) {
							sbBuilder
									.append(sentences.get(j).getSentenceText());
							j++;
						}
						ts.setEnd(sentences.get(j - 1).getEnd());
						ts.setSnippetText(sbBuilder.toString());
						ts.setLocationWordCount(-1); // -1代表其地名词是geoInfo
						ts.setId(new TravelogueSnippetId(doc_id, snippetId));
						// word_id-1代表是geoInfo,word_id 0 代表没有其余的word_id
						ts.wordList = new ArrayList<MyLocationElement>();
						MyLocationElement mleGeoElement = new MyLocationElement(
								new MyLocationElementId(doc_id, -1), null,
								geoInfo, null, null, 0, 0, null, null,
								snippetId);

						// public MyLocationElement(MyLocationElementId id,
						// String locationName,
						// String locationOriginalText, String type, Integer
						// confidence,
						// Integer start, Integer end, Double latitude, Double
						// longitude,
						// Integer snippetId) {

						ts.wordList.add(mleGeoElement);

						// ts.setLocationWordCount();
						snippetId++;
						tsList.add(ts);
					}
					while (j < sentences.size()) {
						ts = new TravelogueSnippet();
						ts.setLocationWordCount(sentences.get(j).getLocations()
								.size());

						ts.wordList = new ArrayList<MyLocationElement>();
						List<MyLocationElement> mles = sentences.get(j)
								.getLocations();
						// 设置LocationElement的snippetId
						for (Iterator iterator = mles.iterator(); iterator
								.hasNext();) {
							MyLocationElement myLocationElement = (MyLocationElement) iterator
									.next();
							myLocationElement.setSnippetId(snippetId);
						}
						// 把单词加入wordList当中
						ts.wordList.addAll(mles);

						ts.setStart(sentences.get(j).getStart());
						ts.setId(new TravelogueSnippetId(doc_id, snippetId));
						sbBuilder = new StringBuilder(sentences.get(j)
								.getSentenceText());

						j++;
						while (j < sentences.size()
								&& sentences.get(j).getLocations() == null) {
							sbBuilder
									.append(sentences.get(j).getSentenceText());
							j++;
						}
						ts.setSnippetText(sbBuilder.toString());
						ts.setEnd(sentences.get(j - 1).getEnd());
						tsList.add(ts);
						snippetId++;
						// 测试snippet的显示以及如何存储
					}

				} else // sentenceSize = 0,直接给一个geoInfo作为单词列表
				{
					ts.setSnippetText(null);
					// snippet id为零表示没有内容
					ts.setId(new TravelogueSnippetId(doc_id, 0));
					ts.setLocationWordCount(0);
					ts.setStart(0);
					ts.setEnd(0);
					MyLocationElement mleGeoElement = new MyLocationElement(
							new MyLocationElementId(doc_id, -1), null, geoInfo,
							null, null, 0, 0, null, null, 0); // snippetId
																// 0表示没有snippet,word_id
																// -1表示是geoInfo
					ts.wordList = new ArrayList<MyLocationElement>();
					ts.wordList.add(mleGeoElement);
					tsList.add(ts);
				}
				// 表示这篇文章没有Snippet (文章)，添加geoInfo作为地理位置信息
				// 或者没有LocationElement单词,添加geoInfo作为地理位置信息
				//
				if (tsList.size() == 1
						&& tsList.get(0).getSnippetText() == null) {
					TravelogueSnippet tsTmp = tsList.get(0);
					bw.write("(");
					for (i = 0; i < tsTmp.wordList.size(); i++) {
						bw.write(tsTmp.wordList.get(i)
								.getLocationOriginalText());
						bw.write(",");
					}

					bw.write(")");
					bw.newLine();
					bw.write(tsTmp.getSnippetText());
					continue;
				} else {
					for (i = 0; i < tsList.size(); i++) {
						TravelogueSnippet showTs = tsList.get(i);
						bw.write("(");
						for (j = 0; j < showTs.wordList.size(); j++) {
							bw.write(showTs.wordList.get(j)
									.getLocationOriginalText());
							bw.write(",");
						}
						bw.write(")");
						bw.newLine();
						if (showTs.getId().getSnippetId() == 0) { // 是geoInfo
							bw.write(showTs.getSnippetText());
						} else {
							bw.write(showTs.getLocationWordCount());
							bw.newLine();
						}
					}

				}

				IOTools.closeIO(null, bw);
				// for (j = 0; j < sentences.size(); j++) {
				// sbBuilder = new StringBuilder();
				// List<MyLocationElement> elements = sentences.get(j)
				// .getLocations();
				// if (j == 0 && snippetId == 1) {
				// if (elements == null) {
				//
				// }
				// }
				// if (elements != null) {
				// // System.out.print("\n(");
				// // bw.write("\n(");
				// for (k = 0; k < elements.size(); k++) {
				// wordcount++;
				// //
				// System.out.print(wordcount+":"+elements.get(k).getLocationOriginalText());
				// // bw.write(wordcount
				// // + ":"
				// // + elements.get(k)
				// // .getLocationOriginalText());
				//
				// if (k != elements.size() - 1) {
				// // System.out.print(",");
				// // bw.write(",");
				// }
				// }
				// // System.out.println(")--------------------------");
				// // bw.write(")----------------------------\n");
				// }
				//
				// //
				// System.out.print("sentence:"+sentences.get(j).getSentenceText());
				// // bw.write("sentence:"
				// // + sentences.get(j).getSentenceText());
				// }
			} else {
				System.out.println("getSnippet-insert error---------");
			}
		}

	}

	/**
	 * 3去掉写入文件的测试部分，主要用来做插入数据库
	 * 
	 * @throws IOException
	 */
	public void getSnippet3() throws IOException {
//		 int testList[] = { 1, 2, 3, 100, 200, 400, 800, 1600, 3200, 6400,
//		 12800, 25600, 51200 };
		// 存储TravelogueSnippet的
		List<TravelogueSnippet> tsList = null;
		List<Integer> docIdList = getAllNewYorkDocId();
		TravelogueSnippet ts = null;
//		int testList[] = { 1600 };
//		int testList[] = { 15224 };
		int i, j, k;
		int snippetId = 0;
		// int wordcount = 0;
		int doc_id;
		// String sqlString = "select doc_id from ";// hqn,2102
		// List<Integer> idIntegers = sqlQuery.list();
		List<MySentence> sentences = null;
		boolean isInsert = true;
		// 文档存储的位置
		TravelogueSnippetDAO tsdDao = new TravelogueSnippetDAO();
		MyLocationElementDAO mleDao = new MyLocationElementDAO();
		// bw.newLine();
		for (i = 0; i < docIdList.size(); i++) {
//			for(i = 0; i < testList.length; i++){
			tsList = new ArrayList<TravelogueSnippet>();
//			bw = new BufferedWriter(new FileWriter(new File(fileName), true));// 加在后面
			doc_id = docIdList.get(i);
//			doc_id = testList[i];
			
			myLocationElements = IOTools.getMyLocationElements(doc_id);
			sentences = extractSentences(doc_id); // 已经设置了geoInfo和myLocations
			// sSize1 = sentences.size();

			// 其中的locationElements元素，还没有word_id为-1的情况

			isInsert = insertLocationElementToSentence2(doc_id, sentences);
			// sSize2 = sentences.size();
			if (isInsert) {
				// 先不出力这种情况

				// wordcount = 0;
				snippetId = 1;
				ts = new TravelogueSnippet();
				StringBuilder sbBuilder = null;
				j = 0; // sentence索引
				// 有sentence
				if (sentences.size() > 0) {
					if (sentences.get(j).getLocations() == null) {
						sbBuilder = new StringBuilder();
						ts.setStart(sentences.get(j).getStart());

						while (j < sentences.size()
								&& sentences.get(j).getLocations() == null) {
							// 文章开头，到第一个包含地名词的句子的开端
							sbBuilder
									.append(sentences.get(j).getSentenceText());
							j++;
						}
						ts.setEnd(sentences.get(j - 1).getEnd());
						ts.setSnippetText(sbBuilder.toString());
						ts.setLocationWordCount(-1); // word Count也是-1代表其地名词是geoInfo
						ts.setId(new TravelogueSnippetId(doc_id, snippetId));
						ts.wordList = new ArrayList<MyLocationElement>();

						// word_id-1代表是geoInfo,word_id 0 代表没有其余的word_id
						MyLocationElement mleGeoElement = new MyLocationElement(
								new MyLocationElementId(doc_id, -1), null,
								geoInfo, null, null, 0, 0, null, null,
								snippetId);
						
						ts.wordList.add(mleGeoElement);
						snippetId++;
						// 开头没有地理名词
						tsList.add(ts);
					}
					while (j < sentences.size()) {
						ts = new TravelogueSnippet();
						ts.setLocationWordCount(sentences.get(j).getLocations()
								.size());

						ts.wordList = new ArrayList<MyLocationElement>();
						List<MyLocationElement> mles = sentences.get(j)
								.getLocations();
						// 设置LocationElement的snippetId
						for (Iterator iterator = mles.iterator(); iterator
								.hasNext();) {
							MyLocationElement myLocationElement = (MyLocationElement) iterator
									.next();
							myLocationElement.setSnippetId(snippetId);
							
							mleDao.attachDirty(myLocationElement);
						}
						// 把单词加入wordList当中
						ts.wordList.addAll(mles);

						ts.setStart(sentences.get(j).getStart());
						ts.setId(new TravelogueSnippetId(doc_id, snippetId));
						sbBuilder = new StringBuilder(sentences.get(j)
								.getSentenceText());

						j++;
						while (j < sentences.size()
								&& sentences.get(j).getLocations() == null) {
							sbBuilder
									.append(sentences.get(j).getSentenceText());
							j++;
						}
						ts.setSnippetText(sbBuilder.toString());
						ts.setEnd(sentences.get(j - 1).getEnd());
						tsList.add(ts);
						snippetId++;
						// 测试snippet的显示以及如何存储
					}

				} else // sentenceSize = 0,直接给一个geoInfo作为单词列表
				{
					ts.setSnippetText(null);
					// snippet id为零表示没有内容
					ts.setId(new TravelogueSnippetId(doc_id, 0));
					ts.setLocationWordCount(0); // 表示没有地名词
					ts.setStart(0);
					ts.setEnd(0);
					MyLocationElement mleGeoElement = new MyLocationElement(
							new MyLocationElementId(doc_id, 0), null, null,
							null, null, 0, 0, null, null, 0); // snippetId
																// 0表示没有word
																// 的snippet,
																// word_id
																// 0表示是开头的
					ts.wordList = new ArrayList<MyLocationElement>();
					ts.wordList.add(mleGeoElement);
					tsList.add(ts);
				}
				
				
				// 表示这篇文章没有Snippet (文章)，添加geoInfo作为地理位置信息,snippetId = -1
				// 或者没有LocationElement单词,添加geoInfo作为地理位置信息,snippetId = 0
				if (tsList.size() == 1
						&& tsList.get(0).getId().getSnippetId() == 0) {
					TravelogueSnippet tsTmp = tsList.get(0);
					tsdDao.save(tsTmp);

//					continue;
				} else {
					for (k = 0; k < tsList.size(); k++) {
						TravelogueSnippet showTs = tsList.get(k);
						tsdDao.save(showTs);
					}
//					tran.commit();
				}
				if((i+1)%100==0)
				{
					System.out.println("doc_id:"+i);
					tsdDao.getSession().flush();
					tsdDao.getSession().clear();
				}
			} else {
				System.out.println("getSnippet-insert error---------");
			}
		}
	}
	

	/**
	 * 把地点名词的单词插入句子当中，当locationList的单词为空的话 表明没有单词需要插入，。
	 * 当locationList单词有一个以上，就插入，为了保存geoInfo
	 * 还需要在每个doc的LocationElement当中加入一个geoInfo记录， 暂时还没有加geoInfo，所以逻辑就是这样。
	 * 以后加了geoInfo，就改变查询的判断 暂时geoInfo的word_id为-1，文章中没有地名词是，加一个word_id为零
	 * 的LocatioinElement
	 * 
	 * @param doc_id
	 *            文章的doc_id
	 * @param sentences
	 *            文章中，提取好的句子
	 * @return 正常分配地名单词，返回true，分配地名单词遇到错误，或不正常，返回false;
	 */
	public boolean insertLocationElementToSentence2(int doc_id,
			List<MySentence> sentences) {
		List<Integer[]> errorList = new ArrayList<Integer[]>();
		MySentence sentence = null;
		MyLocationElement mle = null;
		int i = 0, j = 0, k = 0, l = 0;
		int locationSize = 0, sentenceEnd = 0, sentenceStart = 0;
		// 以后加入了geoInfo的逻辑后，这个判断就不存在了
		if (myLocationElements == null
				|| myLocationElements.size() == 0
				|| (myLocationElements.size() == 1 && myLocationElements.get(0)
						.getId().getWordId() == 0)) { // 没有考虑等于1并且其中为空的情况
			// 不需要插入单词,sentence原封不动
			return true;
		} else {
			locationSize = myLocationElements.size();
			// 逐个单词插入sentences，sentences为零也就不用插入，没有snippet
			for (i = 0; i < sentences.size(); i++) {
				sentence = sentences.get(i);
				sentenceEnd = sentence.getEnd();
				sentenceStart = sentence.getStart();
				// 找到包含在这个Sentences中的单词
				while (j < locationSize) {
					mle = myLocationElements.get(j);
					if (mle.getStart() >= sentenceStart
							&& mle.getEnd() <= sentenceEnd) {
						if (sentence.getLocations() == null) {
							sentence.setLocations(new ArrayList<MyLocationElement>());
						}
						sentence.getLocations().add(mle);
					} else { // 即不包含，也不是错误
						break;
					}
					j++;
				}
			}
			if (j == locationSize) {
				return true;
			} else {
			for(i=0;i<sentences.size();i++)
			{
				MySentence myst = sentences.get(i);
				System.out.println("start:"+myst.getStart());
				System.out.println("end"+myst.getEnd());
				System.out.println("i:"+i+","+sentences.get(i).getSentenceText());
			}
			for (int m = 0; m < myLocationElements.size(); m++) {
				MyLocationElement met = myLocationElements.get(m);
				System.out.println("start:"+met.getStart());
				System.out.println("end"+met.getEnd());
				System.out.println(met.getLocationOriginalText());
			}
				System.out.println("doc_id:" + doc_id + ",j:" + j
						+ ",locationSize:" + locationSize);
				logForSnippet.error("doc_id:" + doc_id + ",j:" + j
						+ ",locationSize:" + locationSize);
				return false;
			}
		}
	}

	public void mergeSentence(MySentence ms1, MySentence ms2) {
		ms1.setEnd(ms2.getEnd());
		ms1.setSentenceText(ms1.getSentenceText() + ms2.getSentenceText());
	}

	/**
	 * 提取一篇文档中的句子，句子包括停顿后的空白符号
	 * 
	 * @param doc_id
	 * @return 存储Sentence的列表，大小可能为0.为0是因为文章中的textString为零
	 */
	public List<MySentence> extractSentences(int doc_id) {
		Object[] obj = IOTools.getTravelogueGeoinfoText(doc_id);
		List<MySentence> sentences = new ArrayList<MySentence>();
		int i, start, end;
		int j = 0;
		if (obj != null) { // 查询到结果
			geoInfo = (String) obj[0];
			String textString = (String) obj[1];
			// &结束符号，
			// 1,测试正确;57209有两个换行，中间隔着空格，要处理这种情况
			// 46815,对应特殊符号如结尾的...可以处理
			// 46786，对应
			List<Integer[]> sentenceSpliter = sentenceSplit(textString);

			// SentenceSpliter有可能是空的
			for (i = 0; i < sentenceSpliter.size(); i++) {
				start = sentenceSpliter.get(i)[0];
				end = sentenceSpliter.get(i)[1];
				MySentence mySentence = new MySentence(textString.substring(
						start, end), doc_id, (i + 1), start, end, null);
				sentences.add(mySentence);
			}
			return sentences;
		} else {
			System.out.println(TravelogueSentence.class.getName()
					+ " extractSentences()");
			System.out.println("no such id in travelogue " + doc_id);
		}
		// 没有查询到结果，就返回空，先不处理这种情况
		return null;
	}

	/**
	 * 提取一篇文章分隔符的起点和结束点
	 * 
	 * @param textString
	 * @return SentenceSplit分割痕迹 为零的情况，textString的length是零
	 */
	public List<Integer[]> sentenceSplit(String textString) {
		String paraSpliter = "[\\.!?\\n]"; // 一句话停顿的标识
		// String spaceSpliter = "[\\s\\p{Punct}]";//一句话停顿之后继续无用的符号，包括空白符和标点符号
		String characterSpliter = "[\\p{Alpha}]";
		String tmpString = null;
		// boolean isSpaceFlag = false;
		// 起点和结束点的存储位置
		List<Integer[]> splitPoints = new ArrayList<Integer[]>();
		// 句子的开头和结尾位置
		List<Integer[]> sentenceSpliter = new ArrayList<Integer[]>();
		int start = 0, bEnd, i, state, j = 0;
		int head, tail, mid;// for二分查找
		boolean intersectFlag = false;
//		System.out.println("textLength:"+textString.length());
//		System.out.println(textString);

		// 一直都在state 0,并且一直都是字母,这种情况，分隔符是最后一个字符的后面
		// 所以分隔符长度是零，是和文章长度相同
		boolean allAreCharacter = true;
		i = 0;
		state = 0;
		while (i < textString.length()) {

			tmpString = textString.substring(i, i + 1);
			
			if (state == 0) {
				// 匹配分隔符
				if (tmpString.matches(paraSpliter)) {
					start = i;
					state = 1;
					allAreCharacter = false;
				}
			} else if (state == 1) {
				// 如果是字母,就是分隔符走完了
				if (tmpString.matches(characterSpliter)) {
					intersectFlag = false;
					// 检测是否和单词相交,左闭右闭区间
					if (myLocationElements.size() == 0) // 没查找到
					{

					} else if (myLocationElements.size() == 1
							&& myLocationElements.get(0).getId().getWordId() == 0) {// 表示没单词

					} else {
						head = 0;
						tail = myLocationElements.size() - 1;
						// 二分查找是否相交这个没有起作用
//						if (splitPoints.size() == 17) {
//							head += 1;
//							head -= 1;
//						}
						
						while (head <= tail) {
							mid = (head + tail) / 2;
							MyLocationElement mle = myLocationElements.get(mid);
							if (mle.getStart() >= i) // 在后面
							{
								tail = mid -1;
							} else if (mle.getEnd() <= start)// 在前面
							{
								head = mid + 1;
							} else {
								intersectFlag = true;
								break;
							}
						}
					}
					if (!intersectFlag) {
						Integer[] stopChar = new Integer[2];
						stopChar[0] = start;// 一个停顿的开头和结尾
						stopChar[1] = i;
						splitPoints.add(stopChar);
					}
					// 状态还原，不进行错误添加
					state = 0;
				}
			} 
			else if (state == 2) {
				break;
			}
			i++;
		}
		/**
		 * state = 0，但是没有找到分隔符好，所以，分隔符号是文章末尾
		 * state = 1,但是没找到字母，所以从start到文章结束都是一个句子
		 */
//		System.out.println("i:"+i+",state:"+state+",start:"+start);
		// 处理句子最末端没有分隔符
		if (state == 1) {
			Integer[] stopChar = new Integer[2];
			stopChar[0] = start;
			stopChar[1] = i;
			splitPoints.add(stopChar);
		}
		
		// 表示有文章，但是没有分隔符
		if (allAreCharacter && textString.length() > 0) {
			Integer[] stopChar = new Integer[2];
			stopChar[0] = textString.length();
			stopChar[1] = textString.length();
			splitPoints.add(stopChar);
		}
		bEnd = 0;
		// 句子的开头就是分隔符，那么，第一个分隔符就被纳入第一个句子的开头，
		// 那么就从第二个分隔符开始提取句子
		if (splitPoints.size() > 0) {
			if (splitPoints.get(0)[0] == 0) {
				i = 1; // 从第几个开始查找
			} else {
				i = 0;
			}
		}
		// i=1并且size = 1,也就是没有句子
		for (; i < splitPoints.size(); i++) {
			Integer[] sentenceSplit = new Integer[2];
			sentenceSplit[0] = bEnd;
			sentenceSplit[1] = splitPoints.get(i)[1];
			sentenceSpliter.add(sentenceSplit);
			bEnd = splitPoints.get(i)[1];
		}
		if(sentenceSpliter.size()>0)
		{
			int end = sentenceSpliter.get(sentenceSpliter.size()-1)[1];
			if(end <textString.length())
			{
				Integer[] sentenceSplit = new Integer[2];
				sentenceSplit[0] = end;
				sentenceSplit[1] = textString.length();
				sentenceSpliter.add(sentenceSplit);
			}
		}
		return sentenceSpliter;
	}

}
