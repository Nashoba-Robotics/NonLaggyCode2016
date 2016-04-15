package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;

public class ShooterSpeedCommand extends NRCommand {

	double goalSpeed;
	
	boolean useSwitch;
	
	double kP = 0.75;
	double kF = 1;
	
	/**
	 * Go to a speed
	 * @param goalSpeed
	 */
    public ShooterSpeedCommand(double goalSpeed) {
    	this.goalSpeed = goalSpeed;
    	this.useSwitch = false;
    	requires(Shooter.getInstance());
    }
    
    /**
     * Use the shooter switch
     */
    public ShooterSpeedCommand() {
    	this.goalSpeed = 0;
    	this.useSwitch = true;
    	requires(Shooter.getInstance());
    }
    
    @Override
	protected void onExecute() {
    	
    	
    	if(useSwitch) {
        	if(Robot.getInstance().isAutonomous()) {
    			goalSpeed = RobotMap.SHOOTER_FAST_SPEED;
        	} else {
	    		if(OI.getInstance().shooterOn()) {
	    			goalSpeed = RobotMap.SHOOTER_FAST_SPEED;
	    		} else {
	    			goalSpeed = 0;
	    		}
        	}
    	}
    	
		if(goalSpeed == 0) {
			Shooter.getInstance().setMotor(0);
		} else {
	    	double speed = Shooter.getInstance().getScaledSpeed();
	    	double p = kP * (goalSpeed - speed);
	    	double f = kF * goalSpeed;
	    	
	    	double output = p + f;
	    	Shooter.getInstance().setMotor(output);
		}
	}
	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
}