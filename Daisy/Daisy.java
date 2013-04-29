import lejos.nxt.Sound;


public class Daisy 
{
	public static Initialize daisyInit= new Initialize();
	public static DriveMotors motors = new DriveMotors();
	public static ultraSensor objScanner=new ultraSensor();
	
	public static void main(String[] args) 
	{
		int art=0, dist=daisyInit.sonicSensor.getDistance();
		double[] distanzen=new double[3];
		
		daisyInit.init();

		while (true)
		{
			daisyInit.pilot.forward();
			
			do
			{
				//System.out.println("Speed SET: " + daisyInit.pilot.getTravelSpeed() +"\n");
				//System.out.println("Speed CUR: " + motors.getSpeed() +"\n");
				//motors.checkRise();
				
				dist=daisyInit.sonicSensor.getDistance();
				
			}while ( dist >= 35 );
				
			distanzen = objScanner.scanObject(dist);
			//System.out.println(distanzen[0] +" "+ distanzen[1]+" "+distanzen[2]+"\n");
			art = objScanner.whatKind(distanzen);
			System.out.println("ART: " + art +"\n");
			switch(art)
			{
				//falsche Messung-> Abbruch
				case -1: break;
				
				//Messung ergab großes Obj. -> untersuche auf Wand
				case  1: if(objScanner.isWall(distanzen)) Sound.beepSequence();
							else Sound.beepSequenceUp(); break;
							
				//Objekt ist groß, aber man könnte links vorbei
				case  2: daisyInit.pilot.rotate(Math.tan(distanzen[1] / (1.5* daisyInit.BALLDURCHMESSER) ) );
						 daisyInit.pilot.forward(); break;
				
				// Objekt ist groß, aber man könnte rechts vorbei:
				case  3: daisyInit.pilot.rotate( - Math.tan(distanzen[1] / (1.5* daisyInit.BALLDURCHMESSER) ) );
				 		 daisyInit.pilot.forward(); break;
		 		
				//Objekt ist klein, vermutlich Ball:
				case  4: if(objScanner.isBall(distanzen)) Sound.beepSequenceUp();
						 else Sound.beepSequence(); break;
						 
				//nichts traf zu ?:/ .... alles muss abgedeckt werden!
				default: break;
			}
			
			daisyInit.pilot.stop();
			
			//daisyInit.middleMotor.rotate(35);
			
			daisyInit.pilot.rotate(90);
			
			//daisyInit.middleMotor.rotate(-35);
			
			
		}
	}
	
}
