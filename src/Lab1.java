import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Nmea.Datum;
import Nmea.NmeaFile;
import Nmea.NmeaGGA;
import Nmea.NmeaGSV;
import Nmea.NmeaGSVSatellite;
import Nmea.NmeaMessage;
import Nmea.NmeaVTG;
import Nmea.Spherical;

public class Lab1 {

	public static void ExI1(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.1 ===");
		System.out.println(file.getMessages().size());
	}

	public static void ExI2(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.2 ===");
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (String s : file.getHeaders()) {
			sb.append(s + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		System.out.println(sb.toString());
	}

	public static void ExI3(NmeaFile file) {
		TreeMap<String, Long> freqs = new TreeMap<String, Long>();
		System.out.println("\n=== EXERCISE I.3 ===");
		long currentTime = 0l;
		for (NmeaMessage msg : file.getMessages()) {
		
			
			if (msg instanceof NmeaGGA)
				currentTime = msg._time.getTime();
			if (currentTime > 0) {
				String header = msg.getTag();
				/* A */Long time /* ago, in a galaxy far, far away.... */= freqs
						.get(header);
				if (time == null) {
					freqs.put(header, new Long(-currentTime));
				} else if (time < 0 && -time != currentTime) {
					freqs.remove(header);
					freqs.put(header, currentTime + time);
				}
			}
			
			//System.out.println(msg.getChecksum());
			
		}

		for (Map.Entry<String, Long> entry : freqs.entrySet()) {
			System.out.println(entry.getKey() + " : " + 1000f
					/ entry.getValue() + "Hz");
		}
	}

	public static void ExI4(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.4 ===");
		List<NmeaMessage> zdas = file.getMessages("$GPZDA");
		NmeaMessage start = zdas.get(0);
		NmeaMessage end = zdas.get(zdas.size() - 1);
		System.out.println("Begin: " + start._time.toString());
		System.out.println("End: " + end._time.toString());
	}

	public static void ExI5(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.5 ===");

		Double minLat = null, maxLat = null, minLong = null, maxLong = null;

		for (NmeaMessage msg : file.getMessages("$GPGGA")) {
			NmeaGGA gga = (NmeaGGA) msg;
			if (msg.isValid() && gga.getQuality() != 0) {

				if (minLat == null || gga.getSpherical().getLatitude() < minLat)
					minLat = gga.getSpherical().getLatitude();

				if (maxLat == null || gga.getSpherical().getLatitude() > maxLat)
					maxLat = gga.getSpherical().getLatitude();

				if (minLong == null
						|| gga.getSpherical().getLongitude() < minLong)
					minLong = gga.getSpherical().getLongitude();

				if (maxLong == null
						|| gga.getSpherical().getLongitude() > maxLong)
					maxLong = gga.getSpherical().getLongitude();
			}
		}
		Spherical min = new Spherical(minLat, minLong, 0.0d, Datum.None);
		Spherical max = new Spherical(maxLat, maxLong, 0.0d, Datum.None);

		System.out.println("Min: " + min.toSphericalDM().toString());
		System.out.println("Max: " + max.toSphericalDM().toString());
	}

	public static void ExI6(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.6 ===");
		NmeaGGA maxHeight = null, minHeight = null;

		for (NmeaMessage msg : file.getMessages("$GPGGA")) {
			NmeaGGA gga = (NmeaGGA) msg;
			if (msg.isValid() && gga.getQuality() != 0) {
				if (maxHeight == null
						|| gga.getSpherical().getAltitude() > maxHeight
								.getSpherical().getAltitude())
					maxHeight = gga;
				if (minHeight == null
						|| gga.getSpherical().getAltitude() < minHeight
								.getSpherical().getAltitude())
					minHeight = gga;
			}
		}
		System.out.println("Lowest Altitude: "
				+ minHeight.getSpherical().getAltitude());
		System.out.println("\tTime: " + minHeight._time);
		System.out.println("\tCoordinates: "
				+ minHeight.getSpherical().toSphericalDM().toString());

		System.out.println("Highest Altitude: "
				+ maxHeight.getSpherical().getAltitude());
		System.out.println("\tTime: " + maxHeight._time);
		System.out.println("\tCoordinates: "
				+ maxHeight.getSpherical().toSphericalDM().toString());

	}

	public static void ExI7(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.7 ===");

		DecimalFormat df = new DecimalFormat("#.##");
		
		System.out.println("Cumulative height: "   + df.format(file.cumulativeHeight()) + "m");
		System.out.println("Cumulative distance: " + df.format(file.cumulativeDistance()) + "m");
	}

	public static void ExI8(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.8 ===");
		Double maxVel = 0.0;
		for (NmeaMessage msg : file.getMessages("$GPVTG")) {
			NmeaVTG vtg = (NmeaVTG) msg;

			if (msg.isValid()) {
				maxVel = Math.max(vtg._gsk, maxVel);
			}
		}
		System.out.println("Maximum speed over ground: " + maxVel + "km/h");
	}

	public static void ExI9(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.9 ===");
		int numSats = 0;
		for (NmeaMessage msg : file.getMessages("$GPGGA")) {
			NmeaGGA gga = (NmeaGGA) msg;
			if (msg.isValid()) {
				numSats = Math.max(gga.getSatelites(), numSats);
			}
		}
		for (NmeaMessage msg : file.getMessages("$GPGSV")) {
			NmeaGSV gsv = (NmeaGSV) msg;
			if (msg.isValid()) {
				numSats = Math.max(gsv._nSats, numSats);
			}
		}
		System.out.println("Maximum number of satellites in use: " + numSats);
	}

	public static void ExI10(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.10 ===");
		Double maxAngle = 0.0;
		for (NmeaMessage msg : file.getMessages("$GPGSV")) {
			NmeaGSV gsv = (NmeaGSV) msg;
			if (msg.isValid()) {
				// System.out.println(msg._line);
				for (NmeaGSVSatellite sat : gsv._satelittes) {
					if (sat != null && sat._elevation != null) {
						maxAngle = Math.max(sat._elevation, maxAngle);
					}
				}
			}
		}
		System.out.println("Highest elevation angle: " + maxAngle + "º");
	}

	public static void ExI11(NmeaFile file) {
		System.out.println("\n=== EXERCISE I.11 ===");
		boolean detected = false;
		int line = 1;

		for (NmeaMessage msg : file.getMessages()) {
			if (!msg.isValid()) {
				System.out.println("(" + line + ")WRONG CC at: "
						+ msg.toString());
				detected = true;
			}
			++line;
		}
		if (!detected)
			System.out.println("All Messages are Ok!");
	}

	public static void main(String[] args) {

		// ENU enu = new ENU(new Vector3D(0,0,0), new
		// Vector3D(0.184,-0.983,0.023));
		// System.out.println(enu.toString());

		NmeaFile file = new NmeaFile("ISTShuttle.nmea");
		ExI1(file);
		ExI2(file);
		ExI3(file);
		ExI4(file);
		ExI5(file);
		ExI6(file);
		ExI7(file);
		ExI8(file);
		ExI9(file);
		ExI10(file);
		ExI11(file);
	}

}
