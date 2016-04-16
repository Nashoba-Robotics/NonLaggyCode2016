package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.lib.navx.NavX;
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

	double lastTime;
	
	@Override
	protected boolean isFinishedNR() {	
		
		if(lastTime == 0)
			lastTime = System.currentTimeMillis();
		
		System.out.println("Checking drive angle: current count: " + currentCount + " drive angle error: " + command.getError());
		
		if( Math.abs(command.getError()) < RobotMap.TURN_THRESHOLD)
			currentCount++;
		else
			currentCount = 0;			
		
		//if(currentCount == 20)
		if(System.currentTimeMillis() - lastTime > 250) {
			command.setSetpoint(command.getSetpoint() + AndroidServer.getInstance().getTurnAngle());
		
			lastTime = System.currentTimeMillis();
		}
		
		if(auton)
			return currentCount > 40;
		return false;
	}
	
}
