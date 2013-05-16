//import lejos.nxt.*;

public class ColorSensor {

	// COLORS : Black 7, Dark Gray 11, Gray 9, Green 1, Light Gray 10,
	// White 6, NONE -1

	/*
	 * public static int currentColorID_front; public static int
	 * currentColorID_left; public static int currentColorID_right;
	 */

	void checkTrack(int currentColorID_front, int currentColorID_left,
			int currentColorID_right) {
		// currentColorID_front = (int)
		// Daisy.daisyInit.sensorFront.getColorID();

		if (currentColorID_front != 6 && currentColorID_left == 6
				&& currentColorID_right == 6) {
			// front only
			System.out.println("Front Sensor detected a mark! ID is "
					+ currentColorID_front);
		}
		if (currentColorID_front == 6 && currentColorID_left != 6
				&& currentColorID_right == 6) {
			// left only
			System.out.println("Left Sensor detected a mark! ID is "
					+ currentColorID_left);
		}
		if (currentColorID_front == 6 && currentColorID_left == 6
				&& currentColorID_right != 6) {
			// right only
			System.out.println("Right Sensor detected a mark! ID is "
					+ currentColorID_right);
		}
		if (currentColorID_front != 6 && currentColorID_left != 6
				&& currentColorID_right == 6) {
			// front & left
			System.out.println("Front & Left Sensor detected a mark! IDs are "
					+ currentColorID_front + " " + currentColorID_left);
		}
		if (currentColorID_front != 6 && currentColorID_left == 6
				&& currentColorID_right != 6) {
			// front & right
			System.out.println("Front & Right Sensor detected a mark! IDs are "
					+ currentColorID_front + " " + currentColorID_right);
		}
		if (currentColorID_front == 6 && currentColorID_left != 6
				&& currentColorID_right != 6) {
			// left & right
			System.out.println("Left & Right Sensor detected a mark! IDs are "
					+ currentColorID_left + " " + currentColorID_right);
		}

	}

	void colorTolerance(int inputColorID) {

		switch (inputColorID) {
		case (-1): // NO COLOR
			System.out.println("Critical ColorSensor ERROR!");
			Daisy.daisyInit.pilot.stop();
			break;
		case (11): // DARK GRAY (EQUALS BLACK?)
		case (7): // BLACK
			break;
		case (9): // OTHER SHADES OF GREY :D
		case (10): // LIGHTER SHADES OF GREY RATHER EQUAL WHITE
		case (6): // WHITE
			break;
		default: // ALL OTHER COLORS
			break;
		}
	}

	boolean isBorder() {
		return false;
	}

	boolean isBallmark() {
		return false;
	}

}
