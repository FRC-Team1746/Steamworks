package org.usfirst.frc.team1746.robot;

public class AutonBRG {
	AutonConstants aConstants = new AutonConstants();
	int loops = 0;
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	
	
	States currentState;
	public AutonBRG(Drivetrain drive, GearIntake gear) {
		m_drive = drive;
		m_gear = gear;
	}
	
	public enum States {
		INIT,
		DRIVE,
		DRIVE_ROTATE_RIGHT,
		DRIVE_TO_PEG,
		WAIT_GEAR_REMOVAL,
		DRIVE_FROM_PEG,
		ALLIGN,
		DRIVE_AWAY,
		ROTATE_RIGHT,
		DRIVE_CENTER,
		WAIT_TELEOP
	}
	
	public void init(){
		m_drive.resetEncoders();
		currentState = States.INIT;
	}

	public void auton(){
		switch(currentState){
		case INIT: 
			m_drive.resetEncoders();
			currentState = States.DRIVE;
		break;		
		case DRIVE: 
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.RLG_DIST_DRIVE){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_ROTATE_RIGHT;
			}
		break;
		case DRIVE_ROTATE_RIGHT:
			m_drive.rotate("right");
			if(m_drive.gyroAngle() < -60){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_TO_PEG;
			}
		break;
		
		case DRIVE_TO_PEG:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.RLG_DIST_GEAR_PEG ){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.WAIT_GEAR_REMOVAL;
			}
			
		break;
		case WAIT_GEAR_REMOVAL:
			if(m_gear.gearSensor()){
				loops++;
				if(loops > 50){
					loops = 0;
					m_drive.stop();
					m_drive.resetEncoders();
					currentState = States.DRIVE_FROM_PEG;
				}
			}
		break;
		case DRIVE_FROM_PEG:
			m_drive.straightPID(.4);
			if(m_drive.avgEncoderTicks() < -aConstants.RLG_DIST_FROM_PEG){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.ALLIGN;
			}
		break;
		case ALLIGN:
			m_drive.rotate("left");
			if(m_drive.gyroAngle() > 0){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_AWAY;
			}
		break;
		case DRIVE_AWAY:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.RLG_DIST_CENTER){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.ROTATE_RIGHT;
			}
		break;
		case ROTATE_RIGHT:
			m_drive.rotate("left");
			if(m_drive.gyroAngle() > 45){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.DRIVE_CENTER;
			}
		break;
		case DRIVE_CENTER:
			m_drive.straightPID(-.4);
			if(m_drive.avgEncoderTicks() > aConstants.RLG_DIST_CENTER){
				m_drive.stop();
				m_drive.resetEncoders();
				currentState = States.WAIT_TELEOP;
			}
		break;
		case WAIT_TELEOP: 
						
		break;	
		
		}
	}
}
