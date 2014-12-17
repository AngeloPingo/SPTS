package Signals;

public class CA14 extends CACode {

	public CA14() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(7)^g2.getStageNumber(8));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
