package Signals;

public abstract class CACode {
	
	int [] stageNumbers = new int[1023];
	boolean levelOnes = false;
	G1 g1 = new G1();
	G2 g2 = new G2();

	public CACode() {
	}

	public int[] getStageNumbers() {
		return stageNumbers;
	}
	
	public String toString() {
		String result = "";
		
		for (int value : getStageNumbers()) {
			result+= String.valueOf(value);
		}
		return result;		
	}
	
	public void convertLevelsOnes() {
		if (levelOnes) {
			System.err.println("O nivel ja e de -1 a 1!");
			return;
		}
		for (int i = 0; i<1023; i++) {
			if (stageNumbers[i]==0) {
				stageNumbers[i] = -1;
			}
		}
		levelOnes = true;
	}
	
	public void convertLevelsOneAndZero() {
		if (!levelOnes) {
			System.err.println("O nivel ja e de 0 a 1!");
			return;
		}
		for (int i = 0; i<1023; i++) {
			if (stageNumbers[i]==-1) {
				stageNumbers[i] = 0;
			}
		}
		levelOnes = false;
	}
	
}
