import lejos.robotics.navigation.Pose;


public class ColorSensor 
{
	static boolean turnLeft=true;
	
	int[] getColors()
	{
		int[] colors = new int[3];
		
		colors[0] = Daisy.daisyInit.sensorLeft.getColorID();
		colors[1] = Daisy.daisyInit.sensorFront.getColorID();
		colors[2] = Daisy.daisyInit.sensorRight.getColorID();
		
		return colors;
	}
	
	//-------------------------------------------------------
	
	void checkTrack()
	{
		boolean ballMark=false, border=false;
		int i=0;
		Pose ausgangsPkt = Daisy.poser.getPose();
		
		Daisy.daisyInit.pilot.stop();
		
		//Pruefen ob Ballmarkierung
		for(i=0 ; i<3 ; i++)
		{
			if(isBallmark(i))
			{
				ballMark=true;
				break;
			}
		}
		
		if(ballMark)
		{
			//AUFRUF VON BALLAUFSAMMELN! MUSS NOCH PROGRAMMIERT WERDEN!
			return;
		}
		
		//Pruefen ob Begrenzung
		for(i=0 ; i<3 ; i++)
		{
			if(isBorder(i))
			{
				border=true;
				break;
			}
		}
		
		if(border)
		{
			switch(i)
			{
			case 2:
				//Nur rechts wurde was erkannt
				Daisy.daisyInit.pilot.steer(25, 90, true);
				while(isBorder(2) && !isBorder(1) && Daisy.daisyInit.pilot.isMoving() )
				{
					
				}
				Daisy.daisyInit.pilot.stop();
				if(isBorder(1) || isBallmark(1))
				{
					checkTrack();
					return;
				}
				//wieder "nach vorne" schauen
				Daisy.nav.goTo(Daisy.poser.getPose().getX(), Daisy.poser.getPose().getY(), ausgangsPkt.getHeading());
				//forward() ?
				return;
			
			case 1:
				//Vorne wurde was ekannt, aber nicht Links
				//Pruefen ob auch rechts was ist
				if(isBorder(2))
				{
					Daisy.daisyInit.pilot.rotate(90);
					Daisy.daisyInit.pilot.travel(15);
					Daisy.daisyInit.pilot.rotate(90);
					turnLeft = false;
					//forward() ?
					return;
				}
				
				//rechts ist nichts erkannt worden
				//Drehung je nach dem, ein Stueck vor und dann wieder drehen. Fertig.
				if(turnLeft)
				{
					Daisy.daisyInit.pilot.rotate(90);
					Daisy.daisyInit.pilot.travel(15);
					Daisy.daisyInit.pilot.rotate(90);
					turnLeft = false;
					//forward() ?
					return;
				}
				else
				{
					Daisy.daisyInit.pilot.rotate(-90);
					Daisy.daisyInit.pilot.travel(15);
					Daisy.daisyInit.pilot.rotate(-90);
					turnLeft = true;
					//forward() ?
					return;
				}
				
			case 0:
				//Links wurde was erkannt
				//Pruefen ob auch vorne was ist, aber rechts frei
				if(isBorder(1) && !isBorder(2))
				{
					Daisy.daisyInit.pilot.rotate(-90);
					Daisy.daisyInit.pilot.travel(15);
					Daisy.daisyInit.pilot.rotate(-90);
					turnLeft = true;
					//forward() ?
					return;
				}
				else if(isBorder(2)) //unerwartet. Vermutlich Fehler. ein Stueck zurueck fahren
				{
					Daisy.daisyInit.pilot.travel(-15);
					return;
				}
				
				//Nur Links wurde was erkannt
				Daisy.daisyInit.pilot.steer(-25, 90, true);
				while(isBorder(0) && !isBorder(1) && Daisy.daisyInit.pilot.isMoving() )
				{
					
				}
				Daisy.daisyInit.pilot.stop();
				if(isBorder(1) || isBallmark(1))
				{
					checkTrack();
					return;
				}
				//wieder "nach vorne" schauen
				Daisy.nav.goTo(Daisy.poser.getPose().getX(), Daisy.poser.getPose().getY(), ausgangsPkt.getHeading());
				//forward() ?
				return;				
				
			}//Switch END
		}
		
		
		
	}
	
	//-------------------------------------------------------
	
	boolean isBallmark(int sensornummer)
	{
		int[] colors=new int[3];
		colors=getColors();
		if(colors[sensornummer] == 1)
			return true;
		return false;
	}
	
	//-------------------------------------------------------
	
	boolean isBorder(int sensornummer)
	{
		int[] colors=new int[3];
		colors=getColors();
		if(colors[sensornummer] == 7)
			return true;
		return false;
	}

}
