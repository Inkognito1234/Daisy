

import lejos.robotics.RegulatedMotor;

public class OwnPilot 
{
	// Roboterkonfiguration
	public double wheelDiameter; // Raddurchmesser
	public double trackWidth; // Radabstand
	
	// Instanziierung zweier Motoren
	public RegulatedMotor rightMotor;
	public RegulatedMotor leftMotor;

	// Radumfang
	private double distancePerTurn;
	
	
	// Positionswerte fuer Odometrie,
	// Orientierung 0 in positiver x-Achsenrichtung
	protected double positionX;
	protected double positionY;
	protected double orientation;
	// absolute Drehung
	protected double rotationAbs;
	// gibt an, ob die Odometrie aktuell ist oder nicht
	protected boolean positionIsUpdated;
	
	// zurueckgelegte Entfernung pro Motortick
	private double distancePerDeg;
	/**
	* Konstruktor
	* @param left Instanz fuer linken Motor
	* @param right Instanz fuer rechten Motor
	* @param wheel Raddurchmesser in cm
	* @param track Spurbreite in cm
	*/
	public OwnPilot(RegulatedMotor left, RegulatedMotor right, double wheel, double track) 
	{
	
		this.leftMotor = left;
		this.rightMotor = right;
		this.wheelDiameter = wheel;
		this.trackWidth = track;

		// zurueckgelegte Strecke pro Umdrehung in cm
		this.distancePerTurn = (Math.PI * wheelDiameter); // PI*14 = 43,9822....
		// zurueckgelegte Strecke pro Grad in cm
		this.distancePerDeg = distancePerTurn / 360; // = 0,12217.....
		
		//this.resetPos();
		//this.resetRotationAbs();
	}
	
	/**
	* Methode zur Fahrsteuerung
	* @param v Lineargeschwindigkeit in m/s (max. 0.43)
	* @param omega Winkelgeschwindigkeit in 1/s (negativ bedeutet Drehung im Uhrzeigersinn)
	 */
	public void drive(double v, double omega) 
	{
	
		double _radius;
		double _rightSpeed;
		double _leftSpeed;
		
		// Sollgeschwindigkeit des Roboters in Grad/s
		double _speedDegreesMiddle = (v / distancePerDeg) * 100;
			
		if (omega != 0) 
		{
			_radius = v * 100 / omega; // Umrechnung von m in cm
		
			// die zu fahrenden Radien der beiden Raeder
			double _rightRadius = _radius + trackWidth / 2;
			double _leftRadius = _radius - trackWidth / 2;
		
			if (_radius != 0) 
			{
				// 1. Fall: Kurve
				_rightSpeed = _rightRadius * _speedDegreesMiddle / _radius;
				_leftSpeed = _leftRadius * _speedDegreesMiddle / _radius;
			}
			else 
			{
				// 2. Fall: Drehen auf der Stelle
				_rightSpeed = omega * _rightRadius / distancePerDeg;
				_leftSpeed = -_rightSpeed;
			}
		} 
		else 
		{
			// 3. Fall: Geradeausfahrt
			_rightSpeed = _speedDegreesMiddle;
			_leftSpeed = _speedDegreesMiddle;
		}
		/*
		if (!positionIsUpdated) 
		{
			this.updatePos();
		}
		positionIsUpdated = false;
		*/
		
		// Absolutbetrag, Konversion nach Integer
		int _rightSpeedInt = (int) Math.abs(_rightSpeed);
		int _leftSpeedInt = (int) Math.abs(_leftSpeed);
		
		// Geschwindigkeit begrenzen (maximal 900)
		rightMotor.setSpeed(Math.min(_rightSpeedInt, 900));
		leftMotor.setSpeed(Math.min(_leftSpeedInt, 900));
		
		// Behandlung des Vorzeichens
		if (_rightSpeed != 0) 
		{
			if (_rightSpeed > 0) rightMotor.forward();
			else rightMotor.backward();
		}
		
		if (_leftSpeed != 0) 
		{
			if (_leftSpeed > 0) leftMotor.forward();
			else leftMotor.backward();
		}
	}
	
	/**
	* Methode zum Anhalten (bremsen)
	*/
	public void stop() 
	{
		rightMotor.stop(true);
		leftMotor.stop();
		//this.updatePos();
	}
	
	/**
	* Methode zum Anhalten (ausrollen)
	*/
	public void flt() 
	{
		//this.updatePos(); // Odometrieberechnung
		leftMotor.flt(true);
		rightMotor.flt();
		// da der Roboter noch etwas weiter faehrt:
		//positionIsUpdated = false;
	}
	
	
	/**
	* Methode, die eine bestimmte Strecke zuruecklegt.
	* @param v Lineargeschwindigkeit in m/s
	* @param omega Winkelgeschwindigkeit
	* @param stopDistance Zurueckzulegende Strecke in cm
	* @param flt Wahl, ob Anhalten per stop() oder flt()
	*/
	public void driveDistance(double v, double omega, double stopDistance, boolean flt) 
	{
		double _distance = 0.0;
		// Ruecksetzen der Tachowerte
		rightMotor.resetTachoCount();
		leftMotor.resetTachoCount();
	
		// Fahren mit den angegebenen Parametern
		this.drive(v, omega);
	
		// Wiederholen, solange Entfernung nicht erreicht
		while (_distance <= stopDistance) 
		{
		// Gemittelte Strecke beider Raeder
			_distance = (( Math.abs(leftMotor.getTachoCount()) + Math.abs(rightMotor.getTachoCount()) ) / 2) * distancePerDeg;
		}
	
		// Anhalten je nach gewaehlter Methode
		if (flt) 
		{
			this.flt();
		} 
		else 
		{
			this.stop();
		}
	}
	
	

	
	/**
	* Odometrieberechnung
	*/
	
	/*
	protected void updatePos() 
	{
		// Strecke rechtes Rad (delta_s_r)
		double _distanceRightWheel = rightMotor.getTachoCount() * distancePerDeg;
		// Strecke linkes Rad (delta_s_l)
		double _distanceLeftWheel = leftMotor.getTachoCount() * distancePerDeg;

		// Zuruecksetzen der Motordrehzahlen
		rightMotor.resetTachoCount();
		leftMotor.resetTachoCount();
	
		// Strecke Robotermittelpunkt (delta_s_m)
		double _distanceMiddle = (_distanceRightWheel + _distanceLeftWheel) / 2;
	
		double _radius = 0;
		double _deltaTheta = 0;
		double _deltaX = 0;
		double _deltaY = 0;
		double _deltaS = 0;
	
		if (_distanceRightWheel != _distanceLeftWheel) 
		{
			_radius = (_distanceMiddle * trackWidth) / (_distanceRightWheel - _distanceLeftWheel);
			if (_radius != 0) 
			{
				// 1. Fall: Kurvenfahrt
				_deltaTheta = (_distanceMiddle / _radius); // rad
				_deltaS = 2 * _radius * Math.sin(_deltaTheta / 2);
				_deltaX = _deltaS * Math.cos((_deltaTheta / 2) + orientation);
				_deltaY = _deltaS * Math.sin((_deltaTheta / 2) + orientation);
	
			}
			else
			{
				// 2. Fall: Drehen auf der Stelle
				_deltaTheta = 2 * _distanceRightWheel / trackWidth;
			}
		}
		else
		{
			// 3. Fall: Geradeausfahrt
			_deltaX = _distanceMiddle * Math.cos(orientation);
			_deltaY = _distanceMiddle * Math.sin(orientation);
		}
		// Aktualisieren der Pose
		positionX += _deltaX;
		positionY += _deltaY;
		orientation = (orientation + _deltaTheta) % (2 * Math.PI);
		rotationAbs += _deltaTheta;
	
		positionIsUpdated = true;
	}
	
	public double getPositionX() 
	{
	
		return positionX;
	}
	
	public double getPositionY() 
	{
		return positionY;
	}
		
	public double getOrientation() 
	{
		return Math.toDegrees(orientation);
	}
	
	public double getRotationAbs() 
	{
		return Math.toDegrees(rotationAbs);
	}
	
	*/
	
	/**
	* Setzt die gespeicherte Pose zurueck auf 0,0,0
	*/
	/*
	protected void resetPos() 
	{
		positionX = 0;
		positionY = 0;
		orientation = 0;
		// Zuruecksetzen der Motordrehzahlen
		rightMotor.resetTachoCount();
		leftMotor.resetTachoCount();
		positionIsUpdated = true;
	}
	*/
	/**
	* setzt die absolute gedrehte Orientierung zurueck
	*/
	/*
	public void resetRotationAbs() 
	{
		rotationAbs = 0;
	}
	*/

}	
	
