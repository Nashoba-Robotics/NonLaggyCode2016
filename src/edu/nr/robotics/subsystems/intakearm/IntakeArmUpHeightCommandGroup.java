package edu.nr.robotics.subsystems.intakearm;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeArmUpHeightCommandGroup extends CommandGroup {

    public IntakeArmUpHeightCommandGroup() {
        addSequential(new IntakeArmPositionCommand(RobotMap.INTAKE_TOP_POS));
    }
}
