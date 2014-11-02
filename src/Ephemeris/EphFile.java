package Ephemeris;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class EphFile {

	public ArrayList<Ephemeride> _ephemerides = new ArrayList<Ephemeride>();
	public EphFile(String filename){

		try {
			FileReader file = new FileReader(filename);	
			BufferedReader reader = new BufferedReader(file);
			String line;
			while((line = reader.readLine())!=null){
				_ephemerides.add(new Ephemeride(line));
			}
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
