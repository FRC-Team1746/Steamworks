package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.auton.AutonBase;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
	Vision vision;
	Controls controls;
	Drivetrain drive;
	Climber climber;
	GearIntake gear;
	Intake intake;
	Loader loader;
	Conveyor conveyor;
	Shooter shooter;
	
	AutonBase auton;
	
	@Override
	public void robotInit() {
		controls = new Controls();
		vision = new Vision();
		drive = new Drivetrain(controls);
		climber = new Climber(controls); 
		gear = new GearIntake(controls);
		intake = new Intake(controls);
		loader = new Loader(controls);
		shooter = new Shooter(controls);
		conveyor = new Conveyor(controls);
		auton = new AutonBase(drive, gear);
		shooter.init();
		controls.init();
		vision.init();
		drive.init();
		auton.init();
		controls.init();
		climber.init();
		gear.init();
		loader.init();
		intake.init();
		conveyor.init();
		
		initSmartDashboard();
	}

	@Override
	public void autonomousInit() {
		//auton.init()'
	}
	public void disabledPeriodic(){
		updateSmartDashboard();
	}

	@Override
	public void autonomousPeriodic() {
		auton.run();
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
		
		gear.smartFlaps();
		
		
		conveyor.checkControls();
		shooter.checkControls();
		
	}
		
	@Override
	public void testPeriodic() {
		
	} 
	
	public void initSmartDashboard(){
		auton.initSmartDashboard();
		drive.initSmartDashboard();
		shooter.initSmartDashboard();
	}
	
	public void updateSmartDashboard(){
		drive.updateSmartDashboard();
		auton.updateSmartDashboard();
		gear.updateSmartDashboard();
		shooter.updateSmartDashboard();
	}
}


	


