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
	
	public void set(double VBus){
		loader.set(VBus);
	}
	
	public void test(){
	}
	
	public void stop(){
		loader.stopMotor();
	}

	public void checkControls(){
		if(m_controls.operator_loader()){
			set(.5);
		} else {
			set(0);
		}
	}

}