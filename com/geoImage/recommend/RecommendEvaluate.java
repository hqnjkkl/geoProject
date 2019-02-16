package com.geoImage.recommend;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.common.RandomUtils;

public class RecommendEvaluate {

	/**
	 * @param args
	 * @throws TasteException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws TasteException, IOException {
		 RandomUtils.useTestSeed();
		    DataModel model = new FileDataModel(new File("intro.csv"));

		    RecommenderEvaluator evaluator =
		      new AverageAbsoluteDifferenceRecommenderEvaluator();
		    // Build the same recommender for testing that we did last time:
		    RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
		      @Override
		      public Recommender buildRecommender(DataModel model) throws TasteException {
		        return new SlopeOneRecommender(model);
		      }
		    };
		    // Use 70% of the data to train; test using the other 30%.
		    double score = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
		    System.out.println(score);
	}
}
