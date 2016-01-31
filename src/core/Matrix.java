package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Matrix {
	private double[][] matrix;
	
	public Matrix(double[][] data) {
		int max = 0;
		for (int i = 0; i < data.length; i++) 
			if (data[i].length > max)
				max = data[i].length;
		for (int i = 0; i < data.length; i++) {
			if (data[i].length < max) {
				int j;
				double[] temp = data[i];
				data[i] = new double[max];
				for (j = 0; j < temp.length; j++)
					data[i][j] = temp[j];
				for (; j < max; j++)
					data[i][j] = 0;
			}
		}
		
		matrix = data;
	}
	
	/**
	 * Multiplies by the constant c internally.  Useful for very large matrices
	 * @param c
	 */
	public void internMul(double c) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				matrix[i][j] *= c;
	}
	
	/**
	 * Takes ln of each component of the matrix internally.  Useful for very large matrices
	 * @param c
	 */
	public void internln() {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				matrix[i][j] = Math.log(matrix[i][j]);
	}
	
	/**
	 * exponentiates each component of the matrix internally.  Useful for very large matrices
	 * @param c
	 */
	public void interne(double c) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				matrix[i][j] = Math.exp(matrix[i][j]);
	}
	
	/**
	 * adds the given matrix to this matrix internally.  Useful for very large matrices
	 * @param m
	 */
	public void internAdd(Matrix m) {
		if (matrix.length != m.matrix.length || matrix[0].length != m.matrix[0].length)
			System.err.println("Given matrix of incompatible size!");
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				matrix[i][j] += m.matrix[i][j];
	}
	
	public double sumRow(int row) {
		double toReturn = 0;
		for (int i = 0; i < matrix[row].length; i++)
			toReturn += matrix[row][i];
		return toReturn;
	}
	
	public double sumCol(int col) {
		double toReturn = 0;
		for (int i = 0; i < matrix.length; i++)
			toReturn += matrix[i][col];
		return toReturn;
	}
	
	public Matrix mulConst(double c) {
		double[][] build = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				build[i][j] = c*matrix[i][j];
		return new Matrix(build);
	}
	
	public Matrix ln() {
		double[][] build = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				build[i][j] = Math.log(matrix[i][j]);
		return new Matrix(build);
	}
	
	public Matrix e() {
		double[][] build = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				build[i][j] = Math.exp(matrix[i][j]);
		return new Matrix(build);
	}
	
	public Matrix add(Matrix other) {
		if (matrix.length != other.matrix.length || matrix[0].length != other.matrix[0].length)
			return null;
		double[][] build = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				build[i][j] = matrix[i][j] + other.matrix[i][j];
		return new Matrix(build);
	}
	
	public Matrix mulPairwise(Matrix other) {
		if (matrix.length != other.matrix.length || matrix[0].length != other.matrix[0].length)
			return null;
		double[][] build = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				build[i][j] = matrix[i][j] * other.matrix[i][j];
		return new Matrix(build);
	}
	
	public Matrix divPairwise(Matrix other) {
		if (matrix.length != other.matrix.length || matrix[0].length != other.matrix[0].length)
			return null;
		double[][] build = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				build[i][j] = matrix[i][j] / other.matrix[i][j];
		return new Matrix(build);
	}
	
	public Matrix clone() {
		double[][] build = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				build[i][j] = matrix[i][j];
		return new Matrix(build);
	}
	
	public double[][] getMatrix() {
		return matrix;
	}
	
	public void write(String path, Boolean verbose) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File(path)));
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length-1; j++) {
					bw.write(((Double)matrix[i][j]).toString() + " ");
				}
				bw.write(((Double)matrix[i][matrix[0].length-1]).toString());
				if (verbose)
					System.out.println(i);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String to_return = "";
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length-1; j++) {
				to_return += ((Double)matrix[i][j]).toString() + " ";
			}
			to_return += ((Double)matrix[i][matrix[0].length-1]) + "\n";
		}
		return to_return;
	}
}
