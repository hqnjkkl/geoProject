package com.geoImage.recommend;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class PrecisionRecallUserCF {

	/**
	 * @param args
	 */
	String[] dirList = { "E:/hqn/毕业设计/data/userRecom/pearson4.txt",
			"E:/hqn/毕业设计/data/userRecom/tanimoto4.txt",
			"E:/hqn/毕业设计/data/userRecom/euclidean4.txt",
			"E:/hqn/毕业设计/data/userRecom/spearman4.txt" };

	String[] simString = {
			"org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity",
			"org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity" };
	int[] ks = { 2, 4, 6, 8, 10, 200, 400 };

	int nNeighbor = 2;
	int at = 2;
	String writeFile = "E:/hqn/毕业设计/data/pearsonUserSimilarity2.txt";
	List<String> res = null;

	public static void main(String[] args) {
		RandomUtils.useTestSeed();    
		PrecisionRecallUserCF prucf = new PrecisionRecallUserCF();
		prucf.loopForData();

	}

	public void loopForData() {
		int index = 0;
		
			for (int myid = 0; myid < 4; myid++) {
				res = new ArrayList<String>();
				// for(int i=10;i<16;i++)
				for (int i = 0; i < ks.length; i++) {
					try {
					this.nNeighbor = ks[i];
					// this.at =;
					System.out.println("this is out****************"
							+ nNeighbor);
					System.out.println("this is out****************" + at);

					recallPrecision(myid);
					}
					 catch (Exception e) {
							e.printStackTrace();
							System.out.println(index);
						}
				}
			
				FileToolForRecommend tfr = new FileToolForRecommend();
				tfr.writeContentFile(res, dirList[index++]);
			}
		
	}

	public void recallPrecision(int myid) throws Exception {
		final int id = myid;
		// 有评分的数据
//		 DataModel model = new FileDataModel(new File("intro.csv"));
		DataModel model = new FileDataModel(new File("userItem2.csv"));
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model)
					throws TasteException {
				Class c = null;
				UserNeighborhood neighborhood = null;
				UserSimilarity similarity = null;
				try {
					c = Class.forName(simString[id]);
					Constructor cons = c
							.getConstructor(new Class[] { DataModel.class });
					Object obj = cons.newInstance(model);
					similarity = (UserSimilarity) obj;
					neighborhood = new NearestNUserNeighborhood(nNeighbor,
							similarity, model);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new GenericUserBasedRecommender(model, neighborhood,
						similarity);
			}
		};
		
		// Evaluate precision and recall "at 2":
		IRStatistics stats = evaluator.evaluate(recommenderBuilder, null,
				model, null, at,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		String tmp = new String(nNeighbor + "," + at + ","
				+ stats.getPrecision() + "," + stats.getRecall());
		res.add(tmp);
		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());

		// tfr.writeContentFile(m, fileName);
	}
}
