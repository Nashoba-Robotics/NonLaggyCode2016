package edu.nr.robotics.subsystems.intakearm;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeArmBottomHeightCommandGroup extends CommandGroup {

    public IntakeArmBottomHeightCommandGroup() {
    	addSequential(new IntakeArmPositionCommand(RobotMap.INTAKE_BOTTOM_POS));
    }
}