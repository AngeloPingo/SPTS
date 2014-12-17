package Signals;

public class CA28 extends CACode {

	public CA28() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(8)^g2.getStageNumber(10));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
