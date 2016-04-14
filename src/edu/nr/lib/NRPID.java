package edu.nr.lib;

import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class NRPID {

	double p,i,d,f;
	
	PIDSource source;
	PIDOutput output;
	
	double setpoint = 0;
	
	double totalError = 0;
	
	double lastTime, lastError = 0;
	
	boolean enabled = false;
	
	Timer scheduler;
	
	double maxOutput = 1;
	
	public NRPID(double p, double i, double d, double f, PIDSource source, PIDOutput output) {
		this.p = p;
		this.d = d;
		this.i = i;
		this.f = f;
		this.source = source;
		this.output = output;
		lastTime = System.currentTimeMillis();
		
		scheduler = new Timer();
		scheduler.schedule(new TimerTask() {
			@Override
			public void run() {
				calculate();
			}	
		}, 0, 20);		
	}
	
	public void setSetpoint(double setpoint) {
		this.setpoint = setpoint;
	}
	
	public void enable() {
		lastError = getError();
		lastTime = System.currentTimeMillis();
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
		output.pidWrite(0);
	}
	
	public double getError() {
		return setpoint - source.pidGet();
	}
	
	private void calculate() {
		if(enabled) {
			double error = getError();
			double out = 0;
			
			out += p * error;
			out += f * setpoint;
			
			totalError += error * i;
			
			out += i * totalError;
			
			double dt = (System.currentTimeMillis() - lastTime)/1000.0;
			lastTime = System.currentTimeMillis();
			
			double dE = error - lastError;
			lastError = error;
			
			out += d * dE/dt;
			if(Math.abs(out) > maxOutput) 
				out = Math.signum(out) * maxOutput;
			output.pidWrite(out);		
		}
	}

	public void setMaxOutput(double maxOutput) {
		this.maxOutput = maxOutput;
	}

	public boolean isEnable() {
		return enabled;
	}

	public void reset() {
		if(enabled)
			disable();
		
		totalError = 0;
	}

	public double getSetpoint() {
		return setpoint;
	}

	public void setPID(double p, double i, double d, double f) {
		this.p = p;
		this.d = d;
		this.i = i;
		this.f = f;		
	}
}
