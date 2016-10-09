package edu.nr.robotics.commandgroups;

import edu.nr.lib.NRCommand;
import edu.nr.lib.NavX;
import edu.nr.lib.network.AndroidServer;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.OI;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveWaitForAndroidAngleCommand;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.hood.HoodJetsonPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.nr.robotics.subsystems.intakearm.IntakeArmHomeHeightCommandGroup;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AlignCommandGroup extends CommandGroup {
    
	
    public  AlignCommandGroup() {
    	addParallel(new IntakeArmHomeHeightCommandGroup());
    	addSequential(new WaitCommand(0.25));
    	DriveAnglePIDCommand command = new DriveAnglePIDCommand();
        addParallel(command);
        HoodJetsonPositionCommand hoodCommand = new HoodJetsonPositionCommand();
        addParallel(hoodCommand);
        addSequential(new DriveWaitForAndroidAngleCommand(command,hoodCommand, false));
    }
}
