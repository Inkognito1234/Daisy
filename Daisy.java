import lejos.nxt.Sound;
import lejos.robotics.localization.OdometryPoseProvider;

import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;


public class Daisy 
{
	public static Initialize daisyInit= new Initialize();
	public static DriveMotors motors = new DriveMotors();
	public static ultraSensor objScanner=new ultraSensor();
	public static ColorSensor colorSens=new ColorSensor();
	public static GrabMotor grabber= new GrabMotor();
	public static Sector sector = new Sector();
	
	
	public static OdometryPoseProvider poser=new OdometryPoseProvider(daisyInit.pilot);
	public static Navigator nav=new Navigator(daisyInit.pilot);

	public static boolean rightSensed = false, leftSensed = false;
	public static Pose punktImParcours;
	
	public static void main(String[] args) 
	{
		int distMessung=daisyInit.sonicSensor.getDistance();
		int art, lastSectionX = 0, lastSectionY = 0;
		double[] distanzen=new double[3];
		int[] colors=new int[3];
		boolean sensed = false;
		
		Pose aktuellerPkt = new Pose();
		
		sector.initSector();
		
		daisyInit.init();

		sector.setSector((int)poser.getPose().getX() / 25, (int)poser.getPose().getY() / 25, false);
		
		while (true)
		{
			daisyInit.pilot.forward();
			aktuellerPkt = poser.getPose();
			//daisyInit.pilot.setRotateSpeed( 30 );
			
			do
			{

				colors=colorSens.getColors();
				
				if((int)(poser.getPose().getX() / 25) != lastSectionX || (int)(poser.getPose().getY() / 25) != lastSectionY)
				{
					lastSectionX = (int)(poser.getPose().getX() / 25);
					lastSectionY = (int)(poser.getPose().getY() / 25);
					sector.setSector(lastSectionX , lastSectionY , false);
				}

				if(daisyInit.pilot.getMovementIncrement() >= 15)
				{
					daisyInit.pilot.stop();
					daisyInit.pilot.rotate(-45, true);
					while(daisyInit.pilot.isMoving())
					{
						distMessung=daisyInit.sonicSensor.getDistance();
						if(distMessung <= 20)
						{
							daisyInit.pilot.stop();
							daisyInit.pilot.rotate(5);
							sensed = true;
							break;
						}
					}

					if (sensed) 
					{
						sensed = false;
						distanzen = objScanner.scanObject(distMessung);
						art = objScanner.whatKind(distanzen);
						if (art == 4)
							break;
					}
					
					daisyInit.pilot.rotate(aktuellerPkt.getHeading() - poser.getPose().getHeading());
					daisyInit.pilot.rotate(45, true);
					while(daisyInit.pilot.isMoving())
					{
						distMessung=daisyInit.sonicSensor.getDistance();
						if(distMessung <= 20)
						{
							daisyInit.pilot.stop();
							daisyInit.pilot.rotate(-5);
							sensed = true;
							break;
						}
					}
					
					if (sensed)
					{
						sensed = false;
						distanzen = objScanner.scanObject(distMessung);
						art = objScanner.whatKind(distanzen);
						if (art == 4)
							break;
						else
						{
							distMessung = 255;
							daisyInit.pilot.rotate(aktuellerPkt.getHeading() - poser.getPose().getHeading());
							daisyInit.pilot.forward();
						}
					}
					else
					{
						daisyInit.pilot.rotate(aktuellerPkt.getHeading() - poser.getPose().getHeading());
						daisyInit.pilot.forward();
					}
						
					
				}
				else
					distMessung=daisyInit.sonicSensor.getDistance();
				
			}while ( distMessung > 20 && colors[1] == 6 && colors[0] != 1 && colors[2] != 1);
			
			daisyInit.pilot.stop();
			//daisyInit.pilot.setRotateSpeed( daisyInit.spinSpeed );
			
			
			if(distMessung <= 20)
			{
				Sound.setVolume(100);
				Sound.playSample(daisyInit.soundMGS, daisyInit.volume);	// spielt File ab mit max Lautstaerke	
				Sound.setVolume(20);
				
				distanzen = objScanner.scanObject(distMessung);
				art = objScanner.whatKind(distanzen);
				motors.avoidNearObj( art , distanzen );	
			}
			else
			{
				if( colorSens.checkColor(colors[1]) != 6 )
				{
					sector.setSector((int)poser.getPose().getX() / 25, (int)poser.getPose().getY() / 25, true);
					colorSens.checkTrack();
				}
			}

		}
	}
	
}
