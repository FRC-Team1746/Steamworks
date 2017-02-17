package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	Vision vision;
	Controls controls;
	DriveTrain drive;
	Climber climber;
	GearIntake gear;
	Intake intake;
	Loader loader;

	@Override
	public void robotInit() {
		controls = new Controls();
		vision = new Vision();
		drive = new DriveTrain(controls);
		climber = new Climber(controls); 
		gear = new GearIntake(controls);
		intake = new Intake(controls);
		loader = new Loader(controls);
		
		controls.init();
		vision.init();
		drive.init();
		
		climber.init();
		gear.init();
		loader.init();
		intake.init();
	}

	@Override
	public void autonomousInit() {
		
	}
	public void disabledPeriodic(){
		
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopPeriodic() {
		vision.printPixyStuff();
		drive.teleopDrive();
		climber.checkControls();
		gear.checkControls();
		intake.checkControls();
		loader.checkControls();
	}
		
	@Override
	public void testPeriodic() {
		
	} 

}


	


