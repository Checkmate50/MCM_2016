package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class DataHolder {
	private String[] titles;
	private HashSet<University> data;
	private Hashtable<String, String> types;
	private HashSet<Long> possible;
	private Hashtable<String, Float> averages;
	
	public DataHolder(File possible, File data, File dictionary) {
		readInPossible(possible);
		readInData(data, dictionary);
	}
	
	private void readInPossible(File possible) {
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(possible.toPath())));
			br.readLine(); //ignore title line
			while((line = br.readLine()) != null) {
				this.possible.add(Long.parseLong(line.split("\t")[0]));
			}
		}
		catch (IOException e) {
			
		}
	}
	
	private void readInData(File data, File dictionary) {
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(data.toPath())));
			line = br.readLine();
			titles = line.split("\t");
			readInTypes(dictionary);
			while((line = br.readLine()) != null) {
				this.data.add(new University(titles, line.split("\t")));
			}
		}
		catch (IOException e) {
			
		}
	}
	
	private void readInTypes(File dictionary) {
		try {
			String line;
			String[] splitLine;
			BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(dictionary.toPath())));
			while((line = br.readLine()) != null) {
				splitLine = line.split("\t");
				if (splitLine.length < 5)
					continue;
				if (!isTitle(splitLine[4]))
					continue;
				types.put(splitLine[4], splitLine[3]);
			}
		}
		catch (IOException e) {
			
		}
	}
	
	private Boolean isTitle(String t) {
		for (int i = 0; i < titles.length; i++)
			if (titles[i].equals(t))
				return Boolean.TRUE;
		return Boolean.FALSE;
	}
}
