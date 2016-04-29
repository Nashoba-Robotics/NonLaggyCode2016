package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.lib.NavX;
import edu.nr.lib.network.AndroidData;
import edu.nr.lib.network.AndroidServer;
import edu.nr.lib.network.AndroidServerListener;
import edu.nr.robotics.Robot;
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
	}
	
	@Override
	protected boolean isFinishedNR() {	
		System.out.println("Checking drive angle: current count: " + currentCount + " drive angle error: " + command.getError());
		
		if(auton)
			return currentCount >= 8 && (Math.abs(Hood.getInstance().get() - hoodcommand.getSetpoint()) < 0.25);
		return false;
	}
	
	@Override
	protected void onEnd() {
		AndroidServer.getInstance().deregisterListener(this);
	}
	
	@Override
	protected void onStart() {
		AndroidServer.getInstance().registerListener(this);
		firstTime = false;
		currentCount = 0;
	}

	@Override
	public void onAndroidData(AndroidData data) {
		if(data.getDistance() == 0 && data.getTurnAngle() == 0)
			return;
		
		if(Math.abs(data.getTurnAngle()) <= .5 && firstTime == false) {
			hoodcommand.setAngleAgain();
			firstTime = true;
		}
		if(Math.abs(data.getTurnAngle()) > .5) {
			hoodcommand.setAngleAgain();
			command.setSetpoint(command.getSetpoint() + data.getTurnAngle()/4);
			firstTime = false;
		} 		
		if( Math.abs(data.getTurnAngle()) < RobotMap.TURN_THRESHOLD)
			currentCount++;
		else
			currentCount = 0;			
		
	}
	
}
