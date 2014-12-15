package Nmea;

public class Tools {
	public static Integer IntegerValue(String s){
		try {
			return Integer.valueOf(s);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static Double DoubleValue(String s){
		try {
			return Double.valueOf(s);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static Float FloatValue(String s){
		try {
			return Float.valueOf(s);
		}
		catch (Exception e) {
			return null;
		}
	}

	public static double Wrap(double value, double min, double max){
		double interv = max-min;
		return  value =  value - interv * Math.floor(  value / interv);

	}
	
	

	
}
