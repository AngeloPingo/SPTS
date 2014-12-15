package Nmea;

public class AzEl {
	public double Azimuth, Elevation;
	
	public AzEl(double az, double el){
		Azimuth = az;
		Elevation =el;
	}

	public AzEl(Vector3 from, Vector3 to){
		ENU enu = new ENU(from, to);
		Azimuth = Math.atan2(enu.E,enu.N);
		Elevation = Math.atan2(enu.U, Math.sqrt(enu.E*enu.E+ enu.N*enu.N));
	}
	
	
	public String toString(){
		return "{az = "+Math.toDegrees(Azimuth)+"º "+ " | el = " + Math.toDegrees(Elevation)+"º}";
	}
	

	
}
