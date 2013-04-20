import lejos.util.Delay;

public class DriveMotors {
	
	static final  double oldSpeed = daisy.daisy.driveSpeed;
	static double newSpeed = oldSpeed;
	
	//eigene Geschw.-berechnung, weil vorhandene Finktionen nicht funktionierten :-)
	public double getSpeed()
	{
		int msDelay=500;
		int rightTacho1 = daisy.daisy.rightMotor.getTachoCount();
		Delay.msDelay(msDelay);
		int rightTacho2 = daisy.daisy.rightMotor.getTachoCount();
		int leftTacho1 = daisy.daisy.leftMotor.getTachoCount();
		Delay.msDelay(msDelay);
		int leftTacho2 = daisy.daisy.leftMotor.getTachoCount();
		
		double speed=0, rightSpeed, leftSpeed, diffMotorEinheit=20.52;
		
		rightSpeed = (( rightTacho2 - rightTacho1 ) * 1000/msDelay) / diffMotorEinheit; //Umrechnung der Geschw.
		
		leftSpeed = (( leftTacho2 - leftTacho1 ) * 1000/msDelay) / diffMotorEinheit; //Umrechnung der Geschw.
		
		if(leftSpeed == rightSpeed) speed = rightSpeed ;
		else speed = Math.min(leftSpeed, rightSpeed) ; //gibt den lansameren Wert zurück
				
		return speed;
	}
	
	public void checkRise()
	{

		if( ( newSpeed*2 ) /3 > this.getSpeed() ) 
		{	
			newSpeed =  Math.min( ( newSpeed * 5 ) /3, 50);
			daisy.daisy.pilot.setTravelSpeed(newSpeed);
			
		}
		else if(daisy.daisy.pilot.getTravelSpeed() != oldSpeed && this.getSpeed() > oldSpeed) 
			 {
				 daisy.daisy.pilot.setTravelSpeed(oldSpeed);
				 newSpeed = oldSpeed;
			 } 
				
	}
	
	
}
