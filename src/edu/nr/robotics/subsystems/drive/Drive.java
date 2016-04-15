package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRMath;
import edu.nr.lib.NRPID;
import edu.nr.lib.TalonEncoder;
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
	private NRPID leftPid, rightPid;
	double pidMaxVal;
	CANTalon leftTalon, rightTalon, tempLeftTalon, tempRightTalon;
	TalonEncoder leftTalonEncoder, rightTalonEncoder;
	
	// These values are right so that one distance  
	// unit given by the encoders is one meter
	private final int ticksPerRev = 256 * 60 / 24 * 48 * 4;
	private final double wheelDiameter = 0.6375; //Feet
	private final double distancePerRev = Math.PI * wheelDiameter;
	//The 4.04 is a scaling factor we found...

	private Drive() {
		if(EnabledSubsystems.driveEnabled) {
			leftTalon = new CANTalon(RobotMap.TALON_LEFT_B);
			leftTalon.enableBrakeMode(true);
			leftTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			leftTalon.setEncPosition(0);
	
			tempLeftTalon = new CANTalon(RobotMap.TALON_LEFT_A);
			tempLeftTalon.changeControlMode(TalonControlMode.Follower);
			tempLeftTalon.set(leftTalon.getDeviceID());
			tempLeftTalon.enableBrakeMode(true);
			
			rightTalon = new CANTalon(RobotMap.TALON_RIGHT_A);
			rightTalon.enableBrakeMode(true);
			rightTalon.setInverted(true);
			rightTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			rightTalon.setEncPosition(0);
	
			tempRightTalon = new CANTalon(RobotMap.TALON_RIGHT_B);
			tempRightTalon.changeControlMode(TalonControlMode.Follower);
			tempRightTalon.set(rightTalon.getDeviceID());
			tempRightTalon.enableBrakeMode(true);
	
			leftTalonEncoder = new TalonEncoder(leftTalon);
			rightTalonEncoder = new TalonEncoder(rightTalon);
			
			leftTalonEncoder.setPIDSourceType(PIDSourceType.kRate);
	
			rightTalonEncoder.setPIDSourceType(PIDSourceType.kRate);
	
			rightTalonEncoder.setTicksPerRev(ticksPerRev);
			leftTalonEncoder.setTicksPerRev(ticksPerRev);
			
			rightTalonEncoder.setDistancePerRev(distancePerRev);
			rightTalonEncoder.setScale(RobotMap.MAX_SPEED);
	
			leftTalonEncoder.setDistancePerRev(distancePerRev);
			leftTalonEncoder.setScale(RobotMap.MAX_SPEED);
			
			
			
			leftPid = new NRPID(JOYSTICK_DRIVE_P, 0, 0, 1, leftTalonEncoder, leftTalon);
			rightPid = new NRPID(JOYSTICK_DRIVE_P, 0, 0, 1, rightTalonEncoder, rightTalon);
			
			pidMaxVal = 1.0;
			rightPid.setMaxOutput(pidMaxVal);
			leftPid.setMaxOutput(pidMaxVal);
			
	
					
			LiveWindow.addSensor("Drive", "Distance", LiveWindowClasses.driveEncodersDistance);
			LiveWindow.addSensor("Drive", "Speed", LiveWindowClasses.driveEncodersSpeed);
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
		if(leftPid != null)
			leftPid.setSetpoint(left);
		if(rightPid != null)
			rightPid.setSetpoint(right);
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
			leftTalon.set(left);
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
		if(leftPid != null && rightPid != null)
			return leftPid.isEnable() && rightPid.isEnable();
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
		if(leftPid != null && rightPid != null) {
			if (enabled) {
				if(!getPIDEnabled()) {
					leftPid.enable();
					rightPid.enable();
				}
			} else {
				if(getPIDEnabled()) {
					leftPid.reset();
					rightPid.reset();
				}
			}
		}
	}

	/**
	 * Resets both the left and right encoders
	 */
	public void resetEncoders() {
		if(leftTalonEncoder != null)
			leftTalonEncoder.reset();
		if(rightTalonEncoder != null)
			rightTalonEncoder.reset();
	}

	/**
	 * Get the distance the left encoder has driven since the last reset
	 * 
	 * @return The distance the left encoder has driven since the last reset as scaled by the value from setDistancePerPulse().
	 */
	public double getEncoderLeftDistance() {
		if(leftTalonEncoder != null)
			return -leftTalonEncoder.getDisplacement();
		return 0;
	}

	/**
	 * Get the distance the right encoder has driven since the last reset
	 * 
	 * @return The distance the right encoder has driven since the last reset as scaled by the value from setDistancePerPulse().
	 */
	public double getEncoderRightDistance() {
		if(rightTalonEncoder != null)
			return -rightTalonEncoder.getDisplacement();
		return 0;
	}

	/**
	 * Get the current rate of the left encoder. 
	 * Units are distance per second as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current rate of the encoder
	 */
	public double getEncoderLeftSpeed() {
		if(leftTalonEncoder != null)
			return -leftTalonEncoder.getRate();
		return 0;
	}

	/**
	 * Get the current rate of the right encoder. 
	 * Units are distance per second as scaled by the value from setDistancePerPulse().
	 * 
	 * @return The current rate of the encoder
	 */
	public double getEncoderRightSpeed() {
		if(rightTalonEncoder != null)
			return -rightTalonEncoder.getRate();
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

		SmartDashboard.putString("Left", leftTalonEncoder.get() + ":" + leftPid.getSetpoint() + ":" + leftPid.getError());
		SmartDashboard.putString("Right", rightTalonEncoder.get() + ":" + rightPid.getSetpoint() + ":" + rightPid.getError());
	}

	@Override
	public void periodic() {
		if(leftTalon != null && rightTalon != null && tempLeftTalon != null && tempRightTalon != null && leftPid != null && rightPid != null) {
			if(pidMaxVal > 1.0) {
				pidMaxVal = 1.0;
			}
			if(pidMaxVal < 1.0 && leftTalon.getOutputCurrent() < 40 && tempLeftTalon.getOutputCurrent() < 40 
					&& rightTalon.getOutputCurrent() < 40 && tempRightTalon.getOutputCurrent() < 40) {
				pidMaxVal += 0.01;
			} else {
				pidMaxVal -= 0.03;
			}
	
			leftPid.setMaxOutput(Math.abs(pidMaxVal));
			rightPid.setMaxOutput(Math.abs(pidMaxVal));
		}
	}

	public void setPID(double p, double i, double d, double f) {
		if(leftPid != null)
			leftPid.setPID(p, i, d, f);
		if(rightPid != null)
			rightPid.setPID(p, i, d, f);
	}

}
