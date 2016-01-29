package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File pos = new File("data/IPEDSUID.txt");
		File dat = new File("data/CohortsData.txt");
		File dic = new File("data/ScorecardDictionary.txt");
		MatrixHolder matrices = new MatrixHolder();
		DataHolder data = new DataHolder(pos, dat, dic, matrices);
		
		data.removeTrait("hcm2", 1);
		//data.generateCompMatrix("ugds");
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/results.txt")));
			bw.write(data.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
