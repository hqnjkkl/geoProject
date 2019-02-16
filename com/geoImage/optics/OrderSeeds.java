package com.geoImage.optics;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import javassist.expr.NewArray;



public class OrderSeeds {

	private PriorityQueue<DataPoint> pqDataPoints = null;
	
	public OrderSeeds() {
		//初始化优先队列
		pqDataPoints = new PriorityQueue<DataPoint>(100,new PQComparator());
	}
	
	
	public static void main(String[] args) {
		OrderSeeds oss = new OrderSeeds();
//		oss.testAboutPQ();
	}
	
	public void update(List<DataPoint> neighbors,DataPoint centerPoint)
	{
		double coreDist = centerPoint.getCoreDistance();
		double newReachabilityDis = 0.0;
		for(int i=0;i<neighbors.size();i++)
		{
			
			DataPoint dp = neighbors.get(i);
			if(!dp.isProcessed())
			{				
				double disToCenter = dp.getTempleDistanceToCenter();
				newReachabilityDis = coreDist>dp.getTempleDistanceToCenter()?coreDist:disToCenter;
				if(dp.getReachabilityDistance()==DataPoint.UNDEFINED)
				{
					dp.setReachabilityDistance(newReachabilityDis);
					pqDataPoints.add(dp);
				}else//dp is already in OrderSeeds
				{
					if (newReachabilityDis<dp.getReachabilityDistance()) {
						//test1
						if(pqDataPoints.remove(dp)==false)
						{
							System.err.println("int OrderSeeds the remove is error!!!");
						}
						dp.setReachabilityDistance(newReachabilityDis);
						pqDataPoints.add(dp);
					}
				}
			}
		}
		
	}
	
	
	
	public void testAboutPQ()
	{
		PriorityQueue<DataPoint> pQueue = new PriorityQueue<DataPoint>(100,new Comparator<DataPoint>() {

			@Override
			public int compare(DataPoint o1, DataPoint o2) {
				if(o1.getReachabilityDistance()>o2.getReachabilityDistance())
				{
					return 1;
				}else if(o1.getReachabilityDistance()<o2.getReachabilityDistance())
				{
					return -1;
				}else{					
					return 0;
				}
			}
		}) ;
		
		for (int i = 0; i < 15; i++) {
			double tb = 10*Math.random();
			System.out.println("**"+tb);
			System.out.println(pQueue.size());
			pQueue.add(new DataPoint(false, 6,tb, 6, 0));
		}
		
		while (!pQueue.isEmpty()) {
			System.out.println(pQueue.poll().getReachabilityDistance());
		}
		
		for (int i = 0; i < pQueue.size(); i++) {
			System.out.println(pQueue.poll().getReachabilityDistance());
		}
	}
	/**
	 * to see whether the PriorityQueue is empty
	 * @return
	 */
	public boolean isEmpty() {
		if(pqDataPoints.isEmpty())
		{
			return true;
		}else
		{			
			return false;
		}
	
	}
	protected class PQComparator implements Comparator<DataPoint>{

		public int compare(DataPoint o1, DataPoint o2) {
			if(o1.getReachabilityDistance()>o2.getReachabilityDistance())
			{
				return 1;
			}else if(o1.getReachabilityDistance()<o2.getReachabilityDistance())
			{
				return -1;
			}else{					
				return 0;
			
		}
		}

	}
	public DataPoint nextPoint() {
		return pqDataPoints.poll();
	}
}
