package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Talon;

public class Climber {
	Talon climber;
	
	private Controls m_controls;
	ElectricalConstants eConstants = new ElectricalConstants();
	
	public Climber(Controls controls) {
		m_controls = controls;
	}

	public void init(){
		climber = new Talon(eConstants.MOTOR_CLIMBER);
	}
	
	
	public void update(){
		if(m_controls.operator_climberUp()){
			climber.set(1);
		} else if(m_controls.operator_climberDown()){
			climber.set(-1);
		} else {
			climber.set(0);
		}
	}
	
}
