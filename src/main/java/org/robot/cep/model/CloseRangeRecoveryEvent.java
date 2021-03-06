package org.robot.cep.model;

import org.robot.cep.model.Robot.State;

public class CloseRangeRecoveryEvent {

	public final String name;
	public final State state;
	
	public String getName() {
		return name;
	}

	public State getState() {
		return state;
	}
	
	public CloseRangeRecoveryEvent(String name, State state)
	{
		this.name = name;
		this.state = state;
	}
}
