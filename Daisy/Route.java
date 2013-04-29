
import lejos.nxt.Motor;

public class Route {
	// Positionswerte fuer Odometrie,
	// Orientierung 0 in positiver x-Achsenrichtung
	protected double positionX;
	protected double positionY;
	protected double orientation;
	// absolute Drehung
	protected double rotationAbs;
	
	// gibt an, ob die Positions- werte aktuell sind oder eventuell
	// noch eine Methode im Gange ist, die diese gerade Šndert
	
	protected boolean positionIsUpdated;

	public Route(Motor left, Motor right, double wheel, double track) {
		this.resetPos();
		this.resetRotationAbs();
	}

	/**
	 * Setzt die gespeicherte Pose zurueck auf 0,0,0
	 */
	protected void resetPos() {
		positionX = 0;
		positionY = 0;
		orientation = 0;
		// Zuruecksetzen der Motordrehzahlen
		Daisy.daisyInit.rightMotor.resetTachoCount();
		Daisy.daisyInit.leftMotor.resetTachoCount();
		positionIsUpdated = true;
	}

	/**
	 * setzt die absolute gedrehte Orientierung zurueck
	 */
	public void resetRotationAbs() {
		rotationAbs = 0;
	}
}
