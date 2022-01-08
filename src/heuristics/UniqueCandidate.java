package heuristics;
import java.util.Set;
import java.util.TreeSet;
import cmd.Move;
import model.Cell;
import model.Coordinate;
import model.Grid;


public class UniqueCandidate implements Heuristic {

	
	private Grid grid;
	
	
	public UniqueCandidate(Grid grid) {
		this.grid=grid;
	}
	 
	public Move getSolution() {
		Move move = new Move();
		Integer[] NumberLine = new Integer[Grid.size];
		Integer[] NumberColumn = new Integer[Grid.size];
		Integer[] NumberRegion = new Integer[Grid.size];
		Object[][] Candidates = new Object[Grid.size][Grid.size];
		Cell[] LastLine = new Cell[Grid.size];
		Cell[] LastColumn = new Cell[Grid.size];
		for (int i = 0; i < Grid.size; ++i) {
			NumberLine[i] = 0;
			NumberColumn[i] = 0;
			NumberRegion[i] = 0;
		}
		for (int i = 0; i < Grid.size; ++i) {
			for (int j = 0; j < Grid.size; ++j) {
				if (Candidates[i][j] == null) {
					if (grid.getCellAt(i, j).getCandidates() == null) {
						Candidates[i][j] = new TreeSet<String>();
					} else {
						Candidates[i][j] = grid.getCellAt(i, j).getCandidates();
					}
				}
				if (Candidates[j][i] == null) {
					if (grid.getCellAt(j, i).getCandidates() == null) {
						Candidates[j][i] = new TreeSet<String>();
					} else {
						Candidates[j][i] = grid.getCellAt(j, i).getCandidates();
					}
				}
			
				for (String s : (Set<String>) Candidates[i][j]) {
					NumberLine[Integer.valueOf(s)-1] += 1;
					if (NumberLine[Integer.valueOf(s)-1] < 2) {
						LastLine[Integer.valueOf(s)-1] = grid.getCellAt(i, j);
					}
				}
	
				for (String s : (Set<String>) Candidates[j][i]) {
					NumberColumn[Integer.valueOf(s)-1] += 1;
					if (NumberColumn[Integer.valueOf(s)-1] < 2) {
						LastColumn[Integer.valueOf(s)-1] = grid.getCellAt(j, i);
					}
				}
			}
			Coordinate Cordinate = null;
			for (int j = 0; j < Grid.size; ++j) {
				if (NumberLine[j] == 1) {
					if(!UniqueValueAtUnity(LastLine[j],Integer.toString(j+1) )) {
						move.addAction(Move.SET_VALUE,LastLine[j], Integer.toString(j+1));
						
						move.setDetails(getDescription(LastLine[j], Integer.toString(j+1)));
						
						return move;
					}
				} else {
					NumberLine[j] = 0;
					if (NumberColumn[j] == 1) {
						if(!UniqueValueAtUnity(LastColumn[j], Integer.toString(j+1))) {
							
							move.addAction(Move.SET_VALUE,LastColumn[j], Integer.toString(j+1));
							
							move.setDetails(getDescription(LastColumn[j], Integer.toString(j+1)));
							return move;
						}
					} else {
						NumberColumn[j] = 0;
					}
				}
			}
		}
		return null;
	}
	private String getDescription(Cell cell,String j) {
		StringBuilder Data = new StringBuilder();
		Data.append("Candidat Unique:\n");
		Data.append("Le candidat '" + j + "' sur la ");
		Data.append( "colonne");
		Data.append(" est disponible uniquement sur la cellule de coordonnée ");
		Data.append("(" + (cell.getCoordinate().getY() + 1) + ";" + (cell.getCoordinate().getX() + 1) + ")");
		return Data.toString();
	}
	
	private boolean UniqueValueAtUnity(Cell cell, String value) {
		
		
		Coordinate bc = cell.getCoordinate();
		for (int i = 0; i < Grid.size; ++i) {
			if (i != bc.getX()) {
				if (value.equals(grid.getCellAt(i, bc.getY()).getValue())) {
					return true;
				}
			}
			if (i != bc.getY()) {
				if (value.equals(grid.getCellAt(bc.getX(), i).getValue())) {
					return true;
				}
			}
		}
		int _row=(bc.getX()/3)*3;
		int _col=(bc.getY()/3)*3;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
					if(value.equals(grid.getCellAt(_row+i, _col+j).getValue())){
						return true;
					}
					
				}
			}
		
		return false;
	}
	
}
