package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.RobotMap;

public class DriveWaitForAndroidAngleCommand extends NRCommand {

	int currentCount = 0;
	
	@Override
	protected boolean isFinishedNR() {
    	if(!AndroidServer.getInstance().goodToGo()) { 
    		System.out.println("Android connection not good to go");
    		return false;
    	}
    	
    	System.out.println("Android server wait angle: " + Math.abs(AndroidServer.getInstance().getTurnAngle()) + " current count: " + currentCount);
		
		if( Math.abs(AndroidServer.getInstance().getTurnAngle()) < RobotMap.TURN_THRESHOLD)
			currentCount++;
		
		return currentCount > 3;
	}
	
}
