package it.unipr.scarpenti.ant;

import java.util.HashMap;
import java.util.Map;

public enum Direction {
	UP(38, 0), 
	RIGHT(39, 1), 
	DOWN(40, 2), 
	LEFT(37, 3);
	
	private int keyCode;
	private int value;

	private Direction(int keyCode, int value) {
		this.keyCode = keyCode;
		this.value = value;
	}
	
	public static Direction getDirectionFromCode(int keyCode) {
		return Direction.codeMap.get(keyCode);
	}
	
	public Direction clockwiseNext() {
		return valueMap.get((this.value+1)%4);
	}
	
	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}



	private static final Map<Integer, Direction> codeMap;
	private static final Map<Integer, Direction> valueMap;
	
	static {
		codeMap = new HashMap<>();
		valueMap = new HashMap<>();
		Direction[] values = Direction.values();
		for (Direction direction : values) {
			codeMap.put(direction.keyCode, direction);
			valueMap.put(direction.value, direction);
		}
	}
}
