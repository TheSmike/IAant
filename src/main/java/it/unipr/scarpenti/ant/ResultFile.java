package it.unipr.scarpenti.ant;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.unipr.scarpenti.ant.exception.AntGameException;
import it.unipr.scarpenti.ant.exception.InvalidPathException;

public class ResultFile {

	private Path file;

	public ResultFile(AppData appData) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
		Integer seedNumber = appData.getSeedNumber();
		if (seedNumber == null)
			seedNumber = 1;
		
		String model;
		String[] split = appData.getModelPath().split("\\\\");
		String substring = split[split.length-1].substring(0, 3);
		model = split[split.length-1];
//		if (substring.startsWith("T"))
//			model = "TREE";
//		else
//			model = "NN";
		
		String fileName = "RESULT_" + model + "_startSeed" + seedNumber + ".txt"; //df.format(new Date())
		System.out.println(fileName);
		Path pathOut = Paths.get(appData.getOutputFolder() + fileName);
		this.file = pathOut;
		write("SEED SCORE");
	}

	public void write(String text) throws AntGameException {

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(file.toFile(), true));
			writer.println(text);
		} catch (FileNotFoundException e) {
			throw new InvalidPathException(e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	public void write(int actualSeed, int score) throws InvalidPathException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(file.toFile(), true));
			writer.println(actualSeed + " " + score);
		} catch (FileNotFoundException e) {
			throw new InvalidPathException(e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}
