package Signals;

public class CA27 extends CACode {

	public CA27() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(7)^g2.getStageNumber(9));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
