import lejos.nxt.Sound;
import lejos.robotics.localization.OdometryPoseProvider;

import lejos.robotics.navigation.Navigator;


public class Daisy 
{
	public static Initialize daisyInit= new Initialize();
	public static DriveMotors motors = new DriveMotors();
	public static ultraSensor objScanner=new ultraSensor();
	public static ColorSensor colorSens=new ColorSensor();
	public static GrabMotor grabber= new GrabMotor();
	
	
	public static OdometryPoseProvider poser=new OdometryPoseProvider(daisyInit.pilot);
	public static Navigator nav=new Navigator(daisyInit.pilot);

	public static boolean rightSensed = false, leftSensed = false;
	
	public static void main(String[] args) 
	{
		int distMessung=daisyInit.sonicSensor.getDistance();
		int art;
		double[] distanzen=new double[3];
		int[] colors=new int[3];
				
		daisyInit.init();
	
		while (true)
		{
			daisyInit.pilot.forward();
			
			do
			{

				colors=colorSens.getColors();
				/*if(colors[0]==7)
				{
					Sound.beepSequence();
					leftSensed = true;
					rightSensed = false;
				}
				if(colors[2]==7)
				{
					Sound.beepSequenceUp();
					rightSensed = true;
					leftSensed = false;
				}*/
				distMessung=daisyInit.sonicSensor.getDistance();
				
			}while ( distMessung > 20 && colors[1] == 6 && colors[0] != 1 && colors[2] != 1);
			
			daisyInit.pilot.stop();
			//Daisy.motors.driveBack();

			//System.out.println(distMessung + " " + colors[0]  + " " + colors[1] + " " + colors[2] );
			//Button.waitForAnyPress();
			Sound.setVolume(100);
			Sound.playSample(daisyInit.soundMGS, daisyInit.volume);	// spielt File ab mit max Lautstaerke	
			Sound.setVolume(20);
			
			if(distMessung <= 20)
			{
				distanzen = objScanner.scanObject(distMessung);
				art = objScanner.whatKind(distanzen);
				motors.avoidNearObj( art , distanzen );	
			}
			else
			{
				colorSens.checkTrack();
			}

		}
	}
	
}
