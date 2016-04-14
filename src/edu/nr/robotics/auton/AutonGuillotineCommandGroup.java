package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveDistanceCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveUpUntilPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutonGuillotineCommandGroup extends CommandGroup {
    	
    public  AutonGuillotineCommandGroup() {
    	addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_BOTTOM_POS, 0.5));
    	addParallel(new DriveConstantCommand(false, true, true, 0.4));
    	addSequential(new WaitCommand(4));
    	addParallel(new DriveConstantCommand(false, true, true, 0.4));
    	addSequential(new WaitCommand(0.5));
        addParallel(new DriveConstantCommand(false, true, true , 0.2));
        addSequential(new IntakeArmMoveUpUntilPositionCommand(RobotMap.INTAKE_TOP_POS));
        addSequential(new DriveDistanceCommand(8,0.75));
    }
}
