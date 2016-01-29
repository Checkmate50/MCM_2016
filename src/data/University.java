package data;

import java.util.Hashtable;

public class University implements Comparable<University> {
	private int unitid;
	private Hashtable<String, String> fields;

	public University(String[] titles, String[] data) {
		//titles.length must equal data.length
		for(int i = 0; i < data.length; i++) {
			fields.put(titles[i], data[i]);
		}
		this.unitid = Integer.parseInt(fields.get("unitid"));
	}
	
	public int getUnitid() {
		return this.unitid;
	}

	public String getField(String field) {
		return fields.get(field);
	}
	
	public int compareTo(University arg0) {
		return this.unitid - arg0.getUnitid();
	}
	
	public String toString() {
		return fields.get("name");
	}
}
