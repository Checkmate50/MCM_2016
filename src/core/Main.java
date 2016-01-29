package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File pos = new File("data/IPEDSUID.txt");
		File dat = new File("data/CohortsData.txt");
		File dic = new File("data/ScorecardDictionary.txt");
		MatrixHolder matrices = new MatrixHolder();
//		DataHolder data = new DataHolder(pos, dat, dic, matrices);
		
		/*data.removeTrait("hcm2", 1);
		//data.generateCompMatrix("ugds");
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/results.txt")));
			bw.write(data.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
//		double[] vals = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		//double[] vals = {1, 2, 7, 5, 5};
		double[] vals = {1, (1/3.0), (1/8.0)};
		System.out.println(generateCoeffecients(vals));
	}
	
	/**
	 * Generates special matrix.  Assumes that each element of row is an integer between 1 and row.length inclusive
	 * @param row
	 * @return
	 */
	public static Matrix generateCoeffecients(double[] row) {
		double[][] toReturn = new double[row.length][row.length];
		for (int i = 0; i < row.length; i++)
			for (int j = 0; j < row.length; j++)
				toReturn[i][j] = specialRound(row[j]/row[i]);
		for (int i = 0; i < row.length; i++)
			for (int j = i; j < row.length; j++)
				if (Math.abs(toReturn[i][j]*toReturn[j][i]-1)>.0001) {
					if (toReturn[i][j] >= 1)
						toReturn[i][j] += 1;
					else
						toReturn[j][i] += 1;
				}
		return new Matrix(toReturn);
	}
	
	/**
	 * Assumes that 0 < value < maxValue
	 * @param value
	 * @param maxValue
	 * @return
	 */
	public static double specialRound(double value) {
		if (value >= 0.75)
			return Math.round(value);
		double min = 100;
		int val = 0;
		for (int i = 1; i <= 9; i++) {
			if (Math.abs(value-(1.0/i)) < min) {
				min = Math.abs(value-(1.0/i));
				//System.out.println(min);
				val = i;
			}
		}
		return 1.0/(double)val;
	}
}
