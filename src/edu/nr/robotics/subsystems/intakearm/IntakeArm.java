package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.PID;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.EnabledSubsystems;
import edu.nr.robotics.LiveWindowClasses;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class IntakeArm extends Subsystem implements SmartDashboardSource, Periodic {
    
	private static IntakeArm singleton;
	
	int counter = 0;

	CANTalon talon;
	private AnalogPotentiometer pot;
	PID pid;
	
	boolean pidDisabled = false;
		
	private IntakeArm() {		
		if(EnabledSubsystems.intakeEnabled)
		{
			talon = new CANTalon(RobotMap.INTAKE_ARM_TALON);
			talon.setInverted(true);
			pot = new AnalogPotentiometer(RobotMap.INTAKE_ARM_POT);
			pot.setPIDSourceType(PIDSourceType.kDisplacement);
			pid = new PID(421.8*0.1, 421.8*0.0001, 0.00, pot, talon);
			
			LiveWindow.addSensor("Intake Arm", "PID", pid);
			
			LiveWindow.addSensor("Intake Arm", "Bottom Switch", LiveWindowClasses.intakeArmBottomSwitch);
			LiveWindow.addSensor("Intake Arm", "Top Switch", LiveWindowClasses.intakeArmTopSwitch);
		}

	}
	
    @Override
	public void initDefaultCommand() {
		setDefaultCommand(new IntakeArmJoystickCommand());

    }
    
    public static IntakeArm getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new IntakeArm();
		}
	}
	
	/**
	 * Disable the PID and set the motor to the given speed
	 * @param speed the speed to set the motor to, from -1 to 1
	 */
	public void setMotor(double speed) {
		if(pid != null) {
			if(pid.isEnable())
				pid.disable();
		}
		if(talon != null)
			talon.set(speed);
	}
	
	/**
	 * Set the arm PID setpoint
	 * @param value the value to set the setpoint to
	 */
	public void setSetpoint(double value) {
		if(pid != null)
			pid.setSetpoint(value);	
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
	public void enable() {
		pidDisabled = false;
		if(pid != null)
			pid.enable();
	}
	
	/**
	 * Disable the PID
	 */
	public void disable() {
		pidDisabled = true;
		if(pid != null)
			pid.disable();
	}
	
	/**
	 * Gets whether the PID is enabled or not
	 * @return whether the PID is enabled
	 */
	public boolean isEnable() {
		if(pid != null)
			return pid.isEnable();
		return false;
	}
	/**
	 * Gets the value of the potentiometer
	 * @return the value of the potentiometer
	 */
	public double get() {
		if(pot != null)
			return pot.get();
		return 0;
	}

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.intakeEnabled) {
			SmartDashboard.putNumber("Intake Arm Potentiometer", get());
			SmartDashboard.putNumber("Intake Arm Error", pid.getError());
			SmartDashboard.putBoolean("Intake Arm Moving", pid.isEnable());
			
			LiveWindowClasses.intakeArmBottomSwitch.set(isBotLimitSwitchClosed());
			LiveWindowClasses.intakeArmTopSwitch.set(isTopLimitSwitchClosed());
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

	public void setMaxSpeed(double maxSpeed) {
		if(pid != null)
			pid.setOutputRange(0, maxSpeed);
	}

	@Override
	public void periodic() {
		if(pid != null) {
			if(Math.abs(pid.getError()) < RobotMap.INTAKE_ARM_THRESHOLD * 2/3) {
				counter++;
				if(counter > 2)
					pid.disable();
			} else {
				counter = 0;
			}
		}
		
		RobotMap.INTAKE_OFFSET = SmartDashboard.getNumber("Intake Offset");
		RobotMap.INTAKE_TOP_POS = 0.655 + RobotMap.INTAKE_OFFSET;
		RobotMap.INTAKE_INTAKE_POS = 0.522 + RobotMap.INTAKE_OFFSET;
		RobotMap.INTAKE_HOME_POS = 0.522 + RobotMap.INTAKE_OFFSET; //home == intake
		RobotMap.INTAKE_BOTTOM_POS = 0.494 + RobotMap.INTAKE_OFFSET;

	} 

	public double getError() {
		if(pid != null)
			return pid.getError();
		return 0;
	}
}

