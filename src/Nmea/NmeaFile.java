package Nmea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class NmeaFile {

	private List<NmeaMessage> _messages = new ArrayList<NmeaMessage>();

	private Map<String, List<NmeaMessage>> _header = new TreeMap<String, List<NmeaMessage>>();

	public Set<String> getHeaders() {
		return _header.keySet();

	}

	public List<NmeaMessage> getMessages(String key) {
		return _header.get(key);
	}

	public List<NmeaMessage> getMessages() {
		return _messages;
	}

	public NmeaFile(String filename) {

		try {
			FileReader file = new FileReader(filename);
			BufferedReader reader = new BufferedReader(file);
			String line;
			String[] words;

			while ((line = reader.readLine()) != null) {

				words = line.split(",|\\*");
				NmeaMessage message = null;

				switch (words[0]) {
					case "$GPGGA":message = new NmeaGGA(line);break;
					case "$GPGLL":message = new NmeaGLL(line);break;
					case "$GPGSA":message = new NmeaGSA(line);break;
					case "$GPGSV":message = new NmeaGSV(line);break;
					case "$GPRMC":message = new NmeaRMC(line);break;
					case "$GPVTG":message = new NmeaVTG(line);break;
					case "$GPZDA":message = new NmeaZDA(line);break;
					default: break;
				}
				if(message!=null){
					List<NmeaMessage> list = _header.get(words[0]);
					if (list == null) {
						list = new ArrayList<NmeaMessage>();
						_header.put(words[0], list);
					}
		
					// System.out.println(words[0]);
					list.add(message);
					_messages.add(message);
				}
			}

			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	
	public double cumulativeDistance(){
		Vector3 old = null;
		Double cumDist = 0.0;
		for (NmeaMessage msg : getMessages("$GPGGA")) {
			NmeaGGA gga = (NmeaGGA) msg;
			if (msg.isValid() && gga.getQuality() != 0) {
				Vector3 current = gga.getSpherical().toVector3();
				if (old != null) {
					cumDist += Vector3.distance(current, old);
				}
				old = current;
			}
		}
		return cumDist;
	}

	public double cumulativeHeight(){
		NmeaGGA old = null;
		Double cumHeight = 0.0;
		for (NmeaMessage msg : getMessages("$GPGGA")) {
			NmeaGGA gga = (NmeaGGA) msg;
			if (msg.isValid() && gga.getQuality() != 0) {
				if (old != null) {
					if (old.getSpherical().getAltitude() < gga.getSpherical()
							.getAltitude()) {
						cumHeight += gga.getSpherical().getAltitude()
								- old.getSpherical().getAltitude();
					}
				}
				old = gga;
			}
		}
		return cumHeight;
	}
	
}
