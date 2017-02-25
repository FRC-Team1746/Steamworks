package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Victor;

public class Conveyor {
	Victor conveyor;
	
	private Controls m_controls;
	ElectricalConstants eConstants = new ElectricalConstants();
	
	public Conveyor(Controls controls) {
		m_controls = controls;
	}

	public void init(){
		conveyor = new Victor(eConstants.MOTOR_CONVEYOR);
	}
	
	
	public void checkControls(){
		if(m_controls.operator_conveyor_shooter()){
			conveyor.set(-1);
		} else {
			conveyor.set(0);
		}
	}
}
