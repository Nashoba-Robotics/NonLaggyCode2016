package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.PID;
import edu.nr.lib.TalonEncoder;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.EnabledSubsystems;
import edu.nr.robotics.LiveWindowClasses;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Hood extends Subsystem implements SmartDashboardSource, Periodic, PIDOutput, PIDSource {
    
	public static final double MAX_ACC = 100;
	public static final double MAX_VEL = 30;
	
	public static final double MAX_RPM = 83.325;
	
	
	CANTalon talon;	
	TalonEncoder enc;
	PID pid;
	
	//Max acceleration for motion profiling is 100 degrees per second per second
	//Max velocity for motion profiling is 30 degrees per second
	
	double maxSpeed = 1.0;
	
	private static Hood singleton;
	
	public enum Position {
		BOTTOM (RobotMap.HOOD_BOTTOM_POSITION), TOP(RobotMap.HOOD_TOP_POSITION);
		
		public final double pos;
		Position(double position) {
			this.pos = position;
		}
		
		private static boolean isAtPosition(Position pos, double angle) {
			return pos.pos < angle + RobotMap.HOOD_THRESHOLD && pos.pos > angle - RobotMap.HOOD_THRESHOLD;
		}
		
		private static boolean isAtPosition(Position pos) {
			return isAtPosition(pos,Hood.getInstance().enc.get());
		}
		
		public boolean isAtPosition() {
			return isAtPosition(this);
		}
		
	}
	
	private Hood() {
		if(EnabledSubsystems.hoodEnabled) {
			talon = new CANTalon(RobotMap.HOOD_TALON);
			talon.enableBrakeMode(true);
			
			talon.changeControlMode(TalonControlMode.Speed);
			
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			
			talon.setEncPosition(0);
			
			talon.configEncoderCodesPerRev(256);
			
			talon.setF(7.13);
			talon.setP(10);
			
			enc = new TalonEncoder(talon,true);
			enc.setPIDSourceType(PIDSourceType.kDisplacement);
			enc.setDistancePerRev(RobotMap.HOOD_TICK_TO_ANGLE_MULTIPLIER);
			pid = new PID(0.25, 0.00, 0.001, enc, talon);
			
			LiveWindow.addSensor("Hood", "Bottom Switch", LiveWindowClasses.hoodBottomSwitch);
			LiveWindow.addSensor("Hood", "Top Switch", LiveWindowClasses.hoodTopSwitch);
			
			LiveWindow.addSensor("Hood", "PID", pid);
		}
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new HoodJoystickCommand());
	}
	
	public void resetEncoder() {
		if(enc != null)
			enc.reset();
	}
	
	public static Hood getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Hood();
		}
	}
	
	/**
	 * Disable the PID and set the motor to the given speed
	 * @param speed the speed to set the motor to, from -1 to 1
	 */
	public void setMotor(double speed) {
		if(pid != null)
			pid.disable();
		if(talon != null)
			talon.setSetpoint(speed * MAX_RPM);
	}
	
	/**
	 * Set the PID setpoint
	 * @param value the value to set the setpoint to
	 */
	public void setSetpoint(double value) {
		if(pid != null) {
			if(!pid.isEnable())
				pid.enable();
			pid.setSetpoint(value);	
		}
	}
	
	/**
	 * Get the PID setpoint
	 * @return the PID setpoint
	 */
	public double getSetpoint() {
		if(pid != null)
			return pid.getSetpoint();
		return 0;
	}
	
	/**
	 * Enable the PID
	 */
	public void enablePID() {
		if(pid != null)
			pid.enable();
	}
	
	/**
	 * Disable the PID
	 */
	public void disablePID() {
		if(pid != null)
			pid.disable();
	}
	
	/**
	 * Gets whether the PID is enabled or not
	 * @return whether the PID is enabled
	 */
	public boolean isPIDEnabled() {
		if(pid != null)
			return pid.isEnable();
		return false;
	}
	
	/**
	 * Gets the value of the encoder
	 * @return the value of the encoder
	 */
	public double getDisplacement() {
		if(enc != null)
			return enc.get() ;
		return 0;
	}
	
	/**
	 * Gets whether the motor is still moving
	 * @return whether the motor is still moving
	 */
	public boolean isMoving() {
		if(enc != null)
			return Math.abs(enc.getRateWithoutScaling()) > 0.05;
		return false;
		//0.05 is a number I just made up
	}
	
	public void setMaxSpeedPID(double speed) {
		maxSpeed = speed;
		if(pid != null)
			pid.setOutputRange(0,speed);
	}
	
	@Override
	public void smartDashboardInfo() {
		SmartDashboard.putNumber("Hood Angle", getDisplacement());
		SmartDashboard.putNumber("Hood Distance", angleToDistance(getDisplacement()));
		if(enc != null) {
			SmartDashboard.putNumber("Hood Velocity", enc.getRateWithoutScaling());
			SmartDashboard.putNumber("Hood Acceleration", enc.getAccelWithoutScaling());
		}
		SmartDashboard.putString("Hood PID", 
				talon.getSpeed() /*rpm at the encoder shaft*/ 
				/ 11.11 /*gear ratio*/ 
				* 360 /*degrees per rotation*/ 
				/ 60 /*seconds per minute*/ 
				
				+ ":" + talon.getSetpoint()/*rpm at the encoder shaft*/ 
				/ 11.11 /*gear ratio*/ 
				* 360 /*degrees per rotation*/ 
				/ 60 /*seconds per minute*/ );
		
		
		SmartDashboard.putString("Hood PID", getDisplacement() + ":" + getSetpoint() + ":" + (getSetpoint()-RobotMap.HOOD_THRESHOLD) + ":" + (getSetpoint()+RobotMap.HOOD_THRESHOLD));
		
	}

	@Override
	public void periodic() {
		if(isBotLimitSwitchClosed()) {
			if(enc != null)
				enc.reset();
		}
	}

	public boolean isTopLimitSwitchClosed() {
		if(talon != null)
			return talon.isFwdLimitSwitchClosed();
		return false;
	}
	
	public boolean isBotLimitSwitchClosed() {
		if(talon != null)
			return talon.isRevLimitSwitchClosed();
		return false;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}
	

	//Note: the two angle/distance functions aren't inverses of each other
	//The distanceToAngle is more accurate, but the inverse of it is hard to calculate
	public static double distanceToAngle(double distance) {
		return  0.0095*Math.pow(distance, 3) - 0.4725*Math.pow(distance, 2) + 8.2134*Math.pow(distance, 1) + 9.1025 - 0.1;
	}

	public static double angleToDistance(double angle) {
		angle += 0.1;
		return 0.334902 * Math.exp(0.0657678 * angle);
	}

	@Override
	public void pidWrite(double output) {
		setMotor(output);
	}

	PIDSourceType pidSource;
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		this.pidSource = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return pidSource;
	}

	@Override
	public double pidGet() {
		if(pidSource == PIDSourceType.kDisplacement)
			return getDisplacement();
		else
			return enc.getRateWithoutScaling();
	}

	
}

