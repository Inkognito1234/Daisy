import lejos.util.Delay;

public class DriveMotors {
	
	static final  double oldSpeed = daisy.daisy.driveSpeed;
	static double newSpeed = oldSpeed;
	static boolean poweredUp = false;						//gibt an ob man die Geschw. erhöht hat
	static int counter=0; 									//zählt die Anzahl von checkRise aufrufen (zur Blockadeerkennung)
	
	//eigene Geschw.-berechnung, weil vorhandene Finktionen nicht funktionierten :-)
	public double getSpeed()
	{
		int msDelay=500;
		int rightTacho1 = daisy.daisy.rightMotor.getTachoCount();
		Delay.msDelay(msDelay);
		int rightTacho2 = daisy.daisy.rightMotor.getTachoCount();
		
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
		return rightSpeed;  //rightSpeed = leftSpeed = speed , da beide Motoren gleich angesteuert werden
	}
	
	/**
	* prüft auf Steigung; eventuelle Blockade nach vorher definierter Anzahl an Aufrufen
	* @return1	 Wert= 0: Alles normal = keine Steigung oder Blockade
	* @return2	 Wert=-1: ist eine Blockade, da Anzahl an Aufrufen erreicht
	* @return3	 Wert= 1: Steigung bewältigt, alles wieder auf Anfang
	* @return4 	 Wert= 2: Weiterhin auf Steigung; keine weitere Erhöhung der Geschw.
	* 
	*/
	public int checkRise()
	{

		if( newSpeed * Math.min(daisy.daisy.riseTreshold, 0.99) > this.getSpeed() ) 
		{	
			newSpeed =  Math.min(  newSpeed * Math.max(daisy.daisy.risePowerUp, 1.01), 50);  //erhöht die Geschw. , aber nicht höher als 50
			daisy.daisy.pilot.setTravelSpeed(newSpeed);
			poweredUp = true;
			
			counter++;
			if(counter >= daisy.daisy.blockadeNachVersuchen)
			{
				daisy.daisy.pilot.setTravelSpeed(oldSpeed);
				newSpeed = oldSpeed;
				counter = 0;
				poweredUp = false;
				return -1;
			}
			
		}
		else if(daisy.daisy.pilot.getTravelSpeed() != oldSpeed && this.getSpeed() > oldSpeed) 
			 {
				 daisy.daisy.pilot.setTravelSpeed(oldSpeed);
				 newSpeed = oldSpeed;
				 counter = 0;
				 poweredUp = false;
				 return 1;
			 } 
		if(poweredUp) return 2;
		else return 0;
				
	}
	
	
}
