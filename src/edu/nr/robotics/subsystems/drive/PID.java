package edu.nr.robotics.subsystems.drive;

import java.util.TimerTask;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * Class implements a PID Control Loop.
 *
 * Creates a separate thread which reads the given PIDSource and takes care of
 * the integral calculations, as well as writing the given PIDOutput
 */
public class PID implements LiveWindowSendable {
	
	boolean smartDashboardPrintingP = false;
	boolean smartDashboardPrintingI = false;
	boolean smartDashboardPrintingD = false;
	boolean smartDashboardPrintingF = false;

	public static final double kDefaultPeriod = .02;
	private double m_P; // factor for "proportional" control
	private double m_I; // factor for "integral" control
	private double m_D; // factor for "derivative" control
	private double m_F; // factor for feedforward term
	private double m_maximumOutput = 1.0; // |maximum output|
	private double m_minimumOutput = -1.0; // |minimum output|
	private double m_maximumInput = 0.0; // maximum input - limit setpoint to
											// this
	private double m_minimumInput = 0.0; // minimum input - limit setpoint to
											// this
	private boolean m_continuous = false; // do the endpoints wrap around? eg.
											// Absolute encoder
	private boolean m_enabled = false; // is the pid controller enabled
	private double m_prevError = 0.0; // the prior sensor input (used to compute
										// velocity)
	private double m_totalError = 0.0; // the sum of the errors for use in the
										// integral calc
	private Tolerance m_tolerance; // the tolerance object used to check if on
									// target
	private double m_setpoint = 0.0;
	private double m_error = 0.0;
	private double m_result = 0.0;
	private double m_period = kDefaultPeriod;
	PIDSource m_pidInput;
	PIDOutput m_pidOutput;
	java.util.Timer m_controlLoop;

	private String subsystemName = "";

	/**
	 * Tolerance is the type of tolerance used to specify if the PID controller
	 * is on target. The various implementations of this class such as
	 * PercentageTolerance and AbsoluteTolerance specify types of tolerance
	 * specifications to use.
	 */
	public interface Tolerance {
		public boolean onTarget();
	}

	public double getTotalError() {
		return m_totalError;
	}

	public class PercentageTolerance implements Tolerance {
		double percentage;

		PercentageTolerance(double value) {
			percentage = value;
		}

		@Override
		public boolean onTarget() {
			return Math.abs(getError()) < percentage / 100 * (m_maximumInput - m_minimumInput);
		}
	}

	public class AbsoluteTolerance implements Tolerance {
		double value;

		AbsoluteTolerance(double value) {
			this.value = value;
		}

		@Override
		public boolean onTarget() {
			return Math.abs(getError()) < value;
		}
	}

	public class NullTolerance implements Tolerance {

		@Override
		public boolean onTarget() {
			throw new RuntimeException("No tolerance value set when using PID.onTarget()");
		}
	}

	private class PIDTask extends TimerTask {

		private PID m_controller;

		public PIDTask(PID controller) {
			if (controller == null) {
				throw new NullPointerException("Given PID was null");
			}
			m_controller = controller;
		}

		@Override
		public void run() {
			m_controller.calculate();
		}
	}

	/**
	 * Allocate a PID object with the given constants for P, I, D, and F
	 * 
	 * @param Kp
	 *            the proportional coefficient
	 * @param Ki
	 *            the integral coefficient
	 * @param Kd
	 *            the derivative coefficient
	 * @param Kf
	 *            the feed forward term
	 * @param source
	 *            The PIDSource object that is used to get values
	 * @param output
	 *            The PIDOutput object that is set to the output percentage
	 * @param period
	 *            the loop time for doing calculations. This particularly
	 *            effects calculations of the integral and differential terms.
	 *            The default is 50ms.
	 */
	public PID(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output, double period) {

		if (source == null) {
			throw new NullPointerException("Null PIDSource was given");
		}
		if (output == null) {
			throw new NullPointerException("Null PIDOutput was given");
		}

		m_controlLoop = new java.util.Timer();

		m_P = Kp;
		m_I = Ki;
		m_D = Kd;
		m_F = Kf;

		m_pidInput = source;
		m_pidOutput = output;
		m_period = period;

		m_controlLoop.schedule(new PIDTask(this), 0L, (long) (m_period * 1000));

		m_tolerance = new NullTolerance();
	}

	/**
	 * Allocate a PID object with the given constants for P, I, D and period
	 * 
	 * @param Kp
	 *            the proportional coefficient
	 * @param Ki
	 *            the integral coefficient
	 * @param Kd
	 *            the derivative coefficient
	 * @param source
	 *            the PIDSource object that is used to get values
	 * @param output
	 *            the PIDOutput object that is set to the output percentage
	 * @param period
	 *            the loop time for doing calculations. This particularly
	 *            effects calculations of the integral and differential terms.
	 *            The default is 50ms.
	 */
	public PID(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output, double period) {
		this(Kp, Ki, Kd, 0.0, source, output, period);
	}

	/**
	 * Allocate a PID object with the given constants for P, I, D, using a 50ms
	 * period.
	 * 
	 * @param Kp
	 *            the proportional coefficient
	 * @param Ki
	 *            the integral coefficient
	 * @param Kd
	 *            the derivative coefficient
	 * @param source
	 *            The PIDSource object that is used to get values
	 * @param output
	 *            The PIDOutput object that is set to the output percentage
	 */
	public PID(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
		this(Kp, Ki, Kd, source, output, kDefaultPeriod);
	}

	/**
	 * Allocate a PID object with the given constants for P, I, D, using a 50ms
	 * period.
	 * 
	 * @param Kp
	 *            the proportional coefficient
	 * @param Ki
	 *            the integral coefficient
	 * @param Kd
	 *            the derivative coefficient
	 * @param Kf
	 *            the feed forward term
	 * @param source
	 *            The PIDSource object that is used to get values
	 * @param output
	 *            The PIDOutput object that is set to the output percentage
	 */
	public PID(double Kp, double Ki, double Kd, double Kf, PIDSource source, PIDOutput output) {
		this(Kp, Ki, Kd, Kf, source, output, kDefaultPeriod);
	}

	/**
	 * Free the PID object
	 */
	public void free() {
		m_controlLoop.cancel();
		synchronized (this) {
			m_pidOutput = null;
			m_pidInput = null;
			m_controlLoop = null;
		}
		if (table != null) {
			table.removeTableListener(listener);
		}
	}

	public void resetTotalError() {
		m_totalError = 0;
	}

	/**
	 * Read the input, calculate the output accordingly, and write to the
	 * output. This should only be called by the PIDTask and is created during
	 * initialization.
	 */
	private void calculate() {
		boolean enabled;
		PIDSource pidInput;

		synchronized (this) {
			if (m_pidInput == null) {
				return;
			}
			if (m_pidOutput == null) {
				return;
			}
			enabled = m_enabled; // take snapshot of these values...
			pidInput = m_pidInput;
		}

		if (enabled) {
			double input;
			double result;
			PIDOutput pidOutput = null;
			synchronized (this) {
				input = pidInput.pidGet();
			}
			synchronized (this) {
				m_error = m_setpoint - input;
				if (m_continuous) {
					if (Math.abs(m_error) > (m_maximumInput - m_minimumInput) / 2) {
						if (m_error > 0) {
							m_error = m_error - m_maximumInput + m_minimumInput;
						} else {
							m_error = m_error + m_maximumInput - m_minimumInput;
						}
					}
				}

				//So we can use the same I value from pids calibrated at different refresh rates
				double tempm_I = m_I * kDefaultPeriod/ 0.005 ;

				if (tempm_I != 0) {

					double potentialIGain = (m_totalError + m_error) * tempm_I;
					if (potentialIGain < m_maximumOutput) {
						if (potentialIGain > m_minimumOutput) {
							m_totalError += m_error;
						} else {
							m_totalError = m_minimumOutput / tempm_I;
						}
					} else {
						m_totalError = m_maximumOutput / tempm_I;
					}
				}

				if(smartDashboardPrintingP) {
					SmartDashboard.putNumber(subsystemName + " PID P", m_P * m_error);
				} if(smartDashboardPrintingI) {
					SmartDashboard.putNumber(subsystemName + " PID I", tempm_I * m_totalError);
				} if(smartDashboardPrintingD) {
					SmartDashboard.putNumber(subsystemName + " PID D", m_D * (m_error - m_prevError));
				} if(smartDashboardPrintingF) {
					SmartDashboard.putNumber(subsystemName + " PID F", m_setpoint * m_F);
				}
				
				m_result = m_P * m_error + tempm_I * m_totalError + m_D * (m_error - m_prevError) + m_setpoint * m_F;
				m_prevError = m_error;
				
				double resultSign = Math.signum(m_result);
				m_result = Math.abs(m_result);

				if (m_result > m_maximumOutput) {
					m_result = m_maximumOutput;
				} else if (m_result < m_minimumOutput) {
					m_result = m_minimumOutput;
				}
				m_result = m_result * resultSign;
				pidOutput = m_pidOutput;
				result = m_result;
			}

			pidOutput.pidWrite(result);
		}
	}

	/**
	 * Set the PID Controller gain parameters. Set the proportional, integral,
	 * and differential coefficients.
	 * 
	 * @param p
	 *            Proportional coefficient
	 * @param i
	 *            Integral coefficient
	 * @param d
	 *            Differential coefficient
	 */
	public synchronized void setPID(double p, double i, double d) {
		m_P = p;
		m_I = i;
		m_D = d;

		if (table != null) {
			table.putNumber("p", p);
			table.putNumber("i", i);
			table.putNumber("d", d);
		}
	}

	/**
	 * Set the PID Controller gain parameters. Set the proportional, integral,
	 * and differential coefficients.
	 * 
	 * @param p
	 *            Proportional coefficient
	 * @param i
	 *            Integral coefficient
	 * @param d
	 *            Differential coefficient
	 * @param f
	 *            Feed forward coefficient
	 */
	public synchronized void setPID(double p, double i, double d, double f) {
		m_P = p;
		m_I = i;
		m_D = d;
		m_F = f;

		if (table != null) {
			table.putNumber("p", p);
			table.putNumber("i", i);
			table.putNumber("d", d);
			table.putNumber("f", f);
		}
	}

	/**
	 * Get the Proportional coefficient
	 * 
	 * @return proportional coefficient
	 */
	public synchronized double getP() {
		return m_P;
	}

	/**
	 * Get the Integral coefficient
	 * 
	 * @return integral coefficient
	 */
	public synchronized double getI() {
		return m_I;
	}

	/**
	 * Get the Differential coefficient
	 * 
	 * @return differential coefficient
	 */
	public synchronized double getD() {
		return m_D;
	}

	/**
	 * Get the Feed forward coefficient
	 * 
	 * @return feed forward coefficient
	 */
	public synchronized double getF() {
		return m_F;
	}

	/**
	 * Return the current PID result This is always centered on zero and
	 * constrained the the max and min outs
	 * 
	 * @return the latest calculated output
	 */
	public synchronized double get() {
		return m_result;
	}

	/**
	 * Set the PID controller to consider the input to be continuous, Rather
	 * then using the max and min in as constraints, it considers them to be the
	 * same point and automatically calculates the shortest route to the
	 * setpoint.
	 * 
	 * @param continuous
	 *            Set to true turns on continuous, false turns off continuous
	 */
	public synchronized void setContinuous(boolean continuous) {
		m_continuous = continuous;
	}

	/**
	 * Set the PID controller to consider the input to be continuous, Rather
	 * then using the max and min in as constraints, it considers them to be the
	 * same point and automatically calculates the shortest route to the
	 * setpoint.
	 */
	public synchronized void setContinuous() {
		this.setContinuous(true);
	}

	/**
	 * Sets the maximum and minimum values expected from the input and setpoint.
	 *
	 * @param minimumInput
	 *            the minimum value expected from the input
	 * @param maximumInput
	 *            the maximum value expected from the input
	 */
	public synchronized void setInputRange(double minimumInput, double maximumInput) {
		if (minimumInput > maximumInput) {
			throw new BoundaryException("Lower bound is greater than upper bound");
		}
		m_minimumInput = minimumInput;
		m_maximumInput = maximumInput;
		setSetpoint(m_setpoint);
	}

	/**
	 * Sets the minimum and maximum values to write.
	 *
	 * @param minimumOutput
	 *            the minimum percentage to write to the output
	 * @param maximumOutput
	 *            the maximum percentage to write to the output
	 */
	public synchronized void setOutputRange(double minimumOutput, double maximumOutput) {
		if (minimumOutput > maximumOutput) {
			throw new BoundaryException("Lower bound is greater than upper bound");
		}
		m_minimumOutput = minimumOutput;
		m_maximumOutput = maximumOutput;
	}

	/**
	 * Set the setpoint for the PID
	 * 
	 * @param setpoint
	 *            the desired setpoint
	 */
	public synchronized void setSetpoint(double setpoint) {
		if (m_maximumInput > m_minimumInput) {
			if (setpoint > m_maximumInput) {
				m_setpoint = m_maximumInput;
			} else if (setpoint < m_minimumInput) {
				m_setpoint = m_minimumInput;
			} else {
				m_setpoint = setpoint;
			}
		} else {
			m_setpoint = setpoint;
		}

		if (table != null) {
			table.putNumber("setpoint", m_setpoint);
		}
	}

	/**
	 * Returns the current setpoint of the PID
	 * 
	 * @return the current setpoint
	 */
	public synchronized double getSetpoint() {
		return m_setpoint;
	}

	/**
	 * Returns the current difference of the input from the setpoint
	 * 
	 * @return the current error
	 */
	public synchronized double getError() {
		// return m_error;
		return getSetpoint() - m_pidInput.pidGet();
	}

	/**
	 * Set the percentage error which is considered tolerable for use with
	 * OnTarget. (Input of 15.0 = 15 percent)
	 * 
	 * @param percent
	 *            error which is tolerable
	 * @deprecated Use {@link #setPercentTolerance(double)} or
	 *             {@link #setAbsoluteTolerance(double)} instead.
	 */
	@Deprecated
	public synchronized void setTolerance(double percent) {
		m_tolerance = new PercentageTolerance(percent);
	}

	/**
	 * Set the absolute error which is considered tolerable for use with
	 * OnTarget.
	 * 
	 * @param absvalue
	 *            absolute error which is tolerable in the units of the input
	 *            object
	 */
	public synchronized void setAbsoluteTolerance(double absvalue) {
		m_tolerance = new AbsoluteTolerance(absvalue);
	}

	/**
	 * Set the percentage error which is considered tolerable for use with
	 * OnTarget. (Input of 15.0 = 15 percent)
	 * 
	 * @param percentage
	 *            percent error which is tolerable
	 */
	public synchronized void setPercentTolerance(double percentage) {
		m_tolerance = new PercentageTolerance(percentage);
	}

	/**
	 * Return true if the error is within the percentage of the total input
	 * range, determined by setTolerance. This assumes that the maximum and
	 * minimum input were set using setInput.
	 * 
	 * @return true if the error is less than the tolerance
	 */
	public synchronized boolean onTarget() {
		return m_tolerance.onTarget();
	}

	/**
	 * Begin running the PID
	 */
	public synchronized void enable() {
		m_enabled = true;

		if (table != null) {
			table.putBoolean("enabled", true);
		}
	}

	/**
	 * Stop running the PID, this sets the output to zero before stopping.
	 */
	public synchronized void disable() {
		m_pidOutput.pidWrite(0);
		m_enabled = false;

		if (table != null) {
			table.putBoolean("enabled", false);
		}
	}

	/**
	 * Return true if PID is enabled.
	 */
	public synchronized boolean isEnable() {
		return m_enabled;
	}

	/**
	 * Reset the previous error,, the integral term, and disable the controller.
	 */
	public synchronized void reset() {
		disable();
		m_prevError = 0;
		m_totalError = 0;
		m_result = 0;
	}
	
	public void enableSmartDashboardPrinting(boolean willEnableP,boolean willEnableI,boolean willEnableD,boolean willEnableF, String subsystemName) {
		this.smartDashboardPrintingP = willEnableP;
		this.smartDashboardPrintingI = willEnableI;
		this.smartDashboardPrintingD = willEnableD;
		this.smartDashboardPrintingF = willEnableF;
		this.subsystemName  = subsystemName;
		return;
	}

	@Override
	public String getSmartDashboardType() {
		return "PIDController";
	}

	private final ITableListener listener = new ITableListener() {
		@Override
		public void valueChanged(ITable table, String key, Object value, boolean isNew) {
			if (key.equals("p") || key.equals("i") || key.equals("d") || key.equals("f")) {
				if (getP() != table.getNumber("p", 0.0) || getI() != table.getNumber("i", 0.0)
						|| getD() != table.getNumber("d", 0.0) || getF() != table.getNumber("f", 0.0)) {
					setPID(table.getNumber("p", 0.0), table.getNumber("i", 0.0), table.getNumber("d", 0.0),
							table.getNumber("f", 0.0));
				}
			} else if (key.equals("setpoint")) {
				if (getSetpoint() != ((Double) value).doubleValue()) {
					setSetpoint(((Double) value).doubleValue());
				}
			} else if (key.equals("enabled")) {
				if (isEnable() != ((Boolean) value).booleanValue()) {
					if (((Boolean) value).booleanValue()) {
						enable();
					} else {
						disable();
					}
				}
			}
		}
	};
	private ITable table;

	@Override
	public void initTable(ITable table) {
		if (this.table != null) {
			this.table.removeTableListener(listener);
		}
		this.table = table;
		if (table != null) {
			table.putNumber("p", getP());
			table.putNumber("i", getI());
			table.putNumber("d", getD());
			table.putNumber("f", getF());
			table.putNumber("Current Output", get());
			table.putNumber("setpoint", getSetpoint());
			table.putBoolean("enabled", isEnable());
			table.addTableListener(listener, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITable getTable() {
		return table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTable() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startLiveWindowMode() {
		disable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopLiveWindowMode() {
	}
}
