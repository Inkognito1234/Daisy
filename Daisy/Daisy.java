import lejos.nxt.Sound;

public class Daisy {
	public static Initialize daisyInit = new Initialize();
	public static DriveMotors motors = new DriveMotors();
	public static ultraSensor objScanner = new ultraSensor();
	public static ColorSensor colorSensor = new ColorSensor();

	public static void main(String[] args) {
		int art = 0, dist = daisyInit.sonicSensor.getDistance();
		// COLORSENSOR
		int colourID_front = Daisy.daisyInit.sensorFront.getColorID();
		int colourID_left = Daisy.daisyInit.sensorLeft.getColorID();
		int colourID_right = Daisy.daisyInit.sensorRight.getColorID();
		double[] distanzen = new double[3];

		daisyInit.init();

		while (true) {
			daisyInit.pilot.forward();

			do {
				// System.out.println("Speed SET: " +
				// daisyInit.pilot.getTravelSpeed() +"\n");
				// System.out.println("Speed CUR: " + motors.getSpeed() +"\n");
				// motors.checkRise();

				dist = daisyInit.sonicSensor.getDistance();
				// Only colourID_front is necessary until it turns BLACK
				// (markierung)
				colourID_front = Daisy.daisyInit.sensorFront.getColorID();
				colourID_left = Daisy.daisyInit.sensorLeft.getColorID();
				colourID_right = Daisy.daisyInit.sensorRight.getColorID();

			} while ((dist >= 22) || (colourID_front == 6)
					|| (colourID_left == 6) || (colourID_right == 6));

			daisyInit.pilot.stop();
			colorSensor.checkTrack(colourID_front, colourID_left, colourID_right);

			System.out.println("DIST: " + dist + "\n");
			// Button.waitForAnyPress();
			Sound.setVolume(100);
			Sound.playSample(daisyInit.soundMGS, daisyInit.volume);
			// spielt File ab mit max Lautstaerke

			Sound.setVolume(20);
			distanzen = objScanner.scanObject(dist);
			// System.out.println(distanzen[0] +" "+
			// distanzen[1]+" "+distanzen[2]+"\n");
			art = objScanner.whatKind(distanzen);
			System.out.println("ART: " + art + "\n");
			switch (art) {
			// falsche Messung-> Abbruch
			case -1:
				break;

			// Messung ergab grosses Obj. -> untersuche auf Wand
			case 1:
				if (objScanner.isWall(distanzen))
					Sound.beep();
				else
					Sound.beepSequenceUp();
				break;

			// Objekt ist gross, aber man koennte links vorbei
			case 2:
				daisyInit.pilot.rotate(Math.tan(distanzen[1]
						/ (1.5 * daisyInit.BALLDURCHMESSER)));
				daisyInit.pilot.forward();
				break;

			// Objekt ist gross, aber man koennte rechts vorbei:
			case 3:
				daisyInit.pilot.rotate(-Math.tan(distanzen[1]
						/ (1.5 * daisyInit.BALLDURCHMESSER)));
				daisyInit.pilot.forward();
				break;

			// Objekt ist klein, vermutlich Ball:
			case 4:
				if (objScanner.isBall(distanzen))
					Sound.beepSequenceUp();
				else
					Sound.beepSequence();
				break;

			// nichts traf zu ?:/ .... alles muss abgedeckt werden!
			default:
				break;
			}

			if (art != -1) {

				daisyInit.pilot.stop();

				daisyInit.pilot.rotate(90);
			}

			// daisyInit.middleMotor.rotate(35);

			// daisyInit.middleMotor.rotate(-35);

		}
	}

}
