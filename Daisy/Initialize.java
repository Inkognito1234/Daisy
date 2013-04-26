
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import java.io.File;


public class Initialize {
	
	//wichtige Konstanten
	public final double RADDURCHMESSER = 5.6;
	public final double RADABSTAND = 11.5;

	
	//Motoren definieren um gleichzietig anzusprechen 
	public final DifferentialPilot pilot = new DifferentialPilot(RADDURCHMESSER, RADABSTAND, Motor.B, Motor.A) ;
		
	//Ultraschall sensor definieren
	public final UltrasonicSensor sonicSensor = new UltrasonicSensor( SensorPort.S1);
		
	//Motoren definieren (rechts/links wegen Geschw.-berechnung nochmal einzeln nötig)
	public  NXTRegulatedMotor middleMotor = Motor.C;
	public  NXTRegulatedMotor rightMotor = Motor.A;
	public  NXTRegulatedMotor leftMotor = Motor.B;

	//Farbsensoren definieren
	public final ColorSensor sensorFront = new ColorSensor( SensorPort.S2);
	public final ColorSensor sensorLeft = new ColorSensor( SensorPort.S3);
	public final ColorSensor sensorRight = new ColorSensor( SensorPort.S4);
	
	//Soundfile
	public final File sound1 = new File("sound1.wav");
	
	//Variablen zum initialisieren
	double driveSpeed=20, spinSpeed=350 ;// drive= cm/s ; spin=Grad/s (Motorleistung)
	int volume=100, grabSpeed=150;
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
