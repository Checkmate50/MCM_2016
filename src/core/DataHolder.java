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

/**
 * Credit to Bo Zhu and Chong Wang for developing the model
 * @author Dietrich Geisler
 */
public class DataHolder {
	private String[] titles;
	private double[] averages;
	private int[] averageCount;
	private ArrayList<University> data;
	private Hashtable<String, String> types;
	private HashSet<Long> possible;
	private MatrixHolder matrices;
	private ArrayList<Double> pcip; 
	
	public DataHolder(File possible, File data, File dictionary, MatrixHolder matrices) {
		this.matrices = matrices;
		this.possible = new HashSet<Long>();
		this.data = new ArrayList<University>();
		this.types = new Hashtable<String, String>();
		this.pcip = new ArrayList<Double>();
		readInPossible(possible);
		readInData(data, dictionary, matrices);
		readInPCIP(new File("data/pcip.txt"));
	}
	
	private void readInPossible(File possible) {
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(possible.toPath())));
			br.readLine(); //ignore title line
			while((line = br.readLine()) != null) {
				this.possible.add(Long.parseLong(line.split("\t")[0]));
			}
			br.close();
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
						splitLine[i] = "-1";
				if (possible.contains(Long.parseLong(splitLine[0])))
					this.data.add(new University(titles, splitLine, types, matrices));
				//calculate averages
				for (int i = 0; i < splitLine.length; i++) {
					if (types.get(titles[i]).equals("float")) {
						temp1 = Double.parseDouble(splitLine[i]);
						if (temp1 != -1) {
							averages[i] += temp1;
							averageCount[i] += 1;
						}
					}
						
					else if (types.get(titles[i]).equals("integer")) {
						temp2 = Integer.parseInt(splitLine[i]);
						if (temp2 != -1) {
							averages[i] += temp2;
							averageCount[i] += 1;
						}
					}
				}
			}
			
			for (int i = 0; i < averages.length; i++)
				averages[i] = averages[i]/averageCount[i];
			
			for (int i = 0; i < titles.length; i++) {
				if (types.get(titles[i]).equals("string"))
					continue;
				for (int j = 0; j < this.data.size(); j++)
					if (this.data.get(j).getField(titles[i])==-1.0)
						this.data.get(j).setField(titles[i], averages[i]);
			}
			br.close();
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
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readInPCIP(File pcip) {
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(pcip.toPath())));
			while((line = br.readLine()) != null) {
				this.pcip.add(Double.parseDouble(line));
			}
			br.close();
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
	
	/**
	 * Create a trait by summing the values of the given traits (must all be real values)
	 * @param title
	 * @param titlesToSum
	 */
	public void generateSumTrait(String title, String[] titlesToSum) {
		double sum;
		for (int i = 0; i < data.size(); i++) {
			sum = 0;
			for (int j = 0; i < titlesToSum.length; i++) {
				sum += data.get(i).getField(titlesToSum[j]);
			}
			data.get(i).addField(title, sum);
		}
	}
	
	/**
	 * Create a trait by averaging the values of the given traits
	 * @param title
	 * @param titlesToAverage
	 */
	public void generateAverageTrait(String title, String[] titlesToAverage) {
		double sum;
		double temp;
		int count;
		for (int i = 0; i < data.size(); i++) {
			sum = 0;
			count = 0;
			for (int j = 0; j < titlesToAverage.length; j++) {
				temp = data.get(i).getField(titlesToAverage[j]);
				count++;
				sum += temp;
			}
			data.get(i).addField(title, sum/count);
		}
	}
	
	/**
	 * Create a trait by averaging the values of the given traits
	 * Specially converts act scores (values less than 100)
	 * @param title
	 * @param titlesToAverage
	 */
	public void generateAverageScoreTrait(String title, String[] titlesToAverage) {
		double sum;
		double temp;
		int count;
		for (int i = 0; i < data.size(); i++) {
			sum = 0;
			count = 0;
			for (int j = 0; j < titlesToAverage.length; j++) {
				temp = data.get(i).getField(titlesToAverage[j]);
				count++;
				if (temp < 100) {
						temp = actToSat(temp);
				}
				sum += temp;
			}
			data.get(i).addField(title, sum/count);
		}
	}
	
	/**
	 * Create a trait by averaging the values of the given traits
	 * Special for the pcip data fields
	 * @param title
	 * @param titlesToAverage
	 */
	public void generateAveragePCIPTrait(String title, String[] titlesToAverage) {
		double sum;
		int count;
		for (int i = 0; i < data.size(); i++) {
			sum = 0;
			count = 0;
			for (int j = 0; j < titlesToAverage.length; j++) {
				count++;
				sum += data.get(i).getField(titlesToAverage[j])*pcip.get(j);
			}
			if (sum == 0) //WTF?
				data.get(i).addField(title, data.get(i).getField("md_earn_wne_p10")); //just set the whole vm ratio to 1...
			else
				data.get(i).addField(title, sum/count);
		}
	}
	
	private double actToSat(double value) {
		return value * 60 + 200;
	}
	
	public void generateRatioTrait(String title, String numerator, String denominator) {
		University temp;
		for (int i = 0; i < data.size(); i++) {
			temp = data.get(i);
			temp.addField(title, temp.getField(numerator)/temp.getField(denominator));
		}
	}
	
	public void removeTrait(String title, String value) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getStringField(title).equals(value)) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void removeTrait(String title, double value) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getField(title) == value) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void removeTrait(String title, int value) {
		for (int i = 0; i < data.size(); i++) {
			if ((int)data.get(i).getField(title) == value) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void setMinTrait(String title, double value) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getField(title) < value) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void setMinTrait(String title, int value) {
		for (int i = 0; i < data.size(); i++) {
			if ((int)data.get(i).getField(title) < value) {
				data.remove(i);
				i--;
			}
		}
	}
	
	public void generateEmptyMatrix(String title) {
		double[][] build = new double[data.size()][data.size()];
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < data.size(); j++) {
				build[i][j] = 0;
			}
		}
		matrices.addMatrix(title, new Matrix(build));
	}
	
	public void generateCompMatrix(String title) {
		double[][] build = new double[data.size()][data.size()];
		double max = 0;
		double numerator;
		double denominator;
		ArrayList<Integer> zeroes = new ArrayList<Integer>();
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < data.size(); j++) {
				numerator = data.get(i).getField(title);
				denominator = data.get(j).getField(title);
				if (denominator == 0) {
					if (!zeroes.contains(j))
						zeroes.add(j);
					if (numerator == 0)
						build[i][j] = 1;
					else
						build[i][j] = 0;
				}
				else {
					if (numerator/denominator > max)
						max = numerator/denominator;
					build[i][j] = numerator/denominator;
				}
			}
		}
		for (int i = 0; i < zeroes.size(); i++) {
			int j = zeroes.get(i);
			if (build[i][j] == 0)
				build[i][j] = max;
		}
		matrices.addMatrix(title, new Matrix(build));
	}
	
	public void calcScores() {
		for (int i = 0; i < data.size(); i++)
			data.get(i).calcScore(i, data.size());
	}
	
	public void sort() {
		Collections.sort(data);
	}
	
	public void printField(String field) {
		for (int i = 0; i < data.size(); i++)
			System.out.println(data.get(i).getField(field));
	}
	
	public void printStringField(String field) {
		for (int i = 0; i < data.size(); i++)
			System.out.println(data.get(i).getStringField(field));
	}
	
	public String toString() {
		//String to_return = data.size() + "\n";
		String to_return = "";
		for (int i = 0; i < data.size(); i++) {
			to_return += data.get(i).toString()+"\n";
		}
		return to_return;
	}
}
