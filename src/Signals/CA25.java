package Signals;

public class CA25 extends CACode {

	public CA25() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(5)^g2.getStageNumber(7));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
