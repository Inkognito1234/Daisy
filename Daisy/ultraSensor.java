import lejos.nxt.SensorConstants;


public class ultraSensor
{
	int[] dist;
	
	int count=0, sum=0, durchschnitt=0, checkLeft=0, checkRight=0;
	
	public double[] scanObject(int lastDist)
	{
		
		double[] distanzen={-1};
		
		Daisy.daisyInit.sonicSensor.ping();
		count = Daisy.daisyInit.sonicSensor.getDistances(dist);
		for(int i=0; i < count; i++)
		{
			sum = sum + dist[i];
		}
		durchschnitt = sum / count;
		
		if( durchschnitt > lastDist+10) 
			{
				distanzen[1] = durchschnitt;
				Daisy.daisyInit.sonicSensor.continuous();
				return distanzen; //Messung lastDist war falsch -> kein Objekt in der Nähe -> [0] und [2] = -1
			}
		else
		{
			Daisy.daisyInit.pilot.rotate(Math.tan(durchschnitt/Daisy.daisyInit.BALLDURCHMESSER)); //Drehung um BALLDURCHMESSER nach links
			Daisy.daisyInit.sonicSensor.ping();
			count = Daisy.daisyInit.sonicSensor.getDistances(dist);
			for(int i=0; i < count; i++)
			{
				sum = sum + dist[i];
			}
			checkLeft = sum / count;
			
			Daisy.daisyInit.pilot.rotate( -2* Math.tan(durchschnitt/Daisy.daisyInit.BALLDURCHMESSER)); //Drehung um 2* BALLDURCHMESSER nach rechts
			
			Daisy.daisyInit.sonicSensor.ping();
			count = Daisy.daisyInit.sonicSensor.getDistances(dist);
			for(int i=0; i < count; i++)
			{
				sum = sum + dist[i];
			}
			checkRight = sum / count;
			
			Daisy.daisyInit.pilot.rotate(Math.tan(durchschnitt/Daisy.daisyInit.BALLDURCHMESSER)); //Drehung um BALLDURCHMESSER nach links um wieder Objekt anzuschauen

			distanzen[0]=checkLeft;
			distanzen[1]=durchschnitt;
			distanzen[2]=checkRight;
			Daisy.daisyInit.sonicSensor.continuous();
			
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
		
		Daisy.daisyInit.pilot.setTravelSpeed(15);
		Daisy.daisyInit.middleMotor.rotate(40);
		Daisy.daisyInit.pilot.travel(distanzen[1] -2);
		if(Daisy.daisyInit.sensorFront.getColorID() == SensorConstants.GREEN) return true;
		else 
			{
				Daisy.daisyInit.pilot.travel(-10);
				Daisy.daisyInit.pilot.setTravelSpeed(Daisy.motors.oldSpeed);
				return false;
			}
		
	}
	
	public boolean isWall(double [] distanzen)
	{
		int checker=0;
		
		Daisy.daisyInit.pilot.travel(distanzen[1]);
		Daisy.daisyInit.pilot.forward();
		do
		{
			checker = Daisy.motors.checkRise();
			if(checker == 1) return false; //ist keine Wand, da checkRise()=1 -> war Steigung und wurde bewältigt
		}while (checker != -1 );
		return true;  //ist eine Wand, da checkRise()=-1 -> Blockade
	}

}
