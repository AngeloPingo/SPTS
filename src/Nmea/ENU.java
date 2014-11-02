package Nmea;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class ENU {
	
	public double E, N, U;
	
	public ENU(Vector3 from, Vector3 to){
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
	
	public String toString(){
        return "{E:" + E + " N:" + N + " U:" + U + "}";
    }

	

}
