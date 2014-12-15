import Nmea.AzEl;
import Nmea.CardialLatitude;
import Nmea.CardialLongitude;
import Nmea.Datum;
import Nmea.ENU;
import Nmea.NmeaFile;
import Nmea.NmeaGGA;
import Nmea.NmeaMessage;
import Nmea.Spherical;
import Nmea.SphericalD;
import Nmea.SphericalDMS;
import Nmea.Vector3;

public class Teste1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Spherical d1 = new SphericalD(38f, CardialLatitude.N, 0f,
				CardialLongitude.E, 0, Datum.WGS84).toSpherical();
		Spherical d2 = new SphericalD(38f, CardialLatitude.N, 20,
				CardialLongitude.E, 0, Datum.WGS84).toSpherical();

		double phi = Math.toRadians(38);
		double a = 6378137.0d;
		double f = 1 / 298.257223563d;
		double b = a * (1 - f);
		double sin_phi_2 = Math.sin(phi);
		sin_phi_2 *= sin_phi_2;

		Vector3 d1v = d1.toVector3();
		System.out.println(d1v.toString());
		double R = d1v.getX();

		System.out.println("Rn=" + R);
		System.out.println("l=" + Math.toRadians(20));
		System.out.println("d=" + Math.toRadians(20) * R);



		System.out.println("=== 2 ===");
		{
			NmeaFile file = new NmeaFile("trackWA1.nmea");

			int i = 1;
			for (NmeaMessage m : file.getMessages()) {
				if (!m.isValid())
					System.out.println(i);
				++i;
			}

			NmeaGGA first = (NmeaGGA) file.getMessages("$GPGGA").get(0);
			System.out
					.println(first.getSpherical().toSphericalDMS().toString());
			System.out.println(first.getSpherical().toVector3().toString());

		}

		System.out.println("=== 3 ===");
		{
			Spherical dms = new SphericalDMS(38, 44, 44.86d, CardialLatitude.N,
					9, 14, 5.54d, CardialLongitude.W, 207.9d, Datum.WGS84)
					.toSpherical();
			Spherical dms2 = dms.toDatum(Datum.European1950);
			System.out.println(dms2.toSphericalDMS());
		}
		System.out.println("=== 4 ===");


		SphericalDMS dms = new SphericalDMS(38, 44, 12.46f, CardialLatitude.N,
				9, 8, 18.89f, CardialLongitude.W, 101.9, Datum.WGS84);

		ENU.getDirection(dms.toSpherical().toVector3(),
				new AzEl(Math.toRadians(265), Math.toRadians(50)));

	}

}
