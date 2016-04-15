package edu.nr.robotics.commandgroups;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.lib.navx.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveWaitForAndroidAngleCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.intakearm.IntakeArmHomeHeightCommandGroup;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AlignCommandGroup extends CommandGroup {
    
	
    public  AlignCommandGroup() {
    	addParallel(new IntakeArmHomeHeightCommandGroup());
    	addSequential(new WaitCommand(0.25));
        addParallel(new DriveAnglePIDCommand());
        addSequential(new HoodJetsonPositionCommand());
        addSequential(new DriveWaitForAndroidAngleCommand());

        addSequential(new WaitCommand(0.25));
    }
    
    @Override
    public void end() {
    	if(!AndroidServer.getInstance().goodToGo()) { 
    		System.out.println("Android connection not good to go");
    		return;
    	}
    	
    	System.out.println("Align command group started ending");
		System.out.println("Hood: " + (Math.abs(Hood.getInstance().get() - 
				Hood.distanceToAngle(AndroidServer.getInstance().getDistance())) > RobotMap.HOOD_THRESHOLD));
		System.out.println("Turn:  " + Math.abs(AndroidServer.getInstance().getTurnAngle()) + " " + (Math.abs(AndroidServer.getInstance().getTurnAngle()) > RobotMap.TURN_THRESHOLD));
		System.out.println("Shooter: " + Shooter.getInstance().getScaledSpeed() + " " + (Shooter.getInstance().getScaledSpeed() < RobotMap.SHOOTER_FAST_SPEED - RobotMap.SHOOTER_THRESHOLD));

    	if(OI.getInstance().alignButton.get()) {
    		boolean flag = false;
    		if(Math.abs(Hood.getInstance().get() - Hood.distanceToAngle(AndroidServer.getInstance().getDistance())) > RobotMap.HOOD_THRESHOLD ) {flag = true;}
    		if(Math.abs(AndroidServer.getInstance().getTurnAngle()) > RobotMap.TURN_THRESHOLD) {flag = true;}
    		if(Shooter.getInstance().getScaledSpeed() < RobotMap.SHOOTER_FAST_SPEED - RobotMap.SHOOTER_THRESHOLD) {flag = true;}
    		if(flag) {
	    		System.out.println("Starting align again angle: " + NavX.getInstance().getYaw(AngleUnit.DEGREE) + " shooter speed: " + Shooter.getInstance().getScaledSpeed() + " Hood angle " + Hood.getInstance().get());
	    		
	    		OI.getInstance().alignCommand = new AlignCommandGroup();
	    		OI.getInstance().alignCommand.start();
	    		return;
    		}
    	}
    	System.out.println("Ended align correction");
    }
    
}
