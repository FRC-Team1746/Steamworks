package org.usfirst.frc.team1746.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	I2C pixyCam;
	DigitalOutput VisionTiming;//<<<<<<<<<<<<<<<<<<<<<<<
	DigitalOutput PixyReadTiming;//<<<<<<<<<<<<<<<<<<<<<<<
	
	boolean foundBothTargets = true;
	boolean checkSumAValid = true;
	boolean checkSumBValid = true;
	boolean sigAValid = true;
	boolean sigBValid = true;
	boolean frameFound = true;

	
	
    public void init() {
    	pixyCam = new I2C(I2C.Port.kOnboard, 0x54);
    	VisionTiming = new DigitalOutput(0);//<<<<<<<<<<<<<<<<<<<<<<<
    	PixyReadTiming = new DigitalOutput(1);//<<<<<<<<<<<<<<<<<<<<<<<

    }
    
    public void printPixyStuff(){
    	byte[] pixyValues = new byte[64];
    	
    	VisionTiming.set(true);//<<<<<<<<<<<<<<<<<<<<<<<
    	PixyReadTiming.set(true);//<<<<<<<<<<<<<<<<<<<<<<<
    	
    	pixyCam.readOnly(pixyValues, 64);
    	
    	PixyReadTiming.set(false);  //<<<<<<<<<<<<<<<<<<<<<<<

    	
    	// debug - print data buffer as hex bytes
//		System.out.println(">>>Frame <<<<");
//		for(int j=0; j<64; j++)
//		{
//			String hex = Integer.toHexString(pixyValues[j]|0x1000);
//			System.out.printf(hex.substring(hex.length()-2).toUpperCase().concat(" "));
//		}
//    	System.out.printf("\n");	
    	//Given all of that
    	//  update average
    	//  find center between two targets
    	//  calc distance/angle/etc
    	// if can't find or validate frame, 
    	//    - set bad frame flag - possibly make it persistent over tbd frames?
    	//    - abort and wait for next iteration and new frame data
    	
    	foundBothTargets = true;
    	checkSumAValid = true;
    	checkSumBValid = true;
    	sigAValid = true;
    	sigBValid = true;
    	frameFound = true;
    	
    	//find new frame
    	int i = 0;
    	
    	//>>>>>>>>>>>>>>> added 0xff to make it work
    	while (!( ((pixyValues[i] & 0xff) == 0x55) && ((pixyValues[i + 1] & 0xff) == 0xAA) ) && i < 34) { ///<<<<<<<<<<<<< changed 36 to 34	
    		i++;
    	}//0-34
    	
    	if(i >= 34)  // Word Not Found    ///<<<<<<<<<<<<< changed 36 to 34	
    	{
    		frameFound = false;
    		return;
    	}
    	
    	i+=2;//Start of Object
    	
    	// verify there are two targets //>>>>>>>>>>>>>>> added 0xff to make it work
    	if(!( ((pixyValues[i]&0Xff) == 0x55) && ((pixyValues[i+1]&0xff) == 0xAA) && ((pixyValues[i+14]&0xff) == 0x55) && ((pixyValues[i+15]&0xff) == 0xAA) )) {
//    		System.out.printf("  i+0 = %X",  pixyValues[i]);
//    		System.out.printf("  i+1 = %X",  pixyValues[i+1]);
//    		System.out.printf("  i+14 = %X",  pixyValues[i+14]);
//    		System.out.printf("  i+15 = %X",  pixyValues[i+15]);
//    		System.out.printf("\n");
    		foundBothTargets = false;
    		return; //couldnt find both frames
    	}
    	
    	// verify checksum
    	int checkSumA = 0;
    	int checkSumB = 0;
    	for(int j=0; j<10; j+=2){  /// <<<<<<<<<<<<<< Added j<10
    		checkSumA += (((pixyValues[i+j + 5 ]&0xff) << 8) | (pixyValues[i+j + 4]&0xff)); /// <<<<<<<<<<<<<< Added i+  and 0xff
//    		System.out.printf("  CkSumA = %X \n",  checkSumA);
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
    	
    	// verify both are signature 1
      	if(((pixyValues[i + 5] << 8) | pixyValues[i + 4]) != 1 ) {
      		sigAValid = false;
      		return; 
      	}
      	if(((pixyValues[i + 19] << 8) | pixyValues[i + 18]) != 1 ) {
      		sigBValid = false;
      		return; 
      	}
    	
      	char Signature = (char) (((pixyValues[i + 5]&0xff) << 8)   | (pixyValues[i + 4]&0xff));     ///<<<<<<<<<<< added 0xff
      	char xPosition1 = (char) (((pixyValues[i + 7]&0xff) << 8)   | (pixyValues[i + 6]&0xff));     ///<<<<<<<<<<< added 0xff
      	char xPosition2 = (char) (((pixyValues[i + 21]&0xff) << 8)   | (pixyValues[i + 20]&0xff));     ///<<<<<<<<<<< NEW
      	char xMidPoint = (char) ((xPosition1+xPosition2)/2);
      	
      	char yPosition = (char) (((pixyValues[i + 9]&0xff) << 8)   | (pixyValues[i + 8]&0xff));     ///<<<<<<<<<<< added 0xff
      	char width =     (char) (((pixyValues[i + 11]&0xff)  << 8) | (pixyValues[i + 10]&0xff));    ///<<<<<<<<<<< added 0xff
      	char height =    (char) (((pixyValues[i + 13]&0xff) << 8)  | (pixyValues[i + 12]&0xff));    ///<<<<<<<<<<< added 0xff
   		SmartDashboard.putNumber("Signature", Signature);
    	SmartDashboard.putNumber("xPosition1", xPosition1);
    	SmartDashboard.putNumber("xPosition2", xPosition2);  /// <<<<<<<<<<<<<<<<<NEW
    	SmartDashboard.putNumber("xMidPoint", xMidPoint);  
    	SmartDashboard.putNumber("yPosition", yPosition);
    	SmartDashboard.putNumber("width", width);
    	SmartDashboard.putNumber("height", height);

    	VisionTiming.set(false); //<<<<<<<<<<<<<<<
    }
    
}
