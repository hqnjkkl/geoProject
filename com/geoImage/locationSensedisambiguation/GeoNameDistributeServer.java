package com.geoImage.locationSensedisambiguation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.geoImage.tools.IOTools;

public class GeoNameDistributeServer {

	/**
	 * @param args
	 */
	String fileName = null;
	Map<String, String> idMap = null;
	Integer listPort = 8089;
	Integer dis[] = new Integer[100];

	public GeoNameDistributeServer(String fileName) {
		this.fileName = fileName;
		idMap = IOTools.readPureMap(fileName);
	}

	public static void main(String[] args) {
		String fileName = "E:/hqn/新项目初识/图片地理位置研究/第十次会议(2014-3-28)/idDistribute.txt";
		GeoNameDistributeServer gds = new GeoNameDistributeServer(fileName);
		// gds.distributeIds();
		gds.testServer();

	}

	/**
	 * 获取map中元素最大的数字
	 * @return
	 */
	public Integer initialInteger() {
		Integer res = 0;
		for (Iterator it = idMap.values().iterator(); it.hasNext();) {
			Integer st = Integer.parseInt((String) it.next());
			if (res < st) {
				res = st;
			}
		}
		return res;
	}

	public void testServer() {
		ServerSocket ss = null;
		String hostAddress = null;
		Socket socket = null;
		ObjectOutputStream oos = null;
		SocketAddress sa = null;
		String ipAddress = null;
		Integer nextInteger = initialInteger();
		BufferedWriter bw = null;
		
		try {

			ss = new ServerSocket(listPort);

			System.out.println("server start at:" + listPort + ","
					+ ss.getLocalSocketAddress());
			bw = new BufferedWriter(new FileWriter(new File(fileName),true));
			while (true) {
				socket = ss.accept();
				System.out.println("connect successful!");
				sa = socket.getRemoteSocketAddress();
				String address = sa.toString();
				System.out.println("the remove host:" + sa);
				ipAddress = address.substring(1, address.indexOf(":"));
				System.out.println(ipAddress);
				oos = new ObjectOutputStream(socket.getOutputStream());
				
				if (idMap.get(ipAddress) != null) {
					nextInteger = Integer.parseInt(idMap.get(ipAddress));
//					oos.writeInt(nextInteger);
				} else {
					nextInteger = getNextId(nextInteger);
					bw.write(ipAddress+"\t"+nextInteger);
					bw.flush();
					bw.newLine();
					idMap.put(ipAddress, nextInteger.toString());
				}
				System.out.println(ipAddress+":"+nextInteger);
				oos.writeInt(nextInteger);
				oos.flush();
				oos.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ss.close();
				socket.close();
				oos.close();
				IOTools.closeIO(null, bw);
			} catch (Exception e2) {
			}
			 IOTools.writePureMapToFile(idMap, fileName);
		}
	}

	public void distributeIds() {
		boolean sc = true;
		boolean so = true;
		boolean oosb = true;
		int scc = 0;
		int soc = 0;
		int oosbc = 0;
		ServerSocket server = null;
		Socket socket = null;
		InetAddress iAddress = null;
		String clientHost = null;
		ObjectOutputStream oos = null;
		Integer nextId = 0;
		// int nextId = 0;
		scc = 0;
		try {

			while (true) {
				sc = true;
				try {
					server = new ServerSocket(listPort);
					System.out.println("server start at:" + listPort + ","
							+ server.getLocalSocketAddress());
				} catch (IOException e) {
					e.printStackTrace();
					sc = false;
					scc++;
				}
				// if (scc > 100) {
				// break;
				// }
				// if (!sc) {
				// continue;
				// } else {
				soc = 0;
				while (true) {
					so = true;
					try {
						socket = server.accept();
						System.out.println("connect sucessful:"
								+ socket.getRemoteSocketAddress());
					} catch (IOException e) {
						e.printStackTrace();
						so = false;
						soc++;
					}
					if (soc > 10) {
						break;
					}
					if (!so) {
						continue;
					} else {
						oosb = true;
						iAddress = socket.getLocalAddress();
						clientHost = iAddress.getHostAddress();
						System.out.println(iAddress.getHostAddress());
						nextId = getNextId(nextId);
						if (idMap.get(clientHost) == null) {
							idMap.put(clientHost, nextId.toString());
						} else {
							nextId = Integer.parseInt(idMap.get(clientHost));
						}
						try {
							oos = new ObjectOutputStream(
									socket.getOutputStream());
							oos.write(nextId);
						} catch (IOException e) {
							e.printStackTrace();
							oosb = false;
						}
						break;
					}

					// }
				}
			}
		} catch (Exception e) {
		} finally {
			IOTools.writePureMapToFile(idMap, fileName);
			try {
				socket.close();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public int getNextId(int id) {
		return id + LocationSenseScore.dataJump;
	}
}
