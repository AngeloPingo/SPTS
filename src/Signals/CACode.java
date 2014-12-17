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
	
	public CACode shiftRight() {
		int [] stageNumbersCopy = new int[1023];
		stageNumbersCopy[0] = this.stageNumbers[this.stageNumbers.length - 1];
		for (int i = 0; i < (1023 - 1); i++) {
			stageNumbersCopy[i + 1] = this.stageNumbers[i];
		}
		
		this.stageNumbers = stageNumbersCopy.clone();
		return this;
	}
	
	public CACode shiftRight(int n) {
		for (int i = 0; i<n; i++) {
			this.shiftRight();
		}
		return this;
	}
	
	public CACode shiftLeft() {
		int [] stageNumbersCopy = new int[1023];
		stageNumbersCopy[this.stageNumbers.length - 1] = this.stageNumbers[0];
		for (int i = 1; i < (1023 - 1); i++) {
			stageNumbersCopy[i-1] = this.stageNumbers[i];
		}
		this.stageNumbers = stageNumbersCopy.clone();
		return this;
	}
	
	public CACode shiftLeft(int n) {
		for (int i = 0; i<n; i++) {
			this.shiftLeft();
		}
		return this;
	}
	
	public boolean isLevelOnes() {
		return levelOnes;
	}

	public int autocorrelation(CACode ca) {
		if (!ca.levelOnes) {
			ca.convertLevelsOnes();
		}
		if (!this.levelOnes) {
			this.convertLevelsOnes();
		}
		
		int soma = 0;
		for (int i = 0; i < this.stageNumbers.length; i++) {
			soma += this.stageNumbers[i] * ca.getStageNumbers()[i];
//			System.out.println("Code 1: " + this.stageNumbers[i] + ";  Code 2: " + ca.getStageNumbers()[i] + ";  Soma: " + soma);
		}
		return soma;
	}
	
	public boolean crossCorrelation(CACode[] cas) {
		int numCas = cas.length;
		CAGeneric somaCA = new CAGeneric();
		int somaTem = 0;
		int numChips;
		int soma = 0;
		
		for (int i = 0; i<numCas; i++) {
			if (!cas[i].levelOnes) {
				cas[i].convertLevelsOnes();
			}
		}
		
		for (int i = 0; i < this.stageNumbers.length; i++) {
			for (int j = 0; j<numCas; j++) {
				somaTem += cas[j].getStageNumbers()[i];
			}
			somaCA.setStageNumber(i,somaTem);
			somaTem = 0;
		}
		
		for (numChips = 0; numChips < this.stageNumbers.length; numChips++) {
			for (int i = 0; i < this.stageNumbers.length; i++) {
				soma += this.getStageNumbers()[i] * somaCA.getStageNumbers()[i];
//				System.out.println("Code 1: " + this.stageNumbers[i] + ";  Code 2: " + ca.getStageNumbers()[i] + ";  Soma: " + soma);
			}
			if (soma > 500) {
				System.out.println("Deslocacoes para a direita: " + numChips);
				return true;
			}
			somaCA.shiftRight();
		}
		System.out.println("Deslocacoes para a direita: " + soma);
		return false;
	}
	
	public void crossCorrelation(CACode cas) {
		int numChips;
		int soma = 0;
		int max = 0;
		
		if (!cas.levelOnes) {
			cas.convertLevelsOnes();
		}
		
		for (numChips = 0; numChips < this.stageNumbers.length; numChips++) {
			for (int i = 0; i < this.stageNumbers.length; i++) {
				soma += this.getStageNumbers()[i] * cas.getStageNumbers()[i];
			}
			if (soma > 150 || soma < -150) {
				System.out.println("Soma: " + soma + ";  Deslocacoes para a direita(n+delta): " + numChips + ";  Deslocacoes para a esquerda (n-delta): " + (this.stageNumbers.length - numChips));
			}
			if (max < soma) max = soma;
			soma = 0;
			cas.shiftRight();
		}
		System.out.println("Soma maxima: " + max);
	}
	
}
