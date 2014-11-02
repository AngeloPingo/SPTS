package Nmea;

import java.sql.Timestamp;


public class NmeaGLL extends NmeaMessage{
	
	public Double _latitude, _longitude;
	public boolean _active;
	

	
	public NmeaGLL(String line){
		super(line);
		
		_latitude = (words[2].compareTo("S") ==0 ? -1 : 1 )*Tools.DoubleValue(words[1]);
		_longitude = (words[4].compareTo("W") ==0 ? -1 : 1 )*Tools.DoubleValue(words[3]);
		
		String [] time = words[5].split("\\.");
		int hh = Tools.IntegerValue(time[0].substring(0, 2)); 
		int mm = Tools.IntegerValue(time[0].substring(2, 4)); 
		int ss = Tools.IntegerValue(time[0].substring(4, 6)); 
		int ms = Tools.IntegerValue(time[1]);
		_time = new Timestamp((long)(hh*3600000+mm*60*1000 + ss*1000 + ms));

		
		
		_active = words[6].compareTo("A")==0;

		
	}
}
