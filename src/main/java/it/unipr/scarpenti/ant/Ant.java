package it.unipr.scarpenti.ant;

public class Ant {
	
	
	private Position antPosition;
			
	public Ant(Position initAntPosition) {
		antPosition = initAntPosition;
	}

	/*public void setAntPosition(AntPosition antPosition) {
		this.antPosition = antPosition;
	}*/
	
	public Position getAntPosition() {
		return antPosition;
	}

	public void moveUp() {
		antPosition.addToRows(-1);
	}

	public void moveDown() {
		antPosition.addToRows(1);
	}
	
	public void moveLeft() {
		antPosition.addToCols(-1);
	}

	public void moveRight() {
		antPosition.addToCols(1);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Ant is on " + antPosition;
	}
}
