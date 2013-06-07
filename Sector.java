import java.util.Random;


public class Sector 
{

	protected static int [][] sector1 = new int[256][256];
	protected static int [][] sector2 = new int[256][256];
	protected static int [][] sector3 = new int[256][256];
	protected static int [][] sector4 = new int[256][256];
	
    public void initSector()
    {
    	for (int i=0 ; i < 256 ; i++)
    		for(int j=0 ; j < 256 ; j++ )
    			sector1[i][j] = 0;
    	
    	for (int i=0 ; i < 256 ; i++)
    		for(int j=0 ; j < 256 ; j++ )
    			sector2[i][j] = 0;
    	
    	for (int i=0 ; i < 256 ; i++)
    		for(int j=0 ; j < 256 ; j++ )
    			sector3[i][j] = 0;
    	
    	for (int i=0 ; i < 256 ; i++)
    		for(int j=0 ; j < 256 ; j++ )
    			sector4[i][j] = 0;
    }
	
	public int getSector(int x, int y)
	{
		if(x >= 0 && y >= 0)
			return sector1[x][y];
		else if(x < 0 && y >= 0)
			return sector2[x][y];
		else if(x <= 0 && y < 0)
			return sector3[x][y];
		else if(x > 0 && y < 0)
			return sector4[x][y];
		
		return -13; //sollte nicht vorkommen
	}
	
	public void setSector(int x, int y, boolean hasObstacle)
	{
			
		if (x >= 0 && y >= 0)
			if(hasObstacle)
				sector1[x][y] = -1;
			else
				sector1[x][y]++;
		else if (x < 0 && y >= 0)
			if(hasObstacle)
				sector2[x][y] = -1;
			else
				sector2[x][y]++;
		else if (x <= 0 && y < 0)
			if(hasObstacle)
				sector3[x][y] = -1;
			else
				sector3[x][y]++;
		else if (x > 0 && y < 0)
			if(hasObstacle)
				sector4[x][y] = -1;
			else
				sector4[x][y]++;
	}
	
	//---------------------------------------------------
	
	public int ceckSector()  // gibt Winkel zurück um in die entsprechende Richtung zu drehen
	{
		 int x = (int) Daisy.poser.getPose().getX() / 5;
		 int y = (int) Daisy.poser.getPose().getY() / 5;
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
	public int decideSection(int[][] sector, int x, int y) 
	{
		Random rand = new Random();
		
		if (Daisy.poser.getPose().getHeading() >= 315 && Daisy.poser.getPose().getHeading() <= 45) 
		{
			if (sector[x][y + 1] == sector[x][Math.max(0, y - 1)])
				return (int) Math.max(90, Math.pow(-1, rand.nextInt(2) *270) );

			if (sector[x][y + 1] < sector[x][Math.max(0, y - 1)])
				return 90 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach oben
			else
				return 270 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach unten
		}

		if (Daisy.poser.getPose().getHeading() > 45 && Daisy.poser.getPose().getHeading() <= 135) 
		{
			if (sector[Math.max(0, x - 1)][y] == sector[x + 1][y])
				return (int) Math.max(0, Math.pow(-1, rand.nextInt(2) *180) );

			if (sector[Math.max(0, x - 1)][y] < sector[x + 1][y])
				return 180 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach links
			else
				return 0 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach rechts
		}

		if (Daisy.poser.getPose().getHeading() > 135 && Daisy.poser.getPose().getHeading() <= 225) 
		{
			if (sector[x][y + 1] == sector[x][Math.max(0, y - 1)])
				return (int) Math.max(90, Math.pow(-1, rand.nextInt(2) *270) );

			if (sector[x][y + 1] < sector[x][Math.max(0, y - 1)])
				return 90 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach oben
			else
				return 270 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach unten
		}

		if (Daisy.poser.getPose().getHeading() > 225 && Daisy.poser.getPose().getHeading() < 315) 
		{
			if (sector[Math.max(0, x - 1)][y] == sector[x + 1][y])
				return (int) Math.max(0, Math.pow(-1, rand.nextInt(2) *180) );

			if (sector[Math.max(0, x - 1)][y] < sector[x + 1][y])
				return 180 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach links
			else
				return 0 - (int) Daisy.poser.getPose().getHeading(); // Drehung nach rechts
		}

		return 0;
	}
}