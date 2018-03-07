package it.unipr.scarpenti.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArffFile {

	private static final int n = 1; // 1, 5, 10
	private int m;
	private PrintWriter writer;
	private Path file; 
	
	public ArffFile(int m) throws Exception {
		this.m = m;
		DateFormat df = new SimpleDateFormat("yyyymmdd_hhmm");
		String fileName = String.format("ant_m%s_partiten%s_%s.txt", m, n, df.format(new Date()));
		this.file = Paths.get("D:\\Google Drive\\unipr\\03 AI 17-18\\machine learning\\esercitazioni\\ex1\\es2\\dataset\\" + fileName);
	}

	public void writeCase(List<Integer> neighbourhood, String key) {
		
		try {
			writer = new PrintWriter(file.toFile(), "UTF-8");
			
			List<Integer> newNeighbourhood = neighbourhood;
			for (int i = 0; i < 3; i++) {
				StringBuilder sb = new StringBuilder();
				for (Integer value : newNeighbourhood) {
					sb.append(value).append(',');
				}
				List<Integer> oldNeighbourhood = newNeighbourhood;
				newNeighbourhood = new ArrayList<>();
				newNeighbourhood.add(oldNeighbourhood.get(2));
				
				
				sb.append(key);
				writer.println(sb.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException("writeArff error", e);
		}
		finally {
			if (writer != null)
				writer.close();
		}
		
		
	}
	
}
