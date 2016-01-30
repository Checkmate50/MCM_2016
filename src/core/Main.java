package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File pos = new File("data/IPEDSUIDSmall.txt");
		File dat = new File("data/CohortsData.txt");
		File dic = new File("data/ScorecardDictionary.txt");
		MatrixHolder matrices = new MatrixHolder();
		
		//The eigenvectors from below give us coefficients:
		double[] co = {.1058, .1058, .1904, -.1904, .1904, -.2056, .2056, -.3673, .3673, .4627, -.5481};
		//corresponding to categories:
		String[] cat = {"ugds", "rpy_3yr_rt_supp", "ret", "pctpell", "gt_25k_p6", "ugds/gr", "gr", "grade", "vm", "md_earn_wne_p10", "tuition"};
		
		System.out.println("reading in data");
		
		DataHolder data = new DataHolder(pos, dat, dic, matrices);
		
		System.out.println("generating additional traits");
		
		data.removeTrait("hcm2", 1);
		
		String[] ref_avgs = {"ret_ft4","ret_ftl4","ret_pt4","ret_ptl4",};
		data.generateAverageTrait("ret", ref_avgs);
		
		String[] gr_avgs = {"c150_4_pooled_supp","c150_4_pooled_supp"};
		data.generateAverageTrait("gr", gr_avgs);
		
		data.generateRatioTrait("ugds/gr", "ugds", "gr");
		
		String[] score_avgs = {"satvr25","satvr75","satmt25","satmt75","satwr25","satwr75","satvrmid","satmtmid","satwrmid",
				"actcm25","actcm75","acten25","acten75","actmt25","actmt75","actcmmid","actenmid","actmtmid"};
		data.generateAverageScoreTrait("grade", score_avgs);
		
		String[] tuition_avgs = {"npt4_pub","npt41_pub","npt42_pub","npt43_pub","npt44_pub","npt45_pub",
				"npt4_priv","npt41_priv","npt42_priv","npt43_priv","npt44_priv","npt45_priv",};
		data.generateAverageTrait("tuition", tuition_avgs);
		
		String[] vm_avgs = {"pcip01","pcip03","pcip04","pcip05","pcip09","pcip10","pcip11","pcip12","pcip12","pcip13","pcip14","pcip15","pcip16","pcip19",
				"pcip22","pcip23","pcip24","pcip25","pcip26","pcip27","pcip29","pcip30","pcip31","pcip38","pcip39",
				"pcip40","pcip41","pcip42","pcip43","pcip44","pcip45","pcip46","pcip47","pcip48","pcip49","pcip50","pcip51","pcip52","pcip54"};
		data.generateAverageTrait("vm", vm_avgs);
		
		System.out.println("generating comparison matrices");
		
		for (int i = 0; i < cat.length; i++) {
			data.generateCompMatrix(cat[i]);
			System.out.println(cat[i]);
		}
		
		System.out.println("multiplying by coefficients");
		
		for (int i = 0; i < co.length; i++) {
			matrices.getMatrix(cat[i]).internMul(co[i]);
			System.out.println(cat[i]);
		}
		
		System.out.println("summing up matrices");
		
		data.generateEmptyMatrix("goal");
		for (int i = 0; i < cat.length; i++) {
			matrices.getMatrix("goal").internAdd(matrices.getMatrix(cat[i]));
			System.out.println(cat[i]);
		}
		
		System.out.println("calculating scores");
		
		data.calcScores();
		
		System.out.println("sorting list");
		
		data.sort();
		
		System.out.println("writing results to file");
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/results.txt")));
			bw.write(data.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("done");
		
//		double[] vals = {6, 9, 3, 2, 8, 3, 2, 4, 6, 4, 3};
//		Arrays.sort(vals);
//		System.out.println(generateCoeffecients(vals));
	}
	
	/**
	 * Generates special matrix.  Assumes that each element of row is an integer between 1 and row.length inclusive
	 * @param row
	 * @return
	 */
	public static Matrix generateCoeffecients(double[] row) {
		double[][] toReturn = new double[row.length][row.length];
		for (int i = 0; i < row.length; i++)
			for (int j = 0; j < row.length; j++)
				toReturn[i][j] = specialRound(row[i]/row[j]);
		for (int i = 0; i < row.length; i++)
			for (int j = i; j < row.length; j++)
				if (Math.abs(toReturn[i][j]*toReturn[j][i]-1)>.0001) {
					if (toReturn[i][j] >= 1)
						toReturn[i][j] += 1;
					else
						toReturn[j][i] += 1;
				}
		return new Matrix(toReturn);
	}
	
	/**
	 * Assumes that 0 < value < maxValue
	 * @param value
	 * @param maxValue
	 * @return
	 */
	public static double specialRound(double value) {
		if (value >= 0.75)
			return Math.round(value);
		double min = 100;
		int val = 0;
		for (int i = 1; i <= 9; i++) {
			if (Math.abs(value-(1.0/i)) < min) {
				min = Math.abs(value-(1.0/i));
				//System.out.println(min);
				val = i;
			}
		}
		return 1.0/(double)val;
	}
}
