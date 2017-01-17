package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auton {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	public void initSmartDashboard() {
		autonSelector.addDefault("Default", "default");
		autonSelector.addObject("Drive Straight", "straight");

		SmartDashboard.putData("Auton Selector", autonSelector);
	}
}
