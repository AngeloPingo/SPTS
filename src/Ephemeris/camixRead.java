package Ephemeris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import Signals.CAGeneric;

public class camixRead {

	private CAGeneric CAFile = new CAGeneric();
	public CAGeneric getCAFile() {
		return CAFile;
	}

	private static final Pattern SPACE = Pattern.compile(" ");
	
	public camixRead(String filename){

		try {
			FileReader file = new FileReader(filename);	
			BufferedReader reader = new BufferedReader(file);
			String line;
			int j = 0;
			while((line = reader.readLine())!=null){
				String[] tok = SPACE.split(line);
				for (int i = 0; i < tok.length - 1; ++i) {
					CAFile.setStageNumber(i, Integer.parseInt(tok[i]));
				}
				j++;
			}
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
