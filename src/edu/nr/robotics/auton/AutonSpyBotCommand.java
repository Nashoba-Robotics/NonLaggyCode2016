package edu.nr.robotics.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonSpyBotCommand extends CommandGroup {
    
    public  AutonSpyBotCommand() {
        addSequential(new AutonAlignCommand());
    }
}
