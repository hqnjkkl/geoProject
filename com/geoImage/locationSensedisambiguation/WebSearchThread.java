package com.geoImage.locationSensedisambiguation;

import java.util.List;

import com.geoImage.dao.LocationSenseDuplicate;
import com.geoImage.dao.LocationSenseDuplicateDAO;
import com.geoImage.dao.LocationSenseDuplicateId;
import com.geoImage.dao.MyLocationElement;
import com.geoImage.dao.MyLocationElementId;
import com.geoImage.dao.MyLocationSense;
import com.geoImage.dao.MyLocationSenseDAO;
import com.geoImage.dao.MyLocationSenseId;

/**
 * 专门提取geoNames上的内容的一个控制类
 * @author hqn
 * @description   
 * @version
 * @update 2014-4-10 下午2:10:28
 */
public class WebSearchThread implements Runnable {

	String locationName;
	String geoInfo;
	
	public WebSearchThread()
	{
		
	}
	
	public WebSearchThread(String locationName,String geoInfo) {
		this.locationName = locationName;
		this.geoInfo = geoInfo;
	}
	
	@Override
	public void run() {
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebSearchThread wst = new WebSearchThread();
		wst.testSaveWebSearchResult();
	}
	
	public void testSaveWebSearchResult() {
//		WebSearchProgress wst = new WebSearchProgress();
		String locationName = "at the beach";
		String geoInfo = "Hawaii Volcanoes National Park, Hawaii, United States";
		saveWebSearchResult(locationName,geoInfo);
	}
	
	

	public void saveWebSearchResult(String locationName,String geoInfo) {
//		String locationName = "at the beach";
//		String geoInfo = "Hawaii Volcanoes National Park, Hawaii, United States";
		WebSearchProgress wsp = new WebSearchProgress();
		
		GeoNameSearchByGeoInfo gnsbgi = new GeoNameSearchByGeoInfo();
		MyLocationSenseDAO mlsd = new MyLocationSenseDAO();
		LocationSenseDuplicateDAO lsdd = new LocationSenseDuplicateDAO();
		int duplicateSize = 0;

		List<Object[]> res = gnsbgi.getElementsByNameGeoInfo(locationName,
				geoInfo);
		duplicateSize = res.size();
		if (res.size() > 0) {
			Object[] objects = res.get(0);
			MyLocationElementId meId = new MyLocationElementId(
					(Integer) objects[0], (Integer) objects[1]);
			wsp.getSearchResult2(locationName, meId, geoInfo,duplicateSize);
		}
		// 余下相同的都设置为重复的
		for (int i = 1; i < res.size(); i++) {
			Object[] objects = res.get(i);
			int doc_id = (Integer) objects[0];
			int word_id = (Integer) objects[1];

			LocationSenseDuplicate lsd = new LocationSenseDuplicate(
					new LocationSenseDuplicateId(doc_id, word_id),
					new MyLocationElement(new MyLocationElementId(doc_id,
							word_id)), (Integer) res.get(0)[0],
					(Integer) res.get(0)[1], locationName);

			MyLocationSense mls = new MyLocationSense(new MyLocationSenseId(
					doc_id, word_id, -3), new MyLocationElement(
					new MyLocationElementId(doc_id, word_id)));
			mlsd.save(mls);
			lsdd.save(lsd);
		}
	}
}
