package core;

import java.util.Hashtable;

public class MatrixHolder {

	Hashtable<String, Matrix> data;
	
	public MatrixHolder() {
		data = new Hashtable<String, Matrix>();
	}
	
	public void addMatrix(String name, Matrix value) {
		data.put(name, value);
	}
	
	public Matrix getMatrix(String name) {
		return data.get(name);
	}
}
