package edu.nr.robotics.subsystems.shooter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;

public class MotorSetter implements PIDOutput {

	private CANTalon talon1, talon2;
	
	public MotorSetter(CANTalon talon1, CANTalon talon2) {
		this.talon1 = talon1;
		this.talon2 = talon2;
	}
	
	@Override
	public void pidWrite(double output) {
		write(output);
	}
	
	public void write(double output) {
		talon1.set(output);
		talon2.set(output);
	}
	
	public void setTalonRampRate(double rampRate) {
		talon1.setVoltageRampRate(rampRate);
		talon2.setVoltageRampRate(rampRate);
	}
	
	public double getOutputCurrent() {
		return (talon1.getOutputCurrent() + talon2.getOutputCurrent())/2;
	}
}
