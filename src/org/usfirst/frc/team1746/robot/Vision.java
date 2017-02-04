package org.usfirst.frc.team1746.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.wpi.first.wpilibj.I2C;

public class Vision {
	I2C pixyCam;
	byte[] toSend = new byte[32];
	byte[] rawData = new byte[32];
	
	List<Double> xErrors = Arrays.asList(new Double[10]);
	List<Double> yErrors = Arrays.asList(new Double[10]);
	
	int xPos = -1;
	int yPos = -1;
	int width = -1;
	int height = -1;
	
	boolean foundBothTargets = true;
	boolean checkSumAValid = true;
	boolean checkSumBValid = true;
	boolean sigAValid = true;
	boolean sigBValid = true;
	
	int loops = -1;
	
    public void init() {
    	pixyCam = new I2C(I2C.Port.kOnboard, 0x54);
    }
    
    public void printPixyStuff(){
    	byte[] pixyValues = new byte[64];
    	pixyValues[0] = (byte) 0b01010101;
    	pixyValues[1] = (byte) 0b10101010;
    	pixyCam.readOnly(pixyValues, 64);
    	int i = 0;
    	
    	
    	while (!(pixyValues[i] == 0x55 && pixyValues[i + 1] == 0xAA) && i < 36) {
    		i++;
    	}//0-36
    	
    	if(i >= 36) return; // Word Not Found    	
    	
    	i+=2;//Start of Object
    	
    	// verify there are two targets
    	if(!(pixyValues[i] == 0x55 && pixyValues[i+1] == 0xAA && pixyValues[i+14] == 0x55 && pixyValues[i+15] == 0xAA)) {
    		foundBothTargets = false;
    		return; //couldnt find both frames
    	}
    	
    	// verify checksum
    	int checkSumA = 0;
    	int checkSumB = 0;
    	for(int j=0; j<5; j+=2){
    		checkSumA += ((pixyValues[j + 5] << 8) | pixyValues[j + 4]);
    		checkSumB += ((pixyValues[j + 19] << 8) | pixyValues[j + 18]);
    	}
    	if(checkSumA != ((pixyValues[i + 3] << 8) | pixyValues[i + 2])) {
    		checkSumAValid = false;
    		return;
    	}
    	if(checkSumB != ((pixyValues[i + 17] << 8) | pixyValues[i + 16])) {
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
    	
    	//
   		xPos = (char) ((pixyValues[i + 7] << 8) | pixyValues[i + 6]);
   		yPos = (char) ((pixyValues[i + 9] << 8) | pixyValues[i + 8]);
   		width =     (char) ((pixyValues[i + 11]  << 8) | pixyValues[i + 10]);
   		height =    (char) ((pixyValues[i + 13] << 8) | pixyValues[i + 12]);
    		
    }
    public double getRawXError(){
    	return xPos-150; //x+ move right //x- move left
    }
    public double getRawYError(){
    	return yPos-100; //y+ move up    //y- move down
    }
    public void updateErrors(int max){
    	loops++;
    	if(loops>9) loops = 0;
    	xErrors.set(loops, getRawXError());
    	yErrors.set(loops, getRawYError());
    }
    public double getAvgXError(){
    	double sum = 0;
    	if(xErrors.get(10) != null){
    		for(double xError : xErrors){
    			sum+=xError;
    		}
    	}
    	return sum/10;
    }
    public double getAvgYError(int range){
    	double sum = 0;
    	if(yErrors.get(10) != null){
    		for(double yError : yErrors){
    			sum+=yError;
    		}
    	}
    	return sum/10;
    }
}
