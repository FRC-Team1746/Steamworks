package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	Vision vision;
	Controls controls;
	Drivetrain drive;
	Climber climber;
	GearIntake gear;
	Intake intake;
	Loader loader;
	Auton auton;

	@Override
	public void robotInit() {
		controls = new Controls();
		vision = new Vision();
		drive = new Drivetrain(controls);
		climber = new Climber(controls); 
		gear = new GearIntake(controls);
		intake = new Intake(controls);
		loader = new Loader(controls);
		auton = new Auton(drive);
		
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
		updateSmartDashboard();
	}

	@Override
	public void autonomousPeriodic() {
		updateSmartDashboard();
	}

	@Override
	public void teleopPeriodic() {
		updateSmartDashboard();
		
		vision.printPixyStuff();
		
		drive.teleopArcadeDrive();
		
		climber.checkControls();
		
		intake.checkControls();
		
		loader.checkControls();
		
		gear.checkControls();
		
	}
		
	@Override
	public void testPeriodic() {
		
	} 
	
	public void initSmartDashboard(){
		auton.initSmartDashboard();
		drive.initSmartDashboard();
	}
	
	public void updateSmartDashboard(){
		drive.updateSmartDashboard();
	}
}


	


