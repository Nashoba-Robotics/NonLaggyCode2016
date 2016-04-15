package edu.nr.robotics.commandgroups;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AlignEndCommandGroup extends NRCommand {
    
    public AlignEndCommandGroup() {
    	requires(Drive.getInstance());
    	requires(Hood.getInstance());
    }
	
}
