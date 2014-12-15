import Signals.G1;


public class Test {

	public Test() {
	}

	public static void main(String[] args) {
		G1 cena = new G1();
		for (int i = 0 ; i< 1023 ; i++) {
		System.out.print(cena.getStageNumber() + " ");
		cena.generateNext();
		}
	}

}
