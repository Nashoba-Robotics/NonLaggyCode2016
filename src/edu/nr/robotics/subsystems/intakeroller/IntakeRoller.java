package edu.nr.robotics.subsystems.intakeroller;

import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.EnabledSubsystems;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class IntakeRoller extends Subsystem implements SmartDashboardSource {
    
	private static IntakeRoller singleton;
	
	CANTalon talon;
	DigitalInput gate;

	private IntakeRoller() {
		if(EnabledSubsystems.intakeRollersEnabled) {
			gate = new DigitalInput(RobotMap.INTAKE_PHOTO_GATE);
			talon = new CANTalon(RobotMap.INTAKE_ROLLER_TALON);
		}
	}
	
	public static IntakeRoller getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new IntakeRoller();
		}
	}
	
	/**
	 * Set the roller PID setpoint
	 * @param value the value to set the setpoint to
	 */
	public void setRollerSpeed(double value) {
		if(talon != null)
			talon.set(value);	
	}

    @Override
	public void initDefaultCommand() {
    	setDefaultCommand(new IntakeRollerNeutralCommand());
    }

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.intakeRollersEnabled) {
			SmartDashboard.putBoolean("Intake Roller Forward", isForward());
			SmartDashboard.putBoolean("Intake Roller Reverse", isReverse());
		}
	}
	
	public boolean hasBall() {
		if(gate != null)
			return !gate.get();
		return false;
	}

	public double getRollerSpeed() {
		if(talon != null)
			return talon.get();
		return 0;
	}

	public boolean isForward() {
		if(talon != null)
			return talon.get() < -0.1 ;
		return false;
	}
	
	public boolean isReverse() {
		if(talon != null)
			return talon.get() > 0.1 ;
		return false;	}
}

