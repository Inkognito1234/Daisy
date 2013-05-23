import lejos.nxt.Sound;
import lejos.util.Delay;

public class ultraSensor
{
	
	public double[] scanObject(int lastDist)
	{
		int count=16, distToSave=3;
		
		int[] dist = new int[count];
		
		double sum = 0, durchschnitt = 0, checkLeft = 0, checkRight = 0;
		
		double[] distanzen=new double[distToSave];
		
		for(int i=0; i<distToSave ; i++)
		{
			distanzen[i]=-1;
		}
		//System.out.println("Blubb: " + Math.atan(Daisy.daisyInit.BALLDURCHMESSER / lastDist) * 360/(2*Math.PI) +"\n");

		Daisy.daisyInit.pilot.stop();
		Delay.msDelay(75);
		
		//Berechnung der Entfernung nach vorne:
		
		for(int i=0; i < count; i++)
		{
			dist[i]=Daisy.daisyInit.sonicSensor.getDistance();
			Delay.msDelay(25);
			sum = sum + dist[i];
		}
		durchschnitt = sum / count;
		
		//Entscheidung, ob Messung richtig war (Abweichung +6 erlaubt)
		if( durchschnitt > lastDist+6) 
			{
				distanzen[1] = durchschnitt;
				System.out.println("Blubb: " + distanzen[0] +" "+ distanzen[1]+" "+ distanzen[2]+"\n");  //Ausgabe für Test
				//Button.waitForAnyPress();
				return distanzen; //Messung lastDist war falsch -> kein Objekt in der Nähe -> [0] und [2] = -1
			}
		else
		{
			Daisy.daisyInit.pilot.rotate(Math.atan(Daisy.daisyInit.BALLDURCHMESSER / durchschnitt) * 360/(2*Math.PI)); //Drehung um BALLDURCHMESSER nach links
			Delay.msDelay(50);

			sum=0;
			
			//Messung der Entfernung links vom Objekt:
			
			for(int i=0; i < count; i++)
			{
				dist[i]=Daisy.daisyInit.sonicSensor.getDistance();
				Delay.msDelay(25);
				sum = sum + dist[i];
			}
			checkLeft = sum / count;
			
			Daisy.daisyInit.pilot.rotate( -2* Math.atan(Daisy.daisyInit.BALLDURCHMESSER / durchschnitt) * 360/(2*Math.PI)); //Drehung um 2* BALLDURCHMESSER nach rechts
			Delay.msDelay(50);

			
			sum=0;
			
			//Messung der Entfernung rechts vom Objekt:
			
			for(int i=0; i < count; i++)
			{
				dist[i]=Daisy.daisyInit.sonicSensor.getDistance();
				Delay.msDelay(25);
				sum = sum + dist[i];
			}
			checkRight = sum / count;
			
			Daisy.daisyInit.pilot.rotate(Math.atan(Daisy.daisyInit.BALLDURCHMESSER / durchschnitt)* 360/(2*Math.PI)); //Drehung um BALLDURCHMESSER nach links um wieder Objekt anzuschauen

			distanzen[0]=checkLeft;
			distanzen[1]=durchschnitt;
			distanzen[2]=checkRight;
			System.out.println("Blubb: " + distanzen[0] +" "+ distanzen[1]+" "+ distanzen[2]+"\n"); //Ausgabe für Test
			return distanzen; 

		}
		
	}
	
	public int whatKind(double[] distanzen)
	{
		//falsche Messung:
		if ( distanzen[0] < 0 || distanzen[2] < 0) 
			{
				Sound.beepSequence();
				Sound.beepSequenceUp();

				return -1; 
			}
		
		// Objekt ist groß, vermutlich Wand:
		else if(distanzen[0] <= distanzen[1] +8 && distanzen[2] <= distanzen[1] +8) return 1; 
		
		// Objekt ist groß, aber man könnte links vorbei:
			else if( distanzen[0] > distanzen[1]+15 && distanzen[2] <= distanzen[1] +8) return 2;	
		
		// Objekt ist groß, aber man könnte rechts vorbei:
				else if(distanzen[0] <= distanzen[1] +8 && distanzen[2] > distanzen[1] +15) return 3;
		
		//Objekt ist klein, vermutlich Ball:
					else if(distanzen[0] > distanzen[1] +15 && distanzen[2] > distanzen[1] +15 ) return 4;	
		
		return 0;  //Nichts traf zu ?:/ .... ( einfach um alles abzudecken ^^ )
		
	}
	
	public boolean isBall(double [] distanzen)
	{
		
		Daisy.daisyInit.pilot.setTravelSpeed(15);
		Daisy.daisyInit.middleMotor.rotate(40); //Macht Greifarm auf
		Daisy.daisyInit.pilot.travel(distanzen[1], true);
		while(Daisy.daisyInit.pilot.isMoving())
		{
			if(Daisy.daisyInit.sensorFront.getColorID() == 1)
			{
				Daisy.daisyInit.pilot.stop();
				return true;
			}
		}			

		Daisy.daisyInit.pilot.travel(-distanzen[1]);
		Daisy.daisyInit.middleMotor.rotate(-40); //Schliesst Greifarm
		Daisy.daisyInit.pilot.setTravelSpeed(Daisy.motors.oldSpeed);
		return false;

		
	}
	
	
	public boolean isWall(double [] distanzen)
	{
	
		final double angleSensor = 42;  //Neigungswinkel des Sensors
		final int drive = 8, count = 16;
		double entf1 = 0, entf2 = 0, durchschnitt = 0, sum = 0;
		int[] dist = new int[count];
		
		//Objekt zu nah?
		if(0.3937* distanzen[1] < drive) 
			{
				//Zu nah, also etwas zuruecksetzen
				Daisy.daisyInit.pilot.travel(-1.1*drive);
				
				//Neue Messung notwendig
				for(int i=0; i < count; i++)
				{
					dist[i]=Daisy.daisyInit.sonicSensor.getDistance();
					Delay.msDelay(25);
					sum = sum + dist[i];
				}
				durchschnitt = sum / count;
				
				entf1 = durchschnitt;
			}
		else 
			entf1 = distanzen[1];
			
			sum = 0;
			
			Daisy.daisyInit.pilot.travel(drive);
				
			Delay.msDelay(500);

			for(int i=0; i < count; i++)
			{
				dist[i]=Daisy.daisyInit.sonicSensor.getDistance();
				Delay.msDelay(25);
				sum = sum + dist[i];
			}
			durchschnitt = sum / count;
				
			entf2 = durchschnitt;
				
			//Ist Entfernung bedingt durch Steigung anders als bei einer Wand?
			if(entf1 * Math.cos(angleSensor *  Math.PI / 180) - 0.3937 * drive < entf2 * Math.cos(angleSensor *  Math.PI / 180)) 
				return false;
			else 
				return true;
				
		
	}
	
	
	//Ausrichten falls Daisy schraeg an die Steigung angefahren kam
	void adjust(double[] distanzen)
	{
		//Links weiter weg als rechts -> Daisy kam von links auf die Steigung zu
		if(distanzen[0] < distanzen[2])
		{
			do
			{
				Daisy.daisyInit.pilot.rotate(10,true);
				
			}while(Daisy.daisyInit.sonicSensor.getDistance() > Daisy.daisyInit.sonicSensor.getDistance() && Daisy.daisyInit.sonicSensor.getDistance() < 22);
			
			Daisy.daisyInit.pilot.stop();
			Daisy.daisyInit.pilot.rotate(-2);
		}
		else
		{
			do
			{
				Daisy.daisyInit.pilot.rotate(-10,true);
				
			}while(Daisy.daisyInit.sonicSensor.getDistance() > Daisy.daisyInit.sonicSensor.getDistance() && Daisy.daisyInit.sonicSensor.getDistance() < 22 );
			
			Daisy.daisyInit.pilot.stop();
			Daisy.daisyInit.pilot.rotate(2);
		}
	
		//DISTANZ > 22 ? Soll was getan werden?
		
	}

	/* Alte Version -  gestrichen, da nicht funktioniert aufgrund durchdrehender Reifen

	public boolean isWall(double [] distanzen)
	{
		int checker=0;
		boolean poweredUp= false;
		Daisy.daisyInit.pilot.travel(distanzen[1]);
		Daisy.daisyInit.pilot.forward();
		Delay.msDelay(100);
		
		do
		{
			//counter++;
			checker = Daisy.motors.checkRise();
			switch(checker)
			{
			//Erhöhung der Geschw.
			case 3: poweredUp=true; continue;
			
			//ist keine Wand, da checkRise()=1 -> war Steigung und wurde bewältigt
			case 1: if(poweredUp) return false;
					else continue;
			
			}
			//if(counter >= 2* Daisy.daisyInit.blockadeNachVersuchen) checker = -1;
		}while (checker != -1 );
		return true;  //ist eine Wand, da checkRise()=-1 -> Blockade
	}

}
*/
	

}