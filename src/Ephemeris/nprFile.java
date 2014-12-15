package Ephemeris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class nprFile {

	public ArrayList<ArrayList<Double>> _nprs = new ArrayList<ArrayList<Double>>();
	public nprFile(String filename){

		try {
			FileReader file = new FileReader(filename);	
			BufferedReader reader = new BufferedReader(file);
			String line;
			int j = 0;
			while((line = reader.readLine())!=null){
				_nprs.add(j, new ArrayList<Double>());
				String[] tok = line.split("\t", -1);
				for (int i = 0; i < tok.length - 1; ++i) {
					_nprs.get(j).add(Double.parseDouble(tok[i]));
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
