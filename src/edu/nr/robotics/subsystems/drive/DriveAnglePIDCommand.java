package edu.nr.robotics.subsystems.drive;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.nr.lib.AngleGyroCorrectionSource;
import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.lib.navx.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.auton.AutonFollowInstructionsShootCommand.GetGyro;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveAnglePIDCommand extends NRCommand {

	NRPID pid;
	
	double angle;
	AngleController controller;
	AngleGyroCorrectionSource correction;
	
	public ArrayList<DegreeTimeTuple> gyroData;

	
	double integralDisableDistance = 5;
	double currentCount = 0;
	
	boolean useAndroid;
	
	boolean goodToGo = true;
	
	GetGyro getGyro;
	
	boolean resetCorrection;

	/**
	 * Create one for using the Android to communicate
	 */
    public DriveAnglePIDCommand() {
    	this(0, new AngleGyroCorrectionSource(AngleUnit.DEGREE), true);
    	this.useAndroid = true;
    }
    
    public DriveAnglePIDCommand(double angle, AngleUnit unit) {
    	this(angle, new AngleGyroCorrectionSource(unit), true);    	
    	this.useAndroid = false;
    }
    
    public DriveAnglePIDCommand(double angle, GetGyro getGyro) {
    	this(angle, null, false);  
    	this.getGyro = getGyro;
    	this.useAndroid = false;
    }

    private DriveAnglePIDCommand(double angle, AngleGyroCorrectionSource correction, boolean resetCorrection) {
    	this.angle = angle;
    	this.correction = correction;
    	requires(Drive.getInstance());
    	this.resetCorrection = resetCorrection;
    	controller = new AngleController();
		gyroData = new ArrayList<>();
		pid = new NRPID(RobotMap.TURN_P,RobotMap.TURN_I,RobotMap.TURN_D,0, correction, controller);
		System.out.println("Started angle PID command");
		
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
    	if(!goodToGo)
    		return true;
    	if(Math.abs(pid.getError()) < RobotMap.TURN_THRESHOLD) {
    		currentCount++;
    	} else {
    		currentCount = 0;
    	}
    	
    	if(useAndroid) {
			if(currentCount > 4)
				pid.setPermaOut(pid.getOut());
	
	    	
	    	System.out.println("current count: " + currentCount + " use perma out: " + pid.isPermaOut());
	    	return false;
    	}
    	return currentCount > 4;
    }

	@Override
	protected void onEnd(boolean interrupted) {
		pid.disable();
		pid.reset();
		goodToGo = true;
		gyroData.clear();
	}
	
	@Override
	protected void onExecute() {
		SmartDashboard.putString("GyroPID", correction.pidGet() + ":" + pid.getSetpoint());
		SmartDashboard.putString("GyroPIDAngle", correction.pidGet() +"");
		
		System.out.println("Drive Angle PID Command running");
		
	}

	@Override
	protected void onStart() {
		pid.setPID(DriveGyroAngleSmartDashboardCommand.turn_p,DriveGyroAngleSmartDashboardCommand.turn_i,DriveGyroAngleSmartDashboardCommand.turn_d,0);
		if(useAndroid) {
			if(!AndroidServer.getInstance().goodToGo()) { 
	    		System.out.println("Drive Angle PID: Android connection not good to go");
	    		goodToGo = false;
	    		return;
	    	}
			
			angle = AndroidServer.getInstance().getTurnAngle();

			if(Math.abs(angle) < RobotMap.TURN_THRESHOLD) {
				goodToGo = false;
				return;
			}
			

		}
		
		if(getGyro != null) {
			correction = getGyro.getCorrection();
			pid.setSource(correction);
		}
		
		if(correction == null)
			correction = new AngleGyroCorrectionSource(AngleUnit.DEGREE);
		
		pid.setSetpoint(angle);
		
		Drive.getInstance().setPIDEnabled(true);
		controller = new AngleController();
		if(resetCorrection) {
			correction.reset();
			System.out.println("Resetting correction");
		}
		pid.enable();
	}

	public double getError() {
		return pid.getError();
	}

	public void setSetpoint(double turnAngle) {
		pid.setSetpoint(turnAngle);
	}
	
	public class NRPID {

		double p,i,d,f;
		
		PIDSource source;
		PIDOutput output;
		
		double setpoint = 0;
		
		double totalError = 0;
		
		double lastTime, lastError = 0;
		
		boolean enabled = false;
		
		Timer scheduler;
		
		double maxOutput = 1;
		
		double lastOut = 0;
		
		boolean usePermaOut = false;
		double permaOut;
		
		public NRPID(double p, double i, double d, double f, PIDSource source, PIDOutput output) {
			this.p = p;
			this.d = d;
			this.i = i;
			this.f = f;
			this.source = source;
			this.output = output;
			lastTime = System.currentTimeMillis();
			
			scheduler = new Timer();
			scheduler.schedule(new TimerTask() {
				@Override
				public void run() {
					calculate();
				}	
			}, 0, 20);		
		}
		
		public void setSource(PIDSource source) {
			this.source = source;
		}

		public boolean isPermaOut() {
			return usePermaOut;
		}

		public void setSetpoint(double setpoint) {
			this.setpoint = setpoint;
			totalError = 0;
			lastError = getError();

			usePermaOut = false;
		}
		
		public void enable() {
			lastError = getError();
			lastTime = System.currentTimeMillis();
			enabled = true;
		}
		
		public void disable() {
			enabled = false;
			output.pidWrite(0);
		}
		
		public double getError() {
			return setpoint - source.pidGet();
		}
		
		private void calculate() {
			if(enabled) {
				if(usePermaOut) {
					System.out.println("Perma out: " + permaOut);
					output.pidWrite(permaOut);
				} else {
	
					double error = getError();
					double out = 0;
					
					out += p * error;
					out += f * setpoint;
					
					totalError += error * i;
					
					out += i * totalError;
					
					double dt = (System.currentTimeMillis() - lastTime)/1000.0;
					lastTime = System.currentTimeMillis();
					
					double dE = error - lastError;
					lastError = error;
					
					out += d * dE/dt;
					if(Math.abs(out) > maxOutput) 
						out = Math.signum(out) * maxOutput;
					output.pidWrite(out);
					lastOut = out;					
				}
				gyroData.add(new DegreeTimeTuple(System.currentTimeMillis(), NavX.getInstance().getYaw(AngleUnit.DEGREE)));
			}
		}

		public void setMaxOutput(double maxOutput) {
			this.maxOutput = maxOutput;
		}

		public boolean isEnable() {
			return enabled;
		}

		public void reset() {
			if(enabled)
				disable();
			
			totalError = 0;
		}

		public double getSetpoint() {
			return setpoint;
		}

		public void setPID(double p, double i, double d, double f) {
			this.p = p;
			this.d = d;
			this.i = i;
			this.f = f;		
		}
		
		public double getOut() {
			return lastOut;
		}
		
		public void setPermaOut(double out) {
			this.permaOut = out;
			usePermaOut = true;
		}
	}

	public double getSetpoint() {
		return pid.getSetpoint();
	}
	
	public double getGyroErrorAtTime(long time) {
		
		if(time == 0) {
			DegreeTimeTuple first = gyroData.get(0);
			if(first != null)
				return first.getDegree();
			return 0;
		}
		
		double bestDegreeSoFar = 0;
		
		long bestSoFarError = time;
		
		for(DegreeTimeTuple tuple : gyroData) {
			
			long currentError = Math.abs(tuple.getTime() - time);
			
			if(currentError < bestSoFarError) {
				bestDegreeSoFar = tuple.getDegree();
				bestSoFarError = currentError;
			}
		}
		
		return bestDegreeSoFar;
		
	}
	
	public class DegreeTimeTuple {
			
		private long time;
		private double degree;
		
		public DegreeTimeTuple(long time, double degree) {
			this.time = time;
			this.degree = degree;
		}

		public long getTime() {
			return time;
		}

		public double getDegree() {
			return degree;
		}
	}

}

