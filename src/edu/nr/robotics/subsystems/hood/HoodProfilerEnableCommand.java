package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.Robot;

/**
 *
 */
public class HoodProfilerEnableCommand extends NRCommand {

    public HoodProfilerEnableCommand() {
    	requires(Hood.getInstance());
    }
    
    @Override
    protected void onStart() {
    	Robot.getInstance().profiler.enable();
    }
    
    @Override
    protected void onEnd() {
    	Robot.getInstance().profiler.disable();
    }
    
    @Override
    protected boolean isFinishedNR() {
    	return false;
    }

}
