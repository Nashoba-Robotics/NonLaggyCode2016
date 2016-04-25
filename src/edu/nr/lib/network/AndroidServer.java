package edu.nr.lib.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AndroidServer implements Runnable {
	
	private static AndroidServer singleton;
	
	private ArrayList<AndroidServerListener> listeners = new ArrayList<>();
	
	public static AndroidServer getInstance() {
		if(singleton == null)
			init();
		return singleton;
	}
	
	public static void init() {
		if(singleton == null)
			singleton = new AndroidServer();
	}
	

	public static final int defaultPort = 5432;
	private static final String defaultIpAddress = "127.0.0.1";
		
	AndroidData data;
	
	boolean goodToGo = false;

	@Override
	public void run() {
		
		while(true) {
			try {
				goodToGo = true;
				setData(0,0,System.currentTimeMillis());
				Socket clientSocket;
				try {
					clientSocket = new Socket(defaultIpAddress, defaultPort);
					try {
						while(true) {
							BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							String message = inFromServer.readLine();
							if(message == null) {
								clientSocket.close();
								setData(0,0,System.currentTimeMillis());
								goodToGo = false;
								Thread.sleep(1000);								
								break;
							} 								
							goodToGo = true;
							int x = message.indexOf(':');
							if (x > 0) {
								String left = message.substring(0, x);
							    String right = message.substring(x+1);
							    try {
							    	double distance = Double.valueOf(left)*.97;
							    	double turnAngle = -Double.valueOf(right);
							    	setData(turnAngle, distance, System.currentTimeMillis());
								    //System.out.println("Angle: " + turnAngle + " Distance: " + distance);
								    SmartDashboard.putNumber("Camera distance", distance);
								    SmartDashboard.putNumber("Camera angle", turnAngle);
							    } catch (NumberFormatException e) {
							    	System.err.println("Coudln't parse number from Nexus. Recieved Message: " + message);
							    }
							}
						}
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
						clientSocket.close();
						setData(0,0,System.currentTimeMillis());
						goodToGo = false;
					} 
				} catch (UnknownHostException e) {
					System.out.println("Unknown host to connect to");
				} catch (ConnectException e) {
					goodToGo = false;
					Thread.sleep(1000);
				} catch (IOException e) {
					e.printStackTrace();
				} 
			} catch(Exception e) {
				
			}
		}
	}

	public double getTurnAngle() {
		return data.getTurnAngle();
	}

	public double getDistance() {
		return data.getDistance();
	}
	
	public boolean goodToGo() {
		return goodToGo;
	}
	
	public void registerListener(AndroidServerListener listener) {
		listeners.add(listener);
	}
	
	public void deregisterListener(AndroidServerListener listener) {
		listeners.remove(listener);
	}
	
	public void setData(double turnAngle, double distance, long time) {
		data = new AndroidData(turnAngle, distance, time);
		listeners.forEach((listener) -> {
			listener.onAndroidData(data);
		});
	}
	
	public static void executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(output.toString());

	}

}