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
		
		System.out.println("\nC/A 1 (Level -1 to 1):");
		caCode1.convertLevelsOnes();
		System.out.println(caCode1.toString());
		caCode1.convertLevelsOnes();

	}

}
