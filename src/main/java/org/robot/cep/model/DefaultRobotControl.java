package org.robot.cep.model;

public class DefaultRobotControl implements RobotControl {

	private String name;
	
	public DefaultRobotControl(String name)
	{
		this.name = name;
	}
	
	@Override
	public void turnLeft() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Turning left");
	}

	@Override
	public void turnRight() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Turning right");
	}

	@Override
	public void spinLeft() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Spinning left");
	}

	@Override
	public void spinRight() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Spinning right");
	}

	@Override
	public void moveForward() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Moving forward");
	}

	@Override
	public void moveBackward() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Moving backward");
	}

	@Override
	public void performDeadlockManeuver() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Performing deadlock maneuver");
	}

	@Override
	public void turnAround() {
		// TODO Auto-generated method stub
		System.out.println(name + ": Turning around");
	}

}
