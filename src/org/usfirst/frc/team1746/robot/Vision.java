package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	I2C pixyCam;
	//DigitalOutput VisionTiming;
	//DigitalOutput PixyReadTiming;
	
	boolean foundBothTargets = true;
	boolean checkSumAValid = true;
	boolean checkSumBValid = true;
	boolean sigAValid = true;
	boolean sigBValid = true;
	boolean frameFound = true;
	
	char signature;
	char xPos1;
	char xPos2;
	char xMidPoint;
	char yPos1;
	char yPos2;
	char yMidPoint;
	
    public void init() {
    	pixyCam = new I2C(I2C.Port.kOnboard, 0x54);
    			//VisionTiming = new DigitalOutput(0);
    			//PixyReadTiming = new DigitalOutput(1);

    }
    
    public void printPixyStuff(){
    	byte[] pixyValues = new byte[64];
    			//VisionTiming.set(true);
    	
    			//PixyReadTiming.set(true);
    	pixyCam.readOnly(pixyValues, 64);
    	    	//PixyReadTiming.set(false);

    	foundBothTargets = true;
    	checkSumAValid = true;
    	checkSumBValid = true;
    	sigAValid = true;
    	sigBValid = true;
    	frameFound = true;
    	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// find new frame
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	int i = 0;
    	
    	while (!( ((pixyValues[i] & 0xff) == 0x55) && ((pixyValues[i + 1] & 0xff) == 0xAA) ) && i < 34) {
    		i++;
    	}//0-34
    	
    	if(i >= 34)  // Word Not Found
    	{
    		frameFound = false;
    		return;
    	}
    	
    	i+=2;//Start of Object
    	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// verify both targets exist
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	if(!(   ((pixyValues[i   ]&0Xff) == 0x55) && 
    			((pixyValues[i+ 1]&0xff) == 0xAA) && 
    			((pixyValues[i+14]&0xff) == 0x55) && 
    			((pixyValues[i+15]&0xff) == 0xAA) )) {
    		foundBothTargets = false;
    		return; //couldnt find both frames
    	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// verify checksum
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	int checkSumA = 0;
    	int checkSumB = 0;
    	for(int j=0; j<10; j+=2){  /// <<<<<<<<<<<<<< Added j<10
    		checkSumA += (((pixyValues[i+j + 5 ]&0xff) << 8) | (pixyValues[i+j + 4]&0xff)); /// <<<<<<<<<<<<<< Added i+  and 0xff
    		checkSumB += (((pixyValues[i+j + 19]&0xff) << 8) | (pixyValues[i+j + 18]&0xff));
    	}
    	if(checkSumA != (((pixyValues[i + 3]&0xff) << 8) | (pixyValues[i + 2])&0xff)) {
    		checkSumAValid = false;
    		return;
    	}
    	if(checkSumB != (((pixyValues[i + 17]&0xff) << 8) | (pixyValues[i + 16])&0xff) ) {
    		checkSumBValid = false;
    		return;
    	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// verify both are signature 1
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	if(((pixyValues[i +  5] << 8) | pixyValues[i +  4]) != 1 ) {
      		sigAValid = false;
      		return; 
      	}
      	if(((pixyValues[i + 19] << 8) | pixyValues[i + 18]) != 1 ) {
      		sigBValid = false;
      		return; 
      	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// assign values
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      	signature = (char) (((pixyValues[i +  5]&0xff) << 8) | (pixyValues[i +  4]&0xff));
      	xPos1 =     (char) (((pixyValues[i +  7]&0xff) << 8) | (pixyValues[i +  6]&0xff));
      	xPos2 =     (char) (((pixyValues[i + 21]&0xff) << 8) | (pixyValues[i + 20]&0xff));
        xMidPoint = (char) ((xPos1+xPos2)/2);
      	yPos1 =     (char) (((pixyValues[i +  9]&0xff) << 8) | (pixyValues[i +  8]&0xff));
      	yPos2 =     (char) (((pixyValues[i + 23]&0xff) << 8) | (pixyValues[i + 22]&0xff));
      	yMidPoint = (char) ((yPos1+yPos2)/2);
      	
      	//char width =     (char) (((pixyValues[i + 11]&0xff)  << 8) | (pixyValues[i + 10]&0xff));
      	//char height =    (char) (((pixyValues[i + 13]&0xff) << 8)  | (pixyValues[i + 12]&0xff));
      	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// print values 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   		SmartDashboard.putNumber("signature", signature);
    	SmartDashboard.putNumber("xPos1", xPos1);
    	SmartDashboard.putNumber("xPos2", xPos2);
    	SmartDashboard.putNumber("xMidPoint", xMidPoint);  
    	SmartDashboard.putNumber("yPos", yPos1);
    	//SmartDashboard.putNumber("width", width);
    	//SmartDashboard.putNumber("height", height);

    			//VisionTiming.set(false);
    }
    
}
