package Nmea;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class ENU {
	
	public double E, N, U;
	
	
	
	public ENU(double e, double n, double u) {
		super();
		E = e;
		N = n;
		U = u;
	}

	public ENU(Vector3 from, Vector3 to){
		Spherical sph =from.toSphericalH(false);

	//	System.out.println("sph:"+sph);

	//	System.out.println("RX:"+(Math.PI/2-sph.getLatitude()));
	//	System.out.println("RZ:"+(Math.PI/2+sph.getLongitude()));
		
		Rotation rot_x = new Rotation(Vector3D.PLUS_I,Math.PI/2-sph.getLatitude());
		Rotation rot_z = new Rotation(Vector3D.PLUS_K,Math.PI/2+sph.getLongitude());
		
		
		Vector3D v = to.subtract(from);
		Vector3D res =rot_z.applyTo(rot_x).applyInverseTo(v);
//		System.out.println(res);

		
		E=res.getX();
		N=res.getY();
		U=res.getZ();
	}
	
	public static void getDirection(Vector3 from, AzEl azel){
		System.out.println("AzEl:"+azel.toString());
		System.out.println("from:"+from.toString());

		Spherical sph =from.toSphericalH(false);
		

		
		double E = Math.cos(azel.Elevation)*Math.sin(azel.Azimuth);
		double N = Math.cos(azel.Elevation)*Math.cos(azel.Azimuth);
		double U = Math.sin(azel.Elevation);
		
		System.out.println("E="+E);
		System.out.println("N="+N);
		System.out.println("U="+U);
		Rotation rot_x = new Rotation(Vector3D.PLUS_I,-Math.PI/2+sph.getLatitude());
		Rotation rot_z = new Rotation(Vector3D.PLUS_K,-Math.PI/2-sph.getLongitude());


		Vector3D v = new Vector3D(E,N,U);
		Vector3D dir =rot_x.applyTo(rot_z).applyInverseTo(v).normalize();
		
		System.out.println("dir:"+dir.toString());
	
		Vector3D ans = from.add(dir.scalarMultiply(21384975f));
		System.out.println("ans:"+ans.toString());

	}
	
	
	public ENU normalize(){
		double len = (double) Math.sqrt(E*E+N*N+U*U);
		
		return  new ENU(E/len,N/len,U/len);
	}
	
	
	public String toString(){
        return "{E:" + E + " N:" + N + " U:" + U + "}";
    }
/*
	public static Vector3 invert(Vector3 from, ENU enu){
		Spherical sph =from.toSphericalH(false);
		
		double rx =(Math.PI/2-sph.getLatitude());
		double rz =(Math.PI/2+sph.getLongitude());
		
		Rotation rot_x = new Rotation(Vector3D.PLUS_I,rx);
		Rotation rot_z = new Rotation(Vector3D.PLUS_K,rz);

		Vector3D v = to.subtract(from);
		Vector3D res =rot_z.applyTo(rot_x).applyInverseTo(v);
		E=res.getX();
		N=res.getY();
		U=res.getZ();
	}
*/
}
