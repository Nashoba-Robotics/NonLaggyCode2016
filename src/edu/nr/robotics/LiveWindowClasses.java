package edu.nr.robotics;

import edu.nr.lib.livewindow.LiveWindowBoolean;
import edu.nr.lib.livewindow.LiveWindowNumber;

public class LiveWindowClasses {

	public static LiveWindowBoolean readyToShoot = new LiveWindowBoolean("Ready to shoot", false);
	public static LiveWindowNumber shooterOutput = new LiveWindowNumber("Shooter Output", 0);
	public static LiveWindowNumber shooterCurrent = new LiveWindowNumber("Shooter Current", 0);
	public static LiveWindowNumber intakeRollerSpeed = new LiveWindowNumber("Intake Roller Speed", 0);
	
	public static LiveWindowNumber driveEncodersDistance = new LiveWindowNumber("Drive Encoders Distance", 0);
	public static LiveWindowNumber driveEncodersSpeed = new LiveWindowNumber("Drive Encoders Speed", 0);

	public static LiveWindowNumber elevatorSpeed = new LiveWindowNumber("Elevator Speed", 0);
	
	public static LiveWindowBoolean intakeArmBottomSwitch = new LiveWindowBoolean("Intake Arm Bottom Switch", false);
	public static LiveWindowBoolean intakeArmTopSwitch = new LiveWindowBoolean("Intake Arm Top Switch", false);
	public static LiveWindowBoolean hoodBottomSwitch = new LiveWindowBoolean("Hood Bottom Switch", false);
	public static LiveWindowBoolean hoodTopSwitch = new LiveWindowBoolean("Hood Top Switch", false);

}
