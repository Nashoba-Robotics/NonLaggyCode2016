package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.lib.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;

public class DriveWaitForAndroidAngleCommand extends NRCommand {

	int currentCount = 0;
	
	DriveAnglePIDCommand command;
	HoodJetsonPositionCommand hoodcommand;
	
	boolean firstTime = false;
	
	boolean auton;
	
	public DriveWaitForAndroidAngleCommand(DriveAnglePIDCommand drivecommand, HoodJetsonPositionCommand hoodcommand, boolean auton) {
		this.command = drivecommand;
		this.hoodcommand = hoodcommand;
		this.auton = auton;
	}

	double lastTime;
	
	@Override
	protected boolean isFinishedNR() {	
		
		if(lastTime == 0)
			lastTime = System.currentTimeMillis();
		
		System.out.println("Checking drive angle: current count: " + currentCount + " drive angle error: " + command.getError());
		
		if( Math.abs(AndroidServer.getInstance().getTurnAngle()) < RobotMap.TURN_THRESHOLD)
			currentCount++;
		else
			currentCount = 0;			
		
		if(Math.abs(AndroidServer.getInstance().getTurnAngle()) > .5)
			firstTime = true;
		
		//if(currentCount == 20)
		if(System.currentTimeMillis() - lastTime > 250) {
			if(Math.abs(AndroidServer.getInstance().getTurnAngle()) <= .5 && firstTime) {
				hoodcommand.setAngleAgain();
			}
			if(Math.abs(AndroidServer.getInstance().getTurnAngle()) > .5 || firstTime) {
				command.setSetpoint(command.getSetpoint() + AndroidServer.getInstance().getTurnAngle());
				System.out.println("Setting setpoint. New drive angle error: " + command.getError());
				lastTime = System.currentTimeMillis();
				firstTime = false;
			} 
		}
		
		if(auton)
			return currentCount > 5 && (Math.abs(Hood.getInstance().get() - hoodcommand.getSetpoint()) < 0.25);
		return false;
	}
	
}
