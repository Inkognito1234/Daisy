import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;



public class daisy 
{
	//test213423423523452345
	static double RADDURCHMESSER = 5.6;
	static double RADABSTAND = 14;

	// Konfiguration des Piloten
	public static OwnPilot pilot = new OwnPilot(Motor.B, Motor.A , RADDURCHMESSER, RADABSTAND);
	//Ultraschall Sensor auf Port 1
	public static UltrasonicSensor sonicSensor = new UltrasonicSensor( SensorPort.S1);
	//Drucksensor auf Port 4
	public static TouchSensor touchSensor = new TouchSensor (SensorPort.S4);
	public static void main(String[] args) 
	{
		while (!touchSensor.isPressed())
		{
			while (sonicSensor.getDistance() >= 22 )
			{
				if(touchSensor.isPressed()) break;
				pilot.drive(0.25, 0);
//				System.out.println( "Distanz:\n" + sonicSensor.getDistance() );
			}
			pilot.stop();
			pilot.driveDistance(0, 1, Math.PI * RADDURCHMESSER / 2 , false);  // 1/2 Radumdrehung => 90 Grad auf der Stelle
		}
	}

}
