package edu.nr.robotics.auton;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.navx.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveWaitForAndroidAngleCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;
import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmHomeHeightCommandGroup;
import edu.nr.robotics.subsystems.loaderroller.LaserCannonTriggerCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutonAlignCommand extends CommandGroup {
    	
    public  AutonAlignCommand() {
    	addParallel(new IntakeArmHomeHeightCommandGroup());
    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());
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

    	boolean flag = false;

    	if(Hood.getInstance().get() - Hood.distanceToAngle(AndroidServer.getInstance().getDistance()) > RobotMap.HOOD_THRESHOLD ) {flag = true;}
    	if(Math.abs(AndroidServer.getInstance().getTurnAngle()) > RobotMap.TURN_THRESHOLD) {flag = true;}
    	if(Shooter.getInstance().getScaledSpeed() < RobotMap.SHOOTER_FAST_SPEED - RobotMap.SHOOTER_THRESHOLD) {flag = true;}
    	if(flag) {
    		new AutonAlignCommand().start();
    		System.out.println("Starting auton align again angle: " + NavX.getInstance().getYaw(AngleUnit.DEGREE) + " shooter speed: " + Shooter.getInstance().getScaledSpeed() + " Hood angle " + Hood.getInstance().get());
    		return;
    	}

    	new LaserCannonTriggerCommand().start();
    	System.out.println("Auton Align ended and shot - angle: " + NavX.getInstance().getYaw(AngleUnit.DEGREE));
    }
}
