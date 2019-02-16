package com.geoImage.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		stringTest6();
//		stringTest7();
//		StringTest st = new StringTest();
//		System.out.println(st.isTrue);
//		st.changeBoolean();
//		System.out.println(st.isTrue);
//		StringTest st1 = new StringTest();
//		System.out.println(st1.isTrue);
//		testString8();
//		testString9();
//		testString10();
//		testString11();
//		testString12();
//		testString13();
//		testString14();
		testString15();
	}
	public static void stringTest1()
	{
		String tString1 = "Grove Hill Alabama";
		String tString2 = "Grove Hills Alabama";
		System.out.println(tString1.equals(tString2));
		return ;
	}
	
	public static void stringTest2()
	{
		String tString1 = "Grove Hill Alabama";
		String tString2 = tString1;
		tString1 = "abcde";
		System.out.println(tString1);
		System.out.println(tString2);
		return ;
	}
	
	public static void stringTest3()
	{
		String testString = "I come here ,,,  ,,..   where  \n" +
				"do you.come . from";
		System.out.println(testString);
		// comment: "\\"is \,so "\\s" is "\s","\s" is the kind of blank character,like"\n\t..."
		//"\\s,",is "\s" or ","
		String matchString = testString.replaceAll("[\\s,.]+", " ");
		System.out.println(matchString);
		
		String param = "   I come here ,,,  ,,..   where  \ndo you.come . from    \n";
		System.out.println(param);
		//^表示行开头;X(1,),表示X至少1次;$表示行结尾;"\\s"表示空白符
		String matcString2 = param.replaceAll("(^\\s{1,})|(\\s{1,}$)", "");
		System.out.println(matcString2);
		
		String param2 = "   I come here ,,,  ,,..   where  \ndo you.come . from    \n";
		System.out.println(param2);
		//X*零次或多次
//		String matcString3 = param2.replaceAll("(^[\\s]*)|([\\s]*$)", "") ;
		String matcString3 = param2.replaceAll("(^[\\s]{1,})|([\\s]{1,}$)","");
		System.out.println(matcString3);
		return ;
	}
	
	public static void stringTest4()
	{
		String[] localTerm = {"hill","cliff","mountain","river","spring","pool","pond",
				"lake","sea","ocean","basin","bay","beach","coast","dam","island",
				"coastline","valley","ridge","peak","forest","canyon","dale","desert",
				"sand","oasis","flat","cave","cove","creek","harbor","glacier","veld",
				"veldt","road","avenue","street","highway","country","town","village",
				"zone","park","ground","land","field","court","yard","courtyard","deck",
				"campus","camp","site","lawn","area","garden","orchard","castle","roof",
				"room","tower","bridge","museum","monument","gallery","art","gallery",
				"library","theatre","cinema","plaza","palace","building","hall","center",
				"company","house","church","university","school",
				"hills","cliffs","mountains","rivers","springs","pools",
				"ponds","lakes","seas","oceans","basins","bays","beaches","coasts","dams",
				"islands","coastlines","valleys","ridges","peaks","forests","canyons","dales",
				"deserts","sands","oases","flats","caves","coves","creeks","harbors",
				"glaciers","veld","veldt","roads","avenues","streets","highways","countries",
				"towns","villages","zones","parks","grounds","lands","fields","courts",
				"yards","courtyards","decks","campuses","camps","sites","lawns","areas",
				"gardens","orchards","castles","roofs","rooms","towers","bridges","museums",
				"monuments","galleries","arts","galleries","libraries","theatres","cinemas",
				"plazas","palaces","buildings","halls","centers","companies","houses",
				"churches","universities","schools"};
		int min = 20,max = 0;
		for (int i = 0; i < localTerm.length; i++) {
			String string = localTerm[i];
			if(string.length()>max)
			{
				max = string.length();
			}
			if(string.length()<min)
			{
				min = string.length();
			}
		}
		System.out.println(min+":"+max);
	}
	
	public static void stringTest5()
	{
		String process = "The..., into..!@#$%^&*()., at..., on..., to ..., here..., (各种标点)..., of.., for..., called..., named... ";
		System.out.println(process);
		System.out.println(process.replaceAll("[\\pP]{1,}", ""));
		
		System.out.println(process.replaceAll("[\\p{Punct}]{0,}", ""));//各种标点符号
		Pattern p = Pattern.compile("[\\p{Punct}]{1,}");
		for (int i = 0; i < process.length(); i++) {			
			Matcher m = p.matcher(process.subSequence(i, i+1));
			boolean b = m.matches();
			System.out.println(process.subSequence(i, i+1)+":"+b);
		}
		String process2 =process.replaceAll("[\\s,.]+", " ");
		System.out.println(process2);
		
	}
	
	/**
	 * 把一段文本中包含的杂乱单词解析出来，并以字符数组的形式打印
	 *  example:"is/was, ...had/has/have, ...to, ...on, ...for, ...that, ...where"
	 *  "is","was","had","has","have","to","on","for","that","where"
	 */
	public static void stringTest6()
	{
		/**
		 * [\\p{Punct}]{1,}] 匹配各种标点符号,至少一次
		 *  [[\\s]+]匹配所有空格
		 */
		String process = "The..., into..., at..., on..., to ..., here..., ..., of.., for..., called..., named... ";
//		String process = "a, t, d, s, m, ve, ll, re, about, above, after, against, any, are, around, as, at, be, because, been, before, being, below, between, both, but, by, cannot, could, did, do, does,  doing, down, during, each, few, for, from, further, had, has, have, having, he, her, here, hers, herself, him, himself, his, how, i, if, in, into, is, it, its, itself, me, my, myself, no, nor, not, off, on, onto, once, only, other, ought, our, ours, ourselves, out, over, own, same, should, so, some, such, than, that, their, theirs, them, themselves, then, there, these, they, this, those, through, to, too, under, until, up, very, was, we, were, what, when, where, which, while, who, whom, why, with, without, would, you, your, yours, yourself, yourselves";
//		String process =  "a, about, above, after, against, an, and, any, are, as, at, be, because, been, before, being, below, between, both, but, by, com, came, cannot, for, from, further, i, in, more, most, of, off, let, or, out, over, she, the, why, with, you";
				System.out.println(process);
//		process = process.replaceAll("[[\\p{Punct}][\\s]]{1,}", " ");//
		process = process.replaceAll("[[,][\\s]]{1,}", " ");//
		process = process.replaceAll("[[.][\\s]]{1,}", " ");//
//		System.out.println(process);
//		
//		process = process.replaceAll("[\\s]+"," ");
//		System.out.println(process);
		String processs[] = process.split(" ");
		System.out.println(processs.length);
		for (int i = 0; i < processs.length; i++) {
			String pro = processs[i];
			System.out.print("\""+pro+"\""+",");
			if((i+1)%10==0)
				System.out.println();
		}
		System.out.println();
		
//		process = process.replaceAll("[\\s]+", " ");//合并空格
//		System.out.println(process);
//		System.out.println(process.replaceAll("[\\p{Punct}]{1,}", " "));//去掉标点符号
//		System.out.println("this:::"+process.replaceAll("[\\p{Punct}]{1,}", ""));
//		System.out.println();
//		String process3 = "in, at, go to, went to, onto, into, back to, town of,views of, drive to";
//		String ps3[] = process3.split(",");
//		for (int i = 0; i < ps3.length; i++) {
//			ps3[i] = ps3[i].replaceAll("(^[\\s]{1,})|([\\s]{1,}$)","");
//		}
//		for (int i = 0; i < ps3.length; i++) {
//			System.out.print("\""+ps3[i]+"\",");
//		}
//	System.out.println();
	}
	static boolean isTrue = false;
	public static void stringTest7()
	{
		String teString = "I come from latin, ..      ..America,,,,    ";
		teString = "";
		String splits[] = teString.split("[[\\p{Punct}]|[\\s]]{1,}"); //拆分一个字符串中的单词
		for (int i = 0; i < splits.length; i++) {
			String split = splits[i];
			System.out.println(i+":"+split);
		}
		System.out.println(isTrue);
//		System.out.println(teString.substring(1,2));
//		System.out.println(teString.indexOf(2));
	}
	
	public static void testString8()
	{
		String tString  = "'";
		System.out.println(tString.matches("[\\p{Punct}]|[\\d]"));
		tString = "3";
		System.out.println(tString.matches("[\\p{Punct}]|[\\d]"));
		System.out.println("ab".substring(0,1));
		System.out.println(",".matches("[,.]"));
		System.out.println(".".matches("[,.]"));
		System.out.println("8".matches("[,.]"));
		
		Pattern p=Pattern.compile("\\d+"); 
		Matcher m=p.matcher("aaa2223bb"); 
		Boolean myMache = false;
		m.find();//匹配2223 
		m.start();//返回3 
		m.end();//返回7,返回的是2223后的索引号 
		System.out.println("aaa2223bb group:"+m.group());//返回2223 
		
		Matcher m2= p.matcher("2223bb"); 
		
		m2.lookingAt();   //匹配2223 
		int a = m2.start();   //返回0,由于lookingAt()只能匹配前面的字符串,所以当使用lookingAt()匹配时,start()方法总是返回0 
		a = m2.end();   //返回4 
		m2.group();   //返回2223 

		Matcher m3=p.matcher("2223bb");  
		myMache = m3.find();
//		myMache = m3.matches();   //匹配整个字符串 
		a= m3.start();   //返回0,原因相信大家也清楚了 
		a = m3.end();   //返回6,原因相信大家也清楚了,因为matches()需要匹配所有字符串 
		String aaa = m3.group();   //返回2223bb 
/*
		说了这么多,相信大家都明白了以上几个方法的使用,该说说正则表达式的分组在java中是怎么使用的. 
		start(),end(),group()均有一个重载方法它们是start(int i),end(int i),group(int i)专用于分组操作,Mathcer类还有一个groupCount()用于返回有多少组. 
		Java代码示例: 
*/
		p=Pattern.compile("([a-z]+)(\\d+)"); 
		m=p.matcher("aaa2223bb"); 
		m.find();   //匹配aaa2223 
		a = m.groupCount();   //返回2,因为有2组 
		m.start(1);   //返回0 返回第一组匹配到的子字符串在字符串中的索引号 
		m.start(2);   //返回3 
		m.end(1);   //返回3 返回第一组匹配到的子字符串的最后一个字符在字符串中的索引位置. 
		m.end(2);   //返回7 
		m.group(1);   //返回aaa,返回第一组匹配到的子字符串 
		m.group(2);   //返回2223,返回第二组匹配到的子字符串 
/*
		现在我们使用一下稍微高级点的正则匹配操作,例如有一段文本,里面有很多数字,而且这些数字是分开的,我们现在要将文本中所有数字都取出来,利用java的正则操作是那么的简单. 
		Java代码示例: 
		*/
		System.out.println("我的QQ是:456456 我的电话是:0532214 我的邮箱是:aaa123@aaa.com");
		 p=Pattern.compile("\\d+"); 
		 m=p.matcher("我的QQ是:456456 我的电话是:0532214 我的邮箱是:aaa123@aaa.com"); 
//		 while(m.find()) { 
//		     System.out.println(m.group()); 
//		} 
		 
		 while(m.find()) { 
		     System.out.println(m.group()); 
		     System.out.print("start:"+m.start()); 
		     System.out.println(" end:"+m.end()); 
		} 
		
	}
	//测试输出数字
	public static void testString9()
	{
		int i = 100;
		while(i<80000)
		{
			System.out.print(i+",");
			i*=2;
		}
		
	}
	
	//测试字符数组的分割
	public static void testString10()
	{
		String t1 = "23.94.104.112:7808@HTTP#北美地区";
		String t1s[] = null;
		t1s = t1.split("@");
		for (int i = 0; i < t1s.length; i++) {
			String string = t1s[i];
			System.out.println(string);
		}
		
		String t2 = "23.94.104.112:7808";
		String t2s[] = null;
		t2s = t2.split("@");
		for (int i = 0; i < t2s.length; i++) {
			String string = t2s[i];
			System.out.println(string);
		}
	}
	
	public static void testString11()
	{
		String t1 = "aa";
		System.out.println(t1.matches("a"));
		String t2 = "aa";
		System.out.println(t2.matches("aa"));
		String t3 = "aaa";
		System.out.println(t3.matches("aa"));
		String t4 = "aaa";
		System.out.println(t4.matches("a*"));
		String t5 = "aaa";
		System.out.println(t5.matches(".*"));
		String t6 = "aab";
		System.out.println(t6.matches("c*a*b*"));
		
		String division = "[\\.!?]";
		String test = "I will study here for three years. I like live in HangZhou,It is" +
				" a really beautiful place! Would you like to come? ";
		String test2 = "\"Well, \"the foreigner said to him ,\" you look like an engineer. \"";
	
	}
	
	/**
	 * test for regular expression
	 */
	public static void testString12()
	{
		String division = "[\\.!?\\n]"; //一句话停顿的标识
		String division2 = "[\\s\\p{Punct}]";//停顿之后，继续无用的符号
		String test = "I will study here for three years. I like live in HangZhou,It is" +
				" a really beautiful place! Would you like to come? \n";
		String test2 = "\"Well, \"the foreigner said to him ,\" you look like an engineer. \"";
		Pattern p = Pattern.compile(division2);
		Matcher m = p.matcher(test2);
		boolean find ;
		while((find=m.find()))
		{
			System.out.println("group:"+m.group());
			System.out.println(m.start());
			System.out.println(m.end());
		}
		System.out.println(m.find());
		
	}
	
	/**
	 * test for the index of last char
	 */
	public static void testString13()
	{
		String geoInfo = "Bay Area, California, United States";
		int  l1,l2;
		String subString = null;
		l2 = geoInfo.lastIndexOf(",");
		l1 = geoInfo.lastIndexOf(",",l2-1);
		subString = geoInfo.substring(l1+1,l2);
		System.out.println("l1:"+l1+",l2:"+l2+",sub:"+subString);
		System.out.println("aaa\tnnn");
	
	}
	/**
	 * test print null String;
	 */
	public static void testString14()
	{
		String a = null;
		String b = "b is not a null String ";
		System.out.println(b+a);
	}
	
	/**
	 * test replace '
	 */
	public static void testString15()
	{
		String a = "hello' Are y'ou here";
		System.out.println(a.replaceAll("'", "").toLowerCase());
	}
	
}
