package org.usfirst.frc.team1746.robot;

public class BoilerTracker {
	Vision vision = new Vision();

	int x = vision.xPosition;
	int y = vision.yPosition;
	int width = vision.width;
	int height = vision.height;
	
	
	public boolean turn(){
		return(x >= 150);
	}
	
	
	
}
