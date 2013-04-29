/*//import lejos.nxt.I2CPort;
import lejos.nxt.SensorConstants;
//import lejos.nxt.UltrasonicSensor;


public class ultraSensor{// extends UltrasonicSensor {

	//public ultraSensor(I2CPort port) 
	//{
		//super(port);
		// TODO Auto-generated constructor stub
	//}
	
	int[] dist;
	
	int count=0, sum=0, durchschnitt=0, checkLeft=0, checkRight=0;
	
	public double[] scanObject(int lastDist)
	{
		
		double[] distanzen={-1};
		
		daisy.daisyInit.sonicSensor.ping();
		count = daisy.daisyInit.sonicSensor.getDistances(dist);
		for(int i=0; i < count; i++)
		{
			sum = sum + dist[i];
		}
		durchschnitt = sum / count;
		
		if( durchschnitt > lastDist+10) 
			{
				distanzen[1] = durchschnitt;
				daisy.daisyInit.sonicSensor.continuous();
				return distanzen; //Messung lastDist war falsch -> kein Objekt in der Nähe -> [0] und [2] = -1
			}
		else
		{
			Initialize.pilot.rotate(Math.tan(durchschnitt/daisy.daisyInit.BALLDURCHMESSER)); //Drehung um BALLDURCHMESSER nach links
			daisy.daisyInit.sonicSensor.ping();
			count = daisy.daisyInit.sonicSensor.getDistances(dist);
			for(int i=0; i < count; i++)
			{
				sum = sum + dist[i];
			}
			checkLeft = sum / count;
			
			Initialize.pilot.rotate( -2* Math.tan(durchschnitt/daisy.daisyInit.BALLDURCHMESSER)); //Drehung um 2* BALLDURCHMESSER nach rechts
			
			daisy.daisyInit.sonicSensor.ping();
			count = daisy.daisyInit.sonicSensor.getDistances(dist);
			for(int i=0; i < count; i++)
			{
				sum = sum + dist[i];
			}
			checkRight = sum / count;
			
			Initialize.pilot.rotate(Math.tan(durchschnitt/daisy.daisyInit.BALLDURCHMESSER)); //Drehung um BALLDURCHMESSER nach links um wieder Objekt anzuschauen

			distanzen[0]=checkLeft;
			distanzen[1]=durchschnitt;
			distanzen[2]=checkRight;
			daisy.daisyInit.sonicSensor.continuous();
			
			return distanzen; 

		}
		
	}
	
	public int whatKind(double[] distanzen)
	{
		
		if ( distanzen[0] < 0 || distanzen[2] < 0) return -1; //falsche Messung
		else if(distanzen[0] <= distanzen[1] +10 && distanzen[2] <= distanzen[1] +10) return 1; // Objekt ist groß, vermutlich Wand
			else if( distanzen[0] > distanzen[1] +10 && distanzen[2] <= distanzen[1] +10) return 2;		// Objekt ist groß, aber man könnte links vorbei
				else if(distanzen[0] <= distanzen[1] +10 && distanzen[2] > distanzen[1] +10) return 3;	// Objekt ist groß, aber man könnte rechts vorbei
					else if(distanzen[0] >= distanzen[1] +10 && distanzen[2] >= distanzen[1] +10) return 4;	//Objekt ist klein, vermutlich Ball
		
		return 0;  //Nichts traf zu ?:/ .... ( einfach um alles abzudecken ^^ )
		
	}
	
	public boolean isBall(double [] distanzen)
	{
		
		Initialize.pilot.setTravelSpeed(15);
		daisy.daisyInit.middleMotor.rotate(40);
		Initialize.pilot.travel(distanzen[1] -2);
		if(Initialize.sensorFront.getColorID() == SensorConstants.GREEN) return true;
		else 
			{
				Initialize.pilot.travel(-10);
				Initialize.pilot.setTravelSpeed(DriveMotors.oldSpeed);
				return false;
			}
		
	}
	
	public boolean isWall(double [] distanzen)
	{
		int checker=0;
		
		Initialize.pilot.travel(distanzen[1]);
		Initialize.pilot.forward();
		do
		{
			checker = Initialize.motors.checkRise();
			if(checker == 1) return false; //ist keine Wand, da checkRise()=1 -> war Steigung und wurde bewältigt
		}while (checker != -1 );
		return true;  //ist eine Wand, da checkRise()=-1 -> Blockade
	}

}
*/