package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.RobotMap;

public class DriveWaitForAndroidAngleCommand extends NRCommand {

	int currentCount = 0;
	
	DriveAnglePIDCommand command;
	
	boolean auton;
	
	public DriveWaitForAndroidAngleCommand(DriveAnglePIDCommand command, boolean auton) {
		this.command = command;
		this.auton = auton;
	}

	@Override
	protected boolean isFinishedNR() {	
		
		
		System.out.println("Checking drive angle: current count: " + currentCount + " drive angle error: " + command.getError());
		
		if( Math.abs(command.getError()) < RobotMap.TURN_THRESHOLD)
			currentCount++;
		else
			currentCount = 0;			
		
		if(currentCount == 20)
			command.setSetpoint(command.getSetpoint() + AndroidServer.getInstance().getTurnAngle());
		
		if(auton)
			return currentCount > 10;
		return false;
	}
	
}
