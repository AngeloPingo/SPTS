package Nmea;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;


public class Vector3 extends Vector3D {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8971340755333304180L;
	private Datum datum;
	public Vector3(Double x, Double y , Double z, Datum dat){
		super(x,y,z);
		datum = dat;
	}

	
	public Vector3(Vector3D v) {
		super(v.getX(),v.getY(),v.getZ());
	}


	public Spherical toSpherical(boolean debug){
		double x = getX();
		double y = getY();
		double z = getZ();
		double a = 6378137.0d;
		double f = 1/298.257223563d;
		double b = a*(1-f);
	
		double ee = 1- (b*b)/(a*a);
		double ee_ = (a*a)/(b*b) -1;
		double r= Math.sqrt(x*x+y*y);
		double theta = Math.atan2(a*z,b*r);
		
		double sin_theta_3 = Math.sin(theta); sin_theta_3 *= sin_theta_3*sin_theta_3;
		double cos_theta_3 = Math.cos(theta); cos_theta_3 *= cos_theta_3*cos_theta_3;		
		
		double phi = Math.atan2(
				z + ee_*b* sin_theta_3, 
				r-ee*a*cos_theta_3);
		
		double sin_phi_2 = Math.sin(phi); sin_phi_2*= sin_phi_2;
		double lambda =getLambda();		
		double R = a /Math.sqrt(1-f*(2-f)*sin_phi_2);
		double h = r / Math.cos(phi) - R;
				
		if(debug){
			System.out.println("b = " + b);
			System.out.println("e^2 = " + ee);
			System.out.println("e’^2 = " + ee_);
			System.out.println("r = " + r);
			System.out.println("theta = " + theta);
			System.out.println("R = " + R);
		}
		
		return new Spherical(phi, lambda, h, datum);
	}
	
	private double getLambda(){
		double x = getX();
		double y = getY();
	//	double z = getZ();
		if(y>=0){
			return Math.PI/2-2*Math.atan2(x, Math.sqrt(x*x+y*y)+y);
		}
		else {
			return -Math.PI/2+2*Math.atan2(x, Math.sqrt(x*x+y*y)-y);
		}
	}
	
	public Spherical toSphericalH(boolean debug){
		double x = getX();
		double y = getY();
		double z = getZ();
		double a= 6378137.0d;
		double f = 1/298.257223563d;
		double b = a*(1-f);
		double ee = 1- (b*b)/(a*a);
		double ee_ = (a*a)/(b*b) -1;
		double r= Math.sqrt(x*x+y*y);
		
		double F = 54*b*b*z*z;
		double G = r*r + (1-ee)*z*z-ee*(a*a-b*b);
		double c= ee*ee*F*r*r/(G*G*G);
		double s = Math.cbrt(1+c+Math.sqrt(c*c+2*c));  
		double P = F/(3*(s+1/s+1)*(s+1/s+1)*G*G);
		double Q = Math.sqrt(1+2*ee*ee*P);
		double r0 = -(P*ee*r)/(1+Q) + Math.sqrt( ((a*a)/2)*(1+1/Q) - (P*(1-ee)*z*z)/(Q*(1+Q)) - (P*r*r)/2);
		double U = Math.sqrt((r-ee*r0)*(r-ee*r0)+z*z);
		double V = Math.sqrt( (r-ee*r0)*(r-ee*r0)+(1-ee)*z*z);
		double z0 = (b*b*z)/(a*V);
		
		double phi = Math.atan2(z+ee_*z0, r);
		double sin_phi_2 = Math.sin(phi); sin_phi_2*= sin_phi_2;
		//double R = a /Math.sqrt(1-f*(2-f)*sin_phi_2);

		double h = U*(1-b*b/(a*V));
		
		double lambda =getLambda();
		
		if(debug){
			System.out.println("b = " + b);
			System.out.println("e^2 = " + ee);
			System.out.println("e’^2 = " + ee_);
			System.out.println("r = " + r);
			System.out.println("F = " + F);
			System.out.println("G = " + G);
			System.out.println("c = " + c);
			System.out.println("s = " + s);
			System.out.println("P = " + P);
			System.out.println("Q = " + Q);
			System.out.println("r0 = " + r0);
			System.out.println("U = " + U);
			System.out.println("V = " + V);
			System.out.println("z0 = " + z0);
		}
		
		return new Spherical(phi, lambda, h, datum);
	}
	
	public static double distance(Vector3 a, Vector3 b){
		double dx=b.getX()-a.getX();
		double dy=b.getY()-a.getY();
		double dz=b.getZ()-a.getZ();
		return Math.sqrt(dx*dx+dy*dy+dz*dz);	
	}
	
	public AzEl toAzimuth(Vector3 a){
		return new AzEl(this,a);
	}
	
	public ENU toENU(Vector3 to){
		return new ENU(this,to);
	}
	
	

	public String toString(){
        return "{X:" + getX() + " Y:" + getY() + " Z:" + getZ() + "}";
    }

	public String toCosines(){
		Vector3D v = new Vector3D(getX(), getY(), getZ()).normalize();
        return "{X:" + v.getX() + " Y:" + v.getY() + " Z:" + v.getZ() + "}";
	}
	
	public Vector3 toDatum(Datum dat){
		return new Vector3(getX()+dat.deltaX, getY()+dat.deltaY, getZ()+dat.deltaZ,dat);
	}
	


	
}
