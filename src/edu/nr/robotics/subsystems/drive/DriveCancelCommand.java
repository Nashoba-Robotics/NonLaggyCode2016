package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class DriveCancelCommand extends NRCommand {	
	
    public DriveCancelCommand() {
        requires(Drive.getInstance());
    }
    
}
