package org.usfirst.frc.team1746.auton;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class AutonBase {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	SendableChooser<String> allianceSelector = new SendableChooser<>();
	
	int loops = 0;
	
	/*private Drivetrain m_drive;
	private GearIntake m_gear;
	
	public Auton(Drivetrain drive, GearIntake gear) {
		m_drive = drive;
		m_gear = gear;
	}*/
	
	public void init(){

	}
	public void initSmartDashboard() {
		initAutonSelector();
		initAllianceSelector();
		SmartDashboard.putBoolean("Reset Auton", false);
	}
	public void updateSmartDashboard(){
		SmartDashboard.putString("Selected Alliance", selectedAlliance());
		SmartDashboard.putString("Selected Auton", selectedAuton());
	}
	
	public void initAllianceSelector(){
		allianceSelector.addDefault("Red", "red");
		allianceSelector.addObject("Blue", "blue");
		SmartDashboard.putData("Alliance Selector", allianceSelector);
	}
	
	public void initAutonSelector(){
		autonSelector.addDefault("Left Gear", "leftGear");
		autonSelector.addObject("Center Gear", "centerGear");
		autonSelector.addObject("Right Gear", "rightGear");
		SmartDashboard.putData("Auton Selector", autonSelector);
	}
	
	
	
	public String selectedAuton(){
		return autonSelector.getSelected();
	}
	public String selectedAlliance(){
		return allianceSelector.getSelected();
	}
	
	public void resetAuton(){
	}
	
	public void getCurrentState(){
		
	}
	
	public void run(String auton){
		
	}
}
