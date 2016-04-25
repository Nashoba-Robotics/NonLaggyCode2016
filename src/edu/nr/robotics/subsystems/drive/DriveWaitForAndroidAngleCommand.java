package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.nr.lib.network.AndroidData;
import edu.nr.lib.network.AndroidServer;
import edu.nr.lib.network.AndroidServerListener;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;

public class DriveWaitForAndroidAngleCommand extends NRCommand implements AndroidServerListener {

	int currentCount = 0;
	
	DriveAnglePIDCommand command;
	HoodJetsonPositionCommand hoodcommand;
	
	boolean firstTime = false;
	
	boolean auton;
	
	public DriveWaitForAndroidAngleCommand(DriveAnglePIDCommand drivecommand, HoodJetsonPositionCommand hoodcommand, boolean auton) {
		this.command = drivecommand;
		this.hoodcommand = hoodcommand;
		this.auton = auton;
		AndroidServer.getInstance().registerListener(this);
	}
	
	@Override
	protected boolean isFinishedNR() {	
		System.out.println("Checking drive angle: current count: " + currentCount + " drive angle error: " + command.getError());
		
		if(auton)
			return currentCount > 5 && (Math.abs(Hood.getInstance().get() - hoodcommand.getSetpoint()) < 0.25);
		return false;
	}
	
	@Override
	protected void onEnd() {
		AndroidServer.getInstance().deregisterListener(this);
	}

	@Override
	public void onAndroidData(AndroidData data) {
		if(Math.abs(data.getTurnAngle()) > .5)
			firstTime = true;

		if(Math.abs(data.getTurnAngle()) <= .5 && firstTime) {
			hoodcommand.setAngleAgain();
		}
		if(Math.abs(data.getTurnAngle()) > .5 || firstTime) {
			command.setSetpoint(command.getSetpoint() + data.getTurnAngle());
			System.out.println("Setting setpoint. New drive angle error: " + command.getError());
			firstTime = false;
		} 		
		if( Math.abs(data.getTurnAngle()) < RobotMap.TURN_THRESHOLD)
			currentCount++;
		else
			currentCount = 0;			
		
	}
	
}
