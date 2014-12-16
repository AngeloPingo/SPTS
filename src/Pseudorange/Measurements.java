package Pseudorange;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import MathMatrix.Matrix;
import Nmea.Datum;
import Nmea.ENU;
import Nmea.Spherical;
import Nmea.Vector3;

public class Measurements {
	
	private Vector3D e0s;
	private Vector3D e0sENU;

	public Measurements() {
	}
	
	public void createE0(Vector3 LLHSat, Vector3 LLHRec) {
		
		Vector3D SatMinusRec = LLHSat.subtract(LLHRec);
		e0s = SatMinusRec.normalize();
		ENU enu = new ENU(LLHRec, LLHSat).normalize();
		e0sENU = new Vector3(enu.E, enu.N, enu.U, Datum.European1950);
	}

	public Vector3D getE0s() {
		return e0s;
	}
	
	public Vector3D getE0ENU() {
		return e0sENU;
	}
	
	public Vector3D DirectionCosines() {
		Vector3D v = new Vector3D(e0s.getX(), e0s.getY(), e0s.getZ()).normalize();
		return v;
	}
	
	public Vector3D DirectionCosinesENU() {
		Vector3D v = new Vector3D(e0sENU.getX(), e0sENU.getY(), e0sENU.getZ()).normalize();
		return v;
	}
	
	public Matrix LeastSquares(Matrix H, Matrix Z) throws Exception {
		return H.transpose().times(Matrix.identity(8)).times(H).inverse().times(H.transpose()).times(Matrix.identity(8)).times(Z);
	}

	public double calcPDOP(Matrix h) throws Exception {
		return Math.sqrt(h.transpose().times(h).inverse().trace(1,3));
	}
	
	public double calcHDOP(Matrix h, Vector3 from) throws Exception {
		Spherical sph =from.toSphericalH(false);
		double log = sph.getLongitude();
		double lat = sph.getLatitude();
		double[][] rFill = {
			    {-Math.sin(log),-Math.sin(lat)*Math.cos(log),Math.cos(lat)*Math.cos(log)},
			    {Math.cos(log),-Math.sin(lat)*Math.sin(log),Math.cos(lat)*Math.sin(log)},
			    {0,Math.cos(lat),Math.sin(lat)}};
		Matrix R = new Matrix(rFill);
		Matrix Qxyz = h.transpose().times(h).inverse().createSubMatrixInf(3, 3);
		return Math.sqrt(R.transpose().times(Qxyz).times(R).trace(1,2));
	}
	
	public double calcVDOP(Matrix h, Vector3 from) throws Exception {
		Spherical sph =from.toSphericalH(false);
		double log = sph.getLongitude();
		double lat = sph.getLatitude();
		double[][] rFill = {
			    {-Math.sin(log),-Math.sin(lat)*Math.cos(log),Math.cos(lat)*Math.cos(log)},
			    {Math.cos(log),-Math.sin(lat)*Math.sin(log),Math.cos(lat)*Math.sin(log)},
			    {0,Math.cos(lat),Math.sin(lat)}};
		Matrix R = new Matrix(rFill);
		Matrix Qxyz = h.transpose().times(h).inverse().createSubMatrixInf(3, 3);
		return Math.sqrt(R.transpose().times(Qxyz).times(R).trace(3,3));
	}
	
	public double calcTDOP(Matrix h) throws Exception {
		return Math.sqrt(h.transpose().times(h).inverse().trace(4,4));
	}
	
	public double calcGDOP(Matrix h) throws Exception {
		double pdop = calcPDOP(h);
		double tdop = calcTDOP(h);
		return Math.sqrt(pdop*pdop + tdop*tdop);
	}
}
