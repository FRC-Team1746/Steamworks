package org.usfirst.frc.team1746.robot;

import org.usfirst.frc.team1746.robot.Robot.Defenses;
import org.usfirst.frc.team1746.robot.Robot.Encoders;
import org.usfirst.frc.team1746.robot.Robot.SolenoidStates;
import org.usfirst.frc.team1746.robot.Robot.Solenoids;

public class Cross {
	Robot robot;
	
	//////////////////////////////////////////////////////////////////////////
	//  SPEED  ///////////////////////////////////////////////////////////////
	//////////////  The speed the robot will drive over the defense a      //
	//////////////  number between -1 and 1. Positive numbers drive         //
	//////////////  forward, negative backwards.                            //
	//////////////////////////////////////////////////////////////////////////
	
	                             //  Change these values  \\
	                                      //  |  \\
	                                      //  |  \\
	                                      //  V  \\
	
	double SPEED_MOAT =						 1; // Moat
	double SPEED_RAMPARTS =					 .75; // Ramparts
	double SPEED_ROCK_WALL = 			 	 .75; // Rock Wall
	double SPEED_ROUGH_TERRAIN = 		  	 .75; // Rough Terrain
	double SPEED_SPYBOT = 				 .75; // Sally Port
	
	//////////////////////////////////////////////////////////////////////////
	//  DISTANCE  ////////////////////////////////////////////////////////////
	//////////////  How far the robot will drive before trying to track     //
	//////////////  the goal and attempt to shoot the ball                  //
	//////////////////////////////////////////////////////////////////////////
	
    							//  Change these values  \\
										 //  |  \\
						  				 //  |  \\
										 //  V  \\
	
	double DIST_MOAT = 						25/6*Math.PI*100*1.714; // Moat
	double DIST_RAMPARTS = 					175/6*Math.PI*100*1.714; // Ramparts
	double DIST_ROCK_WALL = 				125/6*Math.PI*100*1.714; // Rock Wall
	double DIST_ROUGH_TERRAIN = 			125/6*Math.PI*100*1.714; // Rough Terrain
	double DIST_SPYBOT = 				    0;//100/6*Math.PI*100*1.714; // Sally Port
	
	//////////////////////////////////////////////////////////////////////////
	//  CODE  ////////////////////////////////////////////////////////////////
	//////////////  If everything is going smoothly, you should not have    //
	//////////////  to touch this part of the code :)                       //
	//////////////////////////////////////////////////////////////////////////
	
	public Cross(Robot robot){
		this.robot = robot;
	}
	
	public boolean defense(Defenses defense){
		if(defense.equals(Defenses.MOAT)){
			robot.drivePID(SPEED_MOAT, 0);
			if(robot.driveLeftEncoder.get() > DIST_MOAT){
				return true;
			} else {
				return false;
			}
		}
		if(defense.equals(Defenses.RAMPARTS)){
			robot.drivePID(SPEED_RAMPARTS, 240);
			if(robot.getEncoderTicks(Encoders.LEFT) > DIST_RAMPARTS){
				return true;
			} else {
				return false;
			}
		}
		if(defense.equals(Defenses.ROCK_WALL)){
			robot.drivePID(SPEED_ROCK_WALL, 0);
			if(robot.getEncoderTicks(Encoders.LEFT) > DIST_ROCK_WALL){
				return true;
			} else {
				return false;
			}
		}
		if(defense.equals(Defenses.ROUGH_TERRAIN)){
			robot.drivePID(SPEED_ROUGH_TERRAIN, 0);
			if(robot.getEncoderTicks(Encoders.LEFT) > DIST_ROUGH_TERRAIN){
				return true;
			} else {
				return false;
			}
		}
		if(defense.equals(Defenses.SPYBOT)){
			robot.solenoidControl(Solenoids.HOOD, SolenoidStates.OUT);
			robot.shooter.set(1);
			robot.loops++;
			if(robot.loops > 100){
				return true;
			} else{
				return false;
			}
		} else {
			return false;
		}
	}
}



