package com.geoImage.test;

import java.util.HashMap;
import java.util.Map;

public class TestForProgramTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test1();
	}
	
	public static void test2()
	{
		
		Map<String, String> testMap = new HashMap<String,String>();
		for (int i = 0; i < 1000000; i++) {
			testMap.put(new String("aaa"), new String("bbb"));
		}
	}

	public static void test1()
	{
		double totalTime = 1*24*60*60;//一天
		double eToW = totalTime/10000;
		System.out.println("一天"+totalTime+"s");
		System.out.println("10000个一天跑完,每个:"+eToW+"s");
		System.out.println("50000个一天跑完,每个"+totalTime/50000+"s");
		return ;
	}
	
	public void memoryRecord() {
		Runtime run = Runtime.getRuntime();
		long max = run.maxMemory();
		long total = run.totalMemory();
		long free = run.freeMemory();
		long usable = max - total + free;

		System.out.println("最大内存 = " + max);
		System.out.println("已分配内存 = " + total);
		System.out.println("已分配内存中的剩余空间 = " + free);
		System.out.println("最大可用内存 = " + usable);
	}

}
