package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerBasic;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectory;
import edu.nr.robotics.EnabledSubsystems;
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
public class IntakeArm extends Subsystem implements SmartDashboardSource, Periodic, PIDOutput, PIDSource {
    
	private static IntakeArm singleton;
	
	public OneDimensionalMotionProfiler profiler;
	
	int counter = 0;

	CANTalon talon;
	
	boolean pidDisabled = false;
	
	static final double MAX_ACC = 100 / 60.0;
	static final double MAX_VEL = 30 / 60.0;
		
	private IntakeArm() {		
		if(EnabledSubsystems.intakeEnabled)
		{
			talon = new CANTalon(RobotMap.INTAKE_ARM_TALON);
			talon.reverseSensor(true);
			talon.setFeedbackDevice(FeedbackDevice.AnalogPot);
			talon.changeControlMode(TalonControlMode.Speed);
						
			talon.configPotentiometerTurns(3);
			
			profiler = new OneDimensionalMotionProfilerBasic(this, this, 1/MAX_VEL, 0.1,0.1,0);
			
			talon.setF(60);
			talon.setP(80);			
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
	 * Set the motor to the given speed
	 * @param speed the speed to set the motor to, from -1 to 1
	 */
	public void setMotor(double speed) {
		if(talon != null) {
			talon.setSetpoint(-speed * 35);
		}
	}
	
	/**
	 * Gets the position of the pot
	 * @return the position of the pot
	 */
	public double getPosition() {
		if(talon != null)
			return -talon.getPosition();
		return 0;
	}
	
	/**
	 * Gets the velocity of the pot
	 * @return the velocity of the pot
	 */
	public double getVelocity() {
		if(talon != null)
			return -talon.getSpeed()/60.0;
		return 0;
	}

	@Override
	public void smartDashboardInfo() {
			SmartDashboard.putNumber("Intake Arm Potentiometer", getPosition());
		if(talon != null) {
			SmartDashboard.putString("Intake Velocity PID", 
					talon.getSpeed() + ":" + talon.getSetpoint() + ":" + talon.getOutputVoltage());	
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

	@Override
	public void periodic() {

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
			return getPosition();
		else
			if(talon != null) {
				return getVelocity();
			}
		return 0;
	}

	public void disableProfiler() {
		if(profiler != null)
			profiler.disable();
	}
	
	public void enableProfiler(OneDimensionalTrajectory traj) {
		if(profiler != null) {
			profiler.setTrajectory(traj);
			profiler.enable();
		}
	}
	
	public void enableProfiler(double position) {
		enableProfiler(new OneDimensionalTrajectorySimple(position - IntakeArm.getInstance().getPosition(), MAX_VEL, MAX_VEL, MAX_ACC));	
	}
	
	public boolean isProfilerEnabled() {
		return profiler.isEnabled();
	}
	
	public void disable() {
		disableProfiler();
		setMotor(0);
	}
}

