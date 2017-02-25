package org.usfirst.frc.team1746.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	Solenoid LEDLeft;
	Solenoid LEDRight;
	DigitalInput beambreak;
	
	boolean lastBB;
	boolean nowBB;
	
	public void init(){
		flapsOut = new Solenoid(eConstants.GEAR_FLAPS_OUT);
		flapsIn = new Solenoid(eConstants.GEAR_FLAPS_IN);
		LEDLeft = new Solenoid(eConstants.GEAR_LED_LEFT);
		LEDRight = new Solenoid(eConstants.GEAR_LED_RIGHT);
		beambreak = new DigitalInput(eConstants.DIFFUSE_GEAR);
		
		flapsIn();
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// GearIntake SmartDashboard
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void initSmartDashboard(){
		
	}
	public void updateSmartDashboard(){
		SmartDashboard.putBoolean("Gear Sensor", beambreak.get());
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
	public void LEDOn(){
		LEDLeft.set(true);
		LEDRight.set(true);
	}
	public void LEDOff(){
		LEDLeft.set(false);
		LEDRight.set(false);
	}
	//beambreak.get()
	//!beambreak.get()
	//m_controls.operator_gearFlapsOut() || m_controls.driver_gearFlapsOut()
	//m_controls.operator_gearFlapsIn() || m_controls.driver_gearFlapsIn)
	
	public void smartFlaps(){
			if(!beambreak.get()){
				flapsIn();
				LEDOn();
			}
			else {
				flapsOut();
				LEDOff();
			}
	}

	public void checkControls(){
		if(m_controls.operator_gearFlapsOut() || m_controls.driver_gearFlapsOut()){
			flapsOut();
		} else if(m_controls.operator_gearFlapsIn() || m_controls.driver_gearFlapsIn()){
			flapsIn();
		}
	}
}
