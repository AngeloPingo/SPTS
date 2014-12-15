package Nmea;

import java.sql.Timestamp;

//     GGA          Global Positioning System Fix Data
public class NmeaGGA extends NmeaMessage{
	
	private Integer _quality,_satelites;
	private Double  _dilution , _geoidHeight, _altitude;
	private Spherical _spherical;
	
	
	public NmeaGGA(String line){
		super(line);
		
		
		String [] time = words[1].split("\\.");
		int hh = Tools.IntegerValue(time[0].substring(0, 2)); 
		int mm = Tools.IntegerValue(time[0].substring(2, 4)); 
		int ss = Tools.IntegerValue(time[0].substring(4, 6)); 
		int ms = Tools.IntegerValue(time[1]);
				
		_time = new Timestamp((long)(hh*3600000+mm*60*1000 + ss*1000 + ms));
		
		String lat = words[2];		
		String lon = words[4];

		int latDD = Tools.IntegerValue(lat.substring(0,2));
		int lonDD = Tools.IntegerValue(lon.substring(0,3));

		double latMM = Tools.DoubleValue(lat.substring(2,lat.length()));
		double lonMM = Tools.DoubleValue(lon.substring(3,lon.length()));
		
		
		_geoidHeight = Tools.DoubleValue(words[11]);
		_altitude = Tools.DoubleValue(words[9]);
		
		
		CardialLatitude cLat = words[3].compareTo("S") ==0 ? CardialLatitude.S :CardialLatitude.N;
		CardialLongitude cLon = words[5].compareTo("W") ==0 ? CardialLongitude.W : CardialLongitude.E;

		
		SphericalDM sp = new SphericalDM(latDD, latMM, cLat, 
										 lonDD, lonMM, cLon, _geoidHeight + _altitude, Datum.WGS84);
		_spherical = sp.toSpherical();
		
	/*	getSpherical() = new Spherical(
				(words[3].compareTo("S") ==0 ? -1 : 1 )*Tools.DoubleValue(words[2]),
				(words[5].compareTo("W") ==0 ? -1 : 1 )*Tools.DoubleValue(words[4]),
				Tools.DoubleValue(words[9]));*/
		
		_quality = Tools.IntegerValue(words[6]);
		_satelites = Tools.IntegerValue(words[7]);
		_dilution = Tools.DoubleValue(words[8]);
		//System.out.println("Created GGA at "+ _hh + ":" + _mm +":"+ _ss + " " + _latitude + "|" + _longitude + " Q" + _quality + " S" + _satelites);
	}
	
	public Spherical getSpherical(){
		return _spherical;
	}

	public Integer getQuality() {
		return _quality;
	}

	public Integer getSatelites() {
		return _satelites;
	}

	public Double getDilution() {
		return _dilution;
	}

	public Double getGeoidHeight() {
		return _geoidHeight;
	}
	
	public Double getAltitude(){
		return _altitude;
	}
	
}
