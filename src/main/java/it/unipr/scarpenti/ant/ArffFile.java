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
import java.util.Properties;

import it.unipr.scarpenti.ant.exception.AntGameException;
import it.unipr.scarpenti.ant.exception.InvalidPathException;

public class ArffFile {

	private Path file;

	public ArffFile(int visibility) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String fileName = String.format("ant_m%s_%s.arff", visibility, df.format(new Date()));
		Properties properties = PropertiesFactory.getProperties();
		Path pathOut = Paths.get(properties.getProperty("output_folder") + fileName);
		this.file = pathOut;
		InputStream templateResource = getClass().getClassLoader()
				.getResourceAsStream(String.format("template/template_m%s.arff", visibility));
		Files.copy(templateResource, pathOut);
	}

	public void writeComment(String comment) throws AntGameException {

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(file.toFile(), true));
			writer.println(comment);
		} catch (FileNotFoundException e) {
			throw new InvalidPathException(e);
		} finally {
			if (writer != null)
				writer.close();
		}

	}

	public void writeCase(int[][] neighbourhood, Direction direction) throws AntGameException {

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(file.toFile(), true));
			
			AntCase originalAntCase = new AntCase(neighbourhood, direction);
			writeLineCase(originalAntCase, writer);
			writeAllRotatedCase(writer, originalAntCase);
			
			AntCase flippedAntCase = flip(originalAntCase);
			writeLineCase(flippedAntCase, writer);
			writeAllRotatedCase(writer, flippedAntCase);
			
			writer.println();
		} catch (FileNotFoundException e) {
			throw new InvalidPathException(e);
		} finally {
			if (writer != null)
				writer.close();
		}

	}

	private void writeAllRotatedCase(PrintWriter writer, AntCase flippedAntCase) {
		AntCase oldAntCase;
		oldAntCase = flippedAntCase;
		for (int i = 0; i < 3; i++) {
			AntCase newAntCase = rotateCaseClockwise(oldAntCase);
			writeLineCase(newAntCase, writer);
			oldAntCase = newAntCase;
		}
	}

	private AntCase flip(AntCase previousAntCase) {
		AntCase newAntCase = new AntCase();
		newAntCase.neighbourhood = new int[previousAntCase.neighbourhood.length][previousAntCase.neighbourhood.length];
		for (int r = 0; r < newAntCase.neighbourhood.length; r++) {
			for (int c = 0; c < newAntCase.neighbourhood.length; c++) {
				newAntCase.neighbourhood[r][c] = previousAntCase.neighbourhood[newAntCase.neighbourhood.length - 1 - c][r];
			}
		}
		newAntCase.direction = previousAntCase.direction.clockwiseNext();
		return newAntCase;
	}

	private AntCase rotateCaseClockwise(AntCase previousAntCase) {
		AntCase newAntCase = new AntCase();
		newAntCase.neighbourhood = new int[previousAntCase.neighbourhood.length][previousAntCase.neighbourhood.length];
		for (int r = 0; r < newAntCase.neighbourhood.length; r++) {
			for (int c = 0; c < newAntCase.neighbourhood.length; c++) {
				newAntCase.neighbourhood[r][c] = previousAntCase.neighbourhood[newAntCase.neighbourhood.length - 1 - c][r];
			}
		}
		newAntCase.direction = previousAntCase.direction.clockwiseNext();
		return newAntCase;
	}

	private void writeLineCase(AntCase antCase, PrintWriter writer) {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < antCase.neighbourhood.length; r++) {
			for (int c = 0; c < antCase.neighbourhood.length; c++) {
				sb.append(antCase.neighbourhood[r][c]).append(',');
			}
		}
		sb.append(antCase.direction.toString());
		writer.println(sb.toString());
	}
	
	
	private class AntCase {
		
		public AntCase() {
		}
		
		public AntCase(int[][] neighbourhood, Direction direction) {
			this.direction = direction;
			this.neighbourhood = neighbourhood;
		}
		Direction direction = null;
		int[][] neighbourhood = null;
	}


}
