package org.robot.cep.model;

public class RawObstacleEvent {

	public final String name;
	public final Direction direction;
	public final int distance;
	
	public String getName() {
		return name;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getDistance() {
		return distance;
	}
	
	public RawObstacleEvent(String name, Direction direction, int distance)
	{
		this.name = name;
		this.direction = direction;
		this.distance = distance;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + " (" + name + ":" + direction + ":" + distance +")";
	}
	
	
}
