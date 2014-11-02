package Nmea;

public class Datum {
	public static final Datum None = new Datum(0d, 0d, 0d, 0d, 0d);
	public static final Datum European1950 = new Datum(251d, 0.14192702d,
			84d, 107d, 120d);
	public static final Datum NorthAmerica1927 = new Datum(69.4d,
			0.37264639d, 8d, 160d, 176d);

	double deltaA, deltaF, deltaX, deltaY, deltaZ;

	public Datum(double da, double df, double dx, double dy, double dz) {
		deltaA = da;
		deltaF = df/10000d;
		deltaX = dx;
		deltaY = dy;
		deltaZ = dz;
	}

	public Datum invert(){
		return new Datum(-deltaA,-deltaF,-deltaX, -deltaY, -deltaZ);
	}
	
}
