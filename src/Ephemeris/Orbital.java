package Ephemeris;

public class Orbital {

	public double A, e ,w;
	public static double µ=3.986005d*Math.pow(10, 14);
	
	public Orbital(double osma, double oe, double aop){
		A =osma;
		e = oe;
		w = aop;
	}
	
	// T
	public double OrbitalPeriod(){
		return 2*Math.PI*Math.sqrt(A*A*A/µ);
	}
	
	// η
	public double MeanAngularVelocity(){
		return Math.sqrt(µ/(A*A*A));	
	}

	// M(t)
	public double MeanAnomaly(double t_minus_tp){
		return MeanAngularVelocity()*t_minus_tp;	
	}
	
	// E(t)
	public double EccentricAnomaly(double t, int i){
		if(i==0)
			return 0;
		else
		    return MeanAnomaly(t) + e*Math.sin(EccentricAnomaly(t,i-1));
	}
	
	// r0(t)
	public double EllipseEquation(double t){
		return A*(1-e*Math.cos(EccentricAnomaly(t, 7)));
	}
	
	// Φ0(t)
	public double TrueAnomaly(double t){
		double E_t = EccentricAnomaly(t, 7);
		return Math.atan2(Math.sqrt(1-e*e)*Math.sin(E_t), Math.cos(E_t)-e);
	}
	
	// Φ(t)
	public double LatitudeArgument(double t){
		return TrueAnomaly(t)+w;
	}
/*
	
	// Θ(t)
	public double RightAscensionReferenceMeridian(double t) {

	}
	
	// RA(t)
	public double RightAscensionAscendingNode(double t) {

	}
	
	
	// Ω(t)
	public double LongitudeAscendingNode(double t){
		
		return RightAscension(t) -LatitudeArgument(t);
	}

*/
}

