package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.auton.AutonBase;
import org.usfirst.frc.team1746.robot.subsystems.Turret;
import org.usfirst.frc.team1746.vision.RGB_LEDs;
import org.usfirst.frc.team1746.vision.VisionBase;
import org.usfirst.frc.team1746.vision.VisionBoiler;
import org.usfirst.frc.team1746.vision.VisionTargeting;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	VisionBase vision;
	VisionBoiler vision_boiler;
	VisionTargeting visionT;
	Turret turret;
	Controls controls;
	Drivetrain drive;
	Climber climber;
	GearIntake gear;
	Intake intake;
	Loader loader;
	Conveyor conveyor;
	Shooter shooter;
	RGB_LEDs rgb_leds;
	AutonBase auton;
	
	
	DigitalOutput test;
	@Override
	public void robotInit() {
		controls = new Controls();
		vision = new VisionBase();
		vision_boiler = new VisionBoiler();
		drive = new Drivetrain(controls, vision);
		climber = new Climber(controls); 
		gear = new GearIntake(controls);
		turret = new Turret(vision_boiler, controls);
		intake = new Intake(controls);
		loader = new Loader(controls);
		shooter = new Shooter(controls, visionT, vision_boiler);
		conveyor = new Conveyor(controls);
		auton = new AutonBase(drive, gear, loader, shooter, conveyor, vision, turret);
		rgb_leds = new RGB_LEDs(vision_boiler, gear);
		
		test = new DigitalOutput(11);
		shooter.init();
		turret.init();
		controls.init();
		
		vision.init();
		vision_boiler.init();
		
		rgb_leds.init();
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
		vision.trackObject();
		vision_boiler.trackObject();
		rgb_leds.update();
		updateSmartDashboard();
		
	}

	@Override
	public void autonomousPeriodic() {
		auton.run();
		vision.trackObject();
		vision_boiler.trackObject();
		rgb_leds.update();
		updateSmartDashboard();
		
	}

	boolean first;
	int i;
	@Override
	public void teleopInit(){
		first = true;
		i = 0;
	}
	@Override
	public void teleopPeriodic() {
		if(first){
			gear.flapsOut();
			i++;
			if(i>50)
			first = false;
		}
		updateSmartDashboard();
		
		vision.trackObject();
		vision_boiler.trackObject();
		
		if(drive.selectedDrive().equalsIgnoreCase("arc")){
			drive.teleopArcadeDrive();
		}else if(drive.selectedDrive().equalsIgnoreCase("fps")){
			drive.teleopFPSDrive();
		}else if(drive.selectedDrive().equalsIgnoreCase("tnk")){
			drive.teleopTankDrive();
		}
		
		climber.checkControls();
		
		intake.checkControls();
		
		loader.checkControls();
		
		gear.checkControls();
		gear.smartFlaps();
		gear.checkControls();
		gear.leds();
		
		rgb_leds.update();
		
		conveyor.checkControls();
		shooter.checkControls();
		
		turret.teleop();
	}
		
	@Override
	public void testPeriodic() {
		if(controls.operator_loader()){
			
		}
	} 
	
	public void initSmartDashboard(){
		auton.initSmartDashboard();
		drive.initSmartDashboard();
		shooter.initSmartDashboard();	}
	
	public void updateSmartDashboard(){
		drive.updateSmartDashboard();
		auton.updateSmartDashboard();
		gear.updateSmartDashboard();
		shooter.updateSmartDashboard();
		turret.updateSmartDashboard();
		vision.updateSmartdashboard();
		vision_boiler.updateSmartdashboard();
		loader.updateSmartDashboard();
	}
}


	


