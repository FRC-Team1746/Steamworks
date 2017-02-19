package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Victor;

public class Loader {
	ElectricalConstants electricalConstants = new ElectricalConstants();

	Victor loader;

	private Controls m_controls;

	public Loader(Controls controls){
		m_controls = controls;
	}

	public void init(){
		loader = new Victor(electricalConstants.MOTOR_LOADER);
	}

	public void checkControls(){
		if(m_controls.operator_loader()){
			loader.set(-1);
		} else if(m_controls.operator_loader()){
			loader.set(1);
		} else {
			loader.set(0);
		}
	}

}