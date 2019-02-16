package com.geoImage.recommend;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;

/**
 * 
 * @author hqn
 * @description   
 * @version
 * @update 2015-1-21 下午9:18:53
 */
public class MyItemBasedRecommender {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws TasteException 
	 */
	public static void main(String[] args) throws IOException, TasteException {
		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File("intro.csv"));
		
		  RecommenderIRStatsEvaluator evaluator2 =
			      new GenericRecommenderIRStatsEvaluator();
		  
		  
		  RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		
		RecommenderBuilder builder = new RecommenderBuilder(){
			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
				ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				
				return new GenericItemBasedRecommender(dataModel, similarity);
			}
		};
		
	double score = evaluator.evaluate(builder, null, model, 0.95, 1.0);
	
	   IRStatistics stats = evaluator2.evaluate(builder,
               null, model, null, 2,
               GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
               1.0);	
	   
	   System.out.println(stats.getPrecision());
	    System.out.println(stats.getRecall());
	    
		System.out.println(score);
	}
}
