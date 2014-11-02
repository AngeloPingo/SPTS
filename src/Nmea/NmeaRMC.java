package Nmea;

import java.sql.Timestamp;


public class NmeaRMC extends NmeaMessage{
	

	public Timestamp _dtime;

	
	public boolean _active;
	public Double _latitude, _longitude,_speed, _angle, _magneticVariation;
	
	public NmeaRMC(String line){
		super(line);
		
		String [] time = words[1].split("\\.");
		int hh = Tools.IntegerValue(time[0].substring(0, 2)); 
		int mm = Tools.IntegerValue(time[0].substring(2, 4)); 
		int ss = Tools.IntegerValue(time[0].substring(4, 6)); 
		int ms = time.length ==2 ? Tools.IntegerValue(time[1]) : 0;
		_time = new Timestamp((long)(hh*3600000+mm*60*1000 + ss*1000 + ms));

		
		_active = words[2].compareTo("A")==0;
		_latitude = (words[4].compareTo("S") ==0 ? -1 : 1 )*Tools.DoubleValue(words[3]);
		_longitude = (words[6].compareTo("W") ==0 ? -1 : 1 )*Tools.DoubleValue(words[5]);

		_speed = Tools.DoubleValue(words[7]); 
		_angle = Tools.DoubleValue(words[8]); 

		time = words[9].split("\\.");
		int dhh = Tools.IntegerValue(time[0].substring(0, 2)); 
		int dmm = Tools.IntegerValue(time[0].substring(2, 4)); 
		int dss = Tools.IntegerValue(time[0].substring(4, 6)); 
		
		int dms = time.length ==2 ? Tools.IntegerValue(time[1]) : 0;
		_time = new Timestamp((long)(dhh*3600000+dmm*60*1000 + dss*1000 + dms));

		
		
		
		try {
			_longitude = (words[11].compareTo("W") ==0 ? -1 : 1 )*Tools.DoubleValue(words[10]);
		}
		catch(Exception e){
			
		}
		
	}
}
