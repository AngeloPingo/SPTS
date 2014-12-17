package Signals;

public class CA11 extends CACode {

	public CA11() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(3)^g2.getStageNumber(4));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
