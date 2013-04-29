
public class daisy 
{

	static int rise=0;
	static Initialize daisyInit= new Initialize();
	
	public static void main(String[] args) 
	{
		//daisyInit.init();
		
		while (true)
		{
			Initialize.pilot.forward();
			if(Initialize.motors.checkRise()== -1) return;
			
			//daisy.checkSound();
			
			while (Initialize.sonicSensor.getDistance() >= 24 )
			{
				System.out.println("Speed SET: " + Initialize.pilot.getTravelSpeed() +"\n");
				System.out.println("Speed CUR: " + Initialize.motors.getSpeed() +"\n");
				//daisy.checkSound();
				rise = Initialize.motors.checkRise();
				if (rise == -1)return;

			}
			
			if (rise == -1) return;
			
			//daisy.checkSound();
			Initialize.pilot.stop();
			//daisy.checkSound();
			daisyInit.middleMotor.rotate(35);
			//daisy.checkSound();
			Initialize.pilot.rotate(90);
			//daisy.checkSound();
			daisyInit.middleMotor.rotate(-35);
			//daisy.checkSound();
			
		}
	}
	
}
