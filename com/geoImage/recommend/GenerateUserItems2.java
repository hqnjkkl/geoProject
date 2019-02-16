package com.geoImage.recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.geoImage.dao.HuItem2;
import com.geoImage.dao.HuItem2DAO;
import com.geoImage.dao.UserItem2;
import com.geoImage.dao.UserItem2DAO;
import com.geoImage.dao.UserItem2Id;

/**
 *  这个类的主体功能是产生新的Item并且把这些数据保存到数据库当中,和UserItems不同，
 *  这个是针对不同阈值所做的实验
 * 
 * @author hqn
 * @description
 * @version
 * @update 2015-1-3 下午3:23:57
 */
public class GenerateUserItems2 {

	List<Integer> docIds = null;

	public GenerateUserItems2() {
		List<Object[]> tmpDocIds = RecommendDatabaseTools.getHawaiiDocIds();
		docIds = new ArrayList<Integer>();
		for (int i = 0; i < tmpDocIds.size(); i++) {
			docIds.add((Integer) (Object) tmpDocIds.get(i));
		}
	}
	//两个线路可以归为一类的相似度阈值
	double threhold = 0.1;
	/**
	 * 根据docId来获得Hawaii的一条路线
	 * 
	 * @param docId
	 * @return
	 */
	public Set<String> getRoutesByDocId(Integer docId) {
		List<Object[]> iniRouteList = RecommendDatabaseTools
				.getRouteByDocId(docId);
		Set<String> route = new HashSet<String>();
		for (int i = 0; i < iniRouteList.size(); i++) {
			String array_element = (String) (Object) iniRouteList.get(i);
			route.add(array_element);
		}
		System.out.println("all ids in hawaii");
		return route;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerateUserItems2 gui = new GenerateUserItems2();
		gui.generateUserItems();
	}

	/**
	 * 
	 */

	public void generateUserItems() {
		Set<Set<String>> uniqueItemSet = new HashSet<Set<String>>();
		HuItem2DAO hid = new HuItem2DAO();
		UserItem2DAO uid = new UserItem2DAO();
		
		for (int i = 0; i < docIds.size(); i++) {
			System.out.println("out loop1");
			//文档的所有ID
			Integer docId1 = docIds.get(i);
			//获取一篇文章的线路
			Set<String> route1 = getRoutesByDocId(docId1);
			//获取用户ID
			Integer userId1 = (Integer) RecommendDatabaseTools
					.getUserIdByDocId(docId1);
			
			Set<Integer> bigDocId = new HashSet<Integer>();
			bigDocId.add(docId1);

			Set<String> bigRoute = new HashSet<String>();
			// 把所有的元素都加入
			bigRoute.addAll(route1);

			Set<Integer> bigUserId = new HashSet<Integer>();
			bigUserId.add(userId1);
			//内部循环，找到和某个路线相似度大于一定阈值的线路，进行合并
			for (int j = 0; j < docIds.size() && j != i; j++) {
				System.out.println("inner loop2");
				Integer docId2 = docIds.get(j);
				Set<String> route2 = getRoutesByDocId(docId2);
				Integer userId2 = (Integer) RecommendDatabaseTools
						.getUserIdByDocId(docId2);
				//相似度算法
				if (getRouteSimilarity(route1, route2) > threhold) {
					bigDocId.add(docId2);
					bigRoute.addAll(route2);
					bigUserId.add(userId2);
				}
			}
			// 这个集合是一个未出现过的集合，需要加入到数据库当中
			if (!uniqueItemSet.contains(bigRoute)) {
				uniqueItemSet.add(bigRoute);
				String itemRoutes = getRouteBySet(bigRoute);
				String itemDocs = getStringBySet(bigDocId);
				String userIds = getStringBySet(bigUserId);
				HuItem2 hi = new HuItem2(itemRoutes, itemDocs, userIds,
						bigRoute.size(), bigDocId.size(), bigUserId.size());
				System.out.println("save one item2");
				hid.save(hi);
				//System.out.println(hi.getItemId());
				storeUserItem2(bigRoute, bigUserId, hi.getItemId());
			}
		}
	}
	/**
	 * 根据所给的UserId,itemId,itemRoute,存储对应的userItem
	 * @param bigRoute
	 * @param bigUserId
	 * @param ItemId
	 */
	public void storeUserItem2(Set<String> bigRoute,Set<Integer> bigUserId,Integer itemId)
	{
		System.out.println("save userItem2 "+itemId);
		UserItem2DAO uid = new UserItem2DAO();
		for (Iterator<Integer> iterator = bigUserId.iterator(); iterator.hasNext();) {
			Integer userId = (Integer) iterator.next();
			
			Set<Integer> docIds = getDocIdsByUserId(userId);
			double similarityMax = 0.0;
			for (Iterator<Integer> it2 = docIds.iterator(); it2.hasNext();) {
				Integer integer = (Integer) it2.next();
				Set<String> routes = getRoutesByDocId(integer);
				double simi2 = getRouteSimilarity(bigRoute, routes);
				if(simi2>similarityMax)
				{
					similarityMax = simi2;
				}
				
			}
			UserItem2Id uii = new UserItem2Id(userId, itemId);
			System.out.println("simi:"+similarityMax);
			UserItem2 ui = new UserItem2(uii, similarityMax);
			uid.save(ui);
		}
	}
	
	/**
	 * 通过userId，得到对应的docId
	 * @param userId
	 * @return
	 */
	private Set<Integer> getDocIdsByUserId(Integer userId) {
		String sql = "select travelogue_Hawaii.doc_id from travelogue_Hawaii,hu_user " +
				"where hu_user.userName = travelogue_Hawaii.author and hu_user.userId = "+userId;
		List<Object[]> list = RecommendDatabaseTools.getListBySql(sql);
		Set<Integer> intSet = new HashSet<Integer>();
		for (int i = 0; i < list.size(); i++) {
			int array_element = (Integer)(Object)list.get(i);
			intSet.add(array_element);
		}
		return intSet;
	}

	String getRouteBySet(Set<String> bigRoute) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<String> iterator = bigRoute.iterator(); iterator
				.hasNext();) {
			String string = (String) iterator.next();
			sb.append(string);
			if (iterator.hasNext()) {
				sb.append("->");
			}
		}
		return sb.toString();
	}

	String getStringBySet(Set<Integer> bigIds) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Integer> iterator = bigIds.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			sb.append(integer);
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 计算出两个Route的相似度
	 * 
	 * @param route1
	 * @param route2
	 * @return
	 */
	public double getRouteSimilarity(Set<String> route1, Set<String> route2) {
		Set<String> union = new HashSet<String>();
		Map<String, Integer> combine = new HashMap<String, Integer>();
		for (Iterator<String> iterator = route1.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			union.add(string);
			combine.put(string, 1);
		}
		for (Iterator<String> iterator = route2.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			union.add(string);
			if (combine.containsKey(string)) {
				combine.put(string, 2);
			}
		}
		for (Iterator<String> iterator = combine.keySet().iterator(); iterator
				.hasNext();) {
			String string = (String) iterator.next();
			if (combine.get(string) == 1) {
				iterator.remove();
			}
		}
		for (Iterator<String> iterator = combine.keySet().iterator(); iterator
				.hasNext();) {
			String string = (String) iterator.next();
			if (combine.get(string) == 1) {
				try {
					throw new Exception("iterator exception in combine");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		double res = (double) combine.size() / union.size();
		return res;
	}
	/*
	 * 
	 * for every doc_id1 from travelogue { get route1,userId1
	 * userids.add(userId1) BigRoute.add(route1); docids.add(doc_id1);
	 * 
	 * for every doc_id2 from travelogue { get route2,userId2 if(route1 &
	 * route2)>0.3 { userids.add(userId1) BigRoute.add(route1);
	 * docids.add(doc_id1); } } if(BigRoute is a new Item) {
	 * insertItem(BigRoute,docids,userIds); insertUserItem(userids,item); } }
	 */
}
