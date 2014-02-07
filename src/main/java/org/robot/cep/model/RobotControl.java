package org.robot.cep.model;

public interface RobotControl {
	
	public void turnLeft();
	public void turnRight();
	public void turnAround();
	public void spinLeft();
	public void spinRight();
	public void moveForward();
	public void moveBackward();
	public void stop();
	public void performDeadlockManeuver();

}
