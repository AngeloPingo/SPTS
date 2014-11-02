package Nmea;

import java.text.DecimalFormat;


public class SphericalDM {

	private double LatitudeMM, LongitudeMM, Altitude;
	private int LatitudeDD, LongitudeDD;
	private CardialLatitude CLat;
	private CardialLongitude CLon;
	private Datum datum;

	public SphericalDM(int latDD, double latMM, CardialLatitude clat, int lonDD, double lonMM, CardialLongitude clon, double alt, Datum dat){
		LatitudeDD = Math.abs(latDD);
		LongitudeDD = Math.abs(lonDD);
		CLat = clat;
		CLon = clon;	
		LatitudeMM = latMM;
		LongitudeMM = lonMM;
		Altitude = alt;	
		datum = dat;
//	System.out.println(ToString());
	}
	



	public String toString(){
        DecimalFormat df = new DecimalFormat("##.####");		
		
        return "{"+LatitudeDD + "º"+ df.format(LatitudeMM) +"'"+ (CLat == CardialLatitude.N ? "N":"S") + " " +  
        	   LongitudeDD + "º"+ df.format(LongitudeMM)+"'"+(CLon == CardialLongitude.E ? "E":"W") +" " + 
        	   Altitude + "m}";
    }
	
	public SphericalD toSphericalD(){
		return new SphericalD(	LatitudeDD +LatitudeMM/60d, CLat,
								LongitudeDD +LongitudeMM/60d,CLon, Altitude, datum);	
	}
	
	public Spherical toSpherical(){
		return toSphericalD().toSpherical();
	}
	
	public SphericalDMS toSphericalDMS(){	
		return new SphericalDMS(LatitudeDD,(int)LatitudeMM,(LatitudeMM -  Math.floor(LatitudeMM))*60d,CLat,
								LongitudeDD,(int)LongitudeMM,(LongitudeMM - Math.floor(LongitudeMM))*60d, CLon, Altitude, datum);
	}
	
	
}
