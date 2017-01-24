
package org.usfirst.frc.team5414.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Compressor;
import com.ctre.CANTalon; 

import java.util.Arrays;

import org.opencv.core.Mat;
import edu.wpi.first.wpilibj.CameraServer;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;




import org.usfirst.frc.team5414.robot.commands.ExampleCommand;
import org.usfirst.frc.team5414.robot.subsystems.Drivetrain;
import org.usfirst.frc.team5414.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team5414.robot.subsystems.Wheel;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	public static Drivetrain drivetrain;
	public static Wheel shoot;
	public static Compressor compressor;
	public static NetworkTable table;

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	Command autonomousCommand;
//	SendableChooser<Command> chooser = new SendableChooser<>();
	boolean currentButtonState=false;
    String test="";
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	
	CameraServer server;
//    USBCamera targetCam;
	@Override
	
	public void robotInit() {
//		CameraServer.getInstance().startAutomaticCapture();
		table = NetworkTable.getTable("GRIP/myContoursReport");
		compressor = new Compressor(0);
		compressor.start();
		drivetrain = new Drivetrain();
		Robot.drivetrain.FullTraction();
		
		shoot=new Wheel();
		oi = new OI();
		
//		for (double area:areas) 
//        {
//        	SmartDashboard.putNumber("Area", area);
//        }
//		chooser.addDefault("Default Auto", new ExampleCommand());
//		 chooser.addObject("My Auto", new MyAutoCommand());
//		SmartDashboard.putData("Auto mode", chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
//		autonomousCommand = chooser.getSelected();
//
//		/*
//		 * String autoSelected = SmartDashboard.getString("Auto Selector",
//		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
//		 * = new MyAutoCommand(); break; case "Default Auto": default:
//		 * autonomousCommand = new ExampleCommand(); break; }
//		 */
//
//		// schedule the autonomous command (example)
//		if (autonomousCommand != null)
//			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		double[] areas = table.getNumberArray("area", new double[0]);
		Scheduler.getInstance().run();
		test="Keys: ";
		currentButtonState = oi.getJoystick1().getRawButton(5);
		SmartDashboard.putNumber("Framerate", table.getNumber("myNumber", Double.NaN));
    	SmartDashboard.putBoolean("HasArea",  table.containsKey("area"));
//    	
    	for (String i: table.getKeys())
    		test+=i+", ";
    	test+="\n";
    	SmartDashboard.putString("Keys", test);
//    	
    		try {
				SmartDashboard.putString("Area: ", Arrays.toString(table.getNumberArray("area", new double[0])));
				SmartDashboard.putBoolean("Errored", false);
				SmartDashboard.putString("CenterX: ", Arrays.toString(table.getNumberArray("centerX", new double[0]))); 
				SmartDashboard.putString("CenterY", Arrays.toString(table.getNumberArray("centerY",new double[0])));
			} catch (Exception e) {
				SmartDashboard.putBoolean("Errored", true);
			}

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	public static boolean containsArea(){
		double[] thing  = table.getNumberArray("area", new double[0]);
//		SmartDashboard.putString("areaThing", Arrays.toString(thing));
//		SmartDashboard.putNumber("Thing length", thing.length);
		return thing.length==0;
	}
	public static double centerX(){
		double x = 0;
		double[] center = table.getNumberArray("centerX", new double[0]);
		if(center.length==1){
			x = center[0];
		}
		return x;
	}
}
