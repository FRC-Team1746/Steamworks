package org.usfirst.frc.team1746.vision;

import org.usfirst.frc.team1746.robot.ElectricalConstants;
import org.usfirst.frc.team1746.robot.GearIntake;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionTargeting {
	
	ElectricalConstants eConstants = new ElectricalConstants();
	private VisionBase m_vision;
	private GearIntake m_gear;
	
	public VisionTargeting(VisionBase vision, GearIntake gear){
		m_vision = vision;
		m_gear = gear;
	}
	DigitalOutput errorB0;
	DigitalOutput errorB1;
	DigitalOutput errorB2;
	DigitalOutput errorB3;
	DigitalOutput errorB4;
	DigitalOutput sign;
	DigitalOutput gear;
	
	public void init(){
		errorB0 = new DigitalOutput(10);
		errorB1 = new DigitalOutput(eConstants.VISION_LED_ERROR_1);
		errorB2 = new DigitalOutput(eConstants.VISION_LED_ERROR_2);
		errorB3 = new DigitalOutput(eConstants.VISION_LED_ERROR_3);
		errorB4 = new DigitalOutput(eConstants.VISION_LED_ERROR_4);
		sign = new DigitalOutput(eConstants.VISION_LED_SIGN);
		gear = new DigitalOutput(eConstants.VISION_LED_GEAR);
		
	}
	public void updateSmartDashboard(){
		SmartDashboard.putBoolean("V0", (error() & 0x01) == 1 ? true : false);
		SmartDashboard.putBoolean("V1", (error() & 0x02) == 2 ? true : false);
		SmartDashboard.putBoolean("V2", (error() & 0x04) == 4 ? true : false);
		SmartDashboard.putBoolean("V3", (error() & 0x08) == 8 ? true : false);
		SmartDashboard.putBoolean("V4", (error() & 0x10) == 16 ? true : false);
		SmartDashboard.putBoolean("VGear", !m_gear.gearSensor());
		SmartDashboard.putBoolean("VSign", m_vision.getError() < 0);
	}
	
	public void run(){
		updateGear();
		updateSign();
		updateError();
	}
	
	public void updateGear(){
		gear.set(!m_gear.gearSensor());
	}
	public void updateSign(){
		sign.set(m_vision.getError() < 0);
	}
	
	public int error(){
		return m_vision.getError()*20/150;
	}

	
	public void updateError(){
//		errorB0.set((error() & 0x01) == 1 ? true : false);
//		
//		
//		errorB0.set((error() & 0x02) == 2 ? true : false);
//		errorB0.set((error() & 0x04) == 4 ? true : false);
//		errorB0.set((error() & 0x08) == 8 ? true : false);
//		errorB0.set((error() & 0x10) == 16 ? true : false);
		
		errorB0.set(false);
		errorB1.set(true);
		errorB2.set(false);
		errorB3.set(true);
		errorB4.set(false);
		
	}
	
	
	
	
}
