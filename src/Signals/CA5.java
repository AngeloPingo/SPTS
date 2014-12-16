package Signals;

public class CA5 extends CACode {

	public CA5() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(1)^g2.getStageNumber(9));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
