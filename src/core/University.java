package core;

import java.util.Hashtable;

public class University implements Comparable<University> {
	private int unitid;
	private double score;
	private Hashtable<String, String> fields;
	private Hashtable<String, Double> num_fields;
	private MatrixHolder matrices;

	public University(String[] titles, String[] data, Hashtable<String, String> types, MatrixHolder matrices) {
		fields = new Hashtable<String, String>();
		num_fields = new Hashtable<String, Double>();
		this.matrices = matrices;
		this.score = 0;
		//titles.length must equal data.length
		for(int i = 0; i < data.length; i++) {
			if (types.get(titles[i]).equals("string"))
				fields.put(titles[i].toLowerCase(), data[i]);
			else 
				num_fields.put(titles[i].toLowerCase(), Double.parseDouble(data[i]));
		}
		this.unitid = (int)(double)num_fields.get("unitid");
	}
	
	public void calcScore(int index) {
		//2919 is # of universities --> 8520561 is # of universities squared
		score = matrices.getMatrix("goal").sumRow(index)/2919.0;
//		score = matrices.getMatrix("goal").sumCol(index)/2919.0;
//		score = (matrices.getMatrix("goal").sumRow(index)*matrices.getMatrix("goal").sumCol(index))/8520561.0;
//		score = matrices.getMatrix("goal").sumRow(index)/matrices.getMatrix("goal").sumCol(index);
	}
	
	public int getUnitid() {
		return this.unitid;
	}
	
	public double getScore() {
		return this.score;
	}
	
	public void setField(String field, double value) {
		String lower = field.toLowerCase();
		num_fields.remove(lower);
		num_fields.put(lower, value);
	}

	public void addField(String field, double value) {
		num_fields.put(field.toLowerCase(), value);
	}
	
	public void addField(String field, String value) {
		fields.put(field.toLowerCase(), value);
	}
	
	public double getField(String field) {
		return num_fields.get(field.toLowerCase());
	}
	
	public String getStringField(String field) {
		return fields.get(field.toLowerCase());
	}
	
	public int compareTo(University arg0) {
		if (this.getScore() > arg0.getScore())
			return -1;
		if (this.getScore() < arg0.getScore())
			return 1;
		return 0;
	}
	
	public String toString() {
		return unitid + "\t" + fields.get("instnm") + "\t" + score;
	}
}
