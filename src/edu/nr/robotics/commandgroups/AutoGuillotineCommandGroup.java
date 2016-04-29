package edu.nr.robotics.commandgroups;

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
public class AutoGuillotineCommandGroup extends CommandGroup {
    
	public static final double firstdistance = 6;
	public static final double firstspeed = 1;
	public static final double seconddistance = 1;
	public static final double secondspeed = 1;
	
    public  AutoGuillotineCommandGroup() {
    	addSequential(new IntakeArmPositionCommand(RobotMap.INTAKE_BOTTOM_POS, 0.5));
    	addParallel(new DriveConstantCommand(false, true, true, -0.4));
    	addSequential(new WaitCommand(0.5));
        addParallel(new DriveConstantCommand(false, true, true , -0.2));
        addSequential(new IntakeArmMoveUpUntilPositionCommand(RobotMap.INTAKE_TOP_POS + 0.08));
        //addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_TOP_POS, 0.08));
        addSequential(new DriveDistanceCommand(5,0.75));
    }
}
