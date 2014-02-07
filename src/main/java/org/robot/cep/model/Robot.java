package org.robot.cep.model;

public class Robot {
	static public final int MID_RANGE = 30;
	static public final int CLOSE_RANGE = 10;
	
	public enum State {
		NORMAL, AVOID, ESCAPE, DEADLOCK, RETRY
	}

    private String name;

    private State state;
    
    public RobotControl control;

    public RobotControl getControl() {
		return control;
	}

	public void setControl(RobotControl control) {
		this.control = control;
	}

	public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }


}
