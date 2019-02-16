package com.geoImage.recommend;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * 这个类专门来进行UserCF实验，其中数据是userItem.txt，主要统计K值不同时，能够进行推荐的用户
 * @author hqn
 * @description   
 * @version
 * @update 2015-1-21 下午9:20:39
 */
public class FirstRecommend {
	//下面是给第m个用户推荐n个物品
	int m = 1; //第m个用户
	int n = 10;//推荐n个物品
	int totalUsers = 800;
	int kSize = 10;
	List<String> precentList = new ArrayList<String>();
	String[] dirList = {"E:/hqn/毕业设计/data/userRecom/pearson.txt",
			"E:/hqn/毕业设计/data/userRecom/tanimoto.txt",
			"E:/hqn/毕业设计/data/userRecom/euclidean.txt",
			"E:/hqn/毕业设计/data/userRecom/spearman.txt"};
	String[] dirList2 = {"E:/hqn/毕业设计/data/userRecom/pearson2.txt",
			"E:/hqn/毕业设计/data/userRecom/tanimoto2.txt",
			"E:/hqn/毕业设计/data/userRecom/euclidean2.txt",
			"E:/hqn/毕业设计/data/userRecom/spearman2.txt"};
	String[] simString = {"org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity"
			};

	/**
	 * 同时对多种相似度算法进行挖掘
	 * @param id
	 */
	public void loopWriteData()
	{
		for(int id=0;id<4;id++)
		{
			List<String> loopPrecent = new ArrayList<String>();
			int[] ks = {2,4,6,8,10,200,400};
			for(int i=0;i<ks.length;i++)
			{
				kSize = ks[i];
				loopManyUserCF(id,loopPrecent);
			}
			FileToolForRecommend ftfr = new FileToolForRecommend();
			ftfr.writeContentFile(loopPrecent, dirList2[id]);
		}
	}
	
	public void loopManyUserCF(int id,List<String> loopList)
	{
		try {			
			DataModel model = new FileDataModel(new File("userItem2.csv"));
			Class c = Class.forName(simString[id]);
			Constructor cons = c.getConstructor(new Class[]{DataModel.class});
			Object obj = cons.newInstance(model);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(kSize,
					(UserSimilarity) obj, model);
			Recommender recommender = new GenericUserBasedRecommender(model,
					neighborhood, (UserSimilarity) obj);
			
			int totalUser = totalUsers;
			int hasRes = 0;
			
			HashMap<Integer, List<RecommendedItem>> statics = new HashMap<Integer, List<RecommendedItem>>();
			for(int i=1;i<totalUsers;i++)
			{
				List<RecommendedItem> recommendations = recommender.recommend(i, 10);
//				System.out.println("size:" + recommendations.size());
//				for (RecommendedItem recommendation : recommendations) {
//					System.out.println(recommendation);
//				}
				if(recommendations.size()>0)
				{
					hasRes++;
					statics.put(i, recommendations);
				}
			}
			
			System.out.println("recommends:"+statics.size());
			String tmp = new String(kSize+","+statics.size()+","+(double)statics.size()/totalUsers);
			loopList.add(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeDataForUserRecommend()
	{
		
		int[] ks = {3,10,20,40,100,200,300,400};
		for(int i=0;i<ks.length;i++)
		{
			kSize = ks[i];
			myUserRecommend();
		}
		FileToolForRecommend ftfr = new FileToolForRecommend();
		ftfr.writeContentFile(precentList, dirList2[0]);
	}
	
	public void myItemRecommend()
	{
		try {
			DataModel model = new FileDataModel(new File("userItem.txt"));
			
			
			//UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
			
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
			
			// UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
			
			Recommender recommender = new GenericItemBasedRecommender(model, similarity);
			int totalUser = totalUsers;
			int hasRes = 0;
			
			HashMap<Integer, List<RecommendedItem>> statics = new HashMap<Integer, List<RecommendedItem>>();
			for(int i=1;i<totalUsers;i++)
			{
				
				List<RecommendedItem> recommendations = recommender.recommend(i, 10);
				System.out.println("size:" + recommendations.size());
//				for (RecommendedItem recommendation : recommendations) {
//					System.out.println(recommendation);
//				}
				if(recommendations.size()>0)
				{
					hasRes++;
					statics.put(i, recommendations);
				}
//				for (RecommendedItem recommendation : recommendations) {
//					System.out.println(recommendation);
//				}
			}
			System.out.println("recommends:"+statics.size());
			String tmp = new String(kSize+","+statics.size()+","+(double)statics.size()/totalUsers);
			precentList.add(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		FirstRecommend fr = new FirstRecommend();
//		fr.example();
		fr.myUserRecommend();
		
//		fr.writeData();
//		fr.myItemRecommend();
//		fr.testReflect();
//		fr.loopWriteData();
		
	}
	public void myUserRecommend() {
		try {

			float ft = 0.0f / 0.0f;
			System.out.println(ft);

			
			DataModel model = new FileDataModel(new File("userItem.txt"));
			//UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			// UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
			//UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
			UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(kSize,
					similarity, model);
			Recommender recommender = new GenericUserBasedRecommender(model,
					neighborhood, similarity);
			int totalUser = totalUsers;
			int hasRes = 0;
			
			HashMap<Integer, List<RecommendedItem>> statics = new HashMap<Integer, List<RecommendedItem>>();
			
			
			for(int i=1;i<totalUsers;i++)
			{
				List<RecommendedItem> recommendations = recommender.recommend(i, 10);
				System.out.println("size:" + recommendations.size());
//				for (RecommendedItem recommendation : recommendations) {
//					System.out.println(recommendation);
//				}
				if(recommendations.size()>0)
				{
					hasRes++;
					statics.put(i, recommendations);
				}
//				for (RecommendedItem recommendation : recommendations) {
//					System.out.println(recommendation);
//				}
			}
			System.out.println("recommends:"+statics.size());
			String tmp = new String(kSize+","+statics.size()+","+(double)statics.size()/totalUsers);
			precentList.add(tmp);
			FileToolForRecommend ftfr = new FileToolForRecommend();
			ftfr.writeContentFile(precentList, dirList[0]);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void example() {
		try {

			float ft = 0.0f / 0.0f;
			System.out.println(ft);
			DataModel model = new FileDataModel(new File("intro.1.csv"));

			// UserSimilarity similarity = new PearsonCorrelationSimilarity
			// (model);

			// UserSimilarity similarity = new
			// TanimotoCoefficientSimilarity(model);

			UserSimilarity similarity = new LogLikelihoodSimilarity(model);

			UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,
					similarity, model);

			Recommender recommender = new GenericUserBasedRecommender(model,
					neighborhood, similarity);

			List<RecommendedItem> recommendations = recommender.recommend(1, 3);
			System.out.println("size:" + recommendations.size());
			for (RecommendedItem recommendation : recommendations) {
				System.out.println(recommendation);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
