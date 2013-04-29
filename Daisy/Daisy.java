
public class Daisy 
{

	static int rise=0;
	static Initialize daisyInit= new Initialize();
	
	public static void main(String[] args) 
	{
		//daisyInit.init();
		
		while (true)
		{
			daisyInit.pilot.forward();
			if(daisyInit.motors.checkRise()== -1) return;
			
			//daisy.checkSound();
			
			while (daisyInit.sonicSensor.getDistance() >= 24 )
			{
				System.out.println("Speed SET: " + daisyInit.pilot.getTravelSpeed() +"\n");
				System.out.println("Speed CUR: " + daisyInit.motors.getSpeed() +"\n");
				//daisy.checkSound();
				rise = daisyInit.motors.checkRise();
				if (rise == -1)return;

			}
			
			if (rise == -1) return;
			
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
