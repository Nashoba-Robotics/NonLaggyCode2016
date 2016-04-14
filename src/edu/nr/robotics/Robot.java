package edu.nr.robotics;

import java.util.ArrayList;

import com.ni.vision.VisionException;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.WaitUntilGyroCommand;
import edu.nr.lib.interfaces.Periodic;
import edu.nr.lib.interfaces.SmartDashboardSource;
import edu.nr.lib.navx.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.robotics.auton.AutonAlignCommand;
import edu.nr.robotics.auton.AutonDoNothingCommand;
import edu.nr.robotics.auton.AutonForwardAlignLeftCommand;
import edu.nr.robotics.auton.AutonForwardAlignLowBarCommand;
import edu.nr.robotics.auton.AutonForwardAlignMiddleCommand;
import edu.nr.robotics.auton.AutonForwardAlignRightCommand;
import edu.nr.robotics.auton.AutonForwardAlignRoughTerrainLeftCommand;
import edu.nr.robotics.auton.AutonForwardAlignRoughTerrainMiddleCommand;
import edu.nr.robotics.auton.AutonForwardAlignRoughTerrainRightCommand;
import edu.nr.robotics.auton.AutonForwardLowBarCommand;
import edu.nr.robotics.auton.AutonForwardOverCommand;
import edu.nr.robotics.auton.AutonForwardRoughTerrainCommand;
import edu.nr.robotics.auton.AutonGuillotineCommandGroup;
import edu.nr.robotics.commandgroups.AlignCommandGroup;
import edu.nr.robotics.subsystems.climb.Elevator;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveSetPIDSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.DriveTurnSmartDashboardCommand;
import edu.nr.robotics.subsystems.drive.FieldCentric;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.intakeroller.IntakeRoller;
import edu.nr.robotics.subsystems.loaderroller.LaserCannonTriggerCommand;
import edu.nr.robotics.subsystems.loaderroller.LoaderRoller;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class. 
 * If you change the name of this class or the package after creating 
 * this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends RobotBase {
		
	public AlignCommandGroup.State state;
	RobotDiagram robotDiagram;
	
	public Command driveWall;
	

	boolean doneFirstTime = false;
	
	public LaserCannonTriggerCommand fireCommand;

	private static Robot singleton;
	
	//This is technically unsafe, since it's not guarenteed not to return a null pointer, but we don't have any code that runs before the robot is initialized.
	public static Robot getInstance() {
		return singleton; 
	}
	
	Command autonomousCommand;
	SendableChooser autoCommandChooser;
	
	public SendableChooser autoCommandPickerOne;
	public SendableChooser autoCommandPickerTwo;
	public SendableChooser autoCommandPickerThree;
	public SendableChooser autoCommandPickerFour;
	public SendableChooser autoCommandPickerFive;
	public SendableChooser autoCommandPickerSix;

	public ArrayList<Subsystem> subsystems = new ArrayList<Subsystem>();
	public ArrayList<SmartDashboardSource> smartDashboardSources = new ArrayList<SmartDashboardSource>();
	public ArrayList<Periodic> periodics = new ArrayList<Periodic>();
	
	private boolean m_disabledInitialized;
	private boolean m_autonomousInitialized;
	private boolean m_teleopInitialized;
	private boolean m_testInitialized;
	
	long prevTime;
	ArrayList<Long> last1000Times;
	
	public enum Mode {
		TELEOP, AUTONOMOUS, DISABLED, TEST
	}

	public Mode currentMode;
	public boolean useDumbShooter;
	
	public Robot() {
		singleton = this;
	    // set status for initialization of disabled, autonomous, and teleop code.
	    m_disabledInitialized = false;
	    m_autonomousInitialized = false;
	    m_teleopInitialized = false;
	    m_testInitialized = false;
	    last1000Times = new ArrayList<Long>();
	    prevTime = System.currentTimeMillis();
	}
	
	private void updateLoopTime() {
		SmartDashboard.putNumber("Time in the loop", System.currentTimeMillis() - prevTime);
	    prevTime = System.currentTimeMillis();
	    last1000Times.add(prevTime);
	    if(last1000Times.size() > 1000) {
	    	last1000Times.remove(0);
	    	SmartDashboard.putNumber("Time of last 1000 loops", System.currentTimeMillis() - last1000Times.get(0));
	    }
	}
	
	@Override
	public void startCompetition() {
		UsageReporting.report(tResourceType.kResourceType_Framework, tInstances.kFramework_Iterative);

		robotInit();

		// Tell the DS that the robot is ready to be enabled
		FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramStarting();

		// loop forever, calling the appropriate mode-dependent function
		LiveWindow.setEnabled(false);
		while (true) {
			updateLoopTime();

			// Call the appropriate function depending upon the current robot
			// mode
			if (isDisabled()) {
				// call DisabledInit() if we are now just entering disabled mode
				// from either a different mode or from power-on
				if (!m_disabledInitialized) {
					LiveWindow.setEnabled(false);
					initialize();
					m_disabledInitialized = true;
					// reset the initialization flags for the other modes
					m_autonomousInitialized = false;
					m_teleopInitialized = false;
					m_testInitialized = false;
				}
				FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramDisabled();
			} else if (isTest()) {
				// call TestInit() if we are now just entering test mode from
				// either a different mode or from power-on
				if (!m_testInitialized) {
					LiveWindow.setEnabled(true);
					initialize();
					m_testInitialized = true;
					m_autonomousInitialized = false;
					m_teleopInitialized = false;
					m_disabledInitialized = false;
				}
				FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTest();
			} else if (isAutonomous()) {
				// call Autonomous_Init() if this is the first time
				// we've entered autonomous_mode
				if (!m_autonomousInitialized) {
					LiveWindow.setEnabled(false);
					autonomousCommand = (Command) autoCommandChooser.getSelected();
					autonomousCommand.start();
					initialize();
					m_autonomousInitialized = true;
					m_testInitialized = false;
					m_teleopInitialized = false;
					m_disabledInitialized = false;
				}
				FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramAutonomous();
			} else {
				// call Teleop_Init() if this is the first time
				// we've entered teleop_mode
				if (!m_teleopInitialized) {
					LiveWindow.setEnabled(false);
					// This makes sure that the autonomous stops running when
					// teleop starts running.
					if (autonomousCommand != null) {
						autonomousCommand.cancel();
					}

					initialize();
					m_teleopInitialized = true;
					m_testInitialized = false;
					m_autonomousInitialized = false;
					m_disabledInitialized = false;
				}
				FRCNetworkCommunicationsLibrary.FRCNetworkCommunicationObserveUserProgramTeleop();
			}
			periodic();
		}
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	private void robotInit() {
		System.out.println("Robot Init Started");
		
		new Thread(new Runnable() {
			
			@Override
			public void run () {			
				AndroidServer.executeCommand("ssh lvuser@localhost ./adbScript.sh");
				
		        AndroidServer.getInstance().run();
	        }}).start();
                
        initCamera();
		initSubsystems();
		initSmartDashboard();
		robotDiagram = new RobotDiagram();
	}
	
	private void initSmartDashboard() {	
		System.out.println("About to init SmartDashboard");
		/*autoCommandPickerOne = new SendableChooser();
		autoCommandPickerOne.addDefault("1-Do nothing", new AutonDoNothingCommand());
		autoCommandPickerOne.addObject("Drive over obstacle25", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_25, 1.0));
		autoCommandPickerOne.addObject("Drive onto obstacle25", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_25, 1.0));
		autoCommandPickerOne.addObject("Drive over obstacle134", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_134, 1.0));
		autoCommandPickerOne.addObject("Drive onto obstacle134", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_134, 1.0));
		autoCommandPickerOne.addObject("Auto Guillotine", new AutoGuillotineCommandGroup());
		autoCommandPickerOne.addObject("Auto Shovel of Fries", new AutoShovelOfFriesCommandGroup());
		autoCommandPickerOne.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandPickerOne.addObject("Return to normal (front)", new AutonReturnToNormalFrontCommandGroup());
		autoCommandPickerOne.addObject("Return to normal (back)", new AutonReturnToNormalBackCommandGroup());
		autoCommandPickerOne.addObject("Rotate 180", new DriveAnglePIDCommand(180, AngleUnit.DEGREE));
		SmartDashboard.putData("Picker One", autoCommandPickerOne);
		
		autoCommandPickerTwo = new SendableChooser();
		autoCommandPickerTwo.addDefault("2-Do nothing", new AutonDoNothingCommand());
		autoCommandPickerTwo.addObject("Drive over obstacle25", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_25, 1.0));
		autoCommandPickerTwo.addObject("Drive onto obstacle25", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_25, 1.0));
		autoCommandPickerTwo.addObject("Drive over obstacle134", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_134, 1.0));
		autoCommandPickerTwo.addObject("Drive onto obstacle134", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_134, 1.0));
		autoCommandPickerTwo.addObject("Auto Guillotine", new AutoGuillotineCommandGroup());
		autoCommandPickerTwo.addObject("Auto Shovel of Fries", new AutoShovelOfFriesCommandGroup());
		autoCommandPickerTwo.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandPickerTwo.addObject("Return to normal (front)", new AutonReturnToNormalFrontCommandGroup());
		autoCommandPickerTwo.addObject("Return to normal (back)", new AutonReturnToNormalBackCommandGroup());
		autoCommandPickerOne.addObject("Rotate 180", new DriveAnglePIDCommand(180, AngleUnit.DEGREE));
		SmartDashboard.putData("Picker Two", autoCommandPickerTwo);

		autoCommandPickerThree = new SendableChooser();
		autoCommandPickerThree.addDefault("3-Do nothing", new AutonDoNothingCommand());
		autoCommandPickerThree.addObject("Drive over obstacle25", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_25, 1.0));
		autoCommandPickerThree.addObject("Drive onto obstacle25", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_25, 1.0));
		autoCommandPickerThree.addObject("Drive over obstacle134", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_134, 1.0));
		autoCommandPickerThree.addObject("Drive onto obstacle134", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_134, 1.0));
		autoCommandPickerThree.addObject("Auto Guillotine", new AutoGuillotineCommandGroup());
		autoCommandPickerThree.addObject("Auto Shovel of Fries", new AutoShovelOfFriesCommandGroup());
		autoCommandPickerThree.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandPickerThree.addObject("Return to normal (front)", new AutonReturnToNormalFrontCommandGroup());
		autoCommandPickerThree.addObject("Return to normal (back)", new AutonReturnToNormalBackCommandGroup());
		autoCommandPickerThree.addObject("Rotate 180", new DriveAnglePIDCommand(180, AngleUnit.DEGREE));
		SmartDashboard.putData("Picker Three", autoCommandPickerThree);

		autoCommandPickerFour = new SendableChooser();
		autoCommandPickerFour.addDefault("4-Do nothing", new AutonDoNothingCommand());
		autoCommandPickerFour.addObject("Drive over obstacle25", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_25, 1.0));
		autoCommandPickerFour.addObject("Drive onto obstacle25", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_25, 1.0));
		autoCommandPickerFour.addObject("Drive over obstacle134", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_134, 1.0));
		autoCommandPickerFour.addObject("Drive onto obstacle134", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_134, 1.0));
		autoCommandPickerFour.addObject("Auto Guillotine", new AutoGuillotineCommandGroup());
		autoCommandPickerFour.addObject("Auto Shovel of Fries", new AutoShovelOfFriesCommandGroup());
		autoCommandPickerFour.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandPickerFour.addObject("Return to normal (front)", new AutonReturnToNormalFrontCommandGroup());
		autoCommandPickerFour.addObject("Return to normal (back)", new AutonReturnToNormalBackCommandGroup());
		autoCommandPickerFour.addObject("Rotate 180", new DriveAnglePIDCommand(180, AngleUnit.DEGREE));
		SmartDashboard.putData("Picker Four", autoCommandPickerFour);

		autoCommandPickerFive = new SendableChooser();
		autoCommandPickerFive.addDefault("5-Do nothing", new AutonDoNothingCommand());
		autoCommandPickerFive.addObject("Drive over obstacle25", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_25, 1.0));
		autoCommandPickerFive.addObject("Drive onto obstacle25", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_25, 1.0));
		autoCommandPickerFive.addObject("Drive over obstacle134", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_134, 1.0));
		autoCommandPickerFive.addObject("Drive onto obstacle134", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_134, 1.0));
		autoCommandPickerFive.addObject("Auto Guillotine", new AutoGuillotineCommandGroup());
		autoCommandPickerFive.addObject("Auto Shovel of Fries", new AutoShovelOfFriesCommandGroup());
		autoCommandPickerFive.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandPickerFive.addObject("Return to normal (front)", new AutonReturnToNormalFrontCommandGroup());
		autoCommandPickerFive.addObject("Return to normal (back)", new AutonReturnToNormalBackCommandGroup());
		autoCommandPickerFive.addObject("Rotate 180", new DriveAnglePIDCommand(180, AngleUnit.DEGREE));
		SmartDashboard.putData("Picker Five", autoCommandPickerFive);

		autoCommandPickerSix = new SendableChooser();
		autoCommandPickerSix.addDefault("6-Do nothing", new AutonDoNothingCommand());
		autoCommandPickerSix.addObject("Drive over obstacle25", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_25, 1.0));
		autoCommandPickerSix.addObject("Drive onto obstacle25", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_25, 1.0));
		autoCommandPickerSix.addObject("Drive over obstacle134", new DriveDistanceCommand(RobotMap.OVER_DISTANCE_134, 1.0));
		autoCommandPickerSix.addObject("Drive onto obstacle134", new DriveDistanceCommand(RobotMap.ONTO_DISTANCE_134, 1.0));
		autoCommandPickerSix.addObject("Auto Guillotine", new AutoGuillotineCommandGroup());
		autoCommandPickerSix.addObject("Auto Shovel of Fries", new AutoShovelOfFriesCommandGroup());
		autoCommandPickerSix.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandPickerSix.addObject("Return to normal (front)", new AutonReturnToNormalFrontCommandGroup());
		autoCommandPickerSix.addObject("Return to normal (back)", new AutonReturnToNormalBackCommandGroup());
		autoCommandPickerSix.addObject("Rotate 180", new DriveAnglePIDCommand(180, AngleUnit.DEGREE));
		SmartDashboard.putData("Picker Six", autoCommandPickerSix);*/
		
		autoCommandChooser = new SendableChooser();
		autoCommandChooser.addDefault("Do Nothing", new AutonDoNothingCommand());
		//autoCommandChooser.addObject("Follow instructions", new AutonFollowInstructionsCommand());
		autoCommandChooser.addObject("Align and shoot", new AutonAlignCommand());
		autoCommandChooser.addObject("Forward no shoot Low Bar", new AutonForwardLowBarCommand());
		autoCommandChooser.addObject("Forward no shoot Rough Terrain", new AutonForwardRoughTerrainCommand());
		autoCommandChooser.addObject("Forward no shoot Guillotine", new AutonGuillotineCommandGroup());
		autoCommandChooser.addObject("Forward no shoot others", new AutonForwardOverCommand());
		autoCommandChooser.addObject("Forward and shoot Low Bar", new AutonForwardAlignLowBarCommand());
		autoCommandChooser.addObject("Forward and shoot Left", new AutonForwardAlignLeftCommand());
		autoCommandChooser.addObject("Forward and shoot Middle", new AutonForwardAlignMiddleCommand());
		autoCommandChooser.addObject("Forward and shoot Right", new AutonForwardAlignRightCommand());
		autoCommandChooser.addObject("Forward and shoot Rough Terrain Left", new AutonForwardAlignRoughTerrainLeftCommand());
		autoCommandChooser.addObject("Forward and shoot Rough Terrain Middle", new AutonForwardAlignRoughTerrainMiddleCommand());
		autoCommandChooser.addObject("Forward and shoot Rough Terrain Right", new AutonForwardAlignRoughTerrainRightCommand());

		/*autoCommandChooser.addObject("Forward over obstacle, align, shoot 2", new AutonOverAlignShootCommandGroup(Positions.two));
		autoCommandChooser.addObject("Forward over obstacle, align, shoot, return to obstacle 2", new AutonOverAlignShootReturnCommandGroup(Positions.two));
		autoCommandChooser.addObject("Forward over obstacle, align, shoot 5", new AutonOverAlignShootCommandGroup(Positions.five));
		autoCommandChooser.addObject("Forward over obstacle, align, shoot, return to obstacle 5", new AutonOverAlignShootReturnCommandGroup(Positions.five));
		autoCommandChooser.addObject("Forward over obstacle, align, shoot 1", new AutonOverAlignShootCommandGroup(Positions.one));
		autoCommandChooser.addObject("Forward over obstacle, align, shoot, return to obstacle 1", new AutonOverAlignShootReturnCommandGroup(Positions.one));
		autoCommandChooser.addObject("Forward over obstacle, align, shoot 34", new AutonOverAlignShootCommandGroup(Positions.threefour));
		autoCommandChooser.addObject("Forward over obstacle, align, shoot, return to obstacle 34", new AutonOverAlignShootReturnCommandGroup(Positions.threefour));*/

		// Add more options like:
		// autoCommandChooser.addObject(String name, Command command);
		SmartDashboard.putData("Autonomous Chooser", autoCommandChooser);
		
		OI.getInstance().drivingModeChooser = new SendableChooser();
		OI.getInstance().drivingModeChooser.addDefault("arcade", DrivingMode.ARCADE);
		OI.getInstance().drivingModeChooser.addObject("tank", DrivingMode.TANK);
		SmartDashboard.putData("Driving Mode Chooser", OI.getInstance().drivingModeChooser);
		
		SmartDashboard.putData("Gyro angle command", new WaitUntilGyroCommand(20));
		
		SmartDashboard.putData("Hood Jetson angle command", new HoodJetsonPositionCommand());
		
		SmartDashboard.putData("Turn 3 degree command", new DriveAnglePIDCommand(-15, AngleUnit.DEGREE));
		
		LiveWindow.addSensor("Jetson", "Ready to shoot", LiveWindowClasses.readyToShoot);
		
		SmartDashboard.putNumber("Hood Multiplier Percent", 100);
		
		SmartDashboard.putNumber("Hood location for setting", 48);
		
		SmartDashboard.putNumber("Turn P", RobotMap.TURN_P);
		SmartDashboard.putNumber("Turn I", RobotMap.TURN_I);

		SmartDashboard.putNumber("Drive P", RobotMap.DRIVE_P);
		SmartDashboard.putNumber("Drive I", RobotMap.DRIVE_I);
		SmartDashboard.putNumber("Drive D", RobotMap.DRIVE_D);
		SmartDashboard.putNumber("Drive F", RobotMap.DRIVE_F);
		
		SmartDashboard.putNumber("Turn Constant Value", 0.3);
		
		SmartDashboard.putData("Set drive pid", new DriveSetPIDSmartDashboardCommand());
		SmartDashboard.putData("Turn Smart Dashboard", new DriveTurnSmartDashboardCommand());


	}
	
	private void initCamera() {
		System.out.flush();
		// the camera name (ex "cam0") can be found through the roborio web interface
		try{ 
			CameraServer server = CameraServer.getInstance();
			server.setQuality(50);
			server.startAutomaticCapture("cam0");
		} catch (VisionException e) {
			e.printStackTrace();
		}
		
	}
	
	private void initSubsystems() {
		System.out.println("About to init the subsystems");
		//Init subsystems
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
		
		//Add subsystems to subsystem array list
		subsystems.add(Drive.getInstance());
		subsystems.add(Shooter.getInstance());
		subsystems.add(IntakeArm.getInstance());
		subsystems.add(LoaderRoller.getInstance());
		subsystems.add(Elevator.getInstance());
		subsystems.add(Hood.getInstance());
		subsystems.add(IntakeRoller.getInstance());
		
		//Add SmartDashboard sources to the smartdashboard source array list
		smartDashboardSources.add(Drive.getInstance());
		smartDashboardSources.add(FieldCentric.getInstance());
		smartDashboardSources.add(Shooter.getInstance());
		smartDashboardSources.add(IntakeArm.getInstance());
		smartDashboardSources.add(Elevator.getInstance());
		smartDashboardSources.add(Hood.getInstance());
		smartDashboardSources.add(IntakeRoller.getInstance());
		smartDashboardSources.add(OI.getInstance());
		smartDashboardSources.add(LoaderRoller.getInstance());
		
		LiveWindow.addSensor("Drive", "Gyro", NavX.getInstance());

		
		periodics.add(Drive.getInstance());
		periodics.add(OI.getInstance());
		periodics.add(Hood.getInstance());
		periodics.add(IntakeArm.getInstance());
	}

	/**
	 * A generic periodic function that is called by the periodic functions for
	 * the specific modes
	 */
	private void periodic() {
		SmartDashboard.putBoolean("Banner 1", IntakeRoller.getInstance().hasBall());
		SmartDashboard.putBoolean("Banner 2", LoaderRoller.getInstance().hasBall());
		SmartDashboard.putBoolean("Banner 3", Shooter.getInstance().hasBall());	
		
		
		Drive.getInstance().setPIDEnabled(!OI.getInstance().dumbDrive.get());

		
		periodics.forEach(Periodic::periodic);
		
		FieldCentric.getInstance().update();
		Scheduler.getInstance().run();

		if(isTest())
			LiveWindow.run();

		smartDashboardSources.forEach(SmartDashboardSource::smartDashboardInfo);
		
		SmartDashboard.putData(robotDiagram);
	}

	/**
	 * A generic initialization function that is called by the periodic
	 * functions for the specific modes
	 */
	private void initialize() {
		if(!doneFirstTime) {
			doneFirstTime = true;
			Elevator.getInstance().resetEncoder();
		}
		if (isDisabled()) {
			IntakeArm.getInstance().disable();
			//Fix intake arm cancelling
			IntakeRoller.getInstance().setRollerSpeed(0);
			LoaderRoller.getInstance().setLoaderSpeed(0);
			Hood.getInstance().disable();
			Elevator.getInstance().setMotorValue(0);
		}
	}
}
