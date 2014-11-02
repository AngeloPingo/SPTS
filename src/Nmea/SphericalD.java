package Nmea;

public class SphericalD {

	private double LatitudeDD, LongitudeDD, Altitude;
	private CardialLatitude CLat;
	private CardialLongitude CLon;
	private Datum datum;
	
	
	public SphericalD(double lat, CardialLatitude clat, double lon,CardialLongitude clon, double alt, Datum dat){
		LatitudeDD = Math.abs(lat);
		LongitudeDD =  Math.abs(lon);
		Altitude = alt;	
		CLat = clat;
		CLon = clon;
		datum = dat;
		
	}

	public String toString(){
        return "{"+ LatitudeDD + "º" + (CLat == CardialLatitude.N ? "N":"S")+" "+
        		LongitudeDD + "º"+  (CLon == CardialLongitude.E ? "E":"W")+" "+
        		Altitude + "m}";
    }
	
	public Spherical toSpherical(){
		return new Spherical((CLat==CardialLatitude.N ? 1:-1)* Math.toRadians(LatitudeDD),(CLon==CardialLongitude.E ? 1:-1)*Math.toRadians(LongitudeDD), Altitude, datum);	
	}
	
	public SphericalDM toSphericalDM(){		
		return new SphericalDM((int)LatitudeDD  ,(LatitudeDD -  Math.floor(LatitudeDD))*60d,CLat,(int)LongitudeDD,(LongitudeDD -  Math.floor(LongitudeDD))*60d,CLon, Altitude, datum);
	}
	

	
	
	
}
