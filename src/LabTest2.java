import java.math.BigDecimal;
import java.util.HashSet;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import Ephemeris.EphFile;
import Ephemeris.Ephemeride;
import Ephemeris.Orbital;
import Nmea.AzEl;
import Nmea.CardialLatitude;
import Nmea.CardialLongitude;
import Nmea.Datum;
import Nmea.ENU;
import Nmea.SphericalDMS;
import Nmea.Vector3;

public class LabTest2 {

    public static void main(String[] args) {
     
        EphFile file = new EphFile("wa2.eph");
        Vector3 receiver = new Vector3(4918526.668d, -791212.115d,
                3969767.140d, Datum.WGS84);
        System.out.println("\n=== EXERCISE V.1 ===");
        for (Ephemeride e : file._ephemerides) {
            e.above = true;
        }

        for (Ephemeride e : file._ephemerides) {
            for (int i = 0; i < 3600; ++i) {
                Vector3D sattellite = e.toWGS84(e.toTime(213984, 1693) + i,
                        receiver, false);
                AzEl azel = new Vector3(sattellite.subtract(receiver))
                        .toAzimuth(new Vector3(receiver));
                if (Math.toDegrees(azel.Elevation) > 10)
                    e.above &= e.above;
            }
        }

        for (Ephemeride e : file._ephemerides) {
            if (e.above) {
                if (e.SV == 9 || e.SV == 12 || e.SV == 15 || e.SV == 17
                        || e.SV == 18 || e.SV == 22 || e.SV == 26 || e.SV == 27) {
                    System.out.print("SVN" + e.SV);

                    for (int i = 0; i < 3; ++i) {
                        double T0 = e.toTime(213984, 1693) + i;
                        Vector3D sattellite = e.toWGS84(T0, receiver, false);
                        System.out
                                .print("\t"
                                        + new BigDecimal(sattellite
                                                .distance(receiver)));
                    }
                    System.out.println();
                }
            }
        }

        System.out.println("\n=== EXERCISE V.2 ===");
        double c = 299792458d;
        double a = 500d / 1000000d;
        double b = 0.4d / 1000000d;

        for (Ephemeride e : file._ephemerides) {
            if (e.above) {
                if (e.SV == 9 || e.SV == 12 || e.SV == 15 || e.SV == 17
                        || e.SV == 18 || e.SV == 22 || e.SV == 26 || e.SV == 27) {
                    System.out.print("SVN" + e.SV);

                    for (int i = 0; i < 3; ++i) {
                        double T0 = e.toTime(213984, 1693) + i;

                        Vector3D sattellite = e.toWGS84(T0, receiver, false);
                        double T = sattellite.distance(receiver) / c;
                        double t = T + a + b * (T - T0) / c;

                        System.out.print("\t" + new BigDecimal(c * t));
                    }
                    System.out.println();
                }
            }
        }
        
        System.out.println("\n=== EXERCISE 1a ===");
        for (Ephemeride e1 : file._ephemerides) {
            Vector3 e1_normal = e1.getNormal(-e1.toe, false);
        	for (Ephemeride e2 : file._ephemerides) {
                if(!e1.equals(e2)){
                	Vector3 e2_normal = e2.getNormal(-e2.toe, false);
                	if(e1_normal.dotProduct(e2_normal)>0.9f && e1.SV < e2.SV){
                		System.out.println(e1.SV + " and " + e2.SV +" are in the same plane!");
                	}
                }
            }
        }
        
        System.out.println("\n=== EXERCISE 1b ===");
        HashSet<Ephemeride> groupedSVs= new HashSet<Ephemeride>();
        for (Ephemeride e1 : file._ephemerides) {
        	if(!groupedSVs.contains(e1)){
	            for (Ephemeride e2 : file._ephemerides) {
	                
	        		if(!e1.equals(e2)){
	                	if(e1.samePlane(e2)){
	                		groupedSVs.add(e2);
	                		groupedSVs.add(e1);
	                	}
	                }
	            }
        	}
        }    
        
        System.out.println("\n=== EXERCISE 2 ===");
        for (Ephemeride e1 : file._ephemerides) {
        	if(e1.SV == 2) {
        		System.out.println(e1.toWGS84(e1.toTime(0, 1765), true, true));
        	}
        }    

        System.out.println("\n=== EXERCISE 3 ===");
        for (Ephemeride e1 : file._ephemerides) {
        	if(e1.SV == 2) {
        		e1.getLastPerigeePassage(0,1765,0);
        	}
        }
        System.out.println("\n=== EXERCISE 4 ===");
        SphericalDMS receptorDMS = new SphericalDMS(38, 44, 15.58d, CardialLatitude.N, 9, 8, 18.67d, CardialLongitude.W, 199.5d, Datum.WGS84);
        
        Vector3 receptor = receptorDMS.toSpherical().toVector3();
        System.out.println(receptorDMS);
        System.out.println(receptorDMS.toSphericalDM());
        System.out.println(receptorDMS.toSphericalD());
        System.out.println(receptorDMS.toSpherical());
        System.out.println(receiver);
        System.out.println("\n=== EXERCISE 4a ===");

        for (Ephemeride e1 : file._ephemerides) {
        	if(e1.SV == 17) {
        		System.out.println("satellite = "+ e1.toWGS84(e1.toTime(0, 1765), true,false));
        	}
        }        
        System.out.println("\n=== EXERCISE 4b ===");
        for (Ephemeride e1 : file._ephemerides) {
        	if(e1.SV == 17) {
        		System.out.println("satellite = "+ e1.toWGS84(e1.toTime(0, 1765), receiver, false));
        	}
        }        
        
        
        System.out.println("\n=== EXERCISE 5 ===");
        for (Ephemeride e1 : file._ephemerides) {
        	if(e1.SV == 17) {
        	System.out.println("highest latitude = "+ e1.getHeighestLatitude());
        	}
        }        
    
    }

}
