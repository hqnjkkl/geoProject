package com.geoImage.optics;

import java.util.ArrayList;
import java.util.List;

import com.geoImage.dao.GeotaggedData;

import gnu.trove.TIntProcedure;

public class MyProcedure implements TIntProcedure {
	
	private List<Integer> ids = new ArrayList<Integer>();
	
	@Override
	public boolean execute(int id) {
		
		ids.add(id);
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public List<Integer> getIds() {
		return ids;
	}
	
	

}
