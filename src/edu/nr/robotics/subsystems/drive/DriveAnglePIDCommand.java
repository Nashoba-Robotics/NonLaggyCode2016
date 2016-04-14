package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleGyroCorrectionSource;
import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveAnglePIDCommand extends NRCommand {

	double totalError = 0;
	
	double angle;
	AngleController controller;
	AngleGyroCorrectionSource correction;
	
	double integralDisableDistance = 5;

	double accuracyFinishCount = 0;
	double currentCount = 0;
	
	boolean useJetson;
	
	boolean goodToGo = true;

	
    public DriveAnglePIDCommand(boolean useJetson) {
    	this.correction = new AngleGyroCorrectionSource(AngleUnit.DEGREE);
    	this.useJetson = useJetson;
    	requires(Drive.getInstance());

    }
    
    public DriveAnglePIDCommand(double angle, AngleUnit unit) {
    	this(angle, new AngleGyroCorrectionSource(unit));    	
    }

    public DriveAnglePIDCommand(double angle, AngleGyroCorrectionSource correction) {
    	this.angle = angle;
    	this.correction = correction;
    	this.useJetson = false;
    	requires(Drive.getInstance());
	}
    
    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	double output = 0;
    	
    	final double TURN_P = SmartDashboard.getNumber("Turn P");
    	final double TURN_I = SmartDashboard.getNumber("Turn I");

		if(Math.abs(correction.pidGet()-angle) > integralDisableDistance) {
			totalError = 0;
		} else {
			totalError += TURN_I*(correction.pidGet()-angle);
		}
		
		if(Math.signum(correction.pidGet()-angle) != Math.signum(totalError)) {
			totalError = 0;
		}
		
		output += TURN_P*(correction.pidGet()-angle);

		output += totalError;
		if(Math.abs(output) < 0.1) {
			output = 0.1 * Math.signum(output);
		} else if(Math.abs(output) > 0.3) {
			output = 0.3 * Math.signum(output);
		}
		
		output *= -1;
		controller.pidWrite(output);
		
    	SmartDashboard.putNumber("Drive Turn Error", correction.pidGet()-angle);
    	SmartDashboard.putNumber("Drive Turn Total Error", totalError);
    	SmartDashboard.putNumber("Drive Turn Output", output);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
    	if(!goodToGo)
    		return true;
    	if(Math.abs(correction.pidGet()-angle) < RobotMap.TURN_THRESHOLD) {
    		currentCount++;
    	} else {
    		currentCount = 0;
    	}
    	return currentCount > accuracyFinishCount;
    }

	@Override
	protected void onEnd(boolean interrupted) {
		if(!interrupted && goodToGo) {
			correction.reset();
		}
		totalError = 0;
		goodToGo = true;

	}

	@Override
	protected void onStart() {
		
		if(useJetson) {
			if(!AndroidServer.getInstance().goodToGo()) { 
	    		System.out.println("Android connection not good to go");
	    		goodToGo = false;
	    		return;
	    	}
			
			angle = AndroidServer.getInstance().getTurnAngle();

			if(Math.abs(angle) < RobotMap.TURN_THRESHOLD) {
				goodToGo = false;
			}
		}
		
		Drive.getInstance().setPIDEnabled(true);
		controller = new AngleController();
		correction.reset();
	}
}
