package Nmea;



public class NmeaGSV extends NmeaMessage{
	
	public Integer _numSentences, _sentenceNum, _nSats;
	public NmeaGSVSatellite [] _satelittes;


	public NmeaGSV(String line){
		super(line);
		_satelittes = new NmeaGSVSatellite[4];
		_numSentences = Tools.IntegerValue(words[1]);
		_sentenceNum= Tools.IntegerValue(words[2]);
		_nSats = Tools.IntegerValue(words[3]);

		try {
			for(int i=0; i < 4 ; ++i){
				_satelittes[i] = new NmeaGSVSatellite(Tools.IntegerValue(words[i*4+4]),
															Tools.DoubleValue(words[i*4+5]),
															Tools.DoubleValue(words[i*4+6]),
															Tools.DoubleValue(words[i*4+7]));
			}
		}
		catch(Exception e){
			
		}

	}
}
