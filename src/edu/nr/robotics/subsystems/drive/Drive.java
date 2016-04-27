package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRMath;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.EnabledSubsystems;
import edu.nr.robotics.LiveWindowClasses;
import edu.nr.robotics.OI;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Drive extends Subsystem implements SmartDashboardSource, Periodic{
	
	/**
	 *  This is a constant that is used for driving with PID control
	 */
	public static final double JOYSTICK_DRIVE_P = 0.25;

	private static Drive singleton;
	CANTalon leftTalon, rightTalon, tempLeftTalon, tempRightTalon;
	
	// These values are right so that one distance  
	// unit given by the encoders is one meter
	private final static double ticksPerRev = /*256 * 60 / 24 * 48 * 4*/ 360;
	private final static double wheelDiameter = 0.3333;//0.6375; //Feet
	private final static double distancePerRev = Math.PI * wheelDiameter;
	private final static double maxRPS = RobotMap.MAX_SPEED / distancePerRev;
	private final static double maxRPM = maxRPS * 60;

	private Drive() {
		if(EnabledSubsystems.driveEnabled) {
			leftTalon = new CANTalon(RobotMap.TALON_LEFT_B);
			
			leftTalon.changeControlMode(TalonControlMode.Speed);
			
			leftTalon.enableBrakeMode(true);
			leftTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			leftTalon.setEncPosition(0);
			
			leftTalon.reverseSensor(true);
			
			
			leftTalon.configEncoderCodesPerRev((int)ticksPerRev);

			leftTalon.setF(RobotMap.DRIVE_F); 
			leftTalon.setP(RobotMap.DRIVE_P); 
			leftTalon.setI(RobotMap.DRIVE_I); 
			leftTalon.setD(RobotMap.DRIVE_D); 
			
	
			tempLeftTalon = new CANTalon(RobotMap.TALON_LEFT_A);
			tempLeftTalon.changeControlMode(TalonControlMode.Follower);
			tempLeftTalon.set(leftTalon.getDeviceID());
			tempLeftTalon.enableBrakeMode(true);
			
			rightTalon = new CANTalon(RobotMap.TALON_RIGHT_A);
			rightTalon.enableBrakeMode(true);
			rightTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			rightTalon.reverseSensor(true);
			rightTalon.setEncPosition(0);
			rightTalon.changeControlMode(TalonControlMode.Speed);
			
			rightTalon.configEncoderCodesPerRev((int)ticksPerRev);
			
			rightTalon.setF(RobotMap.DRIVE_F); 
			rightTalon.setP(RobotMap.DRIVE_P); 
			rightTalon.setI(RobotMap.DRIVE_I); 
			rightTalon.setD(RobotMap.DRIVE_D); 
			


	
			tempRightTalon = new CANTalon(RobotMap.TALON_RIGHT_B);
			tempRightTalon.changeControlMode(TalonControlMode.Follower);
			tempRightTalon.set(rightTalon.getDeviceID());
			tempRightTalon.enableBrakeMode(true);
		}
	}

	public static Drive getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Drive();
		}
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveJoystickCommand());
	}

	/**
	 * Set the voltage ramp rate for both drive talons. 
	 * Limits the rate at which the throttle will change.
	 * 
	 * @param rampRate
	 *            Maximum change in voltage, in volts / sec.
	 */
	public void setTalonRampRate(double rampRate) {
		if(leftTalon != null)
			leftTalon.setVoltageRampRate(rampRate);
		if(rightTalon != null)
			rightTalon.setVoltageRampRate(rampRate);
	}

	/**
	 * Sets left and right motor speeds to the speeds needed for the given move
	 * and turn values
	 * 
	 * @param move
	 *            The speed, from -1 to 1 (inclusive), that the robot should go
	 *            at. 1 is max forward, 0 is stopped, -1 is max backward
	 * @param turn
	 *            The speed, from -1 to 1 (inclusive), that the robot should
	 *            turn at. 1 is max right, 0 is stopped, -1 is max left
	 */
	public void arcadeDrive(double move, double turn) {
		arcadeDrive(move,turn,false);
	}
	
	/**
	 * Sets left and right motor speeds to the speeds needed for the given move
	 * and turn values, multiplied by the OI speed multiplier if the speed multiplier
	 * parameter is true. If you don't care about the speed multiplier parameter, you
	 * might want to use {@link arcadeDrive(double move, double turn)}
	 * 
	 * @param move
	 *            The speed, from -1 to 1 (inclusive), that the robot should go
	 *            at. 1 is max forward, 0 is stopped, -1 is max backward
	 * @param turn
	 *            The speed, from -1 to 1 (inclusive), that the robot should
	 *            turn at. 1 is max right, 0 is stopped, -1 is max left
	 * @param speedMultiplier 
	 * 			  whether or not to use the OI speed multiplier
	 *            It should really only be used for operator driving
	 * 
	 */
	public void arcadeDrive(double move, double turn, boolean speedMultiplier) {
		move = NRMath.limit(move);
		turn = NRMath.limit(turn);
		double leftMotorSpeed, rightMotorSpeed;
		rightMotorSpeed = leftMotorSpeed = move;
		leftMotorSpeed += turn;
		rightMotorSpeed -= turn;

		if (move > 0.0) {
			if (turn > 0.0) {
				leftMotorSpeed = move - turn;
				rightMotorSpeed = Math.max(move, turn);
			} else {
				leftMotorSpeed = Math.max(move, -turn);
				rightMotorSpeed = move + turn;
			}
		} else {
			if (turn > 0.0) {
				leftMotorSpeed = -Math.max(-move, turn);
				rightMotorSpeed = move + turn;
			} else {
				leftMotorSpeed = move - turn;
				rightMotorSpeed = -Math.max(-move, -turn);
			}
		}

		double multiplier = speedMultiplier? OI.getInstance().speedMultiplier : 1;
		tankDrive(leftMotorSpeed*multiplier, rightMotorSpeed*multiplier);
	}

	/**
	 * Sets both left and right motors to the given speed If PID is enabled,
	 * then sets the PID setpoints, otherwise sets the raw motor speeds Notable
	 * is that to go forward, they should be opposite signs, and to turn in
	 * place, they should be the same sign.
	 * 
	 * @param left
	 *            the left motor speed
	 * @param right
	 *            the right motor speed
	 */
	public void tankDrive(double left, double right) {
		if (getPIDEnabled()) {
			setPIDSetpoint(left, right);
		} else {
			setRawMotorSpeed(left, right);
		}
	}

	/**
	 * Enables the PID and sets the setpoint for the left and right motors
	 * 
	 * @param left
	 *            the left motor speed, from -1 to 1
	 * @param right
	 *            the right motor speed, from -1 to 1
	 */
	public void setPIDSetpoint(double left, double right) {
		setPIDSetpoint(left, right, true);
	}

	/**
	 * Optionally enables the PID and sets the setpoint for the left and right
	 * motors
	 * 
	 * @param left
	 *            the left motor speed, from -1 to 1
	 * @param right
	 *            the right motor speed, from -1 to 1
	 * @param enable
	 *            whether or not to enable the PID before setting the setpoints
	 */
	public void setPIDSetpoint(double left, double right, boolean enable) {
		if (enable) {
			setPIDEnabled(true);
		}
		if(leftTalon != null)
			leftTalon.setSetpoint(-left*maxRPM);
		if(rightTalon != null)
			rightTalon.setSetpoint(right*maxRPM);
	}

	/**
	 * Disables the PID and sets the motor speed for the left and right motors
	 * A raw motor speed is actually a scaled voltage value
	 * 
	 * @param left
	 *            the left motor speed, from -1 to 1
	 * @param right
	 *            the right motor speed, from -1 to 1
	 */
	public void setRawMotorSpeed(double left, double right) {
		setPIDEnabled(false);
		if(leftTalon != null)
			leftTalon.set(-left);
		if(rightTalon != null)
			rightTalon.set(right);
	}

	/**
	 * Gets whether the PIDs are enabled or not. If both are enabled, then
	 * returns true, otherwise returns false
	 * 
	 * @return whether the PIDs are enabled
	 */
	public boolean getPIDEnabled() {
		if(leftTalon != null && rightTalon != null)
			return leftTalon.getControlMode() == TalonControlMode.Speed && rightTalon.getControlMode() == TalonControlMode.Speed;
		return false;
	}

	/**
	 * Enables or disables both left and right PIDs. Disabling also resets the
	 * integral term and the previous error of the PID, and sets the output to
	 * zero
	 * 
	 * Doesn't do anything if they are already that state.
	 * 
	 * @param enabled
	 *            whether to enable (true) or disable (false)
	 */
	public void setPIDEnabled(boolean enabled) {
		if(leftTalon != null && rightTalon != null) {
			if (enabled) {
				if(!getPIDEnabled()) {
					leftTalon.changeControlMode(TalonControlMode.Speed);
					rightTalon.changeControlMode(TalonControlMode.Speed);
				}
			} else {
				if(getPIDEnabled()) {
					leftTalon.clearIAccum();
					rightTalon.clearIAccum();
					leftTalon.changeControlMode(TalonControlMode.PercentVbus);
					rightTalon.changeControlMode(TalonControlMode.PercentVbus);
				}
			}
		}
	}

	/**
	 * Resets both the left and right encoders
	 */
	public void resetEncoders() {
		if(leftTalon != null)
			leftTalon.setPosition(0);
		if(rightTalon != null)
			rightTalon.setPosition(0);
	}

	/**
	 * Get the distance the left encoder has driven since the last reset
	 * 
	 * @return The distance the left encoder has driven since the last reset as scaled by the value from setDistancePerPulse().
	 */
	public double getEncoderLeftDistance() {
		if(leftTalon != null)
			return leftTalon.getPosition() * distancePerRev ;
		return 0;
	}

	/**
	 * Get the distance the right encoder has driven since the last reset
	 * 
	 * @return The distance the right encoder has driven since the last reset as scaled by the value from setDistancePerPulse().
	 */
	public double getEncoderRightDistance() {
		if(rightTalon != null)
			return rightTalon.getPosition() * distancePerRev;
		return 0;
	}

	/**
	 * Get the current rate of the left encoder. 
	 * Units are distance per second as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current rate of the encoder
	 */
	public double getEncoderLeftSpeed() {
		if(leftTalon != null)
			return leftTalon.getSpeed() * distancePerRev * 10;
		return 0;
	}

	/**
	 * Get the current rate of the right encoder. 
	 * Units are distance per second as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current rate of the encoder
	 */
	public double getEncoderRightSpeed() {
		if(rightTalon != null)
			return rightTalon.getSpeed() * distancePerRev * 10;
		return 0;
	}

	/**
	 * Gets the average distance of the encoders
	 * 
	 * @return 
	 * 		The average distance the encoders have driven since the 
	 * 		last reset as scaled by the value from setDistancePerPulse().
	 */
	public double getEncoderAverageDistance() {
		return (getEncoderLeftDistance() + getEncoderRightDistance()) / 2;
	}

	/**
	 * Get the average current rate of the encoders. 
	 * Units are distance per second as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current average rate of the encoders
	 */
	public double getEncoderAverageSpeed() {
		return (getEncoderRightSpeed() + getEncoderLeftSpeed()) / 2;
	}

	/**
	 * Sends data to the SmartDashboard
	 */
	@Override
	public void smartDashboardInfo() {
		SmartDashboard.putNumber("Left Pos", getEncoderLeftDistance());
		SmartDashboard.putNumber("Right Pos", getEncoderRightDistance());
		SmartDashboard.putNumber("Average Pos", getEncoderAverageDistance());
		
		SmartDashboard.putNumber("Left speed", getEncoderLeftSpeed());
		SmartDashboard.putNumber("Right speed", getEncoderRightSpeed());
		SmartDashboard.putNumber("Average speed", getEncoderAverageSpeed());
		
		if(leftTalon != null)
			SmartDashboard.putString("Left", leftTalon.getSpeed() + ":" + leftTalon.getSetpoint() + ":" + (leftTalon.getClosedLoopError()));
		if(rightTalon != null)
			SmartDashboard.putString("Right", rightTalon.getSpeed() + ":" + rightTalon.getSetpoint() + ":" + (rightTalon.getClosedLoopError()) );
				
		SmartDashboard.putData(this);
	}

	@Override
	public void periodic() {
		if(leftTalon != null && leftTalon.getSetpoint() == 0)  {
			leftTalon.clearIAccum();
			leftTalon.ClearIaccum();
		}
		if(rightTalon != null && rightTalon.getSetpoint() == 0) {
			rightTalon.clearIAccum();
			rightTalon.ClearIaccum();
		}
	}

	public void setPID(double p, double i, double d, double f) {
		if(leftTalon != null)
			leftTalon.setPID(p, i, d, f, 0, 0, 0);
		if(rightTalon != null)
			rightTalon.setPID(p, i, d, f, 0, 0, 0);
	}

}
