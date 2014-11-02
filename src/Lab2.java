import Nmea.CardialLatitude;
import Nmea.CardialLongitude;
import Nmea.Datum;
import Nmea.Spherical;
import Nmea.SphericalDM;
import Nmea.SphericalDMS;
import Nmea.Vector3;

public class Lab2 {

	public static void ExII1() {
		SphericalDMS point = new SphericalDMS(38, 46, 49.61d,
				CardialLatitude.N, 9, 29, 56.19d, CardialLongitude.W, 103d, Datum.None);
		System.out.println("\n=== EXERCISE II.1.a ===");
		System.out.println(point.toSphericalDM().toString());
		System.out.println("\n=== EXERCISE II.1.b ===");
		System.out.println(point.toSphericalD().toString());
	}

	public static void ExII2() {
		SphericalDMS point = new SphericalDMS(38, 46, 49.61d,
				CardialLatitude.N, 9, 29, 56.19d, CardialLongitude.W, 103d, Datum.None);
		System.out.println("\n=== EXERCISE II.2 ===");
		System.out.println(point.toSpherical().toVector3().toString());
	}

	public static void ExII3() {
		Vector3 vec = new Vector3(4910384.3d, -821478.6d, 3973549.6d, Datum.None);

		System.out.println("\n=== EXERCISE II.3 ===");
		System.out.println("\napproximate solution");
		Spherical s = vec.toSpherical(true);
		System.out.println("\nHeikkinen's method");
		s = vec.toSphericalH(true);

		System.out.println("\n=== EXERCISE II.3.a ===");
		System.out.println(s.toSphericalD().toString());
		System.out.println("\n=== EXERCISE II.3.b ===");
		System.out.println(s.toSphericalDM().toString());
		System.out.println("\n=== EXERCISE II.3.c ===");
		System.out.println(s.toSphericalDMS().toString());
	}

	public static void ExII4() {
		System.out.println("\n=== EXERCISE II.4 ===");
		SphericalDMS p1 = new SphericalDMS(38, 46, 49.61d, CardialLatitude.N,
				9, 29, 56.19d, CardialLongitude.W, 103d, Datum.None);
		Vector3 p2 = new Vector3(4910384.3d, -821478.6d, 3973549.6d, Datum.None);
		System.out.println("distance = "
				+ Vector3.distance(p1.toSpherical().toVector3(), p2) + "m");
	}

	public static void ExII5() {
		System.out.println("\n=== EXERCISE II.5 ===");

		System.out.println("\n\ta)");
		Spherical p1 = new SphericalDMS(38, 46, 49.61d, CardialLatitude.N, 9,
				29, 56.19d, CardialLongitude.W, 103d, Datum.None).toSpherical();
		Spherical newP1 = p1.toDatum(Datum.European1950);
		System.out.println(newP1.toSphericalD().toString());
		System.out.println(newP1.toSphericalDM().toString());
		System.out.println(newP1.toSphericalDMS().toString());

		System.out.println("\n\tb)");
		Spherical p2 = new Vector3(4910384.3d, -821478.6d, 3973549.6d, Datum.None)
				.toSphericalH(false);
		Spherical newP2 = p2.toDatum(Datum.European1950);
		System.out.println(newP2.toSphericalD().toString());
		System.out.println(newP2.toSphericalDM().toString());
		System.out.println(newP2.toSphericalDMS().toString());

		System.out.println("\n\tc)");
		//WGS84 p1ed50molod = newP1.toWGS84(Datum.European1950); 
		Vector3 p1ed50molod = p1.toDatum(Datum.European1950).toVector3();

		//WGS84 p1ed50 = p1.toWGS84(Datum.None).subtract(Datum.European1950);
		Vector3 p1ed50 = p1.toVector3().toDatum(Datum.European1950); 
		System.out.println("p1ED50(molod) = " + p1ed50molod.toString());
		System.out.println("p1ED50 = "+p1ed50.toString());
		System.out.println(p1ed50.distance(p1ed50molod));
	
	}

	
	
	
	
	
	public static void ExII6() {
		System.out.println("\n=== EXERCISE II.6 ===");
		SphericalDMS p1s = new SphericalDMS(38, 46, 49.61d, CardialLatitude.N,
				9, 29, 56.19d, CardialLongitude.W, 103d, Datum.None);

		Vector3 p1 = p1s.toSpherical().toVector3();
		Vector3 p2 = new Vector3(4910384.3d, -821478.6d, 3973549.6d, Datum.None);

		System.out.println("p2-p1 = " + p2.subtract(p1).toString());
		System.out.println(p1.toENU(p2).toString());
		System.out.println(p1.toAzimuth(p2).toString());
	}

	public static void ExII7() {
		System.out.println("\n=== EXERCISE II.7 ===");
		Spherical p1 = new SphericalDM(40, 45d, CardialLatitude.N, 73, 58d,
				CardialLongitude.W, 6378000d, Datum.None).toSpherical();
		Spherical p2 = new SphericalDM(51, 32d, CardialLatitude.N, 0, 10d,
				CardialLongitude.W, 6378000d, Datum.None).toSpherical();
		p1.distanceOrthodrome(p2);
	}

	public static void ExII8() {

		System.out.println("\n=== EXERCISE ORTHO ===");

		Spherical p1 = new SphericalDMS(38, 39, 38, CardialLatitude.N, 9, 17,
				56, CardialLongitude.W, 6378000d, Datum.None).toSpherical();
		Spherical p2 = new SphericalDMS(38, 41, 56, CardialLatitude.S, 38, 28,
				56, CardialLongitude.W, 6378000d, Datum.None).toSpherical();
		Spherical c = new SphericalDMS(0, 0, 0, CardialLatitude.N, 0, 0, 0,
				CardialLongitude.W, 6378000d, Datum.None).toSpherical();
		c.setLatitude((p1.getLatitude() + p2.getLatitude()) / 2f);
		c.setLongitude((p1.getLongitude() + p2.getLongitude()) / 2f);

		System.out.println("### " + c.toSphericalDMS().toString() + " ###");
		p1.distanceOrthodrome(c);

	}

	public static void Test() {
		System.out.println("\n=== EXERCISE MOLOD ===");

		Spherical p1 = new SphericalDMS(38, 44, 30, CardialLatitude.N, 9, 18,
				50, CardialLongitude.W, 100, Datum.None).toSpherical();
		Spherical p2 = new SphericalDMS(38, 42, 40, CardialLatitude.N, 9, 8, 0,
				CardialLongitude.W, 100, Datum.None).toSpherical();

		// XXX: WTF???
		Spherical c = new SphericalDMS(0, 0, 0, CardialLatitude.N, 0, 0, 0,
				CardialLongitude.W, 100, Datum.None).toSpherical();

		c.setLatitude((p1.getLatitude() + p2.getLatitude()) / 2);
		c.setLongitude((p1.getLongitude() + p2.getLongitude()) / 2);

		c.toDatum(Datum.European1950);

	}

	public static void main(String[] args) {

		// ENU enu = new ENU(new Vector3(0d,0d,0d), new
		// Vector3(0.184,-0.983,0.023));
		// System.out.println(enu.toString());

		ExII1();
		ExII2();
		ExII3();
		ExII4();
		ExII5();
		ExII6();
		ExII7();
		ExII8();
		/*
		 * 
		 * 
		 * 
		 * { System.out.println("\n=== EXERCISE 3 ===");
		 * 
		 * Spherical p1 = new SphericalDMS(38, 45, 50, CardialLatitude.N, 9, 8,
		 * 20, CardialLongitude.W, 156d).toSpherical();
		 * 
		 * System.out.println(p1.toWGS84().toString());
		 * 
		 * }
		 */
	}

}
