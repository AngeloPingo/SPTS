import java.text.DecimalFormat;
import java.util.ArrayList;

import Ephemeris.EphFile;
import Ephemeris.Ephemeride;
import Ephemeris.nprFile;
import MathMatrix.Matrix;
import Nmea.CardialLatitude;
import Nmea.CardialLongitude;
import Nmea.Datum;
import Nmea.ENU;
import Nmea.Spherical;
import Nmea.SphericalDMS;
import Nmea.Vector3;
import Pseudorange.Measurements;

public class Lab5 {

	/**
	 * 
	 */
	public Lab5() {
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		EphFile file = new EphFile("eph.eph");
		nprFile fileNpr = new nprFile("npr.txt");

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		df.setGroupingUsed(false);

		System.out.println("\n=== Exercise V.1 – True Ranges ===");
		{
			Vector3 r1 = new Vector3(4918526.668d, -791212.115d, 3969767.140d,
					Datum.WGS84);

			System.out.print("    " + "\t");

			for (int i = 0; i <= 3600; i++) {
				System.out.print("213984 + " + i + "\t");
			}
			System.out.print("\n");

			for (Ephemeride e : file._ephemerides) {
				System.out.print("SVN " + e.SV + ":\t");

				for (int i = 0; i <= 3600; i++) {
					System.out
							.print(df.format(e.toWGS84(
									e.toTime(213984 + i, 1693), r1, false)
									.distance(r1))
									+ " ;\t");
				}

				System.out.print("\n");
			}
		}

		System.out.println("\n=== Exercise V.2 – Noiseless Pseudoranges ===");
		{
			Vector3 r1 = new Vector3(4918526.668d, -791212.115d, 3969767.140d,
					Datum.WGS84);

			System.out.print("    " + "\t");

			for (int i = 0; i <= 3600; i++) {
				System.out.print("213984 + " + i + "\t");
			}
			System.out.print("\n");
			double offSet = 500 * Math.pow(10, -6);
			double drift = 0.4 * Math.pow(10, -6);
			for (Ephemeride e : file._ephemerides) {
				System.out.print("SVN " + e.SV + ":\t");

				for (int i = 0; i <= 3600; i++) {
					System.out.print(df.format(e.toWGS84(
							e.toTime(213984 + i, 1693), r1, false).distance(r1)
							+ (offSet + i * drift) * 299792458)
							+ " ;\t");
				}

				System.out.print("\n");
			}
		}

		System.out
				.println("\n=== Exercise V.3 – Linearized Single Epoch LS Solution ===");
		ArrayList<Matrix> hs = new ArrayList<Matrix>();
		ArrayList<Matrix> zs = new ArrayList<Matrix>();
		ArrayList<Matrix> xs = new ArrayList<Matrix>();
		Measurements pseudorange = new Measurements();
		Vector3 r1 = new Vector3(4918526.668d, -791212.115d, 3969767.140d,Datum.WGS84);
		Vector3 r3 = new SphericalDMS(38, 44, 12.46d, CardialLatitude.N,
				9, 8, 18.91d, CardialLongitude.W, 102d, Datum.WGS84).toSpherical().toVector3();
		{
			double offSet = 500 * Math.pow(10, -6);
			double drift = 0.4 * Math.pow(10, -6);
					
			System.out.println("\nSat:");
			for (Ephemeride e : file._ephemerides) {
	            System.out.println("SVN"
	                    + e.SV
	                    + " "
	                    + e.toWGS84(e.toTime(213984, 1693), r1, false)
	                            .toString());
	        }
			
			for (int i = 0; i<=3600; i++) {
				hs.add(i, null);
				zs.add(i, null);
			}
					
			int j = 0;
			pseudorange.createMatrixs(file._ephemerides.size());
			for (Ephemeride e : file._ephemerides) {
				int i = 0;
//				for (int i = 0; i <= 1; i++) {
					if (hs.get(i)==null) {
						hs.add(i, new Matrix(file._ephemerides.size(),4));
					}
					if (zs.get(i)==null) {
						zs.add(i, new Matrix(file._ephemerides.size(),1));
					}
					Spherical LLHSatNewAltitude = e.toWGS84(e.toTime(213984+i, 1693), r1, false).toSphericalH(false).toDatum(Datum.WGS84);
					Vector3 LLHSat = LLHSatNewAltitude.toVector3();
					pseudorange.createE0(LLHSat, r3);
					double ro = fileNpr._nprs.get(j).get(i);
					pseudorange.measurementMatrix(j, ro, LLHSat);
//					double[] hVector = new double[4];
//					hVector[0] = -1*pseudorange.getE0s().getX();
//					hVector[1] = -1*pseudorange.getE0s().getY();
//					hVector[2] = -1*pseudorange.getE0s().getZ();
//					hVector[3] = 1d;
//					hs.get(i).addVector(hVector, j);
					
//					double[] zVector = new double[1];
//					zVector[0] = fileNpr._nprs.get(j).get(i)-pseudorange.getE0s().dotProduct(LLHSat);
//					zs.get(i).addVector(zVector, j);
//				}
				j++;
			}
			
			for(int i = 0; i<=3600; i++) {
				Matrix x = new Matrix(4,1);
//				Matrix hi = hs.get(i);
//				Matrix zi = zs.get(i);
				x = pseudorange.LeastSquares();
				xs.add(i, x);
			}
			
			for(int i = 0; i<2; i++) {
				System.out.println("\n----  Iteration:" + i + "  ----");
				System.out.println("\nMatrix H:");
				pseudorange.getH().show();
				System.out.print("\n");
				System.out.println("\nMatrix Z:");
				pseudorange.getZ().show();
				System.out.print("\n");
				System.out.println("\nMatrix x:");
				pseudorange.getX().transpose().show();
			}
			
			System.out.println("\nMatrix S:");
			pseudorange.getS().show();
			
		}

		System.out
				.println("\n=== Exercise V.4 - Dilution Of Precision (DOP) ===");
		
		System.out.println("PDOP: " + pseudorange.calcPDOP(pseudorange.getH()));
		System.out.println("GDOP: " + pseudorange.calcGDOP(pseudorange.getH()));
		System.out.println("HDOP: " + pseudorange.calcHDOP(pseudorange.getH(),r1));
		System.out.println("VDOP: " + pseudorange.calcVDOP(pseudorange.getH(),r1));
		{

		}

		System.out.println("\n=== Exercise V.5 ===");
		{

		}

	}

}
