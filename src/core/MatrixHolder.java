package core;

import java.util.Hashtable;

/**
 * Credit to Bo Zhu and Chong Wang for developing the model
 * @author Dietrich Geisler
 */
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
