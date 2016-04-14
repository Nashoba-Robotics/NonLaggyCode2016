package edu.nr.robotics.subsystems.shooter;

import edu.nr.lib.CounterPIDSource;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.EnabledSubsystems;
import edu.nr.robotics.LiveWindowClasses;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shooter extends Subsystem implements SmartDashboardSource{
    
	private static Shooter singleton;

	CANTalon talonA, talonB;
		
	MotorSetter talonOutput;
	
	DigitalInput gate;

	
	//In rotations per second
	CounterPIDSource shooterRate;
	
	private Shooter() {
		if(EnabledSubsystems.shooterEnabled) {
			gate = new DigitalInput(RobotMap.SHOOTER_PHOTO_GATE);
			
			talonA = new CANTalon(RobotMap.SHOOTER_TALON_A);
			talonA.enableBrakeMode(false);
			
			talonB = new CANTalon(RobotMap.SHOOTER_TALON_B);
			talonB.enableBrakeMode(false);
			talonB.setInverted(true);
	
			talonOutput = new MotorSetter(talonA, talonB);
			
			shooterRate = new CounterPIDSource(RobotMap.SHOOTER_RATE_PORT);
			shooterRate.setPIDSourceType(PIDSourceType.kRate);
			shooterRate.setSamplesToAverage(24);
			shooterRate.scale(3 * RobotMap.SHOOTER_MAX_SPEED);
							
			LiveWindow.addSensor("Shooter", "PID Output", LiveWindowClasses.shooterOutput);
			LiveWindow.addSensor("Shooter", "Current", LiveWindowClasses.shooterCurrent);
		}
	}
	
    @Override
	public void initDefaultCommand() {
    	setDefaultCommand(new ShooterSpeedCommand());
    }
    
    public static Shooter getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Shooter();
		}
	}
	
	/**
	 * Disable the PID and set the motor to the given speed
	 * @param speed the speed to set the motor to, from -1 to 1
	 */
	public void setMotor(double speed) {
		if(talonOutput != null)
			talonOutput.write(speed);
	}
	
	/**
	 * Gets the speed of the shooter
	 * @return the speed of the shooter
	 */
	public double getScaledSpeed() {
		if(shooterRate != null)
			return shooterRate.pidGet();
		return 0;
	}
	
	public double getSpeed() {
		return getScaledSpeed() * RobotMap.SHOOTER_MAX_SPEED;
	}
	
	public boolean getRunning() {
		return getScaledSpeed() > 0.1;
	}
	
	public double getSpeedPercent() {
		if(shooterRate != null)
			return shooterRate.getRate() / RobotMap.SHOOTER_MAX_SPEED;
		return 0;
	}
	
	/**
	 * Tells if the shooter is up to speed
	 * @return
	 */
	public boolean getSped() {
		return getScaledSpeed() > RobotMap.SHOOTER_FAST_SPEED - 0.1;
	}

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.shooterEnabled) {
			SmartDashboard.putNumber("Shooter Speed", getSpeed());
			SmartDashboard.putNumber("Shooter Speed Percent", getScaledSpeed());
			SmartDashboard.putData(this);
			LiveWindowClasses.shooterCurrent.set(talonOutput.getOutputCurrent());
		}
	}

	public boolean hasBall() {
		if(gate != null)
			return !gate.get();
		return false;
	}
}