package data;

public class University implements Comparable<University> {
	private int unitid;
	private String[] data;
	public University(int unitid, String[] data) {
		this.unitid = unitid;
		this.data = data;
	}
	
	public String[] getData() {
		return this.data;
	}
	
	public int getUnitid() {
		return this.unitid;
	}

	public int compareTo(University arg0) {
		return this.unitid - arg0.getUnitid();
	}
	
	public String toString() {
		String to_return = ((Integer)this.unitid).toString();
		for(int i = 0; i < data.length; i++) {
			to_return += data[i];
		}
		return  to_return;
	}
}
