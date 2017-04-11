package org.usfirst.frc.team1746.vision;

import org.usfirst.frc.team1746.robot.Controls;
import org.usfirst.frc.team1746.robot.ElectricalConstants;
import org.usfirst.frc.team1746.robot.GearIntake;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//
public class RGB_LEDs {
/////////////////////////////////////////////////////////////////////////////
//////
//////          Collect and send error data to RGB LEDs
//////
/////////////////////////////////////////////////////////////////////////////
/////// Class setup
/////////////////////////////////////////////////////////////////////////////
	ElectricalConstants eConstants = new ElectricalConstants();
	
	I2C mxpI2C;
	VisionBase m_vision;
	GearIntake m_gearIntake;
	int m_error;
	int m_error_prev = 31; //(no target)
	int m_I2CdataToSend; // see bit definitions below
	int m_I2CdataToSend_prev = 0xff; // 
	int missing_frame_count=0;
	
	private Controls m_controls;
	public RGB_LEDs(VisionBase vision, GearIntake gearIntake){
		m_vision = vision;
		m_gearIntake = gearIntake;
	}

///////////////////////////////////////////////////////////////////////////////
///////  init
///////////////////////////////////////////////////////////////////////////////

	public void init(){
		missing_frame_count = 0;
    	mxpI2C = new I2C(I2C.Port.kMXP, 0x08);
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// SmartDashboard
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void updateSmartDashboard(){
		SmartDashboard.putNumber("LED_error", m_error);
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// GearIntake functions
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void update()
	{
		// We are sending one byte.  This is how the bits are used:
		// I2C bit usage (I2CdataToSend)
		//-------------------------
		// bit 0 - error b0
		// bit 1 - error b1
		// bit 2 - error b2
		// bit 3 - error b3
		// bit 4 - error b4
		// bit 5 - error sign     (mask = 0x20)
		// bit 6 - gear present   (mask = 0x40)
		// bit 7 - target found   (mask = 0x80)
		// bit 8 - alliance color (mask = 0x100)

		//Scale the error to +/- 20 (the number of LEDs on each side)
		m_error = m_vision.getError()*20/150;
			
		////////// Take abs value and set the sign bit if negative (b5 for us = 0x20)
		if (m_error >= 0)
		{
			m_I2CdataToSend  = m_error;
		}
		else
		{
			m_error = -m_error; // turn error into  magnitude with no sign
			m_I2CdataToSend = m_error | 0x20; // set sign bit (b5 for us = 0x20)
		}
		
		// if target is found, set the target found bit
		if(m_vision.getNumTargets() > 0)
		{			
			m_I2CdataToSend = m_I2CdataToSend | 0x80; // set target found bit
			missing_frame_count = 0; // reset this since we found a frame
		}
		else
		{
			missing_frame_count++; // we have to account for bad frames
			if(missing_frame_count>=5) // but wait for 5 consecutive before displaying it so we don't blink on occasional missing frame.
			{
				// We've missed a lot of frames so Clear Target Found Bit
				m_I2CdataToSend = m_I2CdataToSend & 0x7F; // reset target found bit
				// Don't let frame count grow
				missing_frame_count = 5; // to keep it from incrementing
			}
			else
			{
				m_I2CdataToSend = m_I2CdataToSend_prev; // leave the digital outputs where they were until we are missing >5 frames
			}
		}
		
		// Add Gear present bit to I2C data
		if(m_gearIntake.gearSensor())
		{
			m_I2CdataToSend = m_I2CdataToSend | 0x40; // set gear present bit
		}
		else
		{
			m_I2CdataToSend = m_I2CdataToSend & 0xBF; // reset target found bit
		}
		
		// Add Alliance color bit to I2C data
		boolean blue = DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue;
		SmartDashboard.putBoolean("blue", blue);
		if(blue){
			m_I2CdataToSend = m_I2CdataToSend | 0x100; // set gear present bit
		}
		else {
			m_I2CdataToSend = m_I2CdataToSend & 0xFEFF; // reset target found bit
		}
		
		// Finally update the I2C port if the data has changed
		if(m_I2CdataToSend != m_I2CdataToSend_prev)
		{
			m_I2CdataToSend_prev = m_I2CdataToSend;
			mxpI2C.write(0x0 , m_I2CdataToSend);
		}
	}	
}
