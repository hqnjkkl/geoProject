package com.geoImage.frequentItem;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.geoImage.ruleIntface.WriteFile;

/**
 * 这个就是一个对文件进行写的一个类
 * @author hqn
 * @description   
 * @version
 * @update 2015-1-3 下午2:55:47
 */
public class MyWriteFile extends WriteFile<Map<Set<String>,Integer>> {
	/**
	 * 输入参数Map，写入文件
	 */
	@Override
	public void write(Map<Set<String>, Integer> m, BufferedWriter bw)
			throws IOException {
		for(Iterator<Set<String>> it = m.keySet().iterator();it.hasNext();)
		{
			Set<String> tmpSet = it.next();
			for(Iterator<String> its = tmpSet.iterator();its.hasNext();)
			{
				bw.write(its.next());
				if(its.hasNext())
				{
					bw.write("--");
				}
			}
			bw.write("->");
			bw.write(m.get(tmpSet).toString());
			bw.newLine();
		}
	}
}
