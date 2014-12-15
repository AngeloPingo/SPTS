package Signals;

public class G2 extends CACode{

	public G2() {
	}
	
	public G2 generateNext() {
		int [] copyStageNumbers = stageNumbers.clone();
		int stageNumbers1 = stageNumbers[1];
		int stageNumbers2 = stageNumbers[2];
		int stageNumbers5 = stageNumbers[5];
		int stageNumbers7 = stageNumbers[7];
		int stageNumbers8 = stageNumbers[8];
		int stageNumbers9 = stageNumbers[9];
		int newStageNumber0 = stageNumbers1 ^ stageNumbers2 ^ stageNumbers5 ^ stageNumbers7 ^ stageNumbers8 ^ stageNumbers9;
		
		stageNumbers[0] = newStageNumber0;
		
		for (int i = 0; i < stageNumbers.length; i++) {
			stageNumbers[i + 1] = copyStageNumbers[i];
		}
		
		return this;
	}

}
