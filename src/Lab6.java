import Signals.CA1;
import Signals.CA2;
import Signals.CACode;
import Signals.G1;
import Signals.G2;


public class Lab6 {

	public static void main(String[] args) throws Exception {
		
		System.out.println("\nG1:");
		G1 g1 = new G1();
		for (int i = 0; i<1023;i++) {
			System.out.print(g1.output());
			g1.generateNext();
		}
		
		System.out.println("\nG2:");
		G2 g2 = new G2();
		for (int i = 0; i<1023;i++) {
			System.out.print(g2.output());
			g2.generateNext();
		}
		
		System.out.println("\nC/A 1:");
		CACode caCode1 = new CA1();
		System.out.println(caCode1.toString());
		
		System.out.println("\nC/A 2:");
		CACode caCode2 = new CA2();
		System.out.println(caCode2.toString());
		
		System.out.println("caCode1.autocorrelation(caCode2):");
		System.out.println(caCode1.autocorrelation(caCode2));
		
		System.out.println("\nC/A 2 (shift left):");
		caCode2.shiftLeft(2);
		System.out.println("INICIO");
		System.out.println(caCode2.toString().length());
		System.out.println(caCode2.toString());
		System.out.println("FIM");
		
		System.out.println("\nC/A 2(shift right):");
		CACode caCode3 = new CA2();
		caCode3.shiftRight();
		System.out.println(caCode3.toString());
		
		System.out.println("\nC/A 1 (Level -1 to 1):");
		caCode1.convertLevelsOnes();
		System.out.println(caCode1.toString());
		
		System.out.println("caCode1.autocorrelation(caCode2):");
		System.out.println(caCode1.autocorrelation(caCode2));
		
		System.out.println("caCode1.autocorrelation(caCode1):");
		System.out.println(caCode1.autocorrelation(caCode1));

	}

}
