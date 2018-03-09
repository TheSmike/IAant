package it.unipr.scarpenti.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

public class ArffFile {

	private int m;
	private PrintWriter writer;
	private Path file;

	public ArffFile(int m) throws Exception {
		this.m = m;
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String fileName = String.format("ant_m%s_%s.arff", m, df.format(new Date()));
		Properties properties = PropertiesFactory.getProperties();
		Path pathOut = Paths.get(properties.getProperty("output_folder") + fileName);
		this.file = pathOut;
		InputStream templateResource = getClass().getClassLoader().getResourceAsStream(
				String.format("template/template_m%s.arff", properties.getProperty("m_for_visibility")));
		// Path pathTemplate = Paths.get("template/template_m" +
		// properties.getProperty("m_for_visibility"));
		//Files.copy(templateResource, pathOut);
	}

	public void writeCase(int[][] neighbourhood, Direction keyCode) {

		try {
			writer = new PrintWriter(new FileOutputStream(file.toFile(), true), true);

			for (int i = 0; i < 4; i++) {
				StringBuilder sb = new StringBuilder();
				for (int r = 0; r < neighbourhood.length; r++) {
					for (int c = 0; c < neighbourhood.length; c++) {
						sb.append(neighbourhood[r][c]).append(',');
					}
				}
				sb.append(keyCode.toString());
				writer.println(sb.toString());

				if (i < 4 - 1) {
					int[][] newNeighbourhood = new int[neighbourhood.length][neighbourhood.length];
					for (int r = 0; r < newNeighbourhood.length; r++) {
						for (int c = 0; c < newNeighbourhood.length; c++) {
							newNeighbourhood[r][c] = neighbourhood[newNeighbourhood.length - 1 - c][r];
						}
					}
					keyCode = keyCode.clockwiseNext();
					neighbourhood = newNeighbourhood;
				}
			}
			writer.println();

		} catch (Exception e) {
			throw new RuntimeException("writeArff error", e);
		} finally {
			if (writer != null)
				writer.close();
		}

	}

}
