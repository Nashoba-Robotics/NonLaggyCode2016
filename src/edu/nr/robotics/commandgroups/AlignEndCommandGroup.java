package edu.nr.robotics.commandgroups;

import edu.nr.robotics.OI;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AlignEndCommandGroup extends CommandGroup {
    
    @Override
    public void start() {
    	OI.getInstance().alignCommand.cancel();
    }
}
