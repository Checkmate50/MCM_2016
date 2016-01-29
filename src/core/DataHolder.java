package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;


public class DataHolder {
	private String[] titles;
	private double[] averages;
	private int[] averageCount;
	private ArrayList<University> data;
	private Hashtable<String, String> types;
	private HashSet<Long> possible;
	private MatrixHolder matrices;
	
	public DataHolder(File possible, File data, File dictionary, MatrixHolder matrices) {
		this.matrices = matrices;
		this.possible = new HashSet<Long>();
		this.data = new ArrayList<University>();
		this.types = new Hashtable<String, String>();
		readInPossible(possible);
		readInData(data, dictionary, matrices);
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
			e.printStackTrace();
		}
	}
	
	private void readInData(File data, File dictionary, MatrixHolder matrices) {
		try {
			String line;
			String[] splitLine;
			double temp1;
			int temp2;
			BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(data.toPath())));
			
			line = br.readLine();
			titles = line.split("\t");
			averages = new double[titles.length];
			averageCount = new int[titles.length];
			for (int i = 0; i < averages.length; i++) {
				averages[i] = 0;
				averageCount[i] = 0;
			}
			
			readInTypes(dictionary);
			while((line = br.readLine()) != null) {
				splitLine = line.split("\t");
				for (int i = 0; i < splitLine.length; i++)
					if (splitLine[i].toLowerCase().equals("null") || splitLine[i].toLowerCase().equals("privacysuppressed"))
						splitLine[i] = "0";
				if (possible.contains(Long.parseLong(splitLine[0])))
					this.data.add(new University(titles, splitLine, matrices));
				//calculate averages
				for (int i = 0; i < splitLine.length; i++) {
					if (types.get(titles[i]).equals("float")) {
						temp1 = Double.parseDouble(splitLine[i]);
						averages[i] += temp1;
						if (temp1 != 0)
							averageCount[i] += 1;
					}
						
					else if (types.get(titles[i]).equals("integer")) {
						temp2 = Integer.parseInt(splitLine[i]);
						averages[i] += temp2;
						if (temp2 != 0)
							averageCount[i] += 1;
					}
				}
			}
			
			for (int i = 0; i < averages.length; i++)
				averages[i] = averages[i]/averageCount[i];
		}
		catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	private Boolean isTitle(String t) {
		for (int i = 0; i < titles.length; i++)
			if (titles[i].equals(t))
				return Boolean.TRUE;
		return Boolean.FALSE;
	}
	
	public void removeTrait(String title, String value) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getField(title).equals(value)) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void removeTrait(String title, double value) {
		for (int i = 0; i < data.size(); i++) {
			if (Double.parseDouble(data.get(i).getField(title)) == value) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void removeTrait(String title, int value) {
		for (int i = 0; i < data.size(); i++) {
			if (Integer.parseInt(data.get(i).getField(title)) == value) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void generateCompMatrix(String title) {
		double[][] build = new double[data.size()][data.size()];
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < data.size(); j++) {
				build[i][j] = Double.parseDouble(data.get(i).getField(title))/Double.parseDouble(data.get(j).getField(title));
			}
		}
		matrices.addMatrix("comp_" + title, new Matrix(build));
	}
	
	public void sort() {
		Collections.sort(data);
	}
	
	public String toString() {
//		String to_return = "";
//		for (int i = 0; i < titles.length; i++) {
//			to_return += titles[i] + "\t" + averages[i] + "\n";
//		}
//		return to_return;
//		String to_return = "";
//		for (int i = 0; i < titles.length; i++) {
//			to_return += titles[i] + " " + types.get(titles[i]) + "\n";
//		}
//		return to_return;
		String to_return = data.size() + "\n";
		for (int i = 0; i < data.size(); i++) {
			to_return += data.get(i).toString()+"\n";
		}
		return to_return;
	}
}
