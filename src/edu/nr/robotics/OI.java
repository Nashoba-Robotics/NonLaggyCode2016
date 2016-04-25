package edu.nr.robotics;

import edu.nr.lib.DoubleJoystickButton;
import edu.nr.lib.NRCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.robotics.commandgroups.AlignCommandGroup;
import edu.nr.robotics.commandgroups.AlignEndCommandGroup;
import edu.nr.robotics.commandgroups.AutoGuillotineCommandGroup;
import edu.nr.robotics.commandgroups.AutoShovelOfFriesCommandGroup;
import edu.nr.robotics.commandgroups.GetLowCommandGroup;
import edu.nr.robotics.commandgroups.LowGoalFinishCommandGroup;
import edu.nr.robotics.commandgroups.LowGoalStartCommandGroup;
import edu.nr.robotics.commandgroups.PrepareCloseShotCommandGroup;
import edu.nr.robotics.commandgroups.PrepareLongShotCommandGroup;
import edu.nr.robotics.commandgroups.PukeCommandGroup;
import edu.nr.robotics.commandgroups.PukeFinishCommandGroup;
import edu.nr.robotics.subsystems.climb.Elevator;
import edu.nr.robotics.subsystems.climb.ElevatorExtendCommand;
import edu.nr.robotics.subsystems.climb.ElevatorResetCommand;
import edu.nr.robotics.subsystems.climb.ElevatorResetEncoderCommand;
import edu.nr.robotics.subsystems.climb.ElevatorResetPart2Command;
import edu.nr.robotics.subsystems.climb.ElevatorRetractCommand;
import edu.nr.robotics.subsystems.climb.ElevatorUnlatchCommand;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveCancelCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveGyroAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveResetEncodersCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodIncreaseDegreeCommand;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;
import edu.nr.robotics.subsystems.hood.HoodPositionCommand;
import edu.nr.robotics.subsystems.hood.HoodResetEncoderCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.intakearm.IntakeArmBottomHeightCommandGroup;
import edu.nr.robotics.subsystems.intakearm.IntakeArmHomeHeightCommandGroup;
import edu.nr.robotics.subsystems.intakearm.IntakeArmIntakeHeightCommandGroup;
import edu.nr.robotics.subsystems.intakearm.IntakeArmPrepareLowGoalCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmUpHeightCommandGroup;
import edu.nr.robotics.subsystems.intakeroller.IntakeRoller;
import edu.nr.robotics.subsystems.intakeroller.IntakeRollerSwapCommand;
import edu.nr.robotics.subsystems.light.LightOffCommand;
import edu.nr.robotics.subsystems.light.LightOnCommand;
import edu.nr.robotics.subsystems.loaderroller.LaserCannonTriggerCommand;
import edu.nr.robotics.subsystems.loaderroller.LoaderRoller;
import edu.nr.robotics.subsystems.loaderroller.LoaderRollerIntakeUntilPhotoCommand;
import edu.nr.robotics.subsystems.loaderroller.LoaderRollerJoystickCommand;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements SmartDashboardSource, Periodic {
	
	public SendableChooser drivingModeChooser;

	public double speedMultiplier = 1;

	private final static double JOYSTICK_DEAD_ZONE = 0.15;

	public double gyroValueforPlayerStation = 0;

	private static OI singleton;

	Joystick driveLeft;
	Joystick driveRight;
	Joystick operatorLeft, operatorRight;

	JoystickButton dumbDrive;
	JoystickButton dumbShooter;
	
	public JoystickButton fireButton;
	public JoystickButton backupFireButton;
	public DoubleJoystickButton alignButton;
	
	
	
	public AlignCommandGroup alignCommand;
	
	private OI() {
		SmartDashboard.putNumber("Speed Multiplier", speedMultiplier);

		driveLeft = new Joystick(0);
		driveRight = new Joystick(1);
		operatorLeft = new Joystick(3);
		operatorRight = new Joystick(2);
		
		initDriveLeft();
		initDriveRight();
		initOperatorLeft();
		initOperatorRight();
		
		new DoubleJoystickButton(new JoystickButton(driveLeft, 6), new JoystickButton(driveRight, 6)).whenPressed(new ElevatorResetCommand());
		new DoubleJoystickButton(new JoystickButton(driveLeft, 6), new JoystickButton(driveRight, 7)).whenPressed(new ElevatorResetPart2Command());
	}
	
	public void initDriveLeft() {
		//Drive Left: (0)
		//->  1: Stall forward
		Robot.getInstance().driveWall = new DriveConstantCommand(true, true, true, .25);
		new JoystickButton(driveLeft, 1).whenPressed(Robot.getInstance().driveWall);
		new JoystickButton(driveLeft, 1).whenReleased(new DriveCancelCommand());
		//->  2: Reverse drive direction
		//->  3: Reset elevator encoder
		new JoystickButton(driveLeft, 3).whenPressed(new ElevatorResetEncoderCommand());
		
		new JoystickButton(driveLeft, 9).whenPressed(new HoodIncreaseDegreeCommand(1));
		new JoystickButton(driveLeft, 8).whenPressed(new HoodIncreaseDegreeCommand(-1));
	}
	
	public void initDriveRight() {
		// Drive Right: (1)
		// => 1: Slow Turn
		new JoystickButton(driveRight, 1).whenPressed(new LightOnCommand());
		new JoystickButton(driveRight, 1).whenReleased(new LightOffCommand());
		
		// -> 10: Reset drive encoders
		new JoystickButton(driveRight, 10).whenPressed(new DriveResetEncodersCommand());
		// => 11: Reset hood encoder
		new JoystickButton(driveRight, 11).whenPressed(new HoodResetEncoderCommand());
		
		new JoystickButton(driveRight, 2).whenPressed(new HoodJetsonPositionCommand());
		
		new JoystickButton(driveRight, 9).whenPressed(new DriveGyroAngleSmartDashboardCommand());
	}

	public void initOperatorLeft() {
		// Operator Left: (3)
		// -> 4: Auto Shovel of Fries
		// Auto shovel of fries routine, ends when drive joysticks are touched
		new JoystickButton(operatorLeft, 4).whenPressed(new AutoShovelOfFriesCommandGroup());
		// -> 2: Auto Guillotine
		// Auto guillotine routine, ends when drive joysticks are touched
		new JoystickButton(operatorLeft, 6).whenPressed(new AutoGuillotineCommandGroup());

		// -> 2 + 3: Align
		// Auto align the robot to target, ends when drive joysticks are touched
		alignButton = new DoubleJoystickButton(new JoystickButton(operatorLeft, 2), new JoystickButton(operatorLeft, 3));
		
		alignCommand = new AlignCommandGroup();
		alignButton.whenPressed(alignCommand);
		alignButton.whenReleased(new AlignEndCommandGroup());
		// => 9: Get low
		// Puts robot in position to go under low bar (hood down, intake to
		// appropriate height)
		new JoystickButton(operatorLeft, 9).whenPressed(new GetLowCommandGroup());
		// => 11: Prepare Long Shot
		// Prepares long shot (shooter wheels to speed, hood up to approximate
		// angle, drops intake to position where it does not block shot, turns
		// on lights)
		new JoystickButton(operatorLeft, 11).whenPressed(new PrepareLongShotCommandGroup());
		// => 10: Prepare Close Shot
		// Prepares close shot (shooter wheels to speed, hood up to approximate
		// angle, drops intake to position where it does not block shot, turns
		// on lights)
		new JoystickButton(operatorLeft, 10).whenPressed(new PrepareCloseShotCommandGroup());
		// => 5: Prepare Low Goal
		// Prepares low goal dump (positions intake to proper height)
		new JoystickButton(operatorLeft, 5).whenPressed(new IntakeArmPrepareLowGoalCommand());
		// => 8: Low Goal
		// Double checks intake height, reverses intake and loader to spit ball
		// into low goal.
		new JoystickButton(operatorLeft, 8).whenPressed(new LowGoalStartCommandGroup());
		new JoystickButton(operatorLeft, 8).whenReleased(new LowGoalFinishCommandGroup());

		// -> 12: Puke
		// Reverses all ball handling systems (shooter, loader, intake) (SHOOTER
		// RAMPING REQUIRED)
		new JoystickButton(operatorLeft, 12).whenPressed(new PukeCommandGroup());
		new JoystickButton(operatorLeft, 12).whenReleased(new PukeFinishCommandGroup());
		// => 1: Laser Cannon Trigger (Shoot)
		// Forces intake on to shoot (loader auto off based on photo sensor 3,
		// turns off lights)
		new JoystickButton(operatorLeft, 1).whenPressed(new LaserCannonTriggerCommand());
	}

	public void initOperatorRight() {
		// Operator Right: (2)

		// => 1: Up Height (Climb Height)
		// Positions intake arm to vertical height, ensures intake off (also
		// used for climb)
		new JoystickButton(operatorRight, 4).whenPressed(new IntakeArmUpHeightCommandGroup());
		// => 2: Intake Height
		// Positions intake arm to collecting height turns on intake
		new JoystickButton(operatorRight, 3).whenPressed(new IntakeArmIntakeHeightCommandGroup());
		new JoystickButton(operatorRight, 3).whenPressed(new LoaderRollerIntakeUntilPhotoCommand());
		// => 3: Bumper Height (Home)
		// Positions intake arm to home height (such that it will contact the
		// bumper of another robot), ensures intake off
		new JoystickButton(operatorRight, 2).whenPressed(new IntakeArmHomeHeightCommandGroup());
		// => 4: Bottom Height
		// Positions intake arm to bottom height, ensures intake off
		new JoystickButton(operatorRight, 1).whenPressed(new IntakeArmBottomHeightCommandGroup());
		// -> 5: Intake On
		// Overrides intake rollers
		new JoystickButton(operatorRight, 5).whenPressed(new IntakeRollerSwapCommand());
		// => 7: Cancel all commands
		new JoystickButton(operatorRight, 7).whenPressed(new CancelAllCommand());
		// => 8: Climb
		// Fully retracts elevator, stops after 1 second of motor stall
		new JoystickButton(operatorRight, 8).whenPressed(new ElevatorRetractCommand());
		new JoystickButton(operatorRight, 8).whenPressed(new IntakeArmUpHeightCommandGroup());
		new JoystickButton(operatorRight, 8).whenPressed(new HoodPositionCommand(RobotMap.HOOD_HANG_SHOT));
		// => 9: Extend & Intake Up
		// Extends elevator completely, brings intake to up position
		new JoystickButton(operatorRight, 9).whenPressed(new ElevatorExtendCommand());
		new JoystickButton(operatorRight, 9).whenPressed(new IntakeArmUpHeightCommandGroup());
		new JoystickButton(operatorRight, 9).whenPressed(new HoodPositionCommand(RobotMap.HOOD_HANG_SHOT));

		// => 10: Prepare Climb
		// Un-latches elevator (drives the elevator down a little)
		new JoystickButton(operatorRight, 10).whenPressed(new ElevatorUnlatchCommand());
		// => 11: Dumb Drive switch
		// Switch closed loop drive off (in case of sensor failure)
		dumbDrive = new JoystickButton(operatorRight, 11);
		// -> 12: Brake Light Cutout Switch
		// Disables robot "shot ready" LED sequences (in the event that
		// signifying we are about to shoot enables defense robots to defend
		// more effectively
		dumbShooter = new JoystickButton(operatorRight, 12);
	}

	public static OI getInstance() {
		init();
		return singleton;
	}

	public static void init() {
		if (singleton == null) {
			singleton = new OI();
		}
	}

	// -> Joy1: Arm Position Joystick
	// Overrides intake arm position (overrides pot, not limit switches)
	// snapCoffinJoysticks(operatorRight.getAxis(AxisType.kY))
	public double getIntakeArmMoveValue() {
		return snapCoffinJoysticks(-operatorRight.getAxis(AxisType.kY));
	}
	
	// -> Joy2: Loader Roller Joystick
	// Overrides loader motor power
	// snapCoffinJoysticks(operatorRight.getAxis(AxisType.kX))
	public double getLoaderRollerMoveValue() {
		return snapCoffinJoysticks(operatorRight.getAxis(AxisType.kX));
	}

	// -> Joy3: Hood Joystick
	// Overrides hood angle (undone if another auto hood angle command is
	// sent)
	// snapCoffinJoysticks(operatorRight.getAxis(AxisType.kZ))
	public double getHoodMoveValue() {
		return snapCoffinJoysticks(operatorRight.getAxis(AxisType.kThrottle));
	}
	
	// -> Joy4: Elevator Joystick
	// Overrides elevator (limit switches still operate)
	// snapCoffinJoysticks(operatorRight.getAxis(AxisType.kThrottle))
	public double getElevatorMoveValue() {
		return snapCoffinJoysticks(operatorRight.getAxis(AxisType.kZ));
	}

	public double getArcadeMoveValue() {
		return snapDriveJoysticks(driveLeft.getY()) * (driveLeft.getRawButton(2) ? 1 : -1);
	}

	public double getArcadeTurnValue() {
		if(new JoystickButton(driveRight, 6).get() || new JoystickButton(driveRight, 7).get() || new JoystickButton(driveRight, 10).get() || new JoystickButton(driveRight, 11).get() || new JoystickButton(driveRight, 8).get()  || new JoystickButton(driveRight, 9).get())
			return 0;

		return snapDriveJoysticks(driveRight.getX());
	}

	public double getTankLeftValue() {
		return -snapDriveJoysticks(driveRight.getY());
	}

	public double getTankRightValue() {
		return snapDriveJoysticks(driveLeft.getY());
	}

	public double getDriveLeftXValue() {
		return snapDriveJoysticks(driveLeft.getX());
	}
	
	public double getDriveLeftYValue() {
		return snapDriveJoysticks(driveLeft.getY());
	}
	
	public double getDriveRightXValue() {
		return snapDriveJoysticks(driveRight.getX());
	}
	
	public double getDriveRightYValue() {
		return snapDriveJoysticks(driveRight.getY());
	}
	
	private static double snapDriveJoysticks(double value) {
		if (Math.abs(value) < JOYSTICK_DEAD_ZONE) {
			value = 0;
		} else if (value > 0) {
			value -= JOYSTICK_DEAD_ZONE;
		} else {
			value += JOYSTICK_DEAD_ZONE;
		}
		value /= 1 - JOYSTICK_DEAD_ZONE;

		return value;
	}
	
	private static double snapCoffinJoysticks(double value)
	{
		if(value > -0.5 && value < 0.5)
			return 0;
		
		return ((Math.abs(value)-0.5) / 0.5) * Math.signum(value);
	}

	public double getRawMove() {
		return -driveLeft.getY();
	}

	public double getRawTurn() {
		return -driveRight.getX();
	}

	public double getTurnAdjust() {
		return driveRight.getRawButton(1) ? 0.5 : 1;
	}

	@Override
	public void smartDashboardInfo() {
		speedMultiplier = SmartDashboard.getNumber("Speed Multiplier");
	}
	
	public boolean isTankNonZero() {
		return getTankLeftValue() != 0 || getTankRightValue() != 0;
	}
	
	public boolean isArcadeNonZero() {
		return getArcadeMoveValue() != 0 || getArcadeTurnValue() != 0;
	}

	public boolean isCurrentModeNonZero() throws DrivingModeException {
		if(drivingModeChooser.getSelected() == DrivingMode.ARCADE) {
			return isArcadeNonZero();
		} else if(drivingModeChooser.getSelected() == DrivingMode.TANK) {
			return isTankNonZero();
		} else {
			throw new DrivingModeException((DrivingMode) drivingModeChooser.getSelected());
		}
	}
	
	public boolean isDriveNonZero() {
		return getDriveLeftXValue() != 0 || getDriveRightXValue() != 0 || getDriveLeftYValue() != 0 || getDriveRightYValue() != 0;
	}
		
	@Override
	public void periodic() {
		
		if(Shooter.getInstance().hasBall() || LoaderRoller.getInstance().hasBall() || IntakeRoller.getInstance().hasBall()) {
			if(LoaderRoller.getInstance().getCurrentCommand() != null && LoaderRoller.getInstance().getCurrentCommand().getName().equals("IntakeArmIntakeHeightCommandGroup")) {
				System.out.println("Here3");
				NRCommand.cancelCommand(LoaderRoller.getInstance().getCurrentCommand());
				LoaderRoller.getInstance().setLoaderSpeed(0);
			}
		}
		
		if(isDriveNonZero()) {
	
			if(Drive.getInstance().getCurrentCommand() != null && !(Drive.getInstance().getCurrentCommand().getName().equals("DriveJoystickCommand") || Drive.getInstance().getCurrentCommand() == Robot.getInstance().driveWall)) {
				System.out.println("Drive Left X " + getDriveLeftXValue());
				System.out.println("Drive Right X " + getDriveRightXValue());
				System.out.println("Drive Left Y " + getDriveLeftYValue());
				System.out.println("Drive Right Y " + getDriveRightYValue());
				System.out.println("Cancelling because of drive joystick");

				NRCommand.cancelCommand(Drive.getInstance().getCurrentCommand());
			}
		}
		
		if(getLoaderRollerMoveValue() != 0) {
			if(LoaderRoller.getInstance().getCurrentCommand() != null && !LoaderRoller.getInstance().getCurrentCommand().getName().equals("LoaderRollerJoystickCommand")) {
				System.out.println("Here1" + LoaderRoller.getInstance().getCurrentCommand().getName());
				NRCommand.cancelCommand(LoaderRoller.getInstance().getCurrentCommand());
				new LoaderRollerJoystickCommand().start();
				LoaderRoller.getInstance().setLoaderSpeed(0);
			}
		} else {
			if(LoaderRoller.getInstance().getCurrentCommand() != null && LoaderRoller.getInstance().getCurrentCommand().getName().equals("LoaderRollerJoystickCommand")) {
				System.out.println("Here2");
				NRCommand.cancelCommand(LoaderRoller.getInstance().getCurrentCommand());
				LoaderRoller.getInstance().setLoaderSpeed(0);
			}
		}
		
		if(getIntakeArmMoveValue() != 0) {
			if(IntakeArm.getInstance().getCurrentCommand() != null && !IntakeArm.getInstance().getCurrentCommand().getName().equals("IntakeArmJoystickCommand")) {
				NRCommand.cancelCommand(IntakeArm.getInstance().getCurrentCommand());
				System.out.println("Intake arm joystick value " + getIntakeArmMoveValue());
				System.out.println("Cancelling because of intake arm joystick");
			}
		}
		
		if(getHoodMoveValue() != 0) {
			if(Hood.getInstance().getCurrentCommand() != null && !Hood.getInstance().getCurrentCommand().getName().equals("HoodJoystickCommand")) {
				NRCommand.cancelCommand(Hood.getInstance().getCurrentCommand());
				System.out.println("Hood joystick value " + getHoodMoveValue());
				System.out.println("Cancelling because of hood joystick");

			}
		}
		
		if(getElevatorMoveValue() != 0) {
			if(Elevator.getInstance().getCurrentCommand() != null && !Elevator.getInstance().getCurrentCommand().getName().equals("ElevatorJoystickCommand") && !Elevator.getInstance().getCurrentCommand().getName().equals("ElevatorResetCommand")) {
				NRCommand.cancelCommand(Elevator.getInstance().getCurrentCommand());
			}
		}
	}


	public boolean shooterOn() {
		return !dumbShooter.get();
	}
}
