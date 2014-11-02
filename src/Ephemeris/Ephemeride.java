package Ephemeris;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import Nmea.Datum;
import Nmea.Tools;
import Nmea.Vector3;

public class Ephemeride {
	public int SV, IODE_sf2, IODE_sf3, WN, toe,FitInterval, SVHealth, URA, Alert, AntiSpoof, L2Channel, L2Data,IODC,SatClockCorr;
	public double TGD, af2,af1,af0, sqrt_A, delta_n, Mo,e,ArgumentPerigee,io, IDOT, Omega0, OmegaDot,Cuc, Cus, Crc, Crs, Cic, Cis, AODO;
	
	// Earth angular velocity
	double OmegaE = 0.000072921151467;
	public Boolean above=true;
	
	public Ephemeride(String line){
		String [] tok = line.split("\t",-1);
		for(int i=0 ; i < tok.length ; ++i)
			tok[i] = tok[i].replaceAll("\\s+", "");
		
		SV = Tools.IntegerValue(tok[0]);
		IODE_sf2= Tools.IntegerValue(tok[1]);
		IODE_sf3= Tools.IntegerValue(tok[2]);
		WN= Tools.IntegerValue(tok[3])+1024;
		toe= Tools.IntegerValue(tok[6]);
		FitInterval = Tools.IntegerValue(tok[7]);
		SVHealth = Tools.IntegerValue(tok[9]);
		URA = Tools.IntegerValue(tok[10]);
		Alert = Tools.IntegerValue(tok[11]);
		AntiSpoof =Tools.IntegerValue(tok[12]);
		L2Channel = Tools.IntegerValue(tok[13]);
		L2Data = Tools.IntegerValue(tok[13]);
		TGD = Tools.DoubleValue(tok[17]);
		IODC = Tools.IntegerValue(tok[18]);
		SatClockCorr = Tools.IntegerValue(tok[21]);
		af2 = Tools.DoubleValue(tok[24]);
		af1 = Tools.DoubleValue(tok[27]);
		af0 = Tools.DoubleValue(tok[30]);
		sqrt_A = Tools.DoubleValue(tok[33]);
		delta_n = Tools.DoubleValue(tok[36]);
		Mo = Tools.DoubleValue(tok[39]);
		e = Tools.DoubleValue(tok[42]);
		ArgumentPerigee = Tools.DoubleValue(tok[45]);
		io = Tools.DoubleValue(tok[48]);
		IDOT = Tools.DoubleValue(tok[51]);
		Omega0 = Tools.DoubleValue(tok[54]);
		OmegaDot = Tools.DoubleValue(tok[57]);
		Cuc = Tools.DoubleValue(tok[60]);
		Cus = Tools.DoubleValue(tok[63]);
		Crc = Tools.DoubleValue(tok[66]);
		Crs = Tools.DoubleValue(tok[69]);
		Cic =  Tools.DoubleValue(tok[72]);
		Cis = Tools.DoubleValue(tok[75]);
		AODO = Tools.DoubleValue(tok[78]);
	
	}
	

	public double toTime(int tow, int wn){
		return (double)(wn-WN)*604800+(tow-toe);	
	}
	


	
	public Vector3 toWGS84(double t_rx, Vector3D r, Boolean debug){
		double t_tx=0d;
		double d_ =0d;
		double c = 299792458d;
		Vector3 s;

		while(true){
			double d = d_;
			
			t_tx = t_rx -d/c;
			
			s = toWGS84(t_tx,true,debug);
			double delta_omega = (OmegaE - OmegaDot)*d/c;
			
			Rotation rot_z = new Rotation(Vector3D.PLUS_K, -delta_omega);
			s=new Vector3(rot_z.applyTo(s));
			d_=r.distance(s);
			
			double delta_dsr = Math.abs(d_-d);
			/*
			System.out.println("-----ITERATION------");
			System.out.println("t="+(tow-d/c));
			System.out.println("s="+s.toString());
			System.out.println("delta_dsr="+delta_dsr);
			System.out.println("delta_omega="+delta_omega);
			System.out.println("-------------------");
			*/
			if(delta_dsr<0.001)
				return s;		
		}
	}
	
	public double E(double M, double e, int it){
		if(it==0)	return 0;
		else		return M + e*Math.sin(E(M,e,it-1));
	}
	
	public double Ep(double M, double e, double erro){
		double E = M;
		double E_ = E;
	    E = M + e*Math.sin(E);

	    while(Math.abs(E - E_)>erro){
	       E_ = E;
	       E = M + e*Math.sin(E);
	    }
	    return E;
	}



	public Vector3 toWGS84(double delta_t, Boolean precise,Boolean debug){				
		double A= sqrt_A*sqrt_A;		
		double n = Math.sqrt( Orbital.µ/(A*A*A))+delta_n;
		double M = Mo+n*delta_t;
		double E = precise? Ep(M,e,0.000000000001): E(M,e,5);
		
		double phi_0 = Math.atan2(Math.sqrt(1-e*e)*Math.sin(E), Math.cos(E)-e);
		double phi = phi_0  + ArgumentPerigee;

		double delta_i = Cic*Math.cos(2*phi) + Cis*Math.sin(2*phi);
		double delta_r =  Crc*Math.cos(2*phi) + Crs*Math.sin(2*phi);
		double delta_u =  Cuc*Math.cos(2*phi) + Cus*Math.sin(2*phi);

		double i= io + delta_i+ IDOT*delta_t;
		
		double omega = Omega0 +(OmegaDot - OmegaE)*delta_t -OmegaE*toe;
		
		double r0 = A*(1-e*Math.cos(E));
		double r=r0 + delta_r;
		
		double u=phi+delta_u;
		
		Vector3 vec = new Vector3(r*Math.cos(u), r*Math.sin(u), 0d, Datum.None);
		Rotation rot_x = new Rotation(Vector3D.PLUS_I, -i);
		Rotation rot_z = new Rotation(Vector3D.PLUS_K, -omega);
		
		if(debug){
			System.out.println("======================");		
			System.out.println("delta_t = "+delta_t);
			System.out.println("A = "+A);
			System.out.println("n = "+n);
			System.out.println("M = "+M);
			System.out.println("E = "+E);
			System.out.println("phi_0 = "+phi_0);
			System.out.println("phi = "+phi);
	
			System.out.println("u = "+u);
			System.out.println("r0 = "+r0);
			System.out.println("Crc = "+Crc);
			System.out.println("Crs = "+Crs);
			System.out.println("Cic = "+Cic);
			System.out.println("Cis = "+Cis);
			System.out.println("Cuc = "+Cuc);
			System.out.println("Cus = "+Cus);
			
			System.out.println("delta_r = "+delta_r);
			System.out.println("delta_i = "+delta_i);
			System.out.println("delta_u = "+delta_u);
	
			System.out.println("r = "+r);
	
			System.out.println("i = "+i);
			System.out.println("Omega = "+omega);
		}
		
		return new Vector3(rot_x.applyTo(rot_z).applyInverseTo(vec));
	}
	
	
	
}
