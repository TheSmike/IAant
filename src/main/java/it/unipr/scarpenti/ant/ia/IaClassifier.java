package it.unipr.scarpenti.ant.ia;

import it.unipr.scarpenti.ant.AppData;
import it.unipr.scarpenti.ant.Direction;
import it.unipr.scarpenti.ant.exception.AntGameException;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class IaClassifier {

	private J48 classifier;
	private Instances isTrainingSet;

	public IaClassifier(AppData appData) throws Exception {
		classifier = (J48) SerializationHelper.read(appData.getModelPath());
		System.out.println("classifier J48: " + classifier);

		// Declare two numeric attributes
		Attribute Attribute1 = new Attribute("index-0-0");
		Attribute Attribute2 = new Attribute("index-0-1");
		Attribute Attribute3 = new Attribute("index-0-2");

		Attribute Attribute4 = new Attribute("index-1-0");
		Attribute Attribute5 = new Attribute("index-1-1");
		Attribute Attribute6 = new Attribute("index-1-2");

		Attribute Attribute7 = new Attribute("index-2-0");
		Attribute Attribute8 = new Attribute("index-2-1");
		Attribute Attribute9 = new Attribute("index-2-2");

		// Declare the class attribute along with its values
		FastVector fvClassVal = new FastVector(4);
		fvClassVal.addElement("UP");
		fvClassVal.addElement("DOWN");
		fvClassVal.addElement("RIGTH");
		fvClassVal.addElement("LEFT");
		Attribute ClassAttribute = new Attribute("direction", fvClassVal);

		// Declare the feature vector
		FastVector fvWekaAttributes = new FastVector(10);
		fvWekaAttributes.addElement(Attribute1);
		fvWekaAttributes.addElement(Attribute2);
		fvWekaAttributes.addElement(Attribute3);
		fvWekaAttributes.addElement(Attribute4);
		fvWekaAttributes.addElement(Attribute5);
		fvWekaAttributes.addElement(Attribute6);
		fvWekaAttributes.addElement(Attribute7);
		fvWekaAttributes.addElement(Attribute8);
		fvWekaAttributes.addElement(Attribute9);
		fvWekaAttributes.addElement(ClassAttribute);

		isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
		isTrainingSet.setClassIndex(9);
	}

	public Direction getDirection(int[][] neighbourhood) throws Exception {

		Instance instance = new DenseInstance(10);

		for (int r = 0; r < neighbourhood.length; r++) {
			for (int c = 0; c < neighbourhood.length; c++) {
				instance.setValue(r * neighbourhood.length + c, neighbourhood[r][c]);
			}
		}
		instance.setDataset(isTrainingSet);
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
