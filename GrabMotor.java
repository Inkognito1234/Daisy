import lejos.robotics.navigation.Pose;


public class GrabMotor 
{

	//Greifzange komplett schließen und dann etwas oeffnen
	void adjustGrabber()
	{
		Daisy.daisyInit.middleMotor.rotate(-180, true);
		while(Daisy.daisyInit.middleMotor.isMoving())
		{
			
		}
		Daisy.daisyInit.middleMotor.stop();
		Daisy.daisyInit.middleMotor.rotate(40);
	}
	
	//fromUSS: Falls Ultraschallsensor das eingeleitet hat
	void collectBall(boolean fromUSS)
	{
		Pose ausgangsPkt = Daisy.poser.getPose();
		
		if(fromUSS)
		{
			Daisy.daisyInit.pilot.travel(-4);
			Daisy.daisyInit.middleMotor.rotate(-90, true);
			while(Daisy.daisyInit.middleMotor.isMoving())
			{
				
			}
			Daisy.daisyInit.middleMotor.stop();
		}
		else //Ballmarkierung erkannt noch bevor USS ein Ball meldet
		{
			int[] colors= new int[3];
			colors = Daisy.colorSens.getColors();
			int messung;
			//Linker Sensor hat erkannt
			if(colors[0] == 1)
			{
				//Fahre ein Stueck zurueck und drehe nach links bis fertig 
				//oder ein Obj. vom USS erkannt
				Daisy.daisyInit.pilot.travel(-15);
				Daisy.daisyInit.pilot.rotate(60, true);
				do
				{
						
					messung = Daisy.daisyInit.sonicSensor.getDistance();
						
				}while( messung > 21 && Daisy.daisyInit.pilot.isMoving());
				
				Daisy.daisyInit.pilot.stop();
				
				if(messung <= 21)
				{
					Daisy.daisyInit.pilot.travel(0.3937 * messung );
					Daisy.daisyInit.middleMotor.rotate(-90, true);
					while(Daisy.daisyInit.middleMotor.isMoving())
					{
						
					}
					Daisy.daisyInit.middleMotor.stop();
					return;
				}
				else 
				{
					//Falls kein Ball vom USS gefunden wurde, zurueck zum Ausgangspunkt
					//Stueck vor fahren, damit die Markierung nicht nochmal erkannt wird
					Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY(), ausgangsPkt.getHeading());
					Daisy.daisyInit.pilot.travel(15);
					return;
				}
			}
			else if(colors[1] == 1)
			{
				//Fahre ein Stueck zurueck und drehe nach rechts bis fertig 
				//oder ein Obj. vom USS erkannt
				Daisy.daisyInit.pilot.travel(-10);
				Daisy.daisyInit.pilot.rotate(-40, true);
				do
				{
							
					messung = Daisy.daisyInit.sonicSensor.getDistance();
							
				}while(messung > 21 && Daisy.daisyInit.pilot.isMoving());
				
				Daisy.daisyInit.pilot.stop();
				
				if(Daisy.daisyInit.sonicSensor.getDistance() <= 21)
				{
					Daisy.daisyInit.pilot.travel(0.3937 * messung );
					Daisy.daisyInit.middleMotor.rotate(-90, true);
					while(Daisy.daisyInit.middleMotor.isMoving())
					{
						
					}
					Daisy.daisyInit.middleMotor.stop();
					return;
				}
				else 
				{
					//Falls kein Ball vom USS gefunden wurde, Drehung in die andere Richtung
					
					Daisy.daisyInit.pilot.rotate(80, true);
					do
					{
								
						messung = Daisy.daisyInit.sonicSensor.getDistance();
						
					}while(messung > 21 && Daisy.daisyInit.pilot.isMoving());
					
					Daisy.daisyInit.pilot.stop();
					
					if(messung <= 21)
					{
						Daisy.daisyInit.pilot.travel(0.3937 * messung );
						Daisy.daisyInit.middleMotor.rotate(-90, true);
						while(Daisy.daisyInit.middleMotor.isMoving())
						{
							
						}
						Daisy.daisyInit.middleMotor.stop();
						return;
					}
					else
					{
						//Kein Ball vorhanden
						Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY(), ausgangsPkt.getHeading());
						Daisy.daisyInit.pilot.travel(15);
						return;
					}
				}
				
			}
			else if(colors[2] == 1)
			{
				//Fahre ein Stueck zurueck und drehe nach rechts bis fertig 
				//oder ein Obj. vom USS erkannt
				Daisy.daisyInit.pilot.travel(-20);
				Daisy.daisyInit.pilot.rotate(-60, true);
				do
				{
							
					messung = Daisy.daisyInit.sonicSensor.getDistance();
					
				}while(messung > 21 && Daisy.daisyInit.pilot.isMoving());
				
				Daisy.daisyInit.pilot.stop();
				
				if(Daisy.daisyInit.sonicSensor.getDistance() <= 21)
				{
					Daisy.daisyInit.pilot.travel(0.3937 * messung );
					Daisy.daisyInit.middleMotor.rotate(-90, true);
					while(Daisy.daisyInit.middleMotor.isMoving())
					{
						
					}
					Daisy.daisyInit.middleMotor.stop();
					return;
				}
				else 
				{
					//Falls kein Ball vom USS gefunden wurde, zurueck zum Ausgangspunkt
					//Stueck vor fahren, damit die Markierung nicht nochmal erkannt wird
					Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY(), ausgangsPkt.getHeading());
					Daisy.daisyInit.pilot.travel(15);
					return;
				}
			}
		}
	}
	
	void deliverBall()
	{
		//Faehrt zum Anfang, dreht sich um, laesst Ball etwas weiter vorne fallen, 
		//faehrt zum Anfang und richtet seine Zange wieder aus
		Daisy.motors.driveBack();
		Daisy.daisyInit.pilot.rotate(180);
		Daisy.daisyInit.pilot.travel(20);
		Daisy.daisyInit.middleMotor.rotate(40);
		Daisy.daisyInit.pilot.travel(-20);
		Daisy.daisyInit.pilot.rotate(-180);
		this.adjustGrabber();
		
	}
}
