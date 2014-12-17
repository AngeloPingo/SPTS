package Signals;

public class CA16 extends CACode {

	public CA16() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(9)^g2.getStageNumber(10));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
