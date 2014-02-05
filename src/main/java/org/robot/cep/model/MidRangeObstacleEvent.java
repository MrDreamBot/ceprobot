package org.robot.cep.model;

public class MidRangeObstacleEvent {

	public final String name;
	public final Direction direction;
	
	public String getName() {
		return name;
	}

	public Direction getDirection() {
		return direction;
	}

	public MidRangeObstacleEvent(String name, Direction direction)
	{
		this.name = name;
		this.direction = direction;
	}
}
