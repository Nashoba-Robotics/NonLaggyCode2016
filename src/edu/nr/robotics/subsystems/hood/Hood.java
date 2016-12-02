package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.TalonEncoder;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.motionprofiling.MotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.SimpleOneDimensionalTrajectory;
import edu.nr.lib.motionprofiling.Trajectory;
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
	public static final double MAX_VEL = 45; //TODO: find more accurately
	
	public static final double MAX_RPM = 83.325;
	
	public MotionProfiler profiler;
	
	CANTalon talon;	
	TalonEncoder enc;
	
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
			
			talon.setF(7.3);
			talon.setP(2);
			
			enc = new TalonEncoder(talon,true);
			enc.setPIDSourceType(PIDSourceType.kDisplacement);
			enc.setDistancePerRev(RobotMap.HOOD_TICK_TO_ANGLE_MULTIPLIER);
			
			profiler = new OneDimensionalMotionProfiler(this, this, 1/MAX_VEL, 0.00125,0.1,/*0.000001*/0);
			//profiler = new OneDimensionalMotionProfiler(this, this, 1/MAX_VEL,0,0,0);
			
			LiveWindow.addSensor("Hood", "Bottom Switch", LiveWindowClasses.hoodBottomSwitch);
			LiveWindow.addSensor("Hood", "Top Switch", LiveWindowClasses.hoodTopSwitch);
			
			//LiveWindow.addSensor("Hood", "PID", pid);
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
	 * Set the motor to the given speed
	 * @param speed the speed to set the motor to, from -1 to 1
	 */
	public void setMotor(double speed) {
		if(talon != null)
			talon.setSetpoint(speed * MAX_RPM);
	}

	public void setMotorInRPM(double rpm) {
		if(talon != null)
			talon.setSetpoint(rpm);
	}
	
	public void setMotorInDPS(double degpersec) {
		if(talon != null)
			talon.setSetpoint(degpersec 
				* 11.11 /*gear ratio*/ 
				/ 360 /*degrees per rotation*/ 
				* 60 /*seconds per minute*/ );
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
	
	@Override
	public void smartDashboardInfo() {
		SmartDashboard.putNumber("Hood Angle", getDisplacement());
		SmartDashboard.putNumber("Hood Distance", angleToDistance(getDisplacement()));
		if(enc != null) {
			SmartDashboard.putNumber("Hood Velocity", enc.getRateWithoutScaling());
			SmartDashboard.putNumber("Hood Acceleration", enc.getAccelWithoutScaling());
		}
		if(talon != null) {
			SmartDashboard.putString("Hood Velocity PID", 
					talon.getSpeed() /*rpm at the encoder shaft*/ 
					/ 11.11 /*gear ratio*/ 
					* 360 /*degrees per rotation*/ 
					/ 60 /*seconds per minute*/ 
					
					+ ":" + talon.getSetpoint()/*rpm at the encoder shaft*/ 
					/ 11.11 /*gear ratio*/ 
					* 360 /*degrees per rotation*/ 
					/ 60 /*seconds per minute*/ );	
		}
		SmartDashboard.putData(this);
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
		if(pidSource == PIDSourceType.kDisplacement) {
			return getDisplacement();
		} else if(enc != null) {
				return enc.getRateWithoutScaling();
		} else {
			return  0;
		}
	}

	public void disableProfiler() {
		if(profiler != null)
			profiler.disable();
	}
	
	public void disable() {
		disableProfiler();
		setMotor(0);
	}
	
	public void enableProfiler(Trajectory traj) {
		if(profiler != null) {
			profiler.setTrajectory(traj);
			profiler.enable();
		}
	}
	
	public void enableProfiler(double delta) {
		enableProfiler(new SimpleOneDimensionalTrajectory(delta, Hood.MAX_VEL, Hood.MAX_VEL, Hood.MAX_ACC));	
	}
	
	public boolean isProfilerEnabled() {
		if(profiler != null) {
			return profiler.isEnabled();
		} else {
			return false;
		}
	}

	
}

