package edu.nr.robotics.commandgroups;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.lib.navx.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AlignCommandGroup extends CommandGroup {
    
	
	public enum State {
		ALIGNING, WAITING, OFF
	}
	
    public  AlignCommandGroup() {
    	addSequential(new AlignStartCommand());
    	addSequential(new WaitCommand(0.25));
        addSequential(new AlignSubcommandGroup());
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
		/*Robot.getInstance().state = State.WAITING;
    	while((!OI.getInstance().fireButton.get() || !OI.getInstance().backupFireButton.get()) && OI.getInstance().alignButton.get()) {}
    	if((OI.getInstance().fireButton.get() || OI.getInstance().backupFireButton.get())) {
			Robot.getInstance().fireCommand = new LaserCannonTriggerCommand();
			Robot.getInstance().fireCommand.start();
	    	System.out.println("Align ended after shot - angle: " + NavX.getInstance().getYaw(AngleUnit.DEGREE));
    	} else {
        	System.out.println("Align ended due to button release - angle: " + NavX.getInstance().getYaw(AngleUnit.DEGREE));
    	}
    	Robot.getInstance().state = State.OFF;
    	System.out.println("Ended align waiting");
    	LiveWindowClasses.readyToShoot.set(false);*/
    }
    
    @Override
    public void interrupted() {
    	IntakeArm.getInstance().disable();
    	Hood.getInstance().disable();
    }
    
    @Override
	public void initialize() {
    	System.out.println("Align started - angle: " + NavX.getInstance().getYaw(AngleUnit.DEGREE));
    }
    
    public class AlignStartCommand extends NRCommand {
    	
    	public AlignStartCommand() {
    		//addSequential(new IntakeArmHomeHeightCommandGroup());
    	}
    	
    	@Override
    	public void onStart() {
    		Robot.getInstance().state = State.ALIGNING;
    	}
    }
}
