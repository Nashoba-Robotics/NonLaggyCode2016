package edu.nr.robotics.subsystems.intakearm;

import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeArmPrepareLowGoalCommand extends CommandGroup {

    public IntakeArmPrepareLowGoalCommand() {
        addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_INTAKE_POS));
    }

}
