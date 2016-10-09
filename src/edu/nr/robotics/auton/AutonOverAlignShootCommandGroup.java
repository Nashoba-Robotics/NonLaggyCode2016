package edu.nr.robotics.auton;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
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
        	addSequential(new DriveAnglePIDCommand(new Angle(Unit.DEGREE, 50)));
        } else if (pos == Positions.two) {
        	addSequential(new DriveAnglePIDCommand(new Angle(Unit.DEGREE, 30)));
        } else if (pos == Positions.five) {
        	addSequential(new DriveAnglePIDCommand(new Angle(Unit.DEGREE, -30)));
        }
        addSequential(new AutonAlignCommand());
    }
}
