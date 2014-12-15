import Ephemeris.EphFile;
import Ephemeris.Ephemeride;
import Ephemeris.Orbital;
import Nmea.AzEl;
import Nmea.CardialLatitude;
import Nmea.CardialLongitude;
import Nmea.Datum;
import Nmea.Spherical;
import Nmea.SphericalDMS;
import Nmea.Vector3;


public class Teste2 {

	public Teste2() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		System.out.println("\n=== EXERCISE 2) ===");
		double sqrt_A = 5153.747;
		double delta_n = 0;
		double toe = 0;
		double Mo = 0.751;
		double turns = 0;
		double tow = 0;
		double wn = 1765;
		
		
		double A = sqrt_A * sqrt_A;
		double n = Math.sqrt(Orbital.µ / (A * A * A)) + delta_n;
		double period = 2*Math.PI/n;
		double tp = toe-Mo/n;
		double time = turns*period+tp+(wn - 1765) * 604800 + (tow - toe);	
		System.out.println("tp="+((time%604800)+604800)%604800+"\twn="+(int)(1765+time/604800));
		
		
		
		EphFile file = new EphFile("wa2-Teste2.eph");
		
		System.out.println("\n=== EXERCISE 3 a) ===");
		
		for (Ephemeride e : file._ephemerides) {
			if (e.SV==4) {
				System.out.println("SVN"
	                    + e.SV
	                    + " "
	                    + e.toWGS84(e.toTime(595200, 1764), false, false)
	                            .toString());
			}
        }
		
		
		System.out.println("\n=== EXERCISE 3 b) ===");
		
		SphericalDMS receiver = new SphericalDMS(38, 44, 15.58d, CardialLatitude.N, 9, 8, 18.67d, CardialLongitude.W, 199.5, Datum.WGS84);
		Vector3 receiverLLH = receiver.toSpherical().toVector3();
        for (Ephemeride e : file._ephemerides) {
        	if (e.SV==4) {
        		 System.out.println("SVN"
                         + e.SV
                         + " "
                         + e.toWGS84(e.toTime(595200, 1764), receiverLLH, false)
                                 .toString());
        	}
           
        }
        
        System.out.println("\n=== EXERCISE 3 c) ===");
        
        double receiverAngle = Math.atan(20/100);
        
        System.out.println("Angulo do recetor com o topo dos predios = " + receiverAngle);
        
        for (Ephemeride e : file._ephemerides) {
        	if(e.SV == 4) {
        	System.out.println("highest latitude = "+ e.getHeighestLatitude());
        	}
        }
        
        for (Ephemeride e : file._ephemerides) {
        	if (e.SV == 4) {
        		Vector3 sattellite = e.toWGS84(e.toTime(595200, 1764), receiverLLH,
                        false);
                
                AzEl azel =new AzEl(receiverLLH, sattellite);
                
                System.out.println("SVN" + e.SV + " " + azel.toString());
        	}            
        }
        
        
        
	}

}
