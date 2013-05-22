import lejos.nxt.Sound;
import lejos.robotics.localization.OdometryPoseProvider;

import lejos.robotics.navigation.Navigator;


public class Daisy 
{
	public static Initialize daisyInit= new Initialize();
	public static DriveMotors motors = new DriveMotors();
	public static ultraSensor objScanner=new ultraSensor();
	public static ColorSensor colorSens=new ColorSensor();
	
	
	public static OdometryPoseProvider poser=new OdometryPoseProvider(daisyInit.pilot);
	public static Navigator nav=new Navigator(daisyInit.pilot);

	public static void main(String[] args) 
	{
		int distMessung=daisyInit.sonicSensor.getDistance();
		double[] distanzen=new double[3];
		int[] colors=new int[3];
		
		daisyInit.init();

		while (true)
		{
			daisyInit.pilot.forward();
			
			do
			{

				colors=colorSens.getColors();
				distMessung=daisyInit.sonicSensor.getDistance();
				
			}while ( distMessung >= 22 && colors[0] == 6 && colors[1] == 6 && colors[2] == 6 );
			
			daisyInit.pilot.stop();


			Sound.setVolume(100);
			Sound.playSample(daisyInit.soundMGS, daisyInit.volume);	// spielt File ab mit max Lautstaerke	
			Sound.setVolume(20);
			
			if(distMessung < 22)
			{
				distanzen = objScanner.scanObject(distMessung);

				motors.avoidNearObj(objScanner.whatKind(distanzen), distanzen );	
			}
			else
			{
				colorSens.checkTrack();
			}

			
		}
	}
	
}
