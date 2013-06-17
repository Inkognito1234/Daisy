import java.util.Random;


public class ColorSensor 
{
	
	public int[] getColors()
	{
		int[] colors = new int[3];
		
		colors[0] = Daisy.daisyInit.sensorLeft.getColorID();
		colors[1] = Daisy.daisyInit.sensorFront.getColorID();
		colors[2] = Daisy.daisyInit.sensorRight.getColorID();
		
		return colors;
	}
	
	boolean[] warsch = new boolean[3];
	
	//-------------------------------------------------------
	
	public boolean warscheinlichkeit(boolean left)
	{

		warsch[2] = warsch[1];
		warsch[1] = warsch[0];
		warsch[0] = left;
		
		if(warsch[0] == false && warsch[1] == true && warsch[2] == false) {
			warsch[0] = false;
			return true;
			
		}
		
		if(warsch[0] == true && warsch[1] == false && warsch[2] == true) {
			warsch[0] = true;
			return true;
		}
		
		return false;
		
	}
	
	
	public void checkTrack()
	{
		boolean isballMark=false;
		int dodge = 45;
		
		Random rand = new Random();
		//turn = 0 -> Rechtsdrehung; 1 -> Linksdrehung
		//int turn = rand.nextInt(2); 
		
		Daisy.daisyInit.pilot.stop();
		
		//Pruefen ob Ballmarkierung
		for(int i=0 ; i<3 ; i++)
		{
			if(isBallmark(i))
			{
				isballMark=true;
				break;
			}
		}
		
		if(isballMark)
		{
			Daisy.grabber.collectBall(false); //false, da USS es nicht ausgeloest hat
			Daisy.grabber.deliverBall();
			return;
		}
		
		Daisy.daisyInit.pilot.travel(-7);
		
		Daisy.daisyInit.pilot.rotate(90,true);
		while(Daisy.daisyInit.pilot.isMoving())
		{
			if(Daisy.daisyInit.sensorFront.getColorID() == 7)
			{
				Daisy.leftSensed = true;	
			}
		}
		if(!Daisy.leftSensed)
		{
			Daisy.daisyInit.pilot.travel(13, true);
			while(Daisy.daisyInit.pilot.isMoving())
			{
				if(Daisy.daisyInit.sensorFront.getColorID() == 7)
				{
					Daisy.leftSensed = true;	
				}
			}
			
			Daisy.daisyInit.pilot.travel(-13);
				
		}
		
		Daisy.daisyInit.pilot.rotate(-90);
		Daisy.daisyInit.pilot.rotate(-90, true);
		while(Daisy.daisyInit.pilot.isMoving())
		{
			if(Daisy.daisyInit.sensorFront.getColorID() == 7)
			{
				Daisy.rightSensed = true;	
			}
		}
		
		if(!Daisy.rightSensed)
		{
			Daisy.daisyInit.pilot.travel(13, true);
			while(Daisy.daisyInit.pilot.isMoving())
			{
				if(Daisy.daisyInit.sensorFront.getColorID() == 7)
				{
					Daisy.rightSensed = true;	
				}
			}
			Daisy.daisyInit.pilot.travel(-13);
		}
		
		Daisy.daisyInit.pilot.rotate(90);

		if(!Daisy.rightSensed && !Daisy.leftSensed)
		{
			Daisy.sector.checkSector();
			//Daisy.daisyInit.pilot.rotate(Math.pow(-1, rand.nextInt(2) ) *dodge );
		}
		
		if(Daisy.rightSensed && !Daisy.leftSensed)
		{
			Daisy.daisyInit.pilot.rotate(dodge);
		}
		
		if(!Daisy.rightSensed && Daisy.leftSensed)
		{
			Daisy.daisyInit.pilot.rotate(-dodge);
		}
		
		if(Daisy.rightSensed && Daisy.leftSensed)
		{
			//Daisy.sector.checkSector();
			Daisy.daisyInit.pilot.rotate(Math.pow(-1, rand.nextInt(2)) * 3 *dodge);
		}
		
		Daisy.leftSensed = false;
		Daisy.rightSensed = false;
		return;
	}
		
		/*
		if (Daisy.daisyInit.pilot.getMovementIncrement() < 6)
		{
			if(warsch[0])
				Daisy.daisyInit.pilot.rotate(dodge);
			else
				Daisy.daisyInit.pilot.rotate(-dodge);
			
			return;

		}
		if ( turn == 0)
		{
			if(warscheinlichkeit(false))//false bedeutet Rechtsabbiegung
				Daisy.daisyInit.pilot.rotate(-Math.pow(-1, turn) * dodge);
			else
				Daisy.daisyInit.pilot.rotate(Math.pow(-1, turn) * dodge);
		}
		else
		{
			if(warscheinlichkeit(true))//false bedeutet Linksabbiegung
				Daisy.daisyInit.pilot.rotate(-Math.pow(-1, turn) * dodge);
			else
				Daisy.daisyInit.pilot.rotate(Math.pow(-1, turn) * dodge);
		}
		
		return;
	}*/
		/*
		if (Daisy.leftSensed) 
		{
			if(warscheinlichkeit(false))
				Daisy.daisyInit.pilot.rotate(-dodge);
			else
				Daisy.daisyInit.pilot.rotate(dodge);
			//Daisy.daisyInit.pilot.travel(5);
			//Daisy.daisyInit.pilot.rotate(90);
			
		} 
		else if (Daisy.rightSensed) 
		{
			if(warscheinlichkeit(true))
				Daisy.daisyInit.pilot.rotate(-dodge);
			else
				Daisy.daisyInit.pilot.rotate(dodge);
			//Daisy.daisyInit.pilot.travel(5);
			//Daisy.daisyInit.pilot.rotate(-90);
		} 
		else if (!Daisy.leftSensed && !Daisy.rightSensed)
		{
			//Blubb -> random turn here
		}
				
		Daisy.leftSensed = false;
		Daisy.rightSensed = false;
		
		return;

	}*/
	
	//-------------------------------------------------------

	
	public void adjustBackground()
	{
		
	}
	
	public void adjustCollor()
	{
		
	}
	
	public void checkColor()
	{
		
	}
	
	public boolean isBallmark(int sensorNummer)
	{
		int[] colors=new int[3];
		colors=getColors();
		if(colors[sensorNummer] == 1)
			return true;
		return false;
	}
	
	//-------------------------------------------------------
	
	public boolean isBorder(int sensorNummer)
	{
		int[] colors=new int[3];
		colors=getColors();
		if(colors[sensorNummer] == 7)
			return true;
		return false;
	}

}
