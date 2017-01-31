package org.usfirst.frc.team1746.robot;

public class BoilerTracker {
	Vision vision = new Vision();

	int x = vision.xPos;
	int y = vision.yPos;
	int width = vision.width;
	int height = vision.height;
	
	
	public boolean turn(){
		return(x >= 150);
	}
	
	
	
}
