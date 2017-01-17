package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Victor;

public class Intake {
	ElectricalConstants electricalConstants = new ElectricalConstants();
	Victor intake;
	
	public void init(){
		intake = new Victor(electricalConstants.MOTOR_INTAKE);
		
	}
	public void stop(){
		intake.set(0);
	}
	
	public void set(int direction){
		intake.set(direction);
	}
}
