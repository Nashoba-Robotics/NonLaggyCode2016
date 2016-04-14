package edu.nr.robotics;

public class DrivingModeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DrivingMode mode;
	
	public DrivingModeException (DrivingMode mode) {
		this.mode = mode;
    }

    public DrivingModeException (String message, DrivingMode mode) {
        super (message);
        
		this.mode = mode;
    }

    public DrivingModeException (Throwable cause, DrivingMode mode) {
        super (cause);
        
		this.mode = mode;
    }

    public DrivingModeException (String message, Throwable cause, DrivingMode mode) {
        super (message, cause);
        
		this.mode = mode;
    }

	/**
	 * @return the mode
	 */
	public DrivingMode getMode() {
		return mode;
	}
}
