package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;
import org.usfirst.frc.team1746.robot.Loader;
import org.usfirst.frc.team1746.robot.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonBase {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	SendableChooser<String> allianceSelector = new SendableChooser<>();
	SendableChooser<String> shootSelector = new SendableChooser<>();
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	private Loader m_loader;
	private Shooter m_shooter;
	
	public AutonBase(Drivetrain drive, GearIntake gear, Loader loader, Shooter shooter){
		m_drive = drive;
		m_gear = gear;
		m_loader = loader;
		m_shooter = shooter;
	}
	
	AutonConstants aConstants = new AutonConstants();
	GearLeft gear_l;
	GearCenter gear_c;
	GearRight gear_r;
	
	int loops = 0;
	
	public void init(){
		gear_l = new GearLeft(m_drive, m_gear);
		gear_c = new GearCenter(m_drive, m_gear, m_loader, m_shooter);
		gear_r = new GearRight(m_drive, m_gear);
		
		gear_l.init();
		gear_c.init();
		gear_r.init();
	}
	public void initSmartDashboard() {
		initAutonSelector();
		initAllianceSelector();
		initShootSelector();
		SmartDashboard.putBoolean("Reset Auton", false);
	}
	public void updateSmartDashboard(){
		SmartDashboard.putString("Selected Alliance", selectedAlliance());
		SmartDashboard.putString("Selected Auton", selectedAuton());
		SmartDashboard.putString("Current State", currentState());
		if(SmartDashboard.getBoolean("Reset Auton", false)){
			resetAll();
			SmartDashboard.putBoolean("Reset Auton", false);
		}
	}
	
	public void initAllianceSelector(){
		allianceSelector.addDefault("Red", "red");
		allianceSelector.addObject("Blue", "blue");
		SmartDashboard.putData("Alliance Selector", allianceSelector);
	}
	
	public void initAutonSelector(){
		autonSelector.addDefault("Left Gear", "gear_l");
		autonSelector.addObject("Center Gear", "gear_c");
		autonSelector.addObject("Right Gear", "gear_r");
		SmartDashboard.putData("Auton Selector", autonSelector);
	}
	public void initShootSelector(){
		shootSelector.addDefault("Yes", "true");
		shootSelector.addObject("No", "false");
		SmartDashboard.putData("Shooter", shootSelector);
	}
	public void resetAll(){
		gear_r.resetState();
		gear_c.resetState();
		gear_l.resetState();
		

		m_drive.resetEncoders();
		m_drive.resetGyro();
	}
	
	public String selectedAuton(){
		return autonSelector.getSelected();
	}
	public String selectedAlliance(){
		return allianceSelector.getSelected();
	}
	public String selectedShoot(){
		return shootSelector.getSelected();
	}
	public String currentState(){
		return "";
	}
	
	public void run(){
		m_gear.smartFlaps();
		if(selectedAuton().equalsIgnoreCase("gear_r")){
			SmartDashboard.putString("Auton State", gear_r.currentState());
			gear_r.auton(selectedAlliance());
		}
		if(selectedAuton().equalsIgnoreCase("gear_c")){
			SmartDashboard.putString("Auton State", gear_c.currentState());
			gear_c.auton(selectedAlliance(), selectedShoot().equalsIgnoreCase("true"));
		}
		if(selectedAuton().equalsIgnoreCase("gear_l")){
			SmartDashboard.putString("Auton State", gear_l.currentState());
			gear_l.auton(selectedAlliance());
		}
	}
	
	public void resetAuton(){
	}
	
	
	
	
	public void getCurrentState(){
		
	}
	
}
