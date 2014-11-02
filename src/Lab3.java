import java.math.BigDecimal;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import Ephemeris.EphFile;
import Ephemeris.Ephemeride;
import Ephemeris.Orbital;
import Nmea.AzEl;
import Nmea.Datum;
import Nmea.ENU;
import Nmea.Vector3;

public class Lab3 {

    public static void main(String[] args) {
        System.out.println("\n=== EXERCISE IV.1 ===");
        Orbital sat = new Orbital(26559755d, 0.017545d, 1.626021d);
        System.out.println("a) T=" + sat.OrbitalPeriod());
        System.out.println("b) n=" + sat.MeanAngularVelocity());
        System.out.println("c) M=" + sat.MeanAnomaly(39929d));
        System.out.println("d) E=" + sat.EccentricAnomaly(39929d, 7));
        System.out.println("e) (r0,phi0)=" + "[" + sat.EllipseEquation(39929d)
                + "," + sat.TrueAnomaly(39929d) + "]");
        System.out.println("f) phi=" + sat.LatitudeArgument(39929d));

        EphFile file = new EphFile("C:\\ephemerides.eph");

        System.out.println("\n=== EXERCISE IV.2 ===");
        for (Ephemeride e : file._ephemerides) {
            System.out.println("SVN"
                    + e.SV
                    + " "
                    + e.toWGS84(e.toTime(213984, 1693), false, false)
                            .toString());

        }

        Vector3 receiver = new Vector3(4918526.668d, -791212.115d,
                3969767.140d, Datum.None);

        System.out.println("\n=== EXERCISE IV.3 ===");
        for (Ephemeride e : file._ephemerides) {
            System.out.println("SVN"
                    + e.SV
                    + " "
                    + e.toWGS84(e.toTime(213984, 1693), receiver, false)
                            .toString());
        }

        System.out.println("\n=== EXERCISE IV.4 ===");
        for (Ephemeride e : file._ephemerides) {
            Vector3 sattellite = e.toWGS84(e.toTime(213984, 1693), receiver,
                    false);
            System.out.println("SVN" + e.SV + " WGS84: "
                    + new Vector3(sattellite.subtract(receiver)).toCosines());
            System.out.println("SVN" + e.SV + " ENU  : "
                    + new ENU(receiver, sattellite).toString());
            System.out.println();
        }

        System.out.println("\n=== EXERCISE IV.5 ===");
        for (Ephemeride e : file._ephemerides) {
            Vector3 sattellite = e.toWGS84(e.toTime(213984, 1693), receiver,
                    false);
            AzEl azel = new Vector3(sattellite.subtract(receiver))
                    .toAzimuth(new Vector3(receiver));
            System.out.println("SVN" + e.SV + " " + azel.toString());
        }

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

    }

}
