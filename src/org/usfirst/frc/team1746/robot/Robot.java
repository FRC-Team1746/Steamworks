package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	
	Auton auton = new Auton();
	Intake intake = new Intake();
	Vision vision = new Vision();
	Controls controls = new Controls();
	DriveTrain drive = new DriveTrain();

	@Override
	public void robotInit() {
		vision.init();
		controls.init();
	}

	@Override
	public void autonomousInit() {
		auton.init();
	}

	public void disabledPeriodic(){
		
	}

	@Override
	public void autonomousPeriodic() {
		
	}

	@Override
	public void teleopPeriodic() {
		drive.teleopDrive();
		vision.printPixyStuff();
		
		//if(controls.)
		
	}
		
	@Override
	public void testPeriodic() {
		
	} 
}