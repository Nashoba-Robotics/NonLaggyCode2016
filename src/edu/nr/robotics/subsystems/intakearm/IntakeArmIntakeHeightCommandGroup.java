package edu.nr.robotics.subsystems.intakearm;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeArmIntakeHeightCommandGroup extends CommandGroup {

    public IntakeArmIntakeHeightCommandGroup() {
        addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_INTAKE_POS));
    }
}