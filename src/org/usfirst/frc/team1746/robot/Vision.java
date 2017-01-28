package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	I2C pixyCam;
	byte[] toSend = new byte[32];
	byte[] rawData = new byte[32];
	
	int xPosition = -1;
	int yPosition = -1;
	int width = -1;
	int height = -1;
	
    public void init() {
    	pixyCam = new I2C(I2C.Port.kOnboard, 0x54);
    }
    
    public void printPixyStuff(){
    	byte[] pixyValues = new byte[64];
    	pixyValues[0] = (byte) 0b01010101;
    	pixyValues[1] = (byte) 0b10101010;
    	

    	pixyCam.readOnly(pixyValues, 64);
    	if (pixyValues != null) {
    		int i = 0;
    		while (!(pixyValues[i] == 85 && pixyValues[i + 1] == -86) && i < 50) {
    			i++;
    		}
    		i++;
    		if (i > 50)
    			i = 49;
    		while (!(pixyValues[i] == 85 && pixyValues[i + 1] == -86) && i < 50) {
    			i++;
    		}
    		xPosition = (char) (((pixyValues[i + 7] & 0xff) << 8) | (pixyValues[i + 6] & 0xff));
    		yPosition = (char) ((pixyValues[i + 9] & 0xff << 8) | pixyValues[i + 8] & 0xff);
    		width = (char) ((pixyValues[i + 11] & 0xff << 8) | pixyValues[i + 10] & 0xff);
    		height = (char) ((pixyValues[i + 13] & 0xff << 8) | pixyValues[i + 12] & 0xff);
    		SmartDashboard.putNumber("xPosition", xPosition);
    		SmartDashboard.putNumber("yPosition", yPosition);
    		SmartDashboard.putNumber("width", width);
    		SmartDashboard.putNumber("height", height);
    	}
    }
}
