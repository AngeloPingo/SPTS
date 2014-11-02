package Nmea;

public class Spherical {

	private Double latitude, longitude, altitude;
	private Datum datum;

	public Spherical(Double lat, Double lon, Double alt, Datum dat) {
		latitude = lat;
		longitude = lon;
		altitude = alt;
		datum = dat;
		normalize();
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void normalize() {
		// Latitude = Tools.Wrap(Latitude, 0, 2*Math.PI);
		// Longitude = Tools.Wrap(Longitude, 0, 2*Math.PI);

		if (Math.abs(longitude) > Math.PI)
			longitude = -(2 * Math.PI - longitude);

		if (Math.abs(latitude) > Math.PI / 2)
			latitude = -(Math.PI - latitude);

	}

	public Vector3 toVector3() {
		double phi = latitude;
		double lambda = longitude;
		double a = 6378137.0d + datum.deltaA;
		double f = 1 / 298.257223563d + datum.deltaF;
		// double b = a * (1 - f);
		double sin_phi = Math.sin(phi);
		double R = a / Math.sqrt(1 - f * (2 - f) * sin_phi * sin_phi);
		double x = (R + altitude) * Math.cos(phi) * Math.cos(lambda);
		double y = (R + altitude) * Math.cos(phi) * Math.sin(lambda);
		double z = ((1 - f) * (1 - f) * R + altitude) * Math.sin(phi);
		return new Vector3(x, y, z,datum);
	}

	public SphericalD toSphericalD() {
		return new SphericalD(Math.abs(Math.toDegrees(latitude)),
				latitude >= 0 ? CardialLatitude.N : CardialLatitude.S,
				Math.abs(Math.toDegrees(longitude)),
				longitude >= 0 ? CardialLongitude.E : CardialLongitude.W,
				altitude, datum);
	}

	public SphericalDM toSphericalDM() {

		return toSphericalD().toSphericalDM();
	}

	public SphericalDMS toSphericalDMS() {
		return toSphericalDM().toSphericalDMS();
	}

	public String toString() {
		return "{" + latitude + "rad " + (latitude >= 0 ? "N" : "S") + " "
				+ longitude + "rad " + (longitude >= 0 ? "E" : "W") + " "
				+ altitude + "m}";
	}

	public Spherical toDatum(Datum dat) {
		double a = 6378137.0d;
		double f = 1 / 298.257223563d;
		double b = a * (1 - f);
		double ee = 1 - (b * b) / (a * a);
		// double ee_ = (a * a) / (b * b) - 1;

		double phi = latitude;
		double lambda = longitude;
		double h = altitude;
		double sin_phi_2 = Math.sin(phi);
		sin_phi_2 *= sin_phi_2;
		double RN = a / Math.sqrt(1 - ee * sin_phi_2);
		double RM = (a * (1 - ee)) / Math.sqrt(Math.pow(1 - ee * sin_phi_2, 3));

		double delta_phi = -dat.deltaX * Math.sin(phi) * Math.cos(lambda)
				- dat.deltaY * Math.sin(phi) * Math.sin(lambda)
				+ dat.deltaZ * Math.cos(phi);
		delta_phi += ((dat.deltaA * RN * ee) / a + dat.deltaF
				* (RM * a / b + RN * b / a))
				* Math.sin(phi) * Math.cos(phi);
		delta_phi /= RM + h;

		double delta_lambda = -dat.deltaX * Math.sin(lambda) + dat.deltaY
				* Math.cos(lambda);
		delta_lambda /= (RN + h) * Math.cos(phi);

		double delta_h = dat.deltaX * Math.cos(phi) * Math.cos(lambda)
				+ dat.deltaY * Math.cos(phi) * Math.sin(lambda)
				+ dat.deltaZ * Math.sin(phi) - dat.deltaA * a / RN
				+ dat.deltaF * b * RN * sin_phi_2 / a;

		System.out.println("RN = " + RN + "m");
		System.out.println("RM = " + RM + "m");
		// System.out.println("phi = " + phi);
		// System.out.println("lambda = " + lambda);
		// System.out.println("h = " + h);

		// System.out.println("delta_phi = " + delta_phi);
		// System.out.println("delta_lambda = " + delta_lambda);
		// System.out.println("delta_h = " + delta_h);
		Spherical delta = new Spherical(delta_phi, delta_lambda, delta_h,dat);
		System.out.println("delta=" + delta.toSphericalDMS().toString());

		return new Spherical(latitude + delta_phi, longitude+ delta_lambda, altitude + delta_h,dat);
	
	}



	public void distanceOrthodrome(Spherical p2) {
		double phi1 = getLatitude();
		double lambda1 = getLongitude();
		double phi2 = p2.latitude;
		double lambda2 = p2.longitude;

		System.out.println("lambda1 = " + lambda1);
		System.out.println("lambda2 = " + lambda2);
		System.out.println("phi1 = " + phi1);
		System.out.println("phi2 = " + phi2);

		double cos_theta = Math.cos(phi2) * Math.cos(lambda1 - lambda2)
				* Math.cos(phi1) + Math.sin(phi2) * Math.sin(phi1);
		double tan_psi1 = -Math.cos(phi2) * Math.sin(lambda1 - lambda2);
		tan_psi1 /= -Math.cos(phi2) * Math.cos(lambda1 - lambda2)
				* Math.sin(phi1) + Math.sin(phi2) * Math.cos(phi1);

		double tan_psi2 = -Math.sin(lambda1 - lambda2) * Math.cos(phi1);
		tan_psi2 /= Math.sin(phi2) * Math.cos(lambda1 - lambda2)
				* Math.cos(phi1) - Math.cos(phi2) * Math.sin(phi1);

		double theta = Math.acos(cos_theta);
		double psi1 = Math.atan(tan_psi1); // departure
		double psi2 = Math.atan(tan_psi2); // approaching
	
		if (psi1 < 0) {
			psi1 += Math.PI;
		}

		if (psi2 < 0) {
			psi2 += Math.PI;
		}

		System.out.println("theta = " + theta + " rad = "+ Math.toDegrees(theta) + "º" );
		System.out.println("psi1 = " + psi1 + " rad = "+ Math.toDegrees(psi1) + "º");
		System.out.println("psi2 = " + psi2 + " rad = "+ Math.toDegrees(psi2) + "º");
		System.out.println("d = " + theta * getAltitude());
	}

	public void setLatitude(double d) {
		latitude = d;
	}

	public void setLongitude(double d) {
		longitude = d;
	}

}
