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
	Solenoid hopperFlapsOut;
	Solenoid hopperFlapsIn;
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
		hopperFlapsOut = new Solenoid(eConstants.GEAR_HOPPER_FLAPS_OUT);
		hopperFlapsIn = new Solenoid(eConstants.GEAR_HOPPER_FLAPS_IN);
		beambreak = new DigitalInput(eConstants.DIFFUSE_GEAR);
		flapsIn();
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// GearIntake SmartDashboard
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void initSmartDashboard(){
		
	}
	public void updateSmartDashboard(){
		SmartDashboard.putBoolean("Gear Sensor", gearSensor());
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
	public void hopperFlapsOut(){
		hopperFlapsIn.set(false);
		hopperFlapsOut.set(true);
	}
	public void hopperFlapsIn(){
		hopperFlapsOut.set(false);
		hopperFlapsIn.set(true);
	}
	public void LEDsOn(){
		LEDLeft.set(true);
		LEDRight.set(true);
	}
	public void LEDsOff(){
		LEDLeft.set(false);
		LEDRight.set(false);
	}
	//beambreak.get()
	//!beambreak.get()
	//m_controls.operator_gearFlapsOut() || m_controls.driver_gearFlapsOut()
	//m_controls.operator_gearFlapsIn() || m_controls.driver_gearFlapsIn)
	
	
	//false = in
	//true = out
	
	boolean prevBeam = false;
	boolean prevControlOut = false;
	boolean prevControlIn = false;
	int loops = 0;
	
	public void leds(){
		if(!beambreak.get()){
			LEDsOn();
		}
		if(beambreak.get()){
			LEDsOff();
		}
	}
	public void smartFlaps(){
			// Controls: Flaps In
			if((m_controls.driver_gearFlapsIn() || m_controls.operator_gearFlapsIn()) && !prevControlIn){ //controllers says go in
				flapsIn();
				prevControlIn = true;
			}
				
			if(!(m_controls.driver_gearFlapsIn() || m_controls.operator_gearFlapsIn()) && prevControlIn){ //controllers says go in
				prevControlIn = false;
			}
			
			
			// Controls: Flaps Out
			if((m_controls.driver_gearFlapsOut() || m_controls.operator_gearFlapsOut()) && !prevControlOut){
				flapsOut();
				prevControlOut = true;
			}
			if(!(m_controls.driver_gearFlapsOut() || m_controls.operator_gearFlapsOut()) && prevControlOut){
				prevControlOut = false;
			}
			
			// Sensor
			if(!beambreak.get() && !prevBeam){ // gear detected
				flapsIn();
				LEDsOn();
				prevBeam = true;
			}
			if(beambreak.get() && prevBeam){ // gear no longer detected
				flapsOut();
				LEDsOff();
				prevBeam = false;
			}
	}
	
	// Not Used
	public void checkControls(){
		if(m_controls.operator_gearHopperOut()){
			hopperFlapsOut();
		}
		if(m_controls.operator_gearHopperIn()){
			hopperFlapsIn();
		} 
		
	}
	
}
