package Signals;

public class G1{
	
	int [] stageNumbers = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	
	public G1() {
	}
	
	public void generateNext() {
		int [] copyStageNumbers = stageNumbers.clone();
		int stageNumbers3 = copyStageNumbers[2];
		int stageNumbers9 = copyStageNumbers[9];
		int newStageNumber0 = stageNumbers3 ^ stageNumbers9;
		
		stageNumbers[0] = newStageNumber0;
		
		for (int i = 0; i < stageNumbers.length - 1; i++) {
			stageNumbers[i + 1] = copyStageNumbers[i];
		}
	}
	
	public int output() {
		return stageNumbers[stageNumbers.length - 1];
	}

}
