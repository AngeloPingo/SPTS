import java.text.DecimalFormat;

import Ephemeris.EphFile;
import Ephemeris.Ephemeride;
import Ephemeris.camixRead;
import Ephemeris.nprFile;
import Nmea.AzEl;
import Nmea.Datum;
import Nmea.ENU;
import Nmea.Spherical;
import Nmea.Vector3;
import Pseudorange.Measurements;
import Signals.CA1;
import Signals.CA10;
import Signals.CA11;
import Signals.CA12;
import Signals.CA13;
import Signals.CA14;
import Signals.CA15;
import Signals.CA16;
import Signals.CA17;
import Signals.CA18;
import Signals.CA19;
import Signals.CA2;
import Signals.CA20;
import Signals.CA21;
import Signals.CA22;
import Signals.CA23;
import Signals.CA24;
import Signals.CA25;
import Signals.CA26;
import Signals.CA27;
import Signals.CA28;
import Signals.CA29;
import Signals.CA3;
import Signals.CA30;
import Signals.CA31;
import Signals.CA32;
import Signals.CA4;
import Signals.CA5;
import Signals.CA6;
import Signals.CA7;
import Signals.CA8;
import Signals.CA9;
import Signals.CACode;


public class Teste3Avaliacao {

	public Teste3Avaliacao() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		EphFile file = new EphFile("wa3.eph");
		nprFile fileNpr = new nprFile("wa3pr.txt");
		Vector3 r1 = new Vector3(4918526.668d, -791212.115d, 3969767.140d,Datum.WGS84);
		Measurements pseudorange = new Measurements();
		camixRead camix = new camixRead("wa3mix.txt");
		final CACode camixCA = camix.getCAFile();
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		df.setGroupingUsed(false);
		
		System.out.println("\n=== Exercise V.1 ===");
		{
			for (Ephemeride e : file._ephemerides) {
				if (e.SV == 9) {
					System.out.print("SVN " + e.SV + ":\t");
					
					System.out.println(e.toWGS84(e.toTime(214000, 1693), r1, false)
	                            .toString());

					System.out.print("\n");
				}
			}
			
		}
		
		System.out.println("\n=== Exercise V.2 ===");
		{
			for (Ephemeride e : file._ephemerides) {
					System.out.print("SVN " + e.SV + ":\t");
					
					System.out.println(e.toWGS84(e.toTime(214000, 1693), r1, false)
	                            .toString());

					System.out.print("\n");
			}
			int j = 0;			
			pseudorange.createMatrixs(7);
			for (Ephemeride e : file._ephemerides) {
				if (e.SV == 9 || e.SV == 12 || e.SV == 15 || e.SV == 17 ||  e.SV == 22 ||  e.SV == 26 || e.SV == 27) {
					Spherical LLHSatNewAltitude = e.toWGS84(e.toTime(213984, 1693), r1, false).toSphericalH(false).toDatum(Datum.WGS84);
					Vector3 LLHSat = LLHSatNewAltitude.toVector3();
					pseudorange.createE0(LLHSat, r1);
					double ro = 1;
					pseudorange.measurementMatrix(j, ro, LLHSat);
					j++;
				}
			}
			pseudorange.LeastSquares();
			
			System.out.println("\nMatrix Direction Cosines:");
			pseudorange.getDirectionCosines().show();
			
			System.out.println("\nMatrix H:");
			pseudorange.getH().show();
			
			System.out.println("PDOP: " + pseudorange.calcPDOP(pseudorange.getH()));
			System.out.println("HDOP: " + pseudorange.calcHDOP(pseudorange.getH(),r1));
		}
		
		System.out.println("\n=== Exercise V.3 ===");
		{
			int j = 0;			
			pseudorange.createMatrixs(7);
			for (Ephemeride e : file._ephemerides) {
				if (e.SV == 9 || e.SV == 12 || e.SV == 15 || e.SV == 17 ||  e.SV == 22 ||  e.SV == 26 || e.SV == 27) {
					Spherical LLHSatNewAltitude = e.toWGS84(e.toTime(213984, 1693), r1, false).toSphericalH(false).toDatum(Datum.WGS84);
					Vector3 LLHSat = LLHSatNewAltitude.toVector3();
					pseudorange.createE0(LLHSat, r1);
					double ro = fileNpr._nprs.get(0).get(j);
					pseudorange.measurementMatrix(j, ro, LLHSat);
					j++;
				}
			}
			pseudorange.LeastSquares();
			
			System.out.println("\nMatrix H:");
			pseudorange.getH().show();
			
			System.out.println("\nMatrix Z:");
			pseudorange.getZ().show();
			
			System.out.println("\nMatrix x:");
			pseudorange.getX().transpose().show();
			
			System.out.println("\nEstimate of Receiver Clock Offset [seg]");
			System.out.println(pseudorange.getEstimateReceiverClockOffsetSeg());
			
		}
		
		System.out.println("\n=== Exercise V.4 ===");
		{
			int j = 0;			
			pseudorange.createMatrixs(7);
			for (Ephemeride e : file._ephemerides) {
				if (e.SV == 9 || e.SV == 12 || e.SV == 15 || e.SV == 17 ||  e.SV == 22 ||  e.SV == 26 || e.SV == 27) {
					Spherical LLHSatNewAltitude = e.toWGS84(e.toTime(213984, 1693), r1, false).toSphericalH(false).toDatum(Datum.WGS84);
					Vector3 LLHSat = LLHSatNewAltitude.toVector3();
					
					AzEl azel = new Vector3(LLHSat.subtract(r1))
                    .toAzimuth(new Vector3(r1));
					System.out.println("SV: " + e.SV + ";  Elevation: " + azel.Elevation);
					double uraSinElev = 0;
					if (e.URA == 0 ) {
						uraSinElev = 2.40d/Math.sin(azel.Elevation);
					} else if (e.URA == 1 ) {
						uraSinElev = 3.40d/Math.sin(azel.Elevation);
					}
					
					pseudorange.createE0(LLHSat, r1);
					double ro = fileNpr._nprs.get(0).get(j);
					pseudorange.measurementMatrixWeighted(j, ro, LLHSat, uraSinElev);
					j++;
				}
			}
			pseudorange.LeastSquares();
			
			System.out.println("\nMatrix H:");
			pseudorange.getH().show();
			
			System.out.println("\nMatrix Q:");
			pseudorange.getQ().show();
			
			System.out.println("\nMatrix Z:");
			pseudorange.getZ().show();
			
			System.out.println("\nMatrix x:");
			pseudorange.getX().transpose().show();
			
			System.out.println("\nEstimate of Receiver Clock Offset [seg]");
			System.out.println(pseudorange.getEstimateReceiverClockOffsetSeg());
		}
		
		System.out.println("\n=== Exercise V.5 ===");
		{
			System.out.println("\nC/A 11:");
			CACode caCode11 = new CA11();
			System.out.println(caCode11.toString().substring(0, 20));
		}
		
		System.out.println("\n=== Exercise V.6 ===");
		{
//			System.out.println("\nVerificar CA1: ");
//			(new CA1()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA2: ");
//			(new CA2()).crossCorrelation(camixCA);
//			
			System.out.println("\nVerificar CA3: ");
			(new CA3()).crossCorrelation(camixCA);
			
//			System.out.println("\nVerificar CA4: ");
//			(new CA4()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA5: ");
//			(new CA5()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA6: ");
//			(new CA6()).crossCorrelation(camixCA);
			
			System.out.println("\nVerificar CA7: ");
			(new CA7()).crossCorrelation(camixCA);
			
//			System.out.println("\nVerificar CA8: ");
//			(new CA8()).crossCorrelation(camixCA);
//			
			System.out.println("\nVerificar CA9: ");
			CACode caCode9 = new CA9();
			caCode9.crossCorrelation(camixCA);
			
//			
//			System.out.println("\nVerificar CA10: ");
//			(new CA10()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA11: ");
//			(new CA11()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA12: ");
//			(new CA12()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA13: ");
//			(new CA13()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA14: ");
//			(new CA14()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA15: ");
//			(new CA15()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA16: ");
//			(new CA16()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA17: ");
//			(new CA17()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA18: ");
//			(new CA18()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA19: ");
//			(new CA19()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA20: ");
//			(new CA20()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA21: ");
//			(new CA21()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA22: ");
//			(new CA22()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA23: ");
//			(new CA23()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA24: ");
//			(new CA24()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA25: ");
//			(new CA25()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA26: ");
//			(new CA26()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA27: ");
//			(new CA27()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA28: ");
//			(new CA28()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA29: ");
//			(new CA29()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA30: ");
//			(new CA30()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA31: ");
//			(new CA31()).crossCorrelation(camixCA);
//			
//			System.out.println("\nVerificar CA32: ");
//			(new CA32()).crossCorrelation(camixCA);
			
		}

	}

}
