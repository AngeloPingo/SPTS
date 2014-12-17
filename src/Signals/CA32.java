package Signals;

public class CA32 extends CACode {

	public CA32() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(4)^g2.getStageNumber(9));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
