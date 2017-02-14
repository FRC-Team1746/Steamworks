package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Victor;

public class Intake {
	ElectricalConstants electricalConstants = new ElectricalConstants();
	
	Victor intake;
	
	private Controls m_controls;
	
	public Intake(Controls controls){
		m_controls = controls;
	}
	
	public void init(){
		intake = new Victor(electricalConstants.MOTOR_INTAKE);
		
	}
	public void checkControls(){
		
	}
}
