package org.usfirst.frc.team1746.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

public class GearIntake {

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Class setup
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	ElectricalConstants eConstants = new ElectricalConstants();
	
	private Controls m_controls;
	public GearIntake(Controls controls){
		m_controls = controls;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// GearIntake init
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	Solenoid flapsOut;
	Solenoid flapsIn;
	DigitalInput beambreak;
	
	boolean lastBB;
	boolean nowBB;
	
	public void init(){
		flapsOut = new Solenoid(eConstants.GEAR_FLAPS_OUT);
		flapsIn = new Solenoid(eConstants.GEAR_FLAPS_IN);
		beambreak = new DigitalInput(eConstants.DIFFUSE_GEAR);
		
		lastBB = !beambreak.get();
		nowBB = beambreak.get();
		
		
		flapsIn();
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// GearIntake functions
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean gearSensor(){
		return beambreak.get();
	}
	
	
	public void flapsOut(){
		flapsIn.set(false);
		flapsOut.set(true);
	}
	public void flapsIn(){
		flapsOut.set(false);
		flapsIn.set(true);
	}
	
	//beambreak.get()
	//!beambreak.get()
	//m_controls.operator_gearFlapsOut() || m_controls.driver_gearFlapsOut()
	//m_controls.operator_gearFlapsIn() || m_controls.driver_gearFlapsIn)
	
	public void smartFlaps(){
		if(lastBB == nowBB){
			checkControls();
		} else {
			if(beambreak.get()){
				flapsIn();
			}
			if(!beambreak.get()){
				flapsOut();
			}
		}
		lastBB = beambreak.get();
	}

	public void checkControls(){
		if(m_controls.operator_gearFlapsOut() || m_controls.driver_gearFlapsOut()){
			flapsOut();
		} else if(m_controls.operator_gearFlapsIn() || m_controls.driver_gearFlapsIn()){
			flapsIn();
		}
	}
}
