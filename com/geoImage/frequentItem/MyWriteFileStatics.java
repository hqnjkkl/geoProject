package com.geoImage.frequentItem;

import java.io.BufferedWriter;
import java.io.IOException;

import com.geoImage.ruleIntface.WriteFile;

/**
 * 写文件的一个类，写的格式不同，所以使用不同的类
 * @author hqn
 * @description   
 * @version
 * @update 2015-1-3 下午2:57:12
 */
public class MyWriteFileStatics extends WriteFile<String> {

	@Override
	public void write(String m, BufferedWriter bw) throws IOException {
		bw.write(m);
		bw.newLine();
	}

}
