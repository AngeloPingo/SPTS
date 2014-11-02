package Nmea;

public class NmeaGSVSatellite{

	public Integer _prn;
	public Double _elevation, _azimuth, _snr;


	public NmeaGSVSatellite(Integer p, Double e, Double a, Double s){
		_prn = p;
		_elevation = e;
		_azimuth = a;
		_snr = s;
	}
}
