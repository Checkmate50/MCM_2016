package data;

import java.io.File;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File pos = new File("data/IPEDSUID.txt");
		File dat = new File("data/CohortsData.txt");
		File dic = new File("data/ScorecardDictionary.txt");
		DataHolder data = new DataHolder(pos, dat, dic);
	}

}
