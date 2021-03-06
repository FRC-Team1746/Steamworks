package org.usfirst.frc.team1746.vision;

import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionBase {
	
	I2C pixyCam;
	//DigitalOutput VisionTiming;
	//DigitalOutput PixyReadTiming;
	
	boolean foundBothTargets = true;
	boolean noTargets = true;
	boolean checkSumAValid = true;
	boolean checkSumBValid = true;
	boolean sigAValid = true;
	boolean sigBValid = true;
	boolean frameFound = true;
	boolean Tracking=false;
	
	char signature;
	char xPos1;
	char xPos2;
	char xMidPoint;
	char yPos1;
	char yPos2;
	char yMidPoint;
	char width1;
	char width2;
	char height1;
	char height2;
	
	char Target_X;
	char Target_Y;
	char Target_W;
	char Target_H;
	
	int numTargets;
	int Setpoint = 150;
	
	
    public void init() {
    	pixyCam = new I2C(I2C.Port.kOnboard, 0x54);
    			//VisionTiming = new DigitalOutput(0);
    			//PixyReadTiming = new DigitalOutput(1);

    }
    
    public void trackingLEDs(){
    	
    }
    
    public void trackObject(){
    	byte[] pixyValues = new byte[64];
    			//VisionTiming.set(true);
    	
    			//PixyReadTiming.set(true);
    	pixyCam.readOnly(pixyValues, 64);
    	    	//PixyReadTiming.set(false);

    	foundBothTargets = true;
    	noTargets = true;
    	checkSumAValid = true;
    	checkSumBValid = true;
    	sigAValid = true;
    	sigBValid = true;
    	frameFound = true;
    	Tracking = false;
    	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// find new frame
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	int i = 0;
    	
    	while (!( ((pixyValues[i] & 0xff) == 0x55) && ((pixyValues[i + 1] & 0xff) == 0xAA) ) && i < 34) {
    		i++;
    	}//0-34
    	

    	if(i >= 34)  // Word Not Found
    	{
//    		System.out.printf("\n\n pixyDump\n", pixyValues[i]);
//    		for(i=0;i<64; i++)
//    		System.out.printf("%4X ", pixyValues[i]);
    		
        	numTargets = 0;
    		frameFound = false;
    		return;
    	}
    	
    	i+=2;//Start of Object
    	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// How Many Targets
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	numTargets = 0;
    	if(((pixyValues[i   ]&0Xff) == 0x55) && ((pixyValues[i+ 1]&0xff) == 0xAA))
    	{
    		numTargets++;
    	}
    	if(((pixyValues[i+14]&0xff) == 0x55) &&	((pixyValues[i+15]&0xff) == 0xAA)) 
    	{
    		numTargets++;
    	}
    	if(numTargets <1)
    	{
    		return;
    	}
    	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// verify checksums
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	int checkSumA = 0;
    	int checkSumB = 0;
    	for(int j=0; j<10; j+=2){ 
    		checkSumA += (((pixyValues[i+j + 5 ]&0xff) << 8) | (pixyValues[i+j + 4]&0xff)); 
    	}
    	if(checkSumA != (((pixyValues[i + 3]&0xff) << 8) | (pixyValues[i + 2])&0xff)) {
    		checkSumAValid = false;
    		return;
    	}
    	if(numTargets >1)
    	{
	    	for(int j=0; j<10; j+=2){ 
	    		checkSumB += (((pixyValues[i+j + 19]&0xff) << 8) | (pixyValues[i+j + 18]&0xff));
	    	}
	    	if(checkSumB != (((pixyValues[i + 17]&0xff) << 8) | (pixyValues[i + 16])&0xff) ) {
	    		checkSumBValid = false;
	    		return;
	    	}
    	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// verify targets are signature 1
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	if(((pixyValues[i +  5] << 8) | pixyValues[i +  4]) != 1 ) {
      		sigAValid = false;
      		return; 
      	}
    	if(numTargets>1)
	    	{
	      	if(((pixyValues[i + 19] << 8) | pixyValues[i + 18]) != 1 ) {
	      		sigBValid = false;
	      		return; 
	      	}
    	}
/////////////////////////////////////////////////////////////////////////////////////////////
//    	Targets are verified so publish results
//      We want to know 
//       - where center of right target is
//       - how far off are we relative to a preset target point
/////////////////////////////////////////////////////////////////////////////////////////////
    	// found at least one valid target so set tracking to True
    	Tracking = true;
    	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// assign values
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      	signature = (char) (((pixyValues[i +  5]&0xff) << 8) | (pixyValues[i +  4]&0xff));
      	xPos1 =     (char) (((pixyValues[i +  7]&0xff) << 8) | (pixyValues[i +  6]&0xff));
      	yPos1 =     (char) (((pixyValues[i +  9]&0xff) << 8) | (pixyValues[i +  8]&0xff));
      	width1 =   (char)  (((pixyValues[i +  11]&0xff) << 8) | (pixyValues[i +10]&0xff));
      	height1 =   (char) (((pixyValues[i +  13]&0xff) << 8) | (pixyValues[i +12]&0xff));
      	
      	if(numTargets > 1)
      	{
	      	xPos2 =     (char) (((pixyValues[i + 21]&0xff) << 8) | (pixyValues[i + 20]&0xff));
	      	yPos2 =     (char) (((pixyValues[i + 23]&0xff) << 8) | (pixyValues[i + 22]&0xff));
	      	width2 =    (char) (((pixyValues[i + 25]&0xff) << 8) | (pixyValues[i + 24]&0xff));
	      	height2 =   (char) (((pixyValues[i + 27]&0xff) << 8) | (pixyValues[i + 26]&0xff));
      	}
      	else
      	{
      		xPos2 = (char) 0;
      		yPos2 = (char) 0;
      		width2 = (char) 0;
      		height2 = (char) 0;
      	}
      	// Find right most target
      	if(xPos1 > xPos2)
      	{
      		Target_X = xPos1;
      		Target_H = height1;
      		Target_W = width1;
      		Target_Y = yPos1;
      	}
      	else
      	{
      		Target_X = xPos2;
      		Target_H = height2;
      		Target_W = width2;
      		Target_Y = yPos2;
      	}
    }
      	//char width =     (char) (((pixyValues[i + 11]&0xff)  << 8) | (pixyValues[i + 10]&0xff));
      	//char height =    (char) (((pixyValues[i + 13]&0xff) << 8)  | (pixyValues[i + 12]&0xff));
      	
    public void updateSmartdashboard() {
    	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///// print values 
    	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	SmartDashboard.putNumber("Target_X", Target_X);
    	SmartDashboard.putNumber("Target_Y", Target_Y);
//    	SmartDashboard.putNumber("Target_W", Target_W);
//    	SmartDashboard.putNumber("Target_H", Target_H);
    	SmartDashboard.putNumber("Target_error", getError());
    	SmartDashboard.putNumber("numTargets: ", numTargets);

//    	SmartDashboard.putNumber("signature", signature);
//    	SmartDashboard.putNumber("xPos1", xPos1);
//    	SmartDashboard.putNumber("xPos2", xPos2);
//    	SmartDashboard.putNumber("yPos1", yPos1);
//    	SmartDashboard.putNumber("yPos2", yPos2);
//    	SmartDashboard.putNumber("width1", width1);
//    	SmartDashboard.putNumber("height", height1);
//    	SmartDashboard.putNumber("width2", width2);
//    	SmartDashboard.putNumber("height", height2);


    	SmartDashboard.putBoolean("foundBothTargets", foundBothTargets);
//    	SmartDashboard.putBoolean("checkSumAValid", checkSumAValid);
//    	SmartDashboard.putBoolean("checkSumBValid", checkSumBValid);
//    	SmartDashboard.putBoolean("sigAValid", sigAValid);
//    	SmartDashboard.putBoolean("sigBValid", sigAValid);
//    	SmartDashboard.putBoolean("frameFound", frameFound);
    }

    public int getError(){
    	return Target_X-Setpoint;
    }
    
    public int getNumTargets(){
    	return numTargets;
    }
    
    public boolean Tracking(){
    	return Tracking;
    }
    
}
