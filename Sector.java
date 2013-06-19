import java.util.Random;


public class Sector 
{

	static final int FELD = 32;
	protected static short [][] sector1 = new short[FELD][FELD];
	protected static short [][] sector2 = new short[FELD][FELD];
	protected static short [][] sector3 = new short[FELD][FELD];
	protected static short [][] sector4 = new short[FELD][FELD];
	
    public void initSector()
    {
    	for (int i=0 ; i < FELD ; i++)
    		for(int j=0 ; j < FELD ; j++ )
    			sector1[i][j] = 0;
    	
    	for (int i=0 ; i < FELD ; i++)
    		for(int j=0 ; j < FELD ; j++ )
    			sector2[i][j] = 0;
    	
    	for (int i=0 ; i < FELD ; i++)
    		for(int j=0 ; j < FELD ; j++ )
    			sector3[i][j] = 0;
    	
    	for (int i=0 ; i < FELD ; i++)
    		for(int j=0 ; j < FELD ; j++ )
    			sector4[i][j] = 0;
    }
	
	public short getSector(int x, int y)
	{
		if(x >= 0 && y >= 0)
			return sector1[x][y];
		else if(x < 0 && y >= 0)
			return sector2[-x][y];
		else if(x <= 0 && y < 0)
			return sector3[-x][-y];
		else if(x > 0 && y < 0)
			return sector4[x][-y];
		
		return -13; //sollte nicht vorkommen
	}
	
	public void setSector(int x, int y, boolean hasObstacle)
	{
			
		if (x >= 0 && y >= 0)
			if(hasObstacle)
				setFrontBlack(sector1 , x , y);
			else
				sector1[x][y]++;
		else if (x < 0 && y >= 0)
			if(hasObstacle)
				setFrontBlack(sector2 , x , y);
			else
				sector2[-x][y]++;
		else if (x <= 0 && y < 0)
			if(hasObstacle)
				setFrontBlack(sector3 , x , y);
			else
				sector3[-x][-y]++;
		else if (x > 0 && y < 0)
			if(hasObstacle)
				setFrontBlack(sector4 , x , y);
			else
				sector4[x][-y]++;
	}
	
	//---------------------------------------------------
	
	public int checkSector()  // gibt Winkel zurück um in die entsprechende Richtung zu drehen
	{
		 int x = (int)( Daisy.poser.getPose().getX() / 25 );
		 int y = (int)( Daisy.poser.getPose().getY() / 25 );
		 int quadrant = 0;
		 
			if(x >= 0 && y >= 0)
				quadrant = 1;
			else if(x < 0 && y >= 0)
				quadrant = 2;
			else if(x <= 0 && y < 0)
				quadrant = 3;
			else if(x > 0 && y < 0)
				quadrant = 4;
			
		switch(quadrant)
		{
		case 1:
			return decideSection(sector1, Math.abs(x), Math.abs(y));
			
		case 2:
			return decideSection(sector2, Math.abs(x), Math.abs(y));
			
		case 3:
			return decideSection(sector3, Math.abs(x), Math.abs(y));
			
		case 4:
			return decideSection(sector4, Math.abs(x), Math.abs(y));

		}
		return 0;
	}

	//-----------------------------------------------------------
	
	// gibt Winkel zurück um in die entsprechende Richtung zu drehen
	// Funktion nur zur vereinfachung der Methode oben
	public int decideSection(short[][] sector, int x, int y) 
	{
		Random rand = new Random();
		
		if (Daisy.poser.getPose().getHeading() >= 315 && Daisy.poser.getPose().getHeading() <= 45) 
		{
			if (sector[x][y + 1] == -1 && sector[x][Math.max(0, y - 1)] == -1 ) //links und rechts schwarz
				return 180 - (int) Daisy.poser.getPose().getHeading();			//umdrehen				
				
			if (sector[x][y + 1] == sector[x][Math.max(0, y - 1)])			//links und rechts gleichwertig
				return (int) Math.max(90, Math.pow(-1, rand.nextInt(2) *270) ); //random-Drehung

			if (sector[x][y + 1] < sector[x][Math.max(0, y - 1)] && sector[x][y + 1] != -1 ) //links kleiner als rechts
				return 90 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach oben
			else
				return 270 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach unten
		}

		if (Daisy.poser.getPose().getHeading() > 45 && Daisy.poser.getPose().getHeading() <= 135) 
		{
			if (sector[Math.max(0, x - 1)][y] == -1 && sector[x + 1][y] == -1 ) //links und rechts schwarz
				return 270 -(int) Daisy.poser.getPose().getHeading();			//umdrehen	
			
			if (sector[Math.max(0, x - 1)][y] == sector[x + 1][y])
				return (int) Math.max(0, Math.pow(-1, rand.nextInt(2) *180) );

			if (sector[Math.max(0, x - 1)][y] < sector[x + 1][y] && sector[Math.max(0, x - 1)][y] != -1) //links kleiner als rechts
				return 180 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach links
			else
				return 0 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach rechts
		}

		if (Daisy.poser.getPose().getHeading() > 135 && Daisy.poser.getPose().getHeading() <= 225) 
		{
			if (sector[x][y + 1] == -1 && sector[x][Math.max(0, y - 1)] == -1 ) //links und rechts schwarz
				return 0 - (int) Daisy.poser.getPose().getHeading();			//umdrehen		
			
			if (sector[x][y + 1] == sector[x][Math.max(0, y - 1)])
				return (int) Math.max(90, Math.pow(-1, rand.nextInt(2) *270) );

			if (sector[x][y + 1] < sector[x][Math.max(0, y - 1)] && sector[x][y + 1] != -1)
				return 90 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach oben
			else
				return 270 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach unten
		}

		if (Daisy.poser.getPose().getHeading() > 225 && Daisy.poser.getPose().getHeading() < 315) 
		{
			if (sector[Math.max(0, x - 1)][y] == -1 && sector[x + 1][y] == -1 )
				return 90 - (int) Daisy.poser.getPose().getHeading();			//umdrehen		
			
			if (sector[Math.max(0, x - 1)][y] == sector[x + 1][y])
				return (int) Math.max(0, Math.pow(-1, rand.nextInt(2) *180) );

			if (sector[Math.max(0, x - 1)][y] < sector[x + 1][y] && sector[Math.max(0, x - 1)][y] != -1)
				return 180 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach links
			else
				return 0 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach rechts
		}

		return 0;
	}
	
	public void setFrontBlack(short[][] sector, int x, int y)
	{
		if (Daisy.poser.getPose().getHeading() >= 340 && Daisy.poser.getPose().getHeading() <= 20) 
		{
			sector[x + 1][y] = -1;
		}

		if (Daisy.poser.getPose().getHeading() > 70 && Daisy.poser.getPose().getHeading() <= 110) 
		{
			sector[x][y + 1] = -1;
		}

		if (Daisy.poser.getPose().getHeading() > 160 && Daisy.poser.getPose().getHeading() <= 200) 
		{
			sector[x + 1][y] = -1;
		}

		if (Daisy.poser.getPose().getHeading() > 250 && Daisy.poser.getPose().getHeading() < 290) 
		{
			sector[x][y + 1] = -1;
		}
	}
}
