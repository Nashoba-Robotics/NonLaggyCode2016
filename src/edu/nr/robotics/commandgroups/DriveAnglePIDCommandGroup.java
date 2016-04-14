package edu.nr.robotics.commandgroups;

import edu.nr.lib.AngleUnit;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceWithGyroCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DriveAnglePIDCommandGroup extends CommandGroup {
    	
    public  DriveAnglePIDCommandGroup(double angle) {
        addSequential(new DriveAnglePIDCommand(angle, AngleUnit.DEGREE));
        addSequential(new DriveSimpleDistanceWithGyroCommand(1, 0.2));
        addSequential(new DriveSimpleDistanceWithGyroCommand(-1, 0.2));
    }
}