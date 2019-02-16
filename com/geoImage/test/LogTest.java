package com.geoImage.test;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogTest {

		static Logger log4j = Logger.getLogger(LogTest.class);
		public LogTest(){
			System.out.println("hello, I am HMain");

			printLog();
		}
		
		public static void main(String[] args) {
			LogTest log4jTest = new LogTest();
			
		}
		
		private void printLog(){
			BasicConfigurator.configure();
			PropertyConfigurator.configure("src/log4j.properties");
			
//			DOMConfigurator.configure("src/log4j.properties");
			
			log4j.debug("log4j debug");
			log4j.info("log4j info");
			log4j.warn("log4j warn");
			log4j.error("log4j error");
			log4j.fatal("log4j fatal");
		}
}
