package com.geoImage.tools;

public class memoryTools {

	public static void main(String args[]) { 
	System.out.println("usage:"); 
	memoryTools m=new memoryTools(); 
	long t=m.showUsage(); 
	System.out.println("Total:"+ t + " Bytes"); 
	long fr=m.freeMemory(); 
	//System.gc(); 
	System.out.println("Free:"+fr + " Bytes"); 
	long rem=t-fr; 
	System.out.println("Occupied Space :"+rem + " Bytes"); 
	
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


	public long showUsage() { 
	long l=Runtime.getRuntime().totalMemory(); 
	return(l); 
	} 


	public long freeMemory() { 
	long f=Runtime.getRuntime().freeMemory(); 
	return(f); 
	} 
}
