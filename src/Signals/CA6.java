package Signals;

public class CA6 extends CACode {

	public CA6() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(2)^g2.getStageNumber(10));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
