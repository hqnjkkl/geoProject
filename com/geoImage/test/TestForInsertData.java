package com.geoImage.test;

import java.text.DecimalFormat;

import com.geoImage.dataInsert.InputData;

public class TestForInsertData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String aaString = "abcde";
//		System.out.println(aaString.length());
//		System.out.println(aaString.substring(1, 1));//substring����ʼ�ͽ�β����ȣ���ô���ǿմ�
//		System.out.println(aaString.substring(2, 2).equals(""));//�����true
//		System.out.println(aaString.substring(2, 2)==""); //�����false
	TestForInsertData t = new TestForInsertData();
//	t.test2();
	t.test3();
	}
//    public GeotaggedData(Integer photoId,Double longitude, Double latitude)
//    {
//    	 this.photoId = photoId;
//         this.longitude = longitude;
//         this.latitude = latitude;
//    }
    
	public void test4()
	{
		
	}
	
	/**
	 * 解析double时的精度
	 * 
	 */
	public void test3()
	{
//		String dString = "-77.529315999999994";
		String dString = "-77.529315994";
		DecimalFormat df1 = new DecimalFormat("000.000000000");
//		String parValue = df1.format(dString);
		double b=120.0000001;
		System.out.println(b);
		System.out.println(Double.parseDouble(dString));
		
	}
	
	/**
	 * �����зֽ����Ƿ�����е���ݶ�����
	 */
	public void test2(){
//	String aLine ="(33315055)()(moretown)(16 March 2010)" +
//			"(-77.447090000000003)(153)(240)(Before Take Off)(38.949396999999998)" +
//			"(http://www.panoramio.com/user/2618873)(2618873)" +
//			"(http://mw2.google.com/mw-panoramio/photos/small/33315055.jpg)" +
//			"(http://www.panoramio.com/photo/33315055)-";
	String aLine="(20418578)(2008,Airport,Google Earth,North America,USA,Virginia,Washington DC,)" +
			"(�� bulgartabak ��)(24 March 2009)(-77.447261999999995)(180)(240)" +
			"(Sunny day at Washington-Dulles airport)(38.947460999999997)" +
			"(http://www.panoramio.com/user/2728888)(2728888)" +
			"(http://mw2.google.com/mw-panoramio/photos/small/20418578.jpg)" +
			"(http://www.panoramio.com/photo/20418578)-";
	String[] aLineStrings = null;
	int sta = 0;//һ���ַ���ʼλ��
	int end = 0;//һ���ַ����λ��
	int left = 0;//�����ŵĸ���
	int right = 0;//�����ŵĸ���
	int j = 0;//�ڼ������

		
//		System.out.println(aLine);
		
		aLineStrings = new String[13];
		//��ÿһ����ݷֽ�
		j=0;//�г�ʼ��
		for(int i=0;i<aLine.length();i++)
		{
			if(aLine.charAt(i)=='('){
				left++;
				if(left==1)
				{						
					sta = i;
				}
			}else if(aLine.charAt(i)==')'){
				right++;
				end = i;
				if (left==right) {
					aLineStrings[j] =  aLine.substring(sta+1, end);
					j++;
					left = 0;
					right = 0;
				}
			}
		}
		for (int i = 0; i < aLineStrings.length; i++) {
		System.out.println(i+"----"+aLineStrings[i]);
		}
		System.out.println(new InputData().tranverseString(aLineStrings[3]));

	}

}
