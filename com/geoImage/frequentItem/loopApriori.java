package com.geoImage.frequentItem;

/**
 * 由于MyApriori算法，每次都只在某一个阈值上进行挖掘。
 * 这个程序可以在多个层次上进行挖掘
 * @author hqn
 * @description   
 * @version
 * @update 2014-12-26 上午11:24:35
 */
public class loopApriori {

	public static void main(String[] args) {
		for(int thr=10;thr<21;thr++)
		{
			MyApriori ma = new MyApriori();
			ma.THRESHOLD = thr;
			ma.myApriori();
		}
	}
}
