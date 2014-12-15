package Pseudorange;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import MathMatrix.Matrix;
import Nmea.Spherical;
import Nmea.Vector3;

public class Measurements {
	
	private Vector3D e0s;

	public Measurements() {
	}
	
	public void createE0(Vector3 LLHSat, Vector3 LLHRec) {
		
		Vector3D SatMinusRec = LLHSat.subtract(LLHRec);
		e0s = SatMinusRec.normalize();
	}

	public Vector3D getE0s() {
		return e0s;
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
