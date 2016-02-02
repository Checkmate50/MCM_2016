package money_calculator;

/**
 * Credit to Bo Zhu and Chong Wang for developing the model
 * @author Dietrich Geisler
 */
public class MiniUniversity {

	private String name;
	private double score;
	private double value;
	
	public MiniUniversity(String name, double score) {
		this.name = name;
		this.score = score;
		this.value = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getScore() {
		return this.score;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public void divValue(double amount) {
		value = score/amount;
	}
	
	public String toString() {
		return (int)value + "\t" + name + "\t" + score;
	}
	
}
