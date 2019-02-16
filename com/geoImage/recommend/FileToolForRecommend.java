package com.geoImage.recommend;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import com.geoImage.ruleIntface.WriteFile;

public class FileToolForRecommend extends WriteFile<List<String>> {

	@Override
	public void write(List<String> m, BufferedWriter bw) throws IOException {
		for (int i = 0; i < m.size(); i++) {
			bw.write(m.get(i));
			bw.newLine();
		}
	}
}
