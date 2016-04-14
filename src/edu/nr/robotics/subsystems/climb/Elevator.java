package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRMath;
import edu.nr.lib.TalonEncoder;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.EnabledSubsystems;
import edu.nr.robotics.LiveWindowClasses;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Elevator extends Subsystem implements SmartDashboardSource {
    
	CANTalon talon;
	public TalonEncoder enc;
	
	private static Elevator singleton;
	
	private Elevator() {
		if(EnabledSubsystems.climbEnabled) {
			talon = new CANTalon(RobotMap.ELEVATOR_TALON);
			talon.enableBrakeMode(true);
			enc = new TalonEncoder(talon);
			LiveWindow.addSensor("Elevator", "Speed", LiveWindowClasses.elevatorSpeed);
		}
		
		
	}
	
	public static Elevator getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new Elevator();
		}
	}
	
	/**
	 * Set the motor to a value from -1 to 1
	 * @param val the value from -1 to 1
	 */
	public void setMotorValue(double val) {
		val = NRMath.limit(val, 1);
		if(talon != null)
			talon.set(val);
	}
	
	public double getMotorValue() {
		if(talon != null)
			return talon.get();
		return 0;
	}
	
	public void resetEncoder() {
		if(enc != null)
			enc.reset();
	}
	
	public double getEncoder() {
		if(enc != null)
			return enc.get();
		return 0;
	}

	
    @Override
	public void initDefaultCommand() {
		setDefaultCommand(new ElevatorJoystickCommand());
    }

	@Override
	public void smartDashboardInfo() {
		if(EnabledSubsystems.climbEnabled) {
			if(enc != null){
				SmartDashboard.putNumber("Elevator speed", enc.getRate());
				SmartDashboard.putNumber("Elevator position", enc.get());
				LiveWindowClasses.elevatorSpeed.set(enc.getRate());
	
			}
			
			if(talon != null)
				SmartDashboard.putNumber("Elevator current", talon.getOutputCurrent());
			SmartDashboard.putBoolean("Elevator moving", getMotorValue() != 0);
			SmartDashboard.putData(this);
		}
	}

	public boolean isMoving() {
		if(enc != null)
			return Math.abs(enc.getRate()) > 1;
		return false;
	}

	public double getCurrent() {
		if(talon != null)
			return talon.getOutputCurrent();
		return 0;
	}
	
}

