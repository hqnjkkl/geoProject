package com.geoImage.dataInsert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试如何去除字符串当中的中文
 * @author hqn
 *
 */
public class DealCharacter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	DealCharacter dc = new DealCharacter();
//	dc.test2();//成功
//	dc.test3();//成功
//	dc.test4();
	dc.testSubS();
	}
	/**
	 * 解析的时候，包容（（）），以及(())))模式的数据。让'（'跟下一个'（'之前最
	 * 外层的'）'匹配，如果下一个'（'之前没有读到'）'就继续往前读。
	 * 在报错的时候处理
	 */
	public void test4()
	{
		String testString = "(8613352)()(Kevin Madden)(17 March 2008)" +
				"(-77.527529999999999)(180)(240)(到拉萨福建爱上代理费内拉手)" +
				"(39.131782999999999)(http://www.panoramio.com/user/1511612)(1511612)" +
				"(http://mw2.google.com/mw-panoramio/photos/small/8613352.jpg)" +
				"(http://www.panoramio.com/photo/8613352)-";
		String[] resultString = new String[13];
//		System.out.println(testString);
		int left=0,left2,right=0;//record the place of "(" and ")"
		int ls = 0,rs = 0;//record the number of "(" and ")" at one time
		int cal=0;//calculate the order of line
		//拆分字符串
		for (int i = 0; i < testString.length(); i++) {
		if(testString.charAt(i)=='(')
		{
			left2 = left;
			left = i;
			ls ++;
			if(rs>0)
			{
				resultString[cal] = testString.substring(left2+1,right);//分出字符串
				cal++;
				ls = 1;
				rs = 0;
			}
		}else if(testString.charAt(i)==')')
		{
			right = i;
			rs ++;
		}else if (i==(testString.length()-1)) {
			resultString[cal] = testString.substring(left+1,right);
			cal++;
			ls = 0;
			rs = 0;
		}
	}
		
		resultString[1] = resultString[1].replaceAll("[^a-zA-Z0-9,]"," ");
		resultString[7] = resultString[7].replaceAll("[^a-zA-Z0-9,]"," ");
		resultString[1] = trimBlank(resultString[1]);
		resultString[7] = trimBlank(resultString[7]);
		System.out.println(resultString[1].equals(""));
		System.out.println(resultString[7].equals(""));
//		System.out.println(resultString[1].equals(" "));
//		System.out.println(resultString[7].equals(" "));
		for (int i = 0; i < resultString.length; i++) {
		System.out.println(resultString[i]);
		}
		
	}
	
	/**
	 * 去除记录当中字符串头尾的空格
	 */
	public void test3(){
		String testString = "      &gt; 999,google Earth,Landscapes and places-韦慰蟺委伪 魏伪喂 ,New York，魏伪    ";
		dealChart(testString);
	}
	
	/**
	 * 1.delete the character is not english character,dot,or number
	 * 2.trim the head and tail blank of a String 
	 * 
	 * @param testString
	 * @param bw
	 * @return
	 */
	public String dealChart(String firString)
	{
//		String firString = testString.replaceAll("[^a-zA-Z0-9,]"," ");
//		if (!firString.equals(testString)) {
//				try {
//					bw.write(firString);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		}
//		System.out.println(firString);
		int slength = firString.length();
		int i=0,j = slength-1;
		System.out.println(firString.substring(i,j+1));//包头不包尾
		boolean change = false;
		while (i<=j){
			change = false;
			if(firString.charAt(i)==' ')
			{
				i++;
				change = true;
			}
			if (firString.charAt(j)==' ') {
				j--;
				change = true;
			}
			if(!change){
				break;
			}
		}
		
		System.out.println(trimBlank(firString));
		return trimBlank(firString);
	}
	
	/**
	 * delete the head and tail blank of a String 
	 * @param firString
	 * @return
	 */
	public String trimBlank(String firString)
	{
		int slength = firString.length();
		int i=0,j = slength-1;
//		System.out.println(firString.substring(i,j+1));//包头不包尾
		boolean change = false;
		while (i<=j){
			change = false;
			if((i<=j)&&(firString.charAt(i)==' '))
			{
				i++;
				change = true;
			}
			if ((i<=j)&&(firString.charAt(j)==' ')) {
				j--;
				change = true;
			}
			if(!change){
				break;
			}
		}
			String secString = firString.substring(i,j+1);
			return secString; 
		}
	
	

	
	/**
	 * 测试如何去除中文
	 */
	public void test2()
	{
		String testString = "(14772789)" +
				"(&gt; 999,google Earth,Landscapes and places-韦慰蟺委伪 魏伪喂 蟿蠈蟺慰喂,New York,)" +
				"(´ڼ`  t s a   k)(06 October 2008)(-73.967106000000001)(160)(240)";
		//非英文字母，逗号的都代替成空格，very good
		System.out.println(testString.replaceAll("[^a-zA-Z0-9,]", " "));
		
		/**
		 * result: 14772789   gt  999 google Earth Landscapes and places       
		 *           New York        t s a   k  06 October 2008   73 967106000000001  160  240 
		 */
	}
	
	/**
	 * 替换指定{}中文字 
	 */
	public void test1(){
		String str = "Java目前的发展史是由{0}年-{1}年"; 
		String[][] object={new String[]{"\\{0\\}","1995"},new String[]{"\\{1\\}","2007"}}; 
		
		System.out.println(replace(str,object)); 
	}

	/**
	 * 替换指定{}中文字 
	 * @param sourceString
	 * @param object
	 * @return
	 */
	public static String replace(final String sourceString,Object[] object) { 
	String temp=sourceString; 
	for(int i=0;i<object.length;i++){ 
	String[] result=(String[])object[i]; 
	Pattern pattern = Pattern.compile(result[0]); 
	Matcher matcher = pattern.matcher(temp); 
	temp=matcher.replaceAll(result[1]); 
	} 
	return temp; 
	} 
	
	/**
	 * test subString of equals
	 */
	public void testSubS(){
		String testString = "this is a test string";
		String aString = testString.substring(3,3);
		System.out.println(aString.equals(""));
		System.out.println(aString=="");
		System.out.println(aString.equals(" "));
	}
}
