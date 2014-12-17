package Signals;

public class CA12 extends CACode {

	public CA12() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(5)^g2.getStageNumber(6));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
