package com.geoImage.ruleIntface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.geoImage.tools.IOTools;

public abstract class WriteFile<M> {
	
	public void writeContentFile(M m, String fileName) {

		BufferedWriter bw = null;
		try {
			File file = new File(fileName);
			bw = new BufferedWriter(new FileWriter(file,true));// 加在后面
			write(m, bw);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOTools.closeIO(null, bw);
		}
	}
	public abstract void write(M m,BufferedWriter bw) throws IOException;
	
}
