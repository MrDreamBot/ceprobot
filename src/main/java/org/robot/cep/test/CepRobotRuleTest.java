package org.robot.cep.test;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.drools.core.time.SessionPseudoClock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.runtime.rule.FactHandle;
import org.robot.cep.model.CloseRangeObstacleEvent;
import org.robot.cep.model.CloseRangeRecoveryEvent;
import org.robot.cep.model.DefaultRobotControl;
import org.robot.cep.model.Direction;
import org.robot.cep.model.MidRangeObstacleEvent;
import org.robot.cep.model.RawObstacleEvent;
import org.robot.cep.model.Robot;
import org.robot.cep.model.Robot.State;

// Make sure the ksession clockType is set to "pseudo" in the kmodule.xml
// for this tests to work
public class CepRobotRuleTest {

	static final String ROBOT_NAME = "3Pi";
	static final String STREAM_NAME = "TelemetryStream";

	
	static KieContainer kcontainer;
	KieSession ksession;
	SessionPseudoClock clock;
	Robot robot;
	FactHandle robotHandle;
	EntryPoint entry;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	    kcontainer = KieServices.Factory.get().getKieClasspathContainer();
	}


	@Before
	public void setUp() throws Exception {
        // load up the knowledge base
    	ksession = kcontainer.newKieSession("CepRobotKS");
    	clock = ksession.getSessionClock();
    	ksession.addEventListener( new DefaultAgendaEventListener() {
 		   public void afterMatchFired(AfterMatchFiredEvent event) {
 		       super.afterMatchFired( event );
 		       System.out.println( "->\"" + event.getMatch().getRule().getName() + "\" Rule fired: " );
 		   }
    	});
        //logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        
        // go !
        robot = new Robot();
        robot.setName(ROBOT_NAME);
        robot.setState(State.NORMAL);
        robot.setControl(new DefaultRobotControl(ROBOT_NAME));
        robotHandle = (FactHandle) ksession.insert(robot);
	}

	@After
	public void tearDown() throws Exception {
	    //if (logger != null) logger.close();
	    ksession.dispose();   
	}

	@Test
	public void eventConversionTest() {
	    printSeparator();
	    
	    entry = ksession.getEntryPoint(STREAM_NAME);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // delete unwanted RawObstacleEvent
	    //insertRawObstacleEvent(ROBOT_NAME, Direction.LEFT, MID_RANGE * 2);
	    
	    // test avoid - turn right
	    insertRawObstacleEvent(ROBOT_NAME, Direction.LEFT, Robot.MID_RANGE);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertRawObstacleEvent(ROBOT_NAME, Direction.MIDDLE, Robot.MID_RANGE);
	    clock.advanceTime(200, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.AVOID);
	    
	    // back to normal
	    clock.advanceTime(7, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - spin right
	    insertRawObstacleEvent(ROBOT_NAME, Direction.MIDDLE, Robot.CLOSE_RANGE);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertRawObstacleEvent(ROBOT_NAME, Direction.LEFT, Robot.CLOSE_RANGE);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.ESCAPE);
	    
	    // back to normal
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	   
	    // test deletion of raw events
	    insertRawObstacleEvent(ROBOT_NAME, Direction.LEFT, Robot.MID_RANGE * 2);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertRawObstacleEvent(ROBOT_NAME, Direction.MIDDLE, Robot.MID_RANGE * 2);
	    clock.advanceTime(200, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	}

	@Test
	public void avoidAndTurnLeftTest() {
	    printSeparator();
	    
	    entry = ksession.getEntryPoint(STREAM_NAME);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
    
	    // test avoid - turn left
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.AVOID);
	    
	    // back to normal
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
    
	    // test avoid - turn left
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.AVOID);
	    
	    // back to normal
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - turn left
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
//	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.AVOID);
	    
	    // back to normal
	    clock.advanceTime(20, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - no turn
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	}	
	
	@Test
	public void avoidAndTurnRightTest() {
	    printSeparator();
	    
	    entry = ksession.getEntryPoint(STREAM_NAME);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - turn right
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.AVOID);
	    
	    // back to normal
	    clock.advanceTime(9, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - turn right
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.AVOID);
	    
	    // back to normal
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - no turn
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	}
	
	@Test
	public void avoidAndTurnAroundTest() {
	    printSeparator();
	    
	    entry = ksession.getEntryPoint(STREAM_NAME);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - turn around
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
//	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
//	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertMidRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(150, TimeUnit.MILLISECONDS);

	    fireAllAndAssertRobotStateEquals(State.AVOID);
	    
	    // back to normal
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	}
	
	@Test
	public void escapeAndTurnLeftTest() {
		
		printSeparator();
		 
	    entry = ksession.getEntryPoint(STREAM_NAME);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
    
	    // test avoid - turn left
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.ESCAPE);
	    
	    // back to normal
	    clock.advanceTime(20, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);

	    // test avoid - turn left
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.ESCAPE);
	    
	    // back to normal
	    clock.advanceTime(20, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - turn left
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
//	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.ESCAPE);
	    
	    // back to normal
	    clock.advanceTime(20, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - no turn
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);

	}
	
	@Test
	public void escapeAndTurnRightTest() {
		printSeparator();
		
	    entry = ksession.getEntryPoint(STREAM_NAME);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - turn right
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.ESCAPE);
	    
	    // back to normal
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - turn right
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.MIDDLE);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.ESCAPE);
	    
	    // back to normal
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    
	    // test avoid - no turn
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.RIGHT);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeObstacleEvent(ROBOT_NAME, Direction.LEFT);
	    clock.advanceTime(100, TimeUnit.MILLISECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	}
	
	@Test
	public void deadlockTest() {
		
		printSeparator();
		
	    entry = ksession.getEntryPoint(STREAM_NAME);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	    robot.setState(State.ESCAPE);
	    ksession.update(robotHandle, robot);
	    fireAllAndAssertRobotStateEquals(State.ESCAPE);
	    
	    // test deadlock recovery
	    insertCloseRangeRecoveryEvent(ROBOT_NAME, State.ESCAPE);
	    clock.advanceTime(10, TimeUnit.MILLISECONDS);
	    insertCloseRangeRecoveryEvent(ROBOT_NAME, State.ESCAPE);
	    insertCloseRangeRecoveryEvent(ROBOT_NAME, State.ESCAPE);
	    insertCloseRangeRecoveryEvent(ROBOT_NAME, State.ESCAPE);
	    insertCloseRangeRecoveryEvent(ROBOT_NAME, State.ESCAPE);
	    insertCloseRangeRecoveryEvent(ROBOT_NAME, State.ESCAPE);
	    clock.advanceTime(4, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.DEADLOCK);
	    
	    clock.advanceTime(10, TimeUnit.SECONDS);
	    fireAllAndAssertRobotStateEquals(State.NORMAL);
	}

	private void insertMidRangeObstacleEvent(String name, Direction direction)
	{
	    entry.insert(new MidRangeObstacleEvent(name, direction));
	    ksession.fireAllRules();
	}

	private void insertCloseRangeObstacleEvent(String name, Direction direction)
	{
	    entry.insert(new CloseRangeObstacleEvent(name, direction));
	    ksession.fireAllRules();
	}
	
	private void insertCloseRangeRecoveryEvent(String name, State state)
	{
	    entry.insert(new CloseRangeRecoveryEvent(name, state));
	    ksession.fireAllRules();
	}
	
	private void insertRawObstacleEvent(String name, Direction direction, int distance)
	{
	    entry.insert(new RawObstacleEvent(name, direction, distance));
	    ksession.fireAllRules();
	}
	
	private void printSeparator()
	{
		System.out.println("***********************************************");
	}
	private void fireAllAndAssertRobotStateEquals(State state)
	{
		ksession.fireAllRules();
		assertTrue(robot.getState() == state);
	}
}
