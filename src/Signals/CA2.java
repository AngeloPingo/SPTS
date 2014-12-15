package Signals;

public class CA2 extends CACode {

	public CA2() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(3)^g2.getStageNumber(7));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
