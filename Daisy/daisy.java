import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import  lejos.robotics.RegulatedMotor;


public class daisy 
{
	
	static double RADDURCHMESSER = 5.6;

	static double RADABSTAND = 11.5;



	// Konfiguration des Piloten
	public static DifferentialPilot pilot = new DifferentialPilot(RADDURCHMESSER, RADABSTAND, Motor.B, Motor.A) ;  //(Motor.B, Motor.A , RADDURCHMESSER, RADABSTAND);
	//Ultraschall Sensor auf Port 1
	public static UltrasonicSensor sonicSensor = new UltrasonicSensor( SensorPort.S1);
	public static RegulatedMotor middleMotor = Motor.C;

	
	
	public static void main(String[] args) 
	{
		//INIT
		pilot.setRotateSpeed( 450 );	
		pilot.setTravelSpeed( 50);		// 50cm/s
		middleMotor.setSpeed(150);
		
		while (true)
		{
			pilot.forward();
			
			while (sonicSensor.getDistance() >= 29 )
			{
				
//				System.out.println( "Distanz:\n" + sonicSensor.getDistance() );
			}
			pilot.stop();
			middleMotor.rotate(35);
			pilot.rotate(90);
			middleMotor.rotate(-35);
			
		}
	}

}
