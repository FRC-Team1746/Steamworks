package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.Talon;

public class Climber {
	Talon climberA;
	Talon climberB;
	
	private Controls m_controls;
	ElectricalConstants eConstants = new ElectricalConstants();
	
	public Climber(Controls controls) {
		m_controls = controls;
	}

	public void init(){
		climberA = new Talon(eConstants.MOTOR_CLIMBER_A);
		climberB = new Talon(eConstants.MOTOR_CLIMBER_B);
	}
	
	
	public void checkControls(){
		if(m_controls.operator_climberSlow()){
			climberA.set(-.4);
			climberB.set(.4);
		}
		else if(m_controls.operator_climberUp() || m_controls.driver_climberUp()){
			climberA.set(-1);
			climberB.set(1);
		} else if(m_controls.operator_climberDown() || m_controls.driver_climberDown()){
			climberA.set(1);
			climberB.set(-1);
		} else {
			climberA.set(0);
			climberB.set(0);
		}
	}
	
}
