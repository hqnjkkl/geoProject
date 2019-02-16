package com.geoImage.recommend;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveArrayIterator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.google.common.base.FinalizablePhantomReference;

public class ItemCoverage {

	String[] dirList = { "E:/hqn/毕业设计/data/userRecom/pearsonItem1.txt",
			"E:/hqn/毕业设计/data/userRecom/tanimotoItem1.txt",
			"E:/hqn/毕业设计/data/userRecom/euclideanItem1.txt",
			"E:/hqn/毕业设计/data/userRecom/spearmanItem1.txt" };

	String[] simString = {
			"org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity" };

	int totalUsers = 800;
	int kSize = 10;

	public void loopWriteData() {
		String sql = "select count(*) from hu_item2";
		int itemTableSize = RecommendDatabaseTools.getTableSize(sql);
		System.out.println(itemTableSize + "------------");
		for (int id = 0; id < 4; id++) {
			byte covArray[] = new byte[itemTableSize];
			System.out.println(covArray.length);
			List<String> loopPrecent = new ArrayList<String>();
			// int[] ks = {2,4,6,8,10,200,400};
			// for(int i=0;i<ks.length;i++)
			// {
			kSize = 10;
			loopManyUserCF(loopPrecent, id, covArray);
			// }
			FileToolForRecommend ftfr = new FileToolForRecommend();
			ftfr.writeContentFile(loopPrecent, dirList[id]);
		}
	}

	/**
	 * ItemCF的进行实验的数据,提取准确率，召回率，平均绝对差
	 * 
	 * @param loopList
	 * @param id
	 * @param cov
	 * @throws Exception
	 */
	public void loopManyItemCF(List<String> loopList, final int id, byte cov[])
			throws Exception {
		DataModel model = new FileDataModel(new File("userItem.txt"));
		// ItemSimilarity similarity = new CityBlockSimilarity(model);
		RecommenderIRStatsEvaluator evaluator2 = new GenericRecommenderIRStatsEvaluator();
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				ItemSimilarity similarity = null;
				try {
					Class class1 = Class.forName(simString[id]);
					Constructor con = class1
							.getConstructor(new Class[] { DataModel.class });
					similarity = (ItemSimilarity) con.newInstance(dataModel);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return new GenericItemBasedRecommender(dataModel, similarity);
			}
		};
		// Recommender recommender = new
		// GenericItemBasedRecommender(model,similarity);
		double score = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		IRStatistics stats = evaluator2.evaluate(builder, null, model, null,
				10, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		System.out.print(stats.getPrecision());
		System.out.print("," + stats.getRecall());
		System.out.println("," + score);
	}

	public static void main(String[] args) {
		ItemCoverage ic = new ItemCoverage();
		ic.loopWriteDataItem();

	}

	public void loopWriteDataItem() {
		String sql = "select count(*) from hu_item2";
		int itemTableSize = RecommendDatabaseTools.getTableSize(sql);
		System.out.println(itemTableSize + "------------");
		for (int id = 0; id < 3; id++) {
			byte covArray[] = new byte[itemTableSize];
			System.out.println(covArray.length);
			List<String> loopPrecent = new ArrayList<String>();
			try {
				loopManyItemCF2(loopPrecent, id, covArray);
			} catch (Exception e) {
				e.printStackTrace();
			}
			FileToolForRecommend ftfr = new FileToolForRecommend();
			ftfr.writeContentFile(loopPrecent, dirList[id]);
		}
	}

	/**
	 * ItemCF的第二个实验，提取用户覆盖度，物品覆盖度
	 * 
	 * @param loopList
	 * @param id
	 * @param cov
	 * @throws Exception
	 */
	public void loopManyItemCF2(List<String> loopList, final int id, byte cov[])
			throws Exception {
//		DataModel model = new FileDataModel(new File("userItem.txt"));
		DataModel model = new FileDataModel(new File("userItem2.csv"));
		Class c = Class.forName(simString[id]);
		Constructor cons = c.getConstructor(new Class[] { DataModel.class });
		ItemSimilarity similarity = (ItemSimilarity) cons.newInstance(model);
		Recommender recommender = new GenericItemBasedRecommender(model,
				similarity);

		int hasRes = 0;
		HashMap<Integer, List<RecommendedItem>> statics = new HashMap<Integer, List<RecommendedItem>>();
		LongPrimitiveIterator lai = model.getUserIDs();
		int itemUsers = 0;
		while (lai.hasNext()) {
			itemUsers++;
			int i = lai.next().intValue();
			List<RecommendedItem> recommendations = recommender
					.recommend(i, 10);
			for (int j = 0; j < recommendations.size(); j++) {
				try {
					cov[(int) (recommendations.get(j).getItemID() - 1)] = 10;

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(id);
					System.out.println(recommendations);
					System.out.println(recommendations.get(j).getItemID());
					System.out.println(cov.length);
				}
				if (recommendations.size() > 0) {
					hasRes++;
					statics.put(i, recommendations);
				}
			}
		}
		int cov2 = 0;
		for (int i = 0; i < cov.length; i++) {
			if (cov[i] == 10) {
				cov2++;
			}
		}
		String tmp = new String(cov2 + "," + cov.length + "," + (double) cov2
				/ cov.length + "," + statics.size() + ","
				+ (double) statics.size() / itemUsers + "," + itemUsers);
		loopList.add(tmp);
	}

	/**
	 * 统计物品覆盖度
	 * 
	 * @param loopList
	 * @param id
	 * @param cov
	 */
	public void loopManyUserCF(List<String> loopList, int id, byte cov[]) {
		try {
//			DataModel model = new FileDataModel(new File("userItem2.csv"));
			DataModel model = new FileDataModel(new File("userItem2.csv"));
			Class c = Class.forName(simString[id]);
			Constructor cons = c
					.getConstructor(new Class[] { DataModel.class });
			Object obj = cons.newInstance(model);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(kSize,
					(UserSimilarity) obj, model);
			Recommender recommender = new GenericUserBasedRecommender(model,
					neighborhood, (UserSimilarity) obj);

			int totalUser = totalUsers;
			int hasRes = 0;

			HashMap<Integer, List<RecommendedItem>> statics = new HashMap<Integer, List<RecommendedItem>>();
			for (int i = 1; i < totalUsers; i++) {
				List<RecommendedItem> recommendations = recommender.recommend(
						i, 10);
				for (int j = 0; j < recommendations.size(); j++) {
					try {
						cov[(int) (recommendations.get(j).getItemID() - 1)] = 1;

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(id);
						System.out.println(recommendations);
						System.out.println(recommendations.get(j).getItemID());
						System.out.println(cov.length);
					}
				}
				if (recommendations.size() > 0) {
					hasRes++;
					statics.put(i, recommendations);
				}
			}
			int cov2 = 0;
			for (int i = 0; i < cov.length; i++) {
				if (cov[i] == 0) {
					cov2++;
				}
			}
			String tmp = new String(kSize + "," + cov2 + "," + cov.length + ","
					+ (double) cov2 / cov.length + "," + statics.size() + ","
					+ (double) statics.size() / totalUsers);
			loopList.add(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
