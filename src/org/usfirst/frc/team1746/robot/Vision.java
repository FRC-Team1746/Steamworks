package org.usfirst.frc.team1746.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	I2C pixyCam;
	byte[] toSend = new byte[32];
	byte[] rawData = new byte[32];
	
	List<Double> xErrors = new ArrayList<Double>();
	List<Double> yErrors = new ArrayList<Double>();
	
	int xPos = -1;
	int yPos = -1;
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
    		xPos = (char) (((pixyValues[i + 7] & 0xff) << 8) | (pixyValues[i + 6] & 0xff));
    		yPos = (char) ((pixyValues[i + 9] & 0xff << 8) | pixyValues[i + 8] & 0xff);
    		width = (char) ((pixyValues[i + 11] & 0xff << 8) | pixyValues[i + 10] & 0xff);
    		height = (char) ((pixyValues[i + 13] & 0xff << 8) | pixyValues[i + 12] & 0xff);
    		SmartDashboard.putNumber("Raw xPos", xPos);
    		SmartDashboard.putNumber("Raw yPos", yPos);
    		SmartDashboard.putNumber("Raw width", width);
    		SmartDashboard.putNumber("Raw height", height);
    		
    		
    		updateErrors(8);
    		SmartDashboard.putNumber("AvgXError", getAvgXError(8));
    		SmartDashboard.putNumber("AvgYError", getAvgYError(8));
    		
    	}
    }
    public double getRawXError(){
    	return xPos-150; //x+ move right //x- move left
    }
    public double getRawYError(){
    	return yPos-100; //y+ move up    //y- move down
    }
    public void updateErrors(int max){
    	if(xErrors.size() > max*2){
    		xErrors = xErrors.subList(max-1, (max*2)-1);
    	}
    	if(yErrors.size() > max*2){
    		yErrors = yErrors.subList(max-1, (max*2)-1);
    	}
    	if(xPos != 0.0 && xPos < 300 && xPos > -300)xErrors.add(getRawXError());
    	if(yPos != 0.0 && yPos < 200 && yPos > -200)yErrors.add(getRawYError());
    }
    public double getAvgXError(int range){
    	double sum = 0;
    	if(xErrors.size() < range+2) return getRawXError();
    	for(double x : xErrors.subList(xErrors.size()-(range+1), xErrors.size()-1))sum+=x;
    	return sum/range;
    }
    public double getAvgYError(int range){
    	double sum = 0;
    	if(yErrors.size() < range+2) return getRawYError();
    	for(double y : yErrors.subList(yErrors.size()-(range+1), yErrors.size()-1))sum+=y;
    	return sum/range;
    }
    public void toLog(){
    	
    }
}
