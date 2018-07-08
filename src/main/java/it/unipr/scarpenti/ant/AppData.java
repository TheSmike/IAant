package it.unipr.scarpenti.ant;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class AppData {

	public static final String PLAYER_IA = "IA";
	public static final String PLAYER_YOU = "YOU";
	
	private static final String PROP_WHO_PLAY = "who_play";
	private static final String PROP_MODEL_PATH = "model_path";
	private static final String PROP_M_FOR_VISIBILITY = "m_for_visibility";
	private static final String PROP_OUTPUT_FOLDER = "output_folder";
	private static final String PROP_WRITE_ARFF_FILE = "write_arff_file";
	private static final String PROP_SEED = "seed_number";
	private static final String PROP_LOOP_ON_SEEDS = "loop_on_seeds";
	private static final String PROP_LOOP_COUNT = "loop_count";
	private static final String PROP_AUTOPLAY = "autoplay";
	
	
	private static final String CONFIG_FILE_NAME = "antConfig.properties";
	private static final Properties DEFAULTS;
	
	
	private Properties props;

	static {
		DEFAULTS = new Properties();
		DEFAULTS.setProperty(PROP_WRITE_ARFF_FILE, "YES");
		DEFAULTS.setProperty(PROP_OUTPUT_FOLDER, "");
		DEFAULTS.setProperty(PROP_M_FOR_VISIBILITY, "1");
		DEFAULTS.setProperty(PROP_MODEL_PATH, "");
		DEFAULTS.setProperty(PROP_WHO_PLAY, AppData.PLAYER_YOU);
		DEFAULTS.setProperty(PROP_SEED, "");
	}

	public AppData() throws IOException {

		FileReader reader = null;
		try {
			File configFile = new File(CONFIG_FILE_NAME);
			if (!configFile.exists())
				configFile.createNewFile();

			reader = new FileReader(configFile);
			props = new Properties(DEFAULTS);
			props.load(reader);

		} finally {
			if (reader != null)
				reader.close();
		}
	}



	public void saveProperties() throws IOException {
		File configFile = new File(CONFIG_FILE_NAME);
		FileWriter writer = new FileWriter(configFile);
		props.store(writer, "play settings");
	}

	public int getVisualField() {
		return Integer.parseInt(props.getProperty(PROP_M_FOR_VISIBILITY));
	}

	public void setVisualField(int value) {
		props.setProperty(PROP_M_FOR_VISIBILITY, String.valueOf(value));
	}
	
	public String getModelPath() {
		return props.getProperty(PROP_MODEL_PATH);
	}
	public void setModelPath(String value) {
		props.setProperty(PROP_MODEL_PATH, value);
		
	}

	public String getWhoPlay() {
		return props.getProperty(PROP_WHO_PLAY);
	}
	public void setWhoPlay(String whoPlay) {
		props.setProperty(PROP_WHO_PLAY, whoPlay);
		
	}

	public boolean isWriteArffOn() {
		return "Y".equals(props.getProperty(PROP_WRITE_ARFF_FILE));
	}
	public void setWriteArffOn(boolean value) {
		props.setProperty(PROP_WRITE_ARFF_FILE, value?"Y":"N");
		
	}

	public String getOutputFolder() {
		return props.getProperty(PROP_OUTPUT_FOLDER);
	}
	public void setOutputFolder(String value) {
		props.setProperty(PROP_OUTPUT_FOLDER, value);		
	}
	
	public Integer getSeedNumber() {
		String property = props.getProperty(PROP_SEED);
		if (StringUtils.isBlank(property))
			return null;
		else
			return Integer.parseInt(property);
	}

	public void setSeedNumber(Integer value) {
		if (value == null)
			props.setProperty(PROP_SEED, "");
		else
			props.setProperty(PROP_SEED, String.valueOf(value));
	}

	public boolean isLoopOnSeedsOn() {
		return "Y".equals(props.getProperty(PROP_LOOP_ON_SEEDS));
	}

	public void setLoopOnSeeds(boolean value) {
		props.setProperty(PROP_LOOP_ON_SEEDS, value?"Y":"N");
		
	}

	public Integer getLoopCount() {
		String property = props.getProperty(PROP_LOOP_COUNT);
		if (StringUtils.isBlank(property))
			return null;
		else
			return Integer.parseInt(property);
	}
	
	public void setLoopCount(Integer loopCount) {
		if (loopCount == null)
			props.setProperty(PROP_LOOP_COUNT, "");
		else
			props.setProperty(PROP_LOOP_COUNT, String.valueOf(loopCount));
	}


	public boolean isAutoPlayOn() {
		return "Y".equals(props.getProperty(PROP_AUTOPLAY));
	}

	public void setAutoPlayOn(boolean value) {
		props.setProperty(PROP_AUTOPLAY, value?"Y":"N");
		
	}



}
