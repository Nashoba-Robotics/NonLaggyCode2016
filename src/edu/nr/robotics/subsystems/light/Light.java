package edu.nr.robotics.subsystems.light;

import edu.nr.robotics.EnabledSubsystems;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Light extends Subsystem {
    
	private DigitalOutput light;
	
	private static Light singleton;
	
	public Light() {
		light = new DigitalOutput(1);
		light.set(true);
	}
	
	
    public void initDefaultCommand() {
    	setDefaultCommand(new LightOffCommand());
    }
    
    public void setLightOn() {
    	if(light != null)
    		light.set(false);
    }
    
    public void setLightOff() {
    	if(light != null)
    		light.set(true);
    }
    
    public static void init() {
    	if(singleton == null) {
    		singleton = new Light();
    	}
    }


	public static Light getInstance() {
		init();
		return singleton;
	}
}

