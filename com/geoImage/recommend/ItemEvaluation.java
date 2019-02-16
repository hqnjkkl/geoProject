package com.geoImage.recommend;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.lucene.search.similarities.Similarity;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorization;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.*;
import org.apache.mahout.common.RandomUtils;

public class ItemEvaluation {

	public static void main(String[] args) throws IOException, TasteException {
		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File("userItem2.csv"));
//		DataModel model = new FileDataModel(new File("userItem.txt"));
		ItemSimilarity similarity = new CityBlockSimilarity(model);
		  RecommenderIRStatsEvaluator evaluator2 =
			      new GenericRecommenderIRStatsEvaluator();
		  
		  RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		
		RecommenderBuilder builder = new RecommenderBuilder(){
			@Override
			public Recommender buildRecommender(DataModel dataModel)
					throws TasteException {
//				ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				ItemSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
//				ItemSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
//				ItemSimilarity similarity = new SpearmanCorrelationSimilarity(dataModel);
//				ItemSimilarity similarity = new CityBlockSimilarity(dataModel);
//				ItemSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
				
				return new GenericItemBasedRecommender(dataModel, similarity);
			}
		};
		
		//Recommender recommender = new GenericItemBasedRecommender(model,similarity);
		
	double score = evaluator.evaluate(builder, null, model, 0.9, 1.0);
	
	   IRStatistics stats = evaluator2.evaluate(builder,
               null, model, null,10,
               GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
               1.0);	
	   System.out.print(stats.getPrecision());
	    System.out.print(","+stats.getRecall());
		System.out.println(","+score);
	}
}
