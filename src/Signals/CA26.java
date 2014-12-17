package Signals;

public class CA26 extends CACode {

	public CA26() {
		for (int i = 0; i<1023; i++) {
			stageNumbers[i] = g1.output()^(g2.getStageNumber(6)^g2.getStageNumber(8));
			g1.generateNext();
			g2.generateNext();
		}
	}

}
