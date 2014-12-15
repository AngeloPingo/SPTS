package Nmea;

import java.sql.Timestamp;


public class NmeaMessage {	
	
	private int _checkSum;
	private boolean _valid = false;
	protected String [] words;
	protected String _line;
	public Timestamp _time;
	private String tag;
	
	public NmeaMessage(String line){
		_line = line;
		words = line.split(",|\\*");
		tag = words[0];
		
		_checkSum = Integer.parseInt(words[words.length-1],16);
		
		int checksum = 0;
		for(int i = 1; i < line.length()-3; i++) {
			checksum = checksum ^ line.charAt(i); 
		}
		if(_checkSum!=checksum)
			System.out.println("Checksum error at " + line);
		else
			_valid = true;
	
	}
	
	public String getTag(){
		return tag;
	}
	
	public String toString(){
		return _line;
	}
	
	public boolean isValid(){
		return _valid;
	}
	
	public String getChecksum(){
		return String.format("%02X", _checkSum);
	}
	
	public String getLine(){
		return _line;
	}
}
