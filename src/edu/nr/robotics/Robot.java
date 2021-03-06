package edu.nr.robotics;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.WaitUntilGyroCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfiler;
import edu.nr.lib.motionprofiling.OneDimensionalMotionProfilerBasic;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectorySimple;
import edu.nr.lib.motionprofiling.OneDimensionalTrajectory;
import edu.nr.lib.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.Robot.defense;
import edu.nr.robotics.Robot.position;
import edu.nr.robotics.auton.AutonAlignCommand;
import edu.nr.robotics.auton.AutonDoNothingCommand;
import edu.nr.robotics.auton.AutonFollowInstructionsForwardCommand;
import edu.nr.robotics.auton.AutonFollowInstructionsShootCommand;
import edu.nr.robotics.auton.AutonForwardDefenseCommand;
import edu.nr.robotics.auton.AutonForwardLowBarCommand;
import edu.nr.robotics.auton.AutonForwardOverCommand;
import edu.nr.robotics.auton.AutonForwardRoughTerrainCommand;
import edu.nr.robotics.auton.AutonGuillotineCommandGroup;
import edu.nr.robotics.auton.AutonTurnPositionCommand;
import edu.nr.robotics.commandgroups.AlignCommandGroup;
import edu.nr.robotics.subsystems.climb.Elevator;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveGyroAngleSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveMotionProfileDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveSetPIDSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveSetTurnPIDSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveTurnSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.FieldCentric;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;
import edu.nr.robotics.subsystems.hood.HoodSmartDashboardVelocityCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveUpUntilPositionCommand;
import edu.nr.robotics.subsystems.intakeroller.IntakeRoller;
import edu.nr.robotics.subsystems.loaderroller.LaserCannonTriggerCommand;
import edu.nr.robotics.subsystems.loaderroller.LoaderRoller;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class. If you change the name
 * of this class or the package after creating this project, you must also
 * update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {

	RobotDiagram robotDiagram;
	

	public Command driveWall;

	boolean doneFirstTime = false;

	public LaserCannonTriggerCommand fireCommand;

	private static Robot singleton;

	// This is technically unsafe, since it's not guarenteed not to return a
	// null pointer, but we don't have any code that runs before the robot is
	// initialized.
	public static Robot getInstance() {
		return singleton;
	}

	Command autonomousCommand;
	SendableChooser<CommandGroup> autoCommandChooser;

	public SendableChooser<defense> defensePicker;
	public SendableChooser<position> positionPicker;

	public enum defense {
		RoughTerrain, Guillotine, LowBar, Other, Cheval
	}

	public enum position {
		One, Two, Three, Four, Five
	}

	public ArrayList<Subsystem> subsystems = new ArrayList<Subsystem>();
	public ArrayList<SmartDashboardSource> smartDashboardSources = new ArrayList<SmartDashboardSource>();
	public ArrayList<Periodic> periodics = new ArrayList<Periodic>();

	long prevTime;
	ArrayList<Long> last1000Times;

	public enum Mode {
		TELEOP, AUTONOMOUS, DISABLED, TEST
	}

	public Mode currentMode;
	public boolean useDumbShooter;

	private void updateLoopTime() {
		SmartDashboard.putNumber("Time in the loop", System.currentTimeMillis() - prevTime);
		prevTime = System.currentTimeMillis();
		last1000Times.add(prevTime);
		if (last1000Times.size() > 1000) {
			last1000Times.remove(0);
			SmartDashboard.putNumber("Time of last 1000 loops", System.currentTimeMillis() - last1000Times.get(0));
		}
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		singleton = this;
		last1000Times = new ArrayList<Long>();
		prevTime = System.currentTimeMillis();

		System.out.println("Robot Init Started");

		new Thread(new Runnable() {

			@Override
			public void run() {
				AndroidServer.executeCommand("ssh lvuser@localhost ./adbScript.sh");

				AndroidServer.getInstance().run();
			}
		}).start();

		//CameraServer.getInstance().startAutomaticCapture();
		initSubsystems();
		initSmartDashboard();
		robotDiagram = new RobotDiagram();
		
	}

	private void initSmartDashboard() {
		System.out.println("About to init SmartDashboard");
		defensePicker = new SendableChooser<defense>();
		defensePicker.addDefault("Other", defense.Other);
		defensePicker.addObject("Rough Terrain", defense.RoughTerrain);
		defensePicker.addObject("Shovel of Fries", defense.Cheval);
		defensePicker.addObject("Guillotine", defense.Guillotine);
		defensePicker.addObject("Low Bar", defense.LowBar);
		SmartDashboard.putData("Defense Picker", defensePicker);

		positionPicker = new SendableChooser<position>();
		positionPicker.addObject("One", position.One);
		positionPicker.addDefault("Two", position.Two);
		positionPicker.addObject("Three", position.Three);
		positionPicker.addObject("Four", position.Four);
		positionPicker.addObject("Five", position.Five);
		SmartDashboard.putData("Position Picker", positionPicker);

		autoCommandChooser = new SendableChooser<CommandGroup>();
		autoCommandChooser.addDefault("Do Nothing", new AutonDoNothingCommand());
		// autoCommandChooser.addObject("Follow instructions", new
		// AutonFollowInstructionsCommand());
		//autoCommandChooser.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandChooser.addObject("Forward no shoot Low Bar", new AutonForwardLowBarCommand());
		autoCommandChooser.addObject("Forward no shoot Rough Terrain", new AutonForwardRoughTerrainCommand());
		autoCommandChooser.addObject("Forward no shoot Guillotine", new AutonGuillotineCommandGroup());
		autoCommandChooser.addObject("Forward no shoot others", new AutonForwardOverCommand());
		autoCommandChooser.addObject("Shoot (choose defense and position)", new AutonFollowInstructionsShootCommand());
		// Add more options like:
		// autoCommandChooser.addObject(String name, Command command);
		SmartDashboard.putData("Autonomous Chooser", autoCommandChooser);

		OI.getInstance().drivingModeChooser = new SendableChooser<DrivingMode>();
		OI.getInstance().drivingModeChooser.addDefault("arcade", DrivingMode.ARCADE);
		OI.getInstance().drivingModeChooser.addObject("tank", DrivingMode.TANK);
		SmartDashboard.putData("Driving Mode Chooser", OI.getInstance().drivingModeChooser);

		SmartDashboard.putData("Gyro angle command", new WaitUntilGyroCommand(20));

		SmartDashboard.putData("Hood Jetson angle command", new HoodJetsonPositionCommand());
		
		SmartDashboard.putNumber("Hood velocity for setting (deg per s)", 0);
		
		SmartDashboard.putData("Hood go at SmartDashboard speed", new HoodSmartDashboardVelocityCommand());
		
		SmartDashboard.putData("Turn 3 degree command", new DriveAnglePIDCommand(-15, AngleUnit.DEGREE));

		SmartDashboard.putNumber("Hood Multiplier Percent", 100);

		SmartDashboard.putNumber("Hood location for setting", 48);

		SmartDashboard.putNumber("Turn P", RobotMap.TURN_P);
		SmartDashboard.putNumber("Turn I", RobotMap.TURN_I);
		SmartDashboard.putNumber("Turn D", RobotMap.TURN_D);

		SmartDashboard.putNumber("Drive P", RobotMap.DRIVE_TURN_P);
		SmartDashboard.putNumber("Drive I", RobotMap.DRIVE_TURN_I);
		SmartDashboard.putNumber("Drive D", RobotMap.DRIVE_TURN_D);
		SmartDashboard.putNumber("Drive F", RobotMap.DRIVE_TURN_F);

		SmartDashboard.putNumber("Turn Constant Value", 0.3);
		SmartDashboard.putNumber("Gyro Angle", 10);

		SmartDashboard.putData("Set drive pid", new DriveSetPIDSmartDashboardCommand());
		SmartDashboard.putData("Gyro Angle SmartDashboard Command", new DriveGyroAngleSmartDashboardCommand());
		SmartDashboard.putData("Turn Smart Dashboard", new DriveTurnSmartDashboardCommand());
		SmartDashboard.putData("Gyro Set Numbers Smart Dashboard", new DriveSetTurnPIDSmartDashboardCommand());
		SmartDashboard.putData("Drive Constant command", new DriveConstantSmartDashboardCommand(true,true,true));
		SmartDashboard.putNumber("Drive Constant Value", 0.5);
		
		SmartDashboard.putString("GyroPID", "0:0");
		SmartDashboard.putNumber("GyroPIDAngle", 0);
				
		SmartDashboard.putData("Go intake arm up",new IntakeArmMoveUpUntilPositionCommand(RobotMap.INTAKE_INTAKE_POS));

		SmartDashboard.putData("Drive Forward 20 Feet with Motion Profiler", new DriveMotionProfileDistanceCommand(20));
		SmartDashboard.putData("Drive Forward 30 Feet with Motion Profiler", new DriveMotionProfileDistanceCommand(30));
		
		
		SmartDashboard.putNumber("Android Adjust Factor", RobotMap.ANDROID_ADJUST_FACTOR);
		
		
	}

	private void initSubsystems() {
		System.out.println("About to init the subsystems");
		// Init subsystems
		Drive.init();
		NavX.init();
		FieldCentric.init();
		Shooter.init();
		IntakeArm.init();
		Elevator.init();
		LoaderRoller.init();
		Hood.init();
		IntakeRoller.init();
		OI.init();

		// Add subsystems to subsystem array list
		subsystems.add(Drive.getInstance());
		subsystems.add(Shooter.getInstance());
		subsystems.add(IntakeArm.getInstance());
		subsystems.add(LoaderRoller.getInstance());
		subsystems.add(Elevator.getInstance());
		subsystems.add(Hood.getInstance());
		subsystems.add(IntakeRoller.getInstance());

		// Add SmartDashboard sources to the smartdashboard source array list
		smartDashboardSources.add(Drive.getInstance());
		smartDashboardSources.add(FieldCentric.getInstance());
		smartDashboardSources.add(Shooter.getInstance());
		smartDashboardSources.add(IntakeArm.getInstance());
		smartDashboardSources.add(Elevator.getInstance());
		smartDashboardSources.add(Hood.getInstance());
		smartDashboardSources.add(IntakeRoller.getInstance());
		smartDashboardSources.add(OI.getInstance());
		smartDashboardSources.add(LoaderRoller.getInstance());

		periodics.add(Drive.getInstance());
		periodics.add(OI.getInstance());
		periodics.add(Hood.getInstance());
		periodics.add(IntakeArm.getInstance());
	}

	/**
	 * Periodic code for test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

	/**
	 * A generic periodic function that is called by the periodic functions for
	 * the specific modes
	 */
	@Override
	public void robotPeriodic() {

		updateLoopTime();

		SmartDashboard.putBoolean("Banner 1", IntakeRoller.getInstance().hasBall());
		SmartDashboard.putBoolean("Banner 2", LoaderRoller.getInstance().hasBall());
		SmartDashboard.putBoolean("Banner 3", Shooter.getInstance().hasBall());

		Drive.getInstance().setPIDEnabled(!OI.getInstance().dumbDrive.get());

		periodics.forEach(Periodic::periodic);
		
		OI.getInstance().speedMultiplier = SmartDashboard.getNumber("Speed Multiplier");
		
		FieldCentric.getInstance().update();
		Scheduler.getInstance().run();

		smartDashboardSources.forEach(SmartDashboardSource::smartDashboardInfo);

		SmartDashboard.putData(robotDiagram);
	}

	/**
	 * Initialization code for disabled mode
	 */
	public void disabledInit() {
		IntakeArm.getInstance().disable();
		// Fix intake arm cancelling
		IntakeRoller.getInstance().setRollerSpeed(0);
		LoaderRoller.getInstance().setLoaderSpeed(0);
		Hood.getInstance().disable();
		Elevator.getInstance().setMotorValue(0);
	}

	/**
	 * Initialization code for autonomous mode
	 */
	public void autonomousInit() {
		autonomousCommand = autoCommandChooser.getSelected();
		if(autonomousCommand instanceof AutonFollowInstructionsShootCommand)
			autonomousCommand = new AutonFollowInstructionsShootCommand();
		else if(autonomousCommand instanceof AutonFollowInstructionsForwardCommand)
			autonomousCommand = new AutonFollowInstructionsForwardCommand();
		autonomousCommand.start();
	}

	/**
	 * Initialization code for teleop mode
	 */
	public void teleopInit() {
		if (!doneFirstTime) {
			doneFirstTime = true;
			Elevator.getInstance().resetEncoder();
		}
	}
	
	
}
