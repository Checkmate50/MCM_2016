package coefficient_calculator;

import java.util.Arrays;

import core.Matrix;

/**
 * Credit to Bo Zhu and Chong Wang for developing the model
 * @author Dietrich Geisler
 */
public class CoMain {
	
	public static void main(String[] args) {
//		double[] vals = {6, 9, 3, 2, 8, 3, 2, 4, 6, 4, 3};
//		double[] vals = {6, 9, 4, 2, 8, 3, 2, 4, 6, 4, 3};
		double[] vals = {6, 9, 4, 2, 7, 3, 2, 4, 7, 4, 3};
//		double[] vals = {6, 8, 3, 2, 8, 3, 2, 4, 6};
		Arrays.sort(vals);
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
				toReturn[i][j] = specialRound(row[i]/row[j]);
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
