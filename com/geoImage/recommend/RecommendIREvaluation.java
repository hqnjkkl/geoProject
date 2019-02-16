package com.geoImage.recommend;

import java.io.File;
import java.io.IOException;
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
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class RecommendIREvaluation {

	/**
	 * @param args
	 * @throws TasteException 
	 * @throws IOException 
	 */
	int nNeighbor =2;
	int at = 100;
//	String writeFile = "E:/hqn/毕业设计/data/pearsonPR.txt"; // at =10
//	String writeFile = "E:/hqn/毕业设计/data/eulideanPR.txt";
	String writeFile = "E:/hqn/毕业设计/data/spearmanPR.txt";
//	String writeFile = "E:/hqn/毕业设计/data/euclidean2.txt";
	List<String> res = new ArrayList<String>();
	
	public static void main(String[] args) throws TasteException, IOException {
		// TODO Auto-generated method stub
		RandomUtils.useTestSeed();    
		RecommendIREvaluation reIR = new RecommendIREvaluation();
		//reIR.recallPrecision();
		reIR.loopForData();
		//reIR.recallPrecision();
		
	}
	public void loopForData() throws TasteException, IOException
	{
		for(int i=10;i<16;i++)
		{
			this.nNeighbor = i;
//			for (int j = 10; j <16 ; j++) {
				this.at =10;
				System.out.println("this is out****************"+nNeighbor);
				System.out.println("this is out****************"+at);
				recallPrecision();
//			}
		}
		FileToolForRecommend tfr = new FileToolForRecommend();
		tfr.writeContentFile(res, writeFile);
	}
	public void recallPrecision() throws TasteException, IOException
	{
		//有评分的数据
//		   DataModel model = new FileDataModel(new File("intro.csv"));
//		DataModel model = new FileDataModel(new File("userItem2.csv"));
		DataModel model = new FileDataModel(new File("userItem.txt"));
		    RecommenderIRStatsEvaluator evaluator =
		      new GenericRecommenderIRStatsEvaluator();
		    
		    // Build the same recommender for testing that we did last time:
		    
		    //测试哪些相似度是可以使用的
		    RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
		      @Override
		      public Recommender buildRecommender(DataModel model) throws TasteException {
//		       UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		        //可以使用
//		      UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
		    	  //可以使用
//		      UserSimilarity similarity = new org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity(model);
		        //在neighborhood是3的时候可以使用
		    	 // UserSimilarity similarity = new CityBlockSimilarity(model);
		        
		    	  //不能够使用
		    	  UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);
		    	  //不能够使用，是10的时候可以使用
		    	//  UserSimilarity similarity = new UncenteredCosineSimilarity(model);
		    	  
		        UserNeighborhood neighborhood =
		          new NearestNUserNeighborhood(nNeighbor, similarity, model);
		        return new GenericUserBasedRecommender(model, neighborhood, similarity);
		      }
		    };
//		    DataModelBuilder modelBuilder = new DataModelBuilder() {
//				
//				@Override
//				public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
//					return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(trainingData));
//				}
//			};
		    // Evaluate precision and recall "at 2":
		    IRStatistics stats = evaluator.evaluate(recommenderBuilder,
		                                            null, model, null, 10,
		                                            GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
		                                            1.0);
		    String tmp = new String(nNeighbor+","+at+","+stats.getPrecision()+","+stats.getRecall());
		    res.add(tmp);
		    System.out.println(stats.getPrecision());
		    System.out.println(stats.getRecall());
		    
		    //tfr.writeContentFile(m, fileName);
	}

}
