package it.unipr.scarpenti.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;


public class Chessboard {

	private int[][] board;
	private final int N;
	private List<Position> foodList;

	public Chessboard(int N) {
		this.N = N;
		board = new int[N+2][N+2];
	}
	
	public Position initBaord() {
		Position antPosition = null;
		Random random = new Random();
		//init bussolotto da cui estrarre le N posizioni con cibo
		List<Integer> bussolotto = new ArrayList<>(N*N);
		for (int i = 0; i < N*N; i++) {
			bussolotto.add(i);
		}
		System.out.println("bussolotto = " + bussolotto);
		
		//estrazione delle N posizioni con cibo
		List<Integer> numeriEstratti = new ArrayList<>(N);
		for (int i = 0; i < N; i++) {
			int nextVal = random.nextInt(bussolotto.size());
			int position = bussolotto.remove(nextVal);
			numeriEstratti.add(position);
		}
		//estrazione posizione della formica
		int nextVal = random.nextInt(bussolotto.size());
		int antNumericPosition = bussolotto.remove(nextVal);
		System.out.println("numeriEstratti = " + numeriEstratti);
		System.out.println("antPposition = " + antNumericPosition);
		
		//creazione lista posizioni del cibo
		foodList = new ArrayList<>(); 
		//antNumericPosition = 0; //ONLY FOR TEST
		
		//set scacchiera con cibo
		for (int r = 0; r < N+2; r++) {
			for (int c = 0; c < N+2; c++) {	
				if (isEndOfTheBoard(r, c))
					board[r][c] = -N-2;
				else{
					if ( numeriEstratti.contains((r-1)*N+(c-1))) {
						board[r][c] = +1;
						foodList.add(new Position(r, c));
					}
					else 
						board[r][c] = 0;
					if (antNumericPosition == (r-1)*N+(c-1)) {
						antPosition = new Position(r, c); 
					}
				}
				
			}
		}
		
		logChessboard(antPosition);
		return antPosition;
		
	}

	public boolean isEndOfTheBoard(Position antPosition) {
		int r = antPosition.getR();
		int c = antPosition.getC();
		return isEndOfTheBoard(r, c);
	}
	
	public boolean isEndOfTheBoard(int r, int c) {
		return r==0 || c == 0 || r == N+1 || c == N+1;
	}
	
	public void logChessboard(Position antPosition) {
		int rPosAnt = antPosition.getR();
		int cPosAnt = antPosition.getC();
		System.out.println("BOARDCHESS");
		for (int r = 0; r < N+2; r++) {
			System.out.print("[");
			for (int c = 0; c < N+2; c++) {
				System.out.print( StringUtils.leftPad(String.valueOf(board[r][c]), 3));
				
				if (rPosAnt == r && cPosAnt == c)
					System.out.print("# ");
				else 
					System.out.print(", ");
					
				
			}
			System.out.println("]");
		}
		System.out.println("");
	}

	public int getPositionScoreAndDecrease(Position antPosition) {
		foodList.remove(antPosition);
		return board[antPosition.getR()][antPosition.getC()]--;
	}

	public List<Position> getFoodList() {
		return foodList;
	}

	public List<Integer> getChessBoardNeighbourhood(Position center, int neighbourhood) {
		List<Integer> retValue = new ArrayList<>();
		
			for (int r = center.getR()-neighbourhood; r <= center.getR()+neighbourhood; r++) {
				for (int c = center.getC()-neighbourhood; c <= center.getC()+neighbourhood; c++) {
					if (r == center.getR() && c == center.getC())
						continue;
					if (r < 0 || c < 0 || r >= N+2 || c >= N+2)
						retValue.add(-N-2);
					else
						retValue.add(board[r][c]);
			}
			
		}
		
		return retValue;
	}

	public Integer getSquareValue(int r, int c) {
		if (r>=0 && r<N+2 && c>=0 && c<N+2)
			return board[r][c];
		else 
			return null;
	}

}
