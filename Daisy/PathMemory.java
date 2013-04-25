public class PathMemory {

	// pseudocode, der nachher durch richtigen code ersetzt wird

	double[][] turns = new double[20][20];

	/*
	 * die beiden array-felder stehen hier fuer die zwei motoren (bzw. deren
	 * motordrehung; 20/20 kann noch genaendert werden, dies kommt auf den
	 * speicherbedarf an. es werden pro eintrag im double array also die linke
	 * und die rechte motordrehung gespeichert und damit auch die kurve. um
	 * zurueck zu fahren, muss eine Funktion turn180(); ausgerufen werden dann
	 * wird der array von hinten her und mit vertauschten werten auslesen
	 */

	// if (motors move ungleich) then double[i][i] =
	// [leftMotor.getActual()][rightMotor.getActual()]
	// (where i counts from 0 to 20; ++ after each turn)

}
