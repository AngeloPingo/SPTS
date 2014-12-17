package Signals;

public class CA24 extends CACode {

	public CA24() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(4)^g2.getStageNumber(6));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
