package edu.nr.robotics.auton;

import edu.nr.lib.AngleUnit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceWithGyroCommand;
import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonOverAlignShootCommandGroup extends CommandGroup {
    
	public enum Positions {
		two, five, threefour, one
	}
	
    public  AutonOverAlignShootCommandGroup(final Positions pos) {
    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());

    	final double overDistance;
    	
    	if(pos == Positions.two || pos == Positions.five) {
    		overDistance = RobotMap.OVER_DISTANCE_25;
    	} else {
    		overDistance = RobotMap.OVER_DISTANCE_134;
    	}
    	
        addSequential(new DriveSimpleDistanceWithGyroCommand(overDistance, 1.0));
        if(pos == Positions.one) {
        	addSequential(new DriveAnglePIDCommand(50, AngleUnit.DEGREE));
        } else if (pos == Positions.two) {
        	addSequential(new DriveAnglePIDCommand(30, AngleUnit.DEGREE));
        } else if (pos == Positions.five) {
        	addSequential(new DriveAnglePIDCommand(-30, AngleUnit.DEGREE));
        }
        addSequential(new AutonAlignCommand());
    }
}
