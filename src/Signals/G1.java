package Signals;

public class G1 extends CACode{
	
	public G1() {
	}
	
	public G1 generateNext() {
		int [] copyStageNumbers = stageNumbers.clone();
		int stageNumbers3 = copyStageNumbers[2];
		int stageNumbers9 = copyStageNumbers[9];
		int newStageNumber0 = stageNumbers3 ^ stageNumbers9;
		
		stageNumbers[0] = newStageNumber0;
		
		for (int i = 0; i < stageNumbers.length - 1; i++) {
			stageNumbers[i + 1] = copyStageNumbers[i];
		}
		
		return this;
	}

}
