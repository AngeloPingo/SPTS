import Ephemeris.EphFile;
import Ephemeris.Ephemeride;
import Ephemeris.nprFile;
import MathMatrix.Matrix;
import Nmea.Datum;
import Nmea.Spherical;
import Nmea.Vector3;
import Pseudorange.Measurements;


public class Teste3AnoPassado {

	public Teste3AnoPassado() {		
	}
	
	public static void main(String[] args) throws Exception {
		
		EphFile file = new EphFile("eph.eph");
		nprFile fileNpr = new nprFile("npr.txt");
		
		System.out.println("\n=== Exercise V.1 – True Ranges ===");
		{
			
			Vector3 r1 = new Vector3(4918526.668d, -791212.115d, 3969767.140d,Datum.WGS84);
			Measurements pseudorange = new Measurements();
			
			System.out.println("\nSat:");
			for (Ephemeride e : file._ephemerides) {
	            System.out.println("SVN"
	                    + e.SV
	                    + " "
	                    + e.toWGS84(e.toTime(213984, 1693), r1, false)
	                            .toString());
	        }
			
			int j = 0;			
			pseudorange.createMatrixs(6);
			for (Ephemeride e : file._ephemerides) {
				if (e.SV == 9 || e.SV == 12 || e.SV == 15 || e.SV == 17 ||  e.SV == 22 || e.SV == 27) {
					for (int i = 0; i <= 1; i++) {
						Spherical LLHSatNewAltitude = e.toWGS84(e.toTime(213984, 1693), r1, false).toSphericalH(false).toDatum(Datum.WGS84);
						Vector3 LLHSat = LLHSatNewAltitude.toVector3();
						pseudorange.createE0(LLHSat, r1);
						double ro = fileNpr._nprs.get(j).get(i);
						pseudorange.measurementMatrix(j, ro, LLHSat);
					}
					j++;
				}
			}
			pseudorange.LeastSquares();
			
			
			System.out.println("\nMatrix H:");
			pseudorange.getH().show();
			System.out.println("\nMatrix H ENU:");
			pseudorange.gethEnu().show();
			System.out.println("\nMatrix Direction Cosines:");
			pseudorange.getDirectionCosines().show();
			System.out.println("\nMatrix Direction Cosines ENU:");
			pseudorange.getDirectionCosinesENU().show();
			System.out.print("\n");
			System.out.println("\nMatrix Z:");
			pseudorange.getZ().show();
			System.out.print("\n");
			System.out.println("\nMatrix x:");
			pseudorange.getX().transpose().show();
			
			System.out.println("\nMatrix S:");
			pseudorange.getS().show();
			
			System.out.println("\nEstimate of Receiver Clock Offset [seg]");
			System.out.println(pseudorange.getEstimateReceiverClockOffsetSeg());
			
			System.out.println("\nEstimate of Receiver Clock Offset [ms]:\n");
			System.out.println(pseudorange.getEstimateReceiverClockOffsetSeg()*Math.pow(10, 3));
			
			System.out.println("PDOP: " + pseudorange.calcPDOP(pseudorange.getH()));
			System.out.println("GDOP: " + pseudorange.calcGDOP(pseudorange.getH()));
			System.out.println("HDOP: " + pseudorange.calcHDOP(pseudorange.getH(),r1));
			System.out.println("VDOP: " + pseudorange.calcVDOP(pseudorange.getH(),r1));
					
		}
		
	}

}
