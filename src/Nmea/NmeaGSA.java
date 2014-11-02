package Nmea;


public class NmeaGSA extends NmeaMessage{
	
	public String _selection;
	public Integer _fix;
	public Integer [] _prn;
	public Double _pdop,_vdop, _hdop;

	public NmeaGSA(String line){
		super(line);
		_selection = words[1];
		_fix = Tools.IntegerValue(words[2]);
		
		_prn = new Integer[12];
		for(int i=0; i< 12 ; ++i)
			_prn[i] = Tools.IntegerValue(words[3+i]);
		
		_pdop = Tools.DoubleValue(words[15]);
		_hdop = Tools.DoubleValue(words[16]);
		_vdop = Tools.DoubleValue(words[17]);

	}
}
