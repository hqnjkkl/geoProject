package com.geoImage.frequentItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.chainsaw.Main;

import com.geoImage.dao.LocationSenseIndex;
import com.geoImage.dao.LocationSenseIndexDAO;
import com.geoImage.tools.IOTools;

/**
 * 操作LocationSenseIndex当中的数据
 * @author hqn
 * @description   
 * @version
 * @update 2015-1-3 下午2:57:52
 */
public class StringIndexOfHawaii {

	public static void main(String[] args) {
		StringIndexOfHawaii sioh = new StringIndexOfHawaii();
		sioh.initialLocationSenseIndex();
	}
		/**
		 * 对LocationSenseindex进行初始化，以后直接进入LocationSenseIndex当中去找数据。
		 * 当前的数据只是Hawaii当中的数据
		 */
		public void initialLocationSenseIndex()
		{
			LocationSenseIndexDAO lsid = new LocationSenseIndexDAO();
			Map<String, Integer> data = getLocationSenseIndex();
			for(Iterator<String> iterator = data.keySet().iterator();iterator.hasNext();
					)
			{
				String next = iterator.next();
				LocationSenseIndex lsi = new LocationSenseIndex(data.get(next),next);
				lsid.save(lsi);
			}
		}
		/**
		 * 把存储在LoationSense当中的单词按照单词来编号，之后写入的就是这个单词的编号
		 * 
		 * @return
		 */
		Map<String, Integer> getLocationSenseIndex()
		{
			List<Object[]> listRes = IOTools.getDocIdAndText();
			Map<String, Integer> res = new HashMap<String, Integer>();
			Integer init = 1;
			for(int i=0;i<listRes.size();i++)
			{
				String next = (String)listRes.get(i)[1];
				if(res.get(next)==null)
				{
					res.put(next, init);
					init++;
				}
			}
			return res;
		}
}
