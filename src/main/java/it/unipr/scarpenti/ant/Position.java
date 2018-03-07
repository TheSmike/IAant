package it.unipr.scarpenti.ant;

public class Position {
	
		private int  r;
		private int  c;
		
		public Position(int r, int c) {
			this.r = r;
			this.c = c;
		}

		public int getR() {
			return r;
		}

		public int getC() {
			return c;
		}

		public void addToRows(int i) {
			r += i;
		}

		public void addToCols(int i) {
			c += i;
		}
		
		@Override
		public String toString() {
		// TODO Auto-generated method stub
		return String.format("[%d,%d]", r,c);
		}
		
		@Override
		public boolean equals(Object other) {
			if (other == null || !(other instanceof Position))
				return false;
			
			Position theOther = (Position) other; 
			return (this.r == theOther.r && this.c == theOther.c);
		}
		
}
