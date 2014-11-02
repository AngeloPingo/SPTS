package Nmea;


public class NmeaVTG extends NmeaMessage{
	public Double _ttmg, _mtmg, _gs, _gsk;
	
	
	public NmeaVTG(String line){
		super(line);
		
		_ttmg = Tools.DoubleValue(words[1]);
		_mtmg = Tools.DoubleValue(words[3]);
		_gs = Tools.DoubleValue(words[5]);
		_gsk = Tools.DoubleValue(words[7]);
		
	}
}
