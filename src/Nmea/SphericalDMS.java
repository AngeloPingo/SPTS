package Nmea;


public class SphericalDMS {

	private double LatitudeSS, LongitudeSS, Altitude;
	private int LatitudeDD,LatitudeMM, LongitudeDD,LongitudeMM;
	private CardialLatitude CLat;
	private CardialLongitude CLon;
	private Datum datum;

	
	public SphericalDMS(int latDD, int latMM, double latSS, CardialLatitude clat, int lonDD, int lonMM, double lonSS, CardialLongitude clon, double alt, Datum dat){
		LatitudeDD = Math.abs(latDD);
		LongitudeDD = Math.abs(lonDD);
		
		CLat = clat;
		CLon = clon;
		
		LatitudeMM = latMM;
		LatitudeSS = latSS;
		LongitudeMM = lonMM;
		LongitudeSS = lonSS;

		Altitude = alt;	datum = dat;
	}
		
	public String toString(){
        return "{"+LatitudeDD + "º "+ LatitudeMM +"' "+ LatitudeSS + "''" + (CLat == CardialLatitude.N ? "N":"S") + " " +  
         	   LongitudeDD + "º "+ LongitudeMM+"' "+ LongitudeSS + "''" +(CLon == CardialLongitude.E ? "E":"W") +" " + 
         	   Altitude + "m}";
    }
	
	public SphericalDM toSphericalDM(){
		return new SphericalDM((int)LatitudeDD, LatitudeMM + LatitudeSS/60d, CLat,
								(int)LongitudeDD, LongitudeMM+ LongitudeSS/60d, CLon, Altitude, datum);	
	}

	public SphericalD toSphericalD(){
		return toSphericalDM().toSphericalD();
	}
	
	public Spherical toSpherical(){
		return toSphericalD().toSpherical();
	}
	
}
