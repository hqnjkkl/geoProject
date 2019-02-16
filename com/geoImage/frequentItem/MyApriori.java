package com.geoImage.frequentItem;

import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javassist.expr.NewArray;

import com.geoImage.ruleIntface.WriteFile;
import com.geoImage.tools.IOTools;


public class MyApriori {
	//存储全表的数据，键是doc_id,值是一个个单词
	Map<Integer, Set<String>> fullTable = new HashMap<Integer, Set<String>>();
	
	//存储非频繁项，键是非频繁的一个个单词，值是出现的次数
	Map<Set<String>,Integer> noFreItems = new HashMap<Set<String>, Integer>();
	//存储1项，2项到n项频繁项集合
	//List<Map<Set<String>,Integer>> resFrequentItems = new ArrayList<Map<Set<String>,Integer>>();
	
	static int ITEMS=1;
	static int THRESHOLD = 5;
	int k = 1;
	String dataNameString = "E:/hqn/毕业设计/data/freQuent";
	String stasticString = "E:/hqn/毕业设计/data/stastic.txt";
	
	MyWriteFile mwf = new MyWriteFile();
	WriteFile<String> writeStastic = new MyWriteFileStatics();
	
	/**
	 * 获得location sense当中的数据，每篇文章提取的地名词，相当于一个篮子。
	 */
	public void getFullTable()
	{
		List<Object[]> listRes = IOTools.getDocIdAndText();
		System.out.println(listRes.size());
		for(int i=0;i<listRes.size();i++)
		{
			Integer id = (Integer)listRes.get(i)[0];
			if(fullTable.containsKey(id))
			{
				fullTable.get(id).add((String)listRes.get(i)[1]);
			}else {
				Set<String> hashSet = new HashSet<String>();
				hashSet.add((String)listRes.get(i)[1]);
				fullTable.put(id,hashSet);
			}
		}
		System.out.println("fullTable is loaded: size "+fullTable.size());
		writeStastic.writeContentFile("The size of documents : "+fullTable.size(),stasticString);
		return ;
	}
	
	/**
	 * 整个apriori算法的流程
	 */
	public void myApriori()
	{
		//MyApriori ma = new MyApriori();
		writeStastic.writeContentFile("Apriori: Threshold:"+THRESHOLD, stasticString);
		getFullTable();
		Map<Set<String>,Integer> f1items = find_frequent_1itemsets();
		mwf.writeContentFile(f1items,dataNameString+k+"thr"+THRESHOLD+".txt");
		//resFrequentItems.add(f1items);
		k++;
		for(;;k++)
		{
			System.out.println(k+" frequent item");
			writeStastic.writeContentFile("k:"+k+",the turn:", stasticString);
			
			Set<Set<String>> newCandidateSet = apriori_gen(f1items);
			//获得下一次的频繁项集合
			Map<Set<String>, Integer> newItems = new HashMap<Set<String>, Integer>();
			//扫描全表，遍历fullTable
			System.out.println("k:"+k+";"+"candidateSize:"+newCandidateSet.size());
			writeStastic.writeContentFile("k:"+k+",the candidates generated:"+newCandidateSet.size(), stasticString);
			
			for(Iterator<Set<String>> itFull = fullTable.values().iterator();itFull.hasNext();)
			{
			Set<String> fullString = itFull.next();
			//扫描candidate
				for(Iterator<Set<String>> itCan = newCandidateSet.iterator();itCan.hasNext();)
				  {
					Set<String> oneCan = itCan.next();
					boolean contains = true;
					for(Iterator<String> oneStringCan = oneCan.iterator();oneStringCan.hasNext();)
					{
						//fullString 不包含某一个元素
						if(!fullString.contains(oneStringCan.next()))
						{
							contains = false;
							break;
						}
					}
					if(contains)
					{
						//System.out.println(i+" fre "+oneCan);
						if(newItems.containsKey(oneCan))
						{
							
							newItems.put(oneCan, newItems.get(oneCan)+1);
						}else {
							newItems.put(oneCan, 1);
						}
						if(newItems.size()%200==0)
						{
							System.out.println("k:"+k+";"+newItems.size());
						}
					}
				}
			}
			newItems = removeItems(newItems);
			if(newItems.size()==0)
			{
				break;
			}else {
				//resFrequentItems.add(newItems);
				f1items = newItems;
				mwf.writeContentFile(newItems,dataNameString+k+"thr"+THRESHOLD+".txt");
			}
		}
	}
	public static void main(String[] args) {
		//对于放入String的TreeSet，其equals和HashCode都是根据元素来的
		//对于String的HashSet也是
		
		
		MyApriori ma = new MyApriori();
		ma.myApriori();
		System.out.println();
		
		Set<String> mySet = new TreeSet<String>();
		
		mySet.equals(new TreeSet<String>());
		
		System.out.println(mySet.hashCode());
		mySet.add(new String("aaaa"));
		System.out.println(mySet.hashCode());
		mySet.add(new String("bbb"));
		System.out.println(mySet.hashCode());
		
		Set<String> mySet2 = new HashSet<String>();
		mySet2.add(new String("aaaa"));
		mySet2.add(new String("bbb"));
		//调用AbstractSet的Hashcode，是把所有元素的hashcode累加的返回值。
		System.out.println(mySet.hashCode());
		//调用AbstractSet的equals，并且equals当中调用AbstractCollection的containsAll方法，所以
		//和是否用TreeSet无关，TreeSet也是这个流程，用HashSet反而更快
		System.out.println(mySet.equals(mySet2));
		
		
	}
	
	/**
	 * 由全表产生1项频繁项
	 * @return
	 */
	public Map<Set<String>, Integer> find_frequent_1itemsets()
	{
		System.out.println("one frequent item");
		writeStastic.writeContentFile("k:"+k+",find_frequent_1itemsets:", stasticString);
		
		Map<Set<String>, Integer> res = new HashMap<Set<String>, Integer>();
		for(Iterator<Set<String>> it = fullTable.values().iterator();it.hasNext();)
		{
			for(Iterator<String> itString = it.next().iterator();itString.hasNext();)
			{
				//找到一个地名词
				String next = itString.next();
				Set<String> newItems = new HashSet<String>();
				newItems.add(next);
				if(res.containsKey(newItems))
				{
					res.put(newItems, res.get(newItems)+1);
				}else {
					res.put(newItems, 1);
				}
			}
		}
		res = removeItems(res);
		return res;
	}
	
	/**
	 * 把全表扫描后的频繁项结果，筛选出不符合阈值的频繁项
	 * @param candidate 得到的新的候选频繁项
	 * @return
	 */
	public Map<Set<String>,Integer> removeItems(Map<Set<String>,Integer> candidate)
	{
		System.out.println("Total dinstinct items in location_sense :"+candidate.size());
		writeStastic.writeContentFile("k:"+k+",the distinct candidate:"+candidate.size(), stasticString);
		
		//去除不频繁的k项集合
		for(Iterator<Set<String>> resIt = candidate.keySet().iterator();resIt.hasNext();)
		{
			Set<String> key = resIt.next();
			Integer value = candidate.get(key);
			if(value<THRESHOLD)
			{
				noFreItems.put(key, value);
				//使用resIt来remove是可以感知到修改的
				resIt.remove();
			}
		}
		System.out.println("After remove items under threshold the size of location_sense ："+candidate.size());
		System.out.println("The size of noFreItems: "+noFreItems.size());
		writeStastic.writeContentFile("k:"+k+",after remove items under threshold:"+candidate.size(), stasticString);
		writeStastic.writeContentFile("k:"+k+",sizeof noFreItems:"+noFreItems.size(), stasticString);
		
		return candidate;
	}
	/**
	 * 通过已有的频繁项集合，产生候选频繁项集合
	 * @param preItems 已经产生的k-1频繁项集合
	 * @return
	 */
	public Set<Set<String>> apriori_gen(Map<Set<String>, Integer> preItems)
	{
		//返回的结果，键是频繁项集合，值是这个集合出现的次数
		Set<Set<String>> res = new HashSet<Set<String>>();
		for(Iterator<Set<String>> iss = preItems.keySet().iterator();iss.hasNext();)
		{
			//显示外层的循环
			System.out.print("->");
			Set<String> oneSet = iss.next();
			for(Iterator<Set<String>> iss2=preItems.keySet().iterator();iss2.hasNext();)
			{
				Set<String> twoSet = iss2.next();
				//制造新的频繁项
				if(!oneSet.equals(twoSet))
				{
					//添加新的元素，形成新的集合
					for(Iterator<String> sit = twoSet.iterator();sit.hasNext();)
					{
						String tmpSit = sit.next();
						if(!oneSet.contains(tmpSit))
						{
							//以原来的集合为基础
							Set<String> tmpCandidate = new HashSet<String>(oneSet);
							tmpCandidate.add(tmpSit);
								if(!res.contains(tmpCandidate))
								{
									//剪枝,去掉包含非频繁项的集合
									if(!has_infrequent_sub(tmpCandidate))
									{
										res.add(tmpCandidate);
										if(res.size()%10000==0){
											System.out.println("produce candidate:"+res.size());
										}
									}
								}
						}
					}
				}
			}
		}
		return res;
	}
	
	public boolean has_infrequent_sub(Set<String> candidate)
	{
		Object[] arrayCandidate = candidate.toArray();
		Set<String> subSet = new HashSet<String>();
		return isSubset(arrayCandidate, subSet, 0);
	} 
	/**
	 * 判断一个集合的子集是否属于非频繁项集合，总体就是一个求子集的函数
	 * @param arrayCandidate
	 * @param subSet
	 * @param start
	 * @return
	 */
	boolean isSubset(Object[] arrayCandidate,Set<String> subSet,int start)
	{
		for(int i=start;i<arrayCandidate.length;i++)
		{
			Set<String> newSubSet = new HashSet<String>(subSet);
			newSubSet.add((String)arrayCandidate[i]);
			if(noFreItems.containsKey(newSubSet))
			{
				return true;
			}else {
				return isSubset(arrayCandidate, newSubSet, start+1);
			}
		}
		return false;
	}
	//自己判断两个集合是否相等，不过不需要了
	public boolean itemsEqual(TreeSet<String> items1,TreeSet<String> items2)
	{
		if(items1.size()!=items2.size())
		{
			return false;
		}
		Iterator<String> it1 = items1.iterator();
		Iterator<String> it2 = items2.iterator();
		while(it1.hasNext())
		{
			if(it1.next()!=it2.next())
			{
				return false;
			}
		}
		return true;
	}
}
