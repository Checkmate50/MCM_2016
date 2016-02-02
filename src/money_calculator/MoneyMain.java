package money_calculator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Credit to Bo Zhu and Chong Wang for developing the model
 * @author Dietrich Geisler
 */
public class MoneyMain {

	public static void main(String[] args) {

		String line;
		String[] splitLine;
		double num;
		double total = 0;
		ArrayList<MiniUniversity> data = new ArrayList<MiniUniversity>();
		
		try {
			File money = new File("data/resultsTop61.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(money.toPath())));
			while((line = br.readLine()) != null) {
				splitLine = line.split("\t");
				num = Double.parseDouble(splitLine[0]);
				total += num;
				data.add(new MiniUniversity(splitLine[1], num));
			}
			for (int i = 0; i < data.size(); i++) {
				data.get(i).divValue(total/100000000); //100000000 = all the money!
			}
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		int t = 0;
		
		for (int i = 0; i < data.size(); i++) {
			t += (int)data.get(i).getValue();
		}
		
		System.out.println(t);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/money.txt")));
			for (int i = 0; i < data.size(); i++)
				bw.write(data.get(i).toString() + "\n");	
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
