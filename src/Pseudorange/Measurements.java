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
	Matrix x = new Matrix(4,1);
	Matrix h = null;
	Matrix hEnu = null;
	Matrix z = null;
	Matrix s = null;
	Matrix DirectionCosines = null;
	Matrix DirectionCosinesENU = null;
			

	public Measurements() {
	}
	
	
	public Vector3D getE0sENU() {
		return e0sENU;
	}
	
	public Matrix getX() {
		return x;
	}
	
	public Matrix getH() {
		return h;
	}
	
	
	public Matrix gethEnu() {
		return hEnu;
	}


	public Matrix getZ() {
		return z;
	}
	
	public Matrix getDirectionCosines() {
		return DirectionCosines;
	}
	
	public Matrix getDirectionCosinesENU() {
		return DirectionCosinesENU;
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
	
	public double getEstimateReceiverClockOffsetSeg() {
		return x.getValueAt(3, 0)/299792458;
	}
	
	public Matrix getS() throws Exception {
		if (s == null) {
			s = h.transpose().times(h).inverse();
		}
		return s;
	}
	
	public Vector3D DirectionCosines() {
		Vector3D v = new Vector3D(e0s.getX(), e0s.getY(), e0s.getZ()).normalize();
		return v;
	}
	
	public Vector3D DirectionCosinesENU() {
		Vector3D v = new Vector3D(e0sENU.getX(), e0sENU.getY(), e0sENU.getZ()).normalize();
		return v;
	}
	
	public Matrix LeastSquares() throws Exception {
		x = this.h.transpose().times(Matrix.identity(h.getNrows())).times(this.h).inverse().times(this.h.transpose()).times(Matrix.identity(h.getNrows())).times(this.z);
		return x;
	}
	
	public void createMatrixs(int Rows) {
		h = new Matrix(Rows,4);
		hEnu = new Matrix(Rows,4);
		z = new Matrix(Rows,1);
		DirectionCosines = new Matrix(Rows,3);
		DirectionCosinesENU = new Matrix(Rows,3);
	}
	
	public void measurementMatrix(int row, double ro, Vector3 LLHSat) {
		if (h != null && z != null) {
			double[] hVector = new double[4];
			hVector[0] = -1*this.getE0s().getX();
			hVector[1] = -1*this.getE0s().getY();
			hVector[2] = -1*this.getE0s().getZ();
			hVector[3] = 1d;
			h.addVector(hVector, row);
			
			double[] zVector = new double[1];
			zVector[0] = ro-this.getE0s().dotProduct(LLHSat);
			z.addVector(zVector, row);
			
			double[] hVectorENU = new double[4];
			hVectorENU[0] = -1*this.getE0ENU().getX();
			hVectorENU[1] = -1*this.getE0ENU().getY();
			hVectorENU[2] = -1*this.getE0ENU().getZ();
			hVectorENU[3] = 1d;
			hEnu.addVector(hVectorENU, row);
			
			double[] vDirectionCosines = new double[3];
			vDirectionCosines[0] = this.DirectionCosines().getX();
			vDirectionCosines[1] = this.DirectionCosines().getY();
			vDirectionCosines[2] = this.DirectionCosines().getZ();
			DirectionCosines.addVector(vDirectionCosines, row);
			
			double[] vDirectionCosinesENU = new double[3];
			vDirectionCosinesENU[0] = this.DirectionCosinesENU().getX();
			vDirectionCosinesENU[1] = this.DirectionCosinesENU().getY();
			vDirectionCosinesENU[2] = this.DirectionCosinesENU().getZ();
			DirectionCosinesENU.addVector(vDirectionCosinesENU, row);
		}
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
