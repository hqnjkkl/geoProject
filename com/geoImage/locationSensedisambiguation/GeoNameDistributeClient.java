package com.geoImage.locationSensedisambiguation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class GeoNameDistributeClient {

	/**
	 * @param args
	 */
	String serverAddress = "10.14.39.83";
	Integer serverPort = 8089;
	public static void main(String[] args) {
		GeoNameDistributeClient gdcClient = new GeoNameDistributeClient();
		gdcClient.getIds();
//		gdcClient.testClient();
	}
	
	public int testClient()
	{
		Socket socket = null;
		int a = 0; 
		SocketAddress sa;
		try {
			socket= new Socket();
			System.out.println("InetAddress.getLocalHost():"+InetAddress.getLocalHost());
			sa = new InetSocketAddress("10.14.39.83", serverPort);
			socket.connect(sa, 10000);

//			socket = new Socket(serverAddress, serverPort);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			a = ois.readInt();
			ois.close();
			System.out.println("a:"+a);
		}catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				socket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return a;
	}
	public int getIds()
	{
		Socket  socket = null;
		InetSocketAddress isAddress = null;
		InputStream is = null;
		
		isAddress = new InetSocketAddress(serverAddress, serverPort);
		System.out.println(isAddress);
		int id = 0;
		boolean getServer = true;
		boolean getId = true;
		while(true)
		{
			getServer = true;
		try {
			socket = new Socket(serverAddress, serverPort);
			is = socket.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			getServer = false;
		}
//		if (!getServer) {
//			continue;
//		}else {
			while(true)
			{				
				getId = true;
				System.out.println("getId:"+getId);
				ObjectInputStream ois;
				try {
					ois = new ObjectInputStream(is);
					id = ois.readInt();
					System.out.println("id:"+id);
				} catch (IOException e) {
					e.printStackTrace();
					getId =false;
				}
				if(!getId)
				{
					continue;
				}else {
					System.out.println(id);
					return id;
				}
			}
			}
//		}
		
	}

}
