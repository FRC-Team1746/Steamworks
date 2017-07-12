package org.usfirst.frc.team1746.auton;

import org.usfirst.frc.team1746.robot.Conveyor;
import org.usfirst.frc.team1746.robot.Drivetrain;
import org.usfirst.frc.team1746.robot.GearIntake;
import org.usfirst.frc.team1746.robot.Loader;
import org.usfirst.frc.team1746.robot.Shooter;
import org.usfirst.frc.team1746.robot.subsystems.Turret;
import org.usfirst.frc.team1746.vision.VisionBase;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonBase {
	SendableChooser<String> autonSelector = new SendableChooser<>();
	SendableChooser<String> allianceSelector = new SendableChooser<>();
	SendableChooser<Boolean> shootSelector = new SendableChooser<>();
	
	private Drivetrain m_drive;
	private GearIntake m_gear;
	private Loader m_loader;
	private Shooter m_shooter;
	private Conveyor m_conveyor;
	private VisionBase m_vision;
	private Turret m_turret;
	
	public AutonBase(Drivetrain drive, GearIntake gear, Loader loader, Shooter shooter, Conveyor conveyor, VisionBase vision, Turret turret){
		m_drive = drive;
		m_gear = gear;
		m_loader = loader;
		m_shooter = shooter;
		m_conveyor = conveyor;
		m_vision = vision;
		m_turret = turret;
	}
	
	AutonConstants aConstants = new AutonConstants();
	
	GearLeft gear_l;
	GearCenter gear_c;
	GearRight gear_r;
	Forward forward;
	Hopper hopper;
	
	int loops = 0;
	
	public void init(){
		gear_l = new GearLeft(m_drive, m_gear, m_loader, m_conveyor, m_shooter, m_vision);
		gear_c = new GearCenter(m_drive, m_gear, m_loader, m_conveyor, m_shooter);
		gear_r = new GearRight(m_drive, m_gear, m_loader, m_conveyor, m_shooter, m_vision);
		forward = new Forward(m_drive);
		hopper = new Hopper(m_drive, m_gear, m_shooter, m_conveyor, m_loader, m_turret);
		gear_l.init();
		gear_c.init();
		gear_r.init();
		forward.init();
		hopper.init();
	}
	public void initSmartDashboard() {
		initAutonSelector();
		initAllianceSelector();
		initShootSelector();
		SmartDashboard.putBoolean("Reset Auton", false);
		
		hopper.initSmartDashboard();
	}
	public void updateSmartDashboard(){
		SmartDashboard.putString("Selected Alliance", selectedAlliance());
		SmartDashboard.putString("Selected Auton", selectedAuton());
		SmartDashboard.putBoolean("Selected Shoot", selectedShoot());
		SmartDashboard.putString("Current State", currentState());
		if(SmartDashboard.getBoolean("Reset Auton", false)){
			resetAll();
			SmartDashboard.putBoolean("Reset Auton", false);
		}
		
		hopper.updateSmartDashboard();
	}
	
	public void initAllianceSelector(){
		allianceSelector.addDefault("Red", "red");
		allianceSelector.addObject("Blue", "blue");
		SmartDashboard.putData("Alliance Selector", allianceSelector);
	}
	
	public void initAutonSelector(){
		autonSelector.addDefault("Left Gear", "gear_l");
		autonSelector.addObject("Center Gear", "gear_c");
		autonSelector.addObject("Right Gear", "gear_r");
		autonSelector.addObject("Hopper", "hopper");
		autonSelector.addObject("Drive Forward", "forward");
		autonSelector.addObject("None", "none");
		SmartDashboard.putData("Auton Selector", autonSelector);
	}
	public void initShootSelector(){
		shootSelector.addDefault("No", false);
		shootSelector.addObject("Yes", true);
		SmartDashboard.putData("Shooter", shootSelector);
	}
	public void resetAll(){
		gear_r.reset();
		gear_c.reset();
		gear_l.reset();
		hopper.reset();
		

		m_drive.resetEncoders();
		m_drive.resetGyro();
	}
	
	public String selectedAuton(){
		return autonSelector.getSelected();
	}
	public String selectedAlliance(){
		return allianceSelector.getSelected();
	}
	public boolean selectedShoot(){
		return shootSelector.getSelected();
	}
	public String currentState(){
		if(selectedAuton().equalsIgnoreCase("gear_r")) return gear_r.currentState();
		if(selectedAuton().equalsIgnoreCase("gear_c")) return gear_c.currentState();
		if(selectedAuton().equalsIgnoreCase("gear_l")) return gear_l.currentState();
		if(selectedAuton().equalsIgnoreCase("hopper")) return hopper.currentState();
		else return "";
	}
	
	public void run(){
		m_gear.smartFlaps();
		if(selectedAuton().equalsIgnoreCase("gear_r")){
			gear_r.auton(selectedAlliance(), selectedShoot());
		}
		if(selectedAuton().equalsIgnoreCase("gear_c")){
			gear_c.auton(selectedAlliance(), selectedShoot(), false);
		}
		if(selectedAuton().equalsIgnoreCase("gear_l")){
			gear_l.auton(selectedAlliance(), selectedShoot());
		}
		
		if(selectedAuton().equalsIgnoreCase("forward")){
			forward.auton();
		}
		if(selectedAuton().equalsIgnoreCase("hopper")){
			hopper.auton(selectedAlliance());
		}
		if(selectedAuton().equalsIgnoreCase("none")){
			
		}
	}	
	
	public void getCurrentState(){
		
	}
	
}
