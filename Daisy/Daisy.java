
public class Daisy 
{
	public static Initialize daisyInit= new Initialize();
	public static DriveMotors motors = new DriveMotors();
	
	public static void main(String[] args) 
	{
		daisyInit.init();

		while (true)
		{
			daisyInit.pilot.forward();
			//if(daisyInit.motors.checkRise()== -1) return;
			
			//daisy.checkSound();
			
			while (daisyInit.sonicSensor.getDistance() >= 24 )
			{
				System.out.println("Speed SET: " + daisyInit.pilot.getTravelSpeed() +"\n");
				System.out.println("Speed CUR: " + motors.getSpeed() +"\n");
				//daisy.checkSound();
				motors.checkRise();
			}
						
			//daisy.checkSound();
			daisyInit.pilot.stop();
			//daisy.checkSound();
			daisyInit.middleMotor.rotate(35);
			//daisy.checkSound();
			daisyInit.pilot.rotate(90);
			//daisy.checkSound();
			daisyInit.middleMotor.rotate(-35);
			//daisy.checkSound();
			
		}
	}
	
}
