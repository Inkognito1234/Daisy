
import java.util.Random;

import lejos.robotics.navigation.Pose;
import lejos.util.Delay;

public class DriveMotors
{
	
	//public DriveMotors motors = new DriveMotors();

	//final double oldSpeed = Daisy.daisyInit.driveSpeed;
	//double newSpeed = oldSpeed;
	//boolean poweredUp = false;						//gibt an ob man die Geschw. erhöht hat
	//int counter=0; 									//zählt die Anzahl von checkRise aufrufen (zur Blockadeerkennung)
	
	int[] colors = new int[3]; 
	boolean complete = true;

	
	
	
	//eigene Geschw.-berechnung, weil vorhandene Finktionen nicht funktionierten :-)
	public double getSpeed()
	{
		
		int msDelay=500;
		int rightTacho1 = Daisy.daisyInit.rightMotor.getTachoCount();
		Delay.msDelay(msDelay);
		int rightTacho2 = Daisy.daisyInit.rightMotor.getTachoCount();
		
		/*  nicht nötig da beim Fahren beide Motoren möglichst gleich schnell sein sollten
		 * 
		int leftTacho1 = daisy.daisy.leftMotor.getTachoCount();
		Delay.msDelay(msDelay);
		int leftTacho2 = daisy.daisy.leftMotor.getTachoCount();
		*
		*/
		//double speed=0, rightSpeed, leftSpeed, diffMotorEinheit=20.52; //diffMotorEinheit = Tachoinkrement des Motors pro s bei Geschw.=1
		
		double rightSpeed, diffMotorEinheit = 20.52; //diffMotorEinheit = Tachoinkrement des Motors pro s bei Geschw.=1
		
		rightSpeed = (( rightTacho2 - rightTacho1 ) * 1000/msDelay) / diffMotorEinheit; //Umrechnung der Geschw.
		
		//leftSpeed = (( leftTacho2 - leftTacho1 ) * 1000/msDelay) / diffMotorEinheit; //Umrechnung der Geschw.
		
		//if(leftSpeed == rightSpeed) speed = rightSpeed ;
		//else speed = Math.min(leftSpeed, rightSpeed) ; //gibt den lansameren Wert zurück
				
		//return speed;
		return Math.max(rightSpeed, 0.1);  //rightSpeed = leftSpeed = speed , da beide Motoren gleich angesteuert werden
	}
	
	/**
	* prüft auf Steigung; eventuelle Blockade nach vorher definierter Anzahl an Aufrufen
	* @return1	 Wert= 0: Alles normal = keine Steigung oder Blockade
	* @return2	 Wert=-1: ist eine Blockade, da Anzahl an Aufrufen erreicht
	* @return3	 Wert= 1: Steigung bewältigt, alles wieder auf Anfang
	* @return4 	 Wert= 2: Weiterhin auf Steigung; keine weitere Erhöhung der Geschw.
	* @return5	 Wert= 3: (weiterhin) Steigung mit Erhöhung der Geschw. 
	* 
	*/
	/*
	public int checkRise() //funktioniert nicht wie gewollt, da Reifen durchdrehen
	{

		if( oldSpeed * Math.min(Daisy.daisyInit.riseTreshold, 0.999) > this.getSpeed() ) 
		{	
			newSpeed =  Math.min(  newSpeed * Math.max(Daisy.daisyInit.risePowerUp, 1.01), 43);  //erhöht die Geschw. , aber nicht höher als 43
			Daisy.daisyInit.pilot.setTravelSpeed(newSpeed);
			poweredUp = true;
			
			counter++;
			if(counter >= Daisy.daisyInit.blockadeNachVersuchen)
			{
				Daisy.daisyInit.pilot.setTravelSpeed(oldSpeed);
				newSpeed = oldSpeed;
				counter = 0;
				poweredUp = false;
				return -1;
			}
			return 3;	// (weiterhin) Steigung, aber mit Erhöhung der Geschw.
			
		}
		else if(this.getSpeed() > oldSpeed * Daisy.daisyInit.riseTreshold) 
			 {
			Daisy.daisyInit.pilot.setTravelSpeed(oldSpeed);
				if(this.checkRise() == 3) return 3;
				newSpeed = oldSpeed;
				counter = 0;
				poweredUp = false;
				return 1;
			 } 
		if(poweredUp) 
			{
				counter++;
				if(counter >= Daisy.daisyInit.blockadeNachVersuchen)
				{
					Daisy.daisyInit.pilot.setTravelSpeed(oldSpeed);
					newSpeed = oldSpeed;
					counter = 0;
					poweredUp = false;
					return -1;
				}
				else return 2;
			}
		else return 0;
				
	}
	*/
	//----------------------------------------------------------------------
	
	void driveRise()
	{
		boolean gewackel = false;
		Pose ausgangsPkt = Daisy.poser.getPose();
		float traveled = 0;
		
		Daisy.daisyInit.pilot.travel(-8);
		Daisy.daisyInit.pilot.rotate(180);
		Daisy.daisyInit.pilot.travel(-100, true);
		while(Daisy.daisyInit.pilot.isMoving())
		{
			//bei Steigung kommt der Boden nah an Sensor (Erwartung)
			colors = Daisy.colorSens.getColors();
			if(colors[0] == 7)
			{
				traveled = traveled + Daisy.daisyInit.pilot.getMovementIncrement();
				Daisy.daisyInit.pilot.stop();
				Daisy.daisyInit.pilot.travel(7);
				Daisy.daisyInit.pilot.rotate(45);
				Daisy.daisyInit.pilot.travel(-100 + traveled -7);
				
			}
			if(colors[2] == 7)
			{
				traveled = traveled + Daisy.daisyInit.pilot.getMovementIncrement();
				Daisy.daisyInit.pilot.stop();
				Daisy.daisyInit.pilot.travel(7);
				Daisy.daisyInit.pilot.rotate(-45);
				Daisy.daisyInit.pilot.travel(-100 + traveled -7);
				
			}
			if(Daisy.daisyInit.sonicSensor.getDistance() < 21)
				gewackel =true;
		}
		
		if(gewackel)
		{
			Daisy.daisyInit.pilot.rotate(180);
			//forward() ?
			return;
		}
		else  //kein Gewackel -> gegen eine Wand gefahren anstatt Steigung 
			//-> position nun falsch interpretiert -> versuchen zu korrigieren
		{
			Daisy.daisyInit.pilot.travel(8);
			Daisy.daisyInit.pilot.rotate(-180);
			Daisy.poser.setPose(ausgangsPkt);
			Daisy.daisyInit.pilot.travel(-10);			
			//forward() ?
		}		
	}
	
	//----------------------------------------------------------------------

	void driveBack()
	{
		Random ran = new Random();
		int turn = ran.nextInt(2);
		
		Daisy.nav.goTo(0, 0);
		while(!Daisy.nav.pathCompleted())// && Daisy.daisyInit.pilot.isMoving())
		{
			if(Daisy.daisyInit.sonicSensor.getDistance() < 22 )
			{
				Daisy.daisyInit.pilot.stop();
				Daisy.daisyInit.pilot.rotate( Math.pow(-1, turn) * 90);
				Daisy.daisyInit.pilot.travel(20);

				Daisy.nav.goTo(0, 0);
			}
		}
		
		Daisy.daisyInit.pilot.rotate(- Daisy.poser.getPose().getHeading());
		
	}
	
	//----------------------------------------------------------------------

	void avoidNearObj(int art, double[] distanzen)
	{
		Random ran=new Random();
		int turn;
		Pose ausgangsPkt = Daisy.poser.getPose();
		colors = Daisy.colorSens.getColors();
		complete = true;
		
		switch(art)
		{
			case -1: 
				//falsche Messung-> Abbruch
				return;
						
			case  1: 
				//Messung ergab großes Obj. -> untersuche auf Wand
				if(Daisy.objScanner.isWall(distanzen))
				{
					turn=ran.nextInt(2);
					//random link/rechts drehen (0 - links , 1 - rechts)
					Daisy.daisyInit.pilot.rotate( Math.pow(-1, turn) * 90); // negativ = Rechtsdrehung
					
					//Ist Weg frei?
					if(Daisy.daisyInit.sonicSensor.getDistance() > 22)
					{
						Daisy.daisyInit.pilot.travel(15, true);
						while(Daisy.daisyInit.pilot.isMoving() )
						{
							if( Daisy.daisyInit.sensorLeft.getColorID() != 6 && Daisy.daisyInit.sensorFront.getColorID() == 1 && Daisy.daisyInit.sensorRight.getColorID() == 1 )
								break;
						}
						Daisy.daisyInit.pilot.stop();
						if(Daisy.daisyInit.pilot.getMovementIncrement() <= 13)
						{
							Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY());
							while(!Daisy.nav.pathCompleted())
							{
								
							}
							Daisy.daisyInit.pilot.rotate(ausgangsPkt.getHeading() - Daisy.poser.getPose().getHeading());
						}
						else
						{
							Daisy.daisyInit.pilot.rotate( -1 * Math.pow(-1, turn) * 90);
							return;
						}
					}
					
					Daisy.daisyInit.pilot.rotate( -1 * Math.pow(-1, turn) * 90);
						
					//Ist Weg frei?
					if(Daisy.daisyInit.sonicSensor.getDistance() > 22)
					{
						Daisy.daisyInit.pilot.travel(15, true);
						while(Daisy.daisyInit.pilot.isMoving() )
						{
							if( Daisy.daisyInit.sensorLeft.getColorID() == 1 && Daisy.daisyInit.sensorFront.getColorID() != 6 && Daisy.daisyInit.sensorRight.getColorID() == 1 )
								break;
						}
						Daisy.daisyInit.pilot.stop();
						if(Daisy.daisyInit.pilot.getMovementIncrement() <= 13)
						{
							//Man kam links und rechts nicht vorbei
							//Ab zum ausgangspunkt und umdrehen
							Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY());
							while(!Daisy.nav.pathCompleted())
							{
								
							}
							Daisy.daisyInit.pilot.rotate(ausgangsPkt.getHeading() - Daisy.poser.getPose().getHeading());
							Daisy.daisyInit.pilot.rotate(180);
							return;
						}
						else
							Daisy.daisyInit.pilot.rotate( Math.pow(-1, turn) * 90);
						return;
					}
						
					
				}
				
				//ist Steigung
				else
				{
					Daisy.objScanner.adjust(distanzen);
					Daisy.motors.driveRise();
					return;
				}
					
				break;
			
			case  2: 
				//Objekt ist groß, aber man könnte links vorbei
				Daisy.daisyInit.pilot.rotate(Math.atan( (distanzen[1] / (1.5* Daisy.daisyInit.BALLDURCHMESSER)) * 180/Math.PI) , true);
				while(Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1)
				{
					colors = Daisy.colorSens.getColors();

					if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
					{
						Daisy.daisyInit.pilot.stop();
						complete = false;
						if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
						{
							Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
							Daisy.grabber.deliverBall();
							return;
						}
						break;
					}

				}
				
				Daisy.daisyInit.pilot.stop();
				
				//Konnten Links nicht vorbei, da eine Linie im Weg-> ein Stueck nach rechts
				if(!complete)
				{	 
					complete = true;

					tryRight(ausgangsPkt);
					return;
						
				}
				else
				{
					Daisy.daisyInit.pilot.travel(18, true);
					while(Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1 && Daisy.daisyInit.sonicSensor.getDistance() > 22)
					{
						colors = Daisy.colorSens.getColors();

						if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
						{
							Daisy.daisyInit.pilot.stop();
							complete = false;
							if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
							{
								Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
								Daisy.grabber.deliverBall();
								return;
							}
						break;
						}	
					}
					
					Daisy.daisyInit.pilot.stop();
					
					//Falls waehrend der Fahrt wieder eine Linie UND man dadruch nicht weit genug kam
					//Zum Ausgangspunkt, umdrehen und weiter.
					if(!complete && Daisy.daisyInit.pilot.getMovementIncrement() <= 15)
					{
						complete = true;

						tryRight(ausgangsPkt);
						return;
	
					}
					else
					{
						Daisy.daisyInit.pilot.rotate(- Math.atan( (distanzen[1] / (1.5* Daisy.daisyInit.BALLDURCHMESSER)) * 180/Math.PI));
						//forward() ?
						return;
					}
						
				}
				
			
			
			case  3: 
				// Objekt ist groß, aber man könnte rechts vorbei:
				
				Daisy.daisyInit.pilot.rotate(- Math.atan( (distanzen[1] / (1.5* Daisy.daisyInit.BALLDURCHMESSER)) * 180/Math.PI) , true);
				while(Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1)
				{
					colors = Daisy.colorSens.getColors();

					if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
					{
						Daisy.daisyInit.pilot.stop();
						complete = false;
						if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
						{
							Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
							Daisy.grabber.deliverBall();
							return;
						}
						break;
					}
				}
				
				Daisy.daisyInit.pilot.stop();
				
				//Konnten Rechts nicht vorbei, da eine Linie im Weg-> ein Stueck nach rechts
				if(!complete)
				{	 
					complete = true;

					tryLeft(ausgangsPkt);
					return;
						
				}
				else
				{
					Daisy.daisyInit.pilot.travel(18, true);
					while(Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1 && Daisy.daisyInit.sonicSensor.getDistance() > 22)
					{
						colors = Daisy.colorSens.getColors();

						if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
						{
							Daisy.daisyInit.pilot.stop();
							complete = false;
							if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
							{
								Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
								Daisy.grabber.deliverBall();
								return;
							}
							break;
						}
					}
					
					Daisy.daisyInit.pilot.stop();
					
					//Falls waehrend der Fahrt wieder eine Linie UND man dadruch nicht weit genug kam
					//Zum Ausgangspunkt, umdrehen und weiter.
					if(!complete && Daisy.daisyInit.pilot.getMovementIncrement() <= 15)
					{
						complete = true;

						tryLeft(ausgangsPkt);
						return;
	
					}
					else
					{
						Daisy.daisyInit.pilot.rotate(Math.atan( (distanzen[1] / (1.5* Daisy.daisyInit.BALLDURCHMESSER)) * 180/Math.PI));
						//forward() ?
						return;
					}
						
				}
					 		
			
			case  4: 
				//Objekt ist klein, vermutlich Ball:
				if(Daisy.objScanner.isBall(distanzen)) 
				{
					Daisy.grabber.collectBall(true); //true, weil Ultraschallsensor vorher ein Objekt erkannt hat
					Daisy.grabber.deliverBall();
					return;
				}
				else
				{
					//Versuchen Links vorbei zu kommen
					Daisy.daisyInit.pilot.rotate(90, true);
					while(Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1)
					{
						colors = Daisy.colorSens.getColors();

						if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
						{
							Daisy.daisyInit.pilot.stop();
							complete = false;
							if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
							{
								Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
								Daisy.grabber.deliverBall();
								return;
							}
							break;
						}
					}
					
					Daisy.daisyInit.pilot.stop();
					
					if(!complete)
					{
						complete = true;
						
						tryRight(ausgangsPkt);
						return;
						
					}
					else
					{
						Daisy.daisyInit.pilot.travel(20, true);
						while (Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1 && Daisy.daisyInit.sonicSensor.getDistance() > 21) 
						{
							colors = Daisy.colorSens.getColors();

							if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
							{
								Daisy.daisyInit.pilot.stop();
								complete = false;
								if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
								{
									Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
									Daisy.grabber.deliverBall();
									return;
								}
								break;
							}
						}

						Daisy.daisyInit.pilot.stop();

						// Falls waehrend der Fahrt wieder eine Linie UND man dadruch nicht weit
						// genug kam
						// Zum Ausgangspunkt, umdrehen und weiter.
						if (!complete && Daisy.daisyInit.pilot.getMovementIncrement() <= 15) 
						{
							Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY());
							while(!Daisy.nav.pathCompleted())
							{
								
							}
							Daisy.daisyInit.pilot.rotate(ausgangsPkt.getHeading() - Daisy.poser.getPose().getHeading());
							Daisy.daisyInit.pilot.rotate(180);
							// forward() ?
							return;
						} 
						else 
						{
							Daisy.daisyInit.pilot.rotate(-90);
							// forward() ?
							return;
						}
					}
				}
					 
			//nichts traf zu ?:/ .... alles muss abgedeckt werden!
			default: //SOLL WAS UNTERNOMMEN WERDEN?!
				break;
		}
	}
	
	//----------------------------------------------------------------------

	void tryRight(Pose ausgangsPkt)
	{
		Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY());
		while(!Daisy.nav.pathCompleted())
		{
			
		}
		Daisy.daisyInit.pilot.rotate(ausgangsPkt.getHeading() - Daisy.poser.getPose().getHeading());
		Daisy.daisyInit.pilot.rotate(-90);
		Daisy.daisyInit.pilot.travel(20, true);
		while(Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1 && Daisy.daisyInit.sonicSensor.getDistance() > 21)
		{
			colors = Daisy.colorSens.getColors();

			if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
			{
				Daisy.daisyInit.pilot.stop();
				complete = false;
				if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
				{
					Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
					Daisy.grabber.deliverBall();
					return;
				}
				break;
			}
		}
		
		Daisy.daisyInit.pilot.stop();
		
		//Falls waehrend der Fahrt wieder eine Linie UND man dadruch nicht weit genug kam
		//Zum Ausgangspunkt, umdrehen und weiter.
		if(!complete && Daisy.daisyInit.pilot.getMovementIncrement() <= 15)
		{
			Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY());
			while(!Daisy.nav.pathCompleted())
			{
				
			}
			Daisy.daisyInit.pilot.rotate(ausgangsPkt.getHeading() - Daisy.poser.getPose().getHeading());
			Daisy.daisyInit.pilot.rotate(180);
			// forward() ?
			return;
		}
		else
		{
			Daisy.daisyInit.pilot.rotate(90);
			//forward() ?
			return;
		}
	}

//----------------------------------------------------------------------

	void tryLeft(Pose ausgangsPkt) 
	{

		Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY());
		while(!Daisy.nav.pathCompleted())
		{
			
		}
		Daisy.daisyInit.pilot.rotate(ausgangsPkt.getHeading() - Daisy.poser.getPose().getHeading());
		Daisy.daisyInit.pilot.rotate(90);
		Daisy.daisyInit.pilot.travel(20, true);
		while (Daisy.daisyInit.pilot.isMoving() && colors[0] != 1 && colors[1] == 6 && colors[2] != 1 && Daisy.daisyInit.sonicSensor.getDistance() > 21) 
		{
			colors = Daisy.colorSens.getColors();
			/*if(colors[0]==7)
			{
				Daisy.leftSensed = true;
				Daisy.rightSensed = false;
			}
			if(colors[2]==7)
			{
				Daisy.leftSensed = false;
				Daisy.rightSensed = true;
			}*/
			if(colors[1] != 6 || colors[0] == 1 || colors[2] == 1)
			{
				Daisy.daisyInit.pilot.stop();
				complete = false;
				if(colors[0] == 1 || colors[1] == 1 || colors[2] == 1)
				{
					Daisy.grabber.collectBall(false); //AUFRUF DER BALLEINSAMMEL-FUNKTION!
					Daisy.grabber.deliverBall();
					return;
				}
				break;
			}
		}

		Daisy.daisyInit.pilot.stop();

		// Falls waehrend der Fahrt wieder eine Linie UND man dadruch nicht weit
		// genug kam
		// Zum Ausgangspunkt, umdrehen und weiter.
		if (!complete && Daisy.daisyInit.pilot.getMovementIncrement() <= 15) 
		{
			Daisy.nav.goTo(ausgangsPkt.getX(), ausgangsPkt.getY());
			while(!Daisy.nav.pathCompleted())
			{
				
			}
			Daisy.daisyInit.pilot.rotate(ausgangsPkt.getHeading() - Daisy.poser.getPose().getHeading());
			Daisy.daisyInit.pilot.rotate(180);
			// forward() ?
			return;
		} 
		else 
		{
			Daisy.daisyInit.pilot.rotate(-90);

			return;
		}
	}
}


