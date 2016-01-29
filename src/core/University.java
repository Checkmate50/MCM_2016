package core;

import java.util.Hashtable;

public class University implements Comparable<University> {
	private int unitid;
	private double score;
	private Hashtable<String, String> fields;
	private MatrixHolder matrices;

	public University(String[] titles, String[] data, MatrixHolder matrices) {
		fields = new Hashtable<String, String>();
		this.matrices = matrices;
		this.score = 0;
		//titles.length must equal data.length
		for(int i = 0; i < data.length; i++) {
			fields.put(titles[i].toLowerCase(), data[i]);
		}
		this.unitid = Integer.parseInt(fields.get("unitid"));
	}
	
	public void calcScore() {
		
	}
	
	public int getUnitid() {
		return this.unitid;
	}
	
	public double getScore() {
		return this.score;
	}

	public String getField(String field) {
		return fields.get(field.toLowerCase());
	}
	
	public int compareTo(University arg0) {
		if (this.getScore() > arg0.getScore())
			return 1;
		if (this.getScore() < arg0.getScore())
			return -1;
		return 0;
	}
	
	public String toString() {
		return fields.get("unitid") + "\t" + fields.get("instnm") + "\t" + score;
	}
}
