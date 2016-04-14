package edu.nr.robotics.subsystems.intakearm;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeArmHomeHeightCommandGroup extends CommandGroup {

    public IntakeArmHomeHeightCommandGroup() {
        addSequential(new IntakeArmPositionCommand(RobotMap.INTAKE_HOME_POS));
    }
}
