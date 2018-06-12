package it.unipr.scarpenti.ant.ia;

import java.util.ArrayList;
import java.util.List;

import it.unipr.scarpenti.ant.AppData;
import it.unipr.scarpenti.ant.Direction;
import it.unipr.scarpenti.ant.exception.AntGameException;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class AntClassifier {

	private J48 classifier;
	private Instances dataSetStructure;
	private AppData appData;

	public AntClassifier(AppData appData) throws Exception {
		this.appData = appData;
		classifier = (J48) SerializationHelper.read(appData.getModelPath());
		System.out.println("classifier J48: " + classifier);

		initClassifier();
	}

	private void initClassifier() {
		// Declare the class attribute along with its values
		List<String> fvClassVal = new ArrayList<>(4);
		fvClassVal.add("UP");
		fvClassVal.add("DOWN");
		fvClassVal.add("RIGTH");
		fvClassVal.add("LEFT");
		Attribute classAttribute = new Attribute("direction", fvClassVal);

		ArrayList<Attribute> fvWekaAttributes = new ArrayList<>();

		int matSide = appData.getVisualField() * 2 + 1;
		
		for (int r = 0; r < matSide; r++) {
			for (int c = 0; c < matSide; c++) {
				fvWekaAttributes.add(new Attribute(String.format("index-%s-%s", r, c)));
			}

		}
		fvWekaAttributes.add(classAttribute);

		dataSetStructure = new Instances("Ant game m " + appData.getVisualField(), fvWekaAttributes, 10);
		dataSetStructure.setClassIndex(matSide*matSide);
	}

	public Direction getDirection(int[][] neighbourhood) throws Exception {

		Instance instance = new DenseInstance(neighbourhood.length * neighbourhood.length);

		for (int r = 0; r < neighbourhood.length; r++) {
			for (int c = 0; c < neighbourhood.length; c++) {
				instance.setValue(r * neighbourhood.length + c, neighbourhood[r][c]);
			}
		}
		instance.setDataset(dataSetStructure);
		System.out.println("instance => " + instance);
		double[] fDistribution = classifier.distributionForInstance(instance);

		double max = 0;
		int index = -1;
		for (int i = 0; i < fDistribution.length; i++) {
			System.out.println("fDistribution[i]=" + fDistribution[i]);
			if (fDistribution[i] > max) {
				index = i;
				max = fDistribution[i];
			}
		}
		System.out.println("direction : " + index + ", likelihood : " + max);
		switch (index) {
		case 0:
			return Direction.UP;
		case 1:
			return Direction.DOWN;
		case 2:
			return Direction.RIGHT;
		case 3:
			return Direction.LEFT;
		default:
			throw new AntGameException("Direzione non prevista", null);
		}

	}

}
