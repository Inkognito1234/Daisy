
public class daisy 
{

	static int rise=0;
	static Initialize daisy= new Initialize();
	static DriveMotors motors=new DriveMotors();
	
	public static void main(String[] args) 
	{
		daisy.init();
		
		while (true)
		{
			daisy.pilot.forward();
			if(motors.checkRise()== -1) return;
			
			//daisy.checkSound();
			
			while (daisy.sonicSensor.getDistance() >= 24 )
			{
				System.out.println("Speed SET: " + daisy.pilot.getTravelSpeed() +"\n");
				System.out.println("Speed CUR: " + motors.getSpeed() +"\n");
				//daisy.checkSound();
				rise = motors.checkRise();
				if (rise == -1)return;

			}
			
			if (rise == -1) return;
			
			//daisy.checkSound();
			daisy.pilot.stop();
			//daisy.checkSound();
			daisy.middleMotor.rotate(35);
			//daisy.checkSound();
			daisy.pilot.rotate(90);
			//daisy.checkSound();
			daisy.middleMotor.rotate(-35);
			//daisy.checkSound();
			
		}
	}
	
}
