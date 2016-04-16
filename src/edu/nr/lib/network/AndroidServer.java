package edu.nr.lib.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AndroidServer implements Runnable {
	
	private static AndroidServer singleton;
	
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
	
	
	double distance;
	double turnAngle;
	
	boolean goodToGo = false;

	@Override
	public void run() {
		
		while(true) {
			try {
				goodToGo = true;
				distance = 0;
				turnAngle = 0;
				Socket clientSocket;
				try {
					clientSocket = new Socket(defaultIpAddress, defaultPort);
					try {
						while(true) {
							BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							String message = inFromServer.readLine();
							if(message == null) {
								clientSocket.close();
								distance = 0;
								turnAngle = 0;
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
							    	distance = Double.valueOf(left)*.97;
							    	turnAngle = -Double.valueOf(right);
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
						distance = 0;
						turnAngle = 0;
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
		return turnAngle;
	}

	public double getDistance() {
		return distance;
	}
	
	public boolean goodToGo() {
		return goodToGo;
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