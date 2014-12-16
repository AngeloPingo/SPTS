import Ephemeris.EphFile;
import Ephemeris.Ephemeride;
import Ephemeris.nprFile;
import MathMatrix.Matrix;
import Nmea.Datum;
import Nmea.Spherical;
import Nmea.Vector3;
import Pseudorange.Measurements;


public class Teste3 {

	public Teste3() {		
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
			Matrix h = new Matrix(file._ephemerides.size(),4);
			Matrix hEnu = new Matrix(file._ephemerides.size(),4);
			Matrix DirectionCosines = new Matrix(file._ephemerides.size(),3);
			Matrix DirectionCosinesENU = new Matrix(file._ephemerides.size(),3);
			Matrix z = new Matrix(file._ephemerides.size(),1);
			
			for (Ephemeride e : file._ephemerides) {
				if (e.SV == 9 || e.SV == 12 || e.SV == 15 || e.SV == 17 ||  e.SV == 22 || e.SV == 27) {
					for (int i = 0; i <= 1; i++) {
						Spherical LLHSatNewAltitude = e.toWGS84(e.toTime(213984, 1693), r1, false).toSphericalH(false).toDatum(Datum.WGS84);
						Vector3 LLHSat = LLHSatNewAltitude.toVector3();
						pseudorange.createE0(LLHSat, r1);
						double[] hVector = new double[4];
						hVector[0] = -1*pseudorange.getE0s().getX();
						hVector[1] = -1*pseudorange.getE0s().getY();
						hVector[2] = -1*pseudorange.getE0s().getZ();
						hVector[3] = 1d;
						h.addVector(hVector, j);
						
						double[] hVectorENU = new double[4];
						hVectorENU[0] = -1*pseudorange.getE0ENU().getX();
						hVectorENU[1] = -1*pseudorange.getE0ENU().getY();
						hVectorENU[2] = -1*pseudorange.getE0ENU().getZ();
						hVectorENU[3] = 1d;
						hEnu.addVector(hVectorENU, j);
						
						double[] vDirectionCosines = new double[3];
						vDirectionCosines[0] = pseudorange.DirectionCosines().getX();
						vDirectionCosines[1] = pseudorange.DirectionCosines().getY();
						vDirectionCosines[2] = pseudorange.DirectionCosines().getZ();
						DirectionCosines.addVector(vDirectionCosines, j);
						
						double[] vDirectionCosinesENU = new double[3];
						vDirectionCosinesENU[0] = pseudorange.DirectionCosinesENU().getX();
						vDirectionCosinesENU[1] = pseudorange.DirectionCosinesENU().getY();
						vDirectionCosinesENU[2] = pseudorange.DirectionCosinesENU().getZ();
						DirectionCosinesENU.addVector(vDirectionCosinesENU, j);
						
						double[] zVector = new double[1];
						zVector[0] = fileNpr._nprs.get(j).get(i)-pseudorange.getE0s().dotProduct(LLHSat);
						z.addVector(zVector, j);
					}
					j++;
				}
			}
			
			Matrix x = new Matrix(4,1);
			x = pseudorange.LeastSquares(h, z);
			
			System.out.println("\nMatrix H:");
			h.show();
			System.out.println("\nMatrix H ENU:");
			hEnu.show();
			System.out.println("\nMatrix Direction Cosines:");
			DirectionCosines.show();
			System.out.println("\nMatrix Direction Cosines ENU:");
			DirectionCosinesENU.show();
			System.out.print("\n");
			System.out.println("\nMatrix Z:");
			z.show();
			System.out.print("\n");
			System.out.println("\nMatrix x:");
			x.transpose().show();
			
			System.out.println("\nMatrix S:");
			h.transpose().times(h).inverse().show();
			
			System.out.println("PDOP: " + pseudorange.calcPDOP(h));
			System.out.println("GDOP: " + pseudorange.calcGDOP(h));
			System.out.println("HDOP: " + pseudorange.calcHDOP(h,r1));
			System.out.println("VDOP: " + pseudorange.calcVDOP(h,r1));
					
		}
		
	}

}
