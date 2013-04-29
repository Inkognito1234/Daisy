
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

import java.io.File;


public class Initialize {
	
	//Ultraschall sensor definieren
	public UltrasonicSensor sonicSensor = new UltrasonicSensor( SensorPort.S1);
		
	//Motoren definieren
	public  NXTRegulatedMotor middleMotor = Motor.C;

	//Piloten definieren
	public DifferentialPilot pilot = new DifferentialPilot(DriveMotors.RADDURCHMESSER,DriveMotors.RADABSTAND, Motor.B, Motor.A);
	public DriveMotors motors=new DriveMotors();


	//Farbsensoren definieren
	public ColorSensor sensorFront = new ColorSensor( SensorPort.S2);
	public ColorSensor sensorLeft = new ColorSensor( SensorPort.S3);
	public ColorSensor sensorRight = new ColorSensor( SensorPort.S4);
	
	//Soundfile
	public final File sound1 = new File("sound1.wav");
	
	//Konstanten
	
	final int BALLDURCHMESSER = 7;
	
	//Variablen zum initialisieren
	public final double driveSpeed=20 ;// drive= cm/s ; spin=Grad/s (Motorleistung)

	public final double spinSpeed=350;
	static int volume=100, grabSpeed=150;
	int blockadeNachVersuchen = 15;		// Hier Zahl einsetzen die bestimmt wie oft checkRise() ausgeführt wird, 
										// bis es als Blockade und nicht als Steigung interpretiert wird
	double riseTreshold = 0.9;			// bestimmt ab wann checkRise die Geschwindigkeit erhöht (Werte < 1); 
										// 0.9 = unter 90% der eigentlichen Geschw. wird beschleunigt.
	double risePowerUp = 1.2;			// gibt an um wie viel checkRise die Geschw. erhöht (werte > 1)
	
	//schaut ob File zuende gespielt wurde um neu zu starten (Endlos/Weiderholschleife)
	public void checkSound()
	{
		if(Sound.getTime() <= 0) Sound.playSample(this.sound1, 100);
	}
	
	//Initialisiert die einzelnen Elemente
	public void init()
	{
		
		Sound.playSample(sound1, volume);	// spielt File ab mit max Lautstaerke

		middleMotor.stop();
		pilot.setRotateSpeed( spinSpeed );	// Grad/s (Roboterdrehung)
		pilot.setTravelSpeed( driveSpeed);	// cm/s
		middleMotor.setSpeed( grabSpeed);	// Grad/s (Motordrehung/-leistung)
		sensorFront.setFloodlight(false);	// ? :-)
		sensorLeft.setFloodlight(false);
		sensorRight.setFloodlight(false);
	}
		
}
