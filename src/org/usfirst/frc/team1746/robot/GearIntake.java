package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class GearIntake {
	ElectricalConstants eConstants = new ElectricalConstants();
	
	Solenoid flapsOut;
	Solenoid flapsIn;
	
	
	private Controls m_controls;
	
	public GearIntake(Controls controls){
		m_controls = controls;
	}
	
	public void init(){
		flapsOut = new Solenoid(eConstants.GEAR_FLAPS_OUT);
		flapsIn = new Solenoid(eConstants.GEAR_FLAPS_IN);
		
		flapsOut.set(true);
		flapsIn.set(false);
	}
	
	
	public void update(){
		if(m_controls.operator_gearFlapsOut() || m_controls.driver_gearFlapsOut()){
			flapsIn.set(false);
			flapsOut.set(true);
			
		} else if(m_controls.operator_gearFlapsIn() || m_controls.driver_gearFlapsIn()){
			flapsOut.set(false);
			flapsIn.set(true);
		}
	}
}
