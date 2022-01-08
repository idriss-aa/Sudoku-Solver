package heuristics;

import java.util.HashMap;
import java.util.Map;
import cmd.Move;
import helpers.Unity;
import model.Cell;
import model.Grid;
import model.StdGrid;

public class Coloring implements Heuristic   {

	
	
	public static final int IDENTICALS_CELLS = 2;
	public static final int MINIMUM_CELLS = 4;
	

	
	private Cell[] start;
	
	private Map<Cell, Colour> result;
	private Grid grid;
	

	public Coloring(Grid grid) {
		this.grid = grid;
		start = new Cell[IDENTICALS_CELLS];
		result = new HashMap<Cell, Colour>();
	}
	

	
	
	public Move getSolution() {
		for (Unity u : Unity.values()) {
			for (String cand : StdGrid.defaultValueSet()) {
				for (int i = 0; i < Grid.size; ++i) {
					twoCandidat(cand, u, i);
					if (isStart()) {
						setResult(cand, u, i);	
						
						Cell[] cells = new Cell[result.size()];
						int cpt = 0;
						for (Cell ci : result.keySet()) {
							cells[cpt] = ci;
							++cpt;
						}
						Colour col = checkResult(cells, cand);
						if (col != null) {
							return setSolution(col, cand);
						} else {
							Cell cell = checkResult_B(cells, cand);
							if (cell != null) {
								return setSolution(cell, cand);
							} else {
								Cell[] cls = checkResult_C(cells, cand);
								if (cls != null) {
									return setSolution(cls, cand);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}


	private Move setSolution(Colour col, String cand) {
		Move move = new Move();
		
		for (Cell c : result.keySet()) {
			if (result.get(c).equals(col)) {
				
					move.addAction(Move.ELIMINATE_CANDIDAT,c, cand);
				
				move.addReason( c);
			} else {
				move.addReason( c);
			}
		}
		move.setDetails("\n Coloring :\n"
			  + "Il est possible de supprimer les candidats " 
				+ cand.toString() + " des cases en " + col.getName() 
				+ ", car il y en a deux dans la meme unité");
		return move;
	}
	
	private Move setSolution(Cell cell, String cand) {
		Move move = new Move();
		
			move.addAction(Move.ELIMINATE_CANDIDAT,cell, cand);
		
		move.addReason( cell);
		
		for (Cell c : result.keySet()) {
			move.addReason( c);	
		}
		
		move.setDetails("\n Coloring :\n"
		+ "Il est possible de supprimer les candidats " + cand
				+ " Ã  l'intersection des deux couleurs");
		
		return move;
	}
	
	private Move setSolution(Cell[] cells, String cand) {
		Move move = new Move();
		
		for (Cell ci : cells) {
			
				move.addAction(Move.ELIMINATE_CANDIDAT,ci, cand);
		
			move.addReason( ci);
		}
		
		for (Cell c : result.keySet()) {
			move.addReason( c);	
		}
		
		move.setDetails("\n Coloring :\n"
		+ "Les candidats " + cand + " des cases en rouge "
				+ "peuvent etre supprimé car deux couleurs sont dans la meme"
				+ " unite.");
	
		return move;
	}
	
	
	
	private Cell[] checkResult_C(Cell[] cells, String cand) {
		for (int i = 0; i < cells.length; ++i) {
			for (int j = i + 1; j < cells.length; ++j) {
				if (!result.get(cells[i]).equals(result.get(cells[j]))) {
					if (getCorOfUnity(cells[i], Unity.REGION) 
							== getCorOfUnity(cells[j], Unity.REGION)) {
						Cell[] res = getCellonRegioin(getCorOfUnity(cells[i], 
								Unity.REGION), cand);
						if (res != null) {
							return res;
						}
					}
				}
			}
		}
		return null;
	}
	
	
	private Cell[] getCellonRegioin(int nb, String cand) {
		Cell[] res = new Cell[Grid.size];
		int cpt = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(Unity.REGION, nb, i);
			int y = getY(Unity.REGION, nb, i);
			Cell ci = grid.getCellAt(x, y);
			if (!result.containsKey(ci) && ci.getCandidates() != null 
					&& ci.getCandidates().contains(cand)) {
				res[cpt] = ci;
				++cpt;
			}
		}
		
		if (cpt == 0) {
			return null;
		}
		Cell[] resCell = new Cell[cpt];
		for (int i = 0; i < res.length; ++i) {
			if (res[i] != null) {
				resCell[i] = res[i];
			}
		}
		
		return resCell;
	}
	
	
	
	private Colour checkResult(Cell[] cells, String c) {
		
		if (result.keySet().size() <= MINIMUM_CELLS) {
			return null;
		}
		
		for (int i = 0; i < cells.length; ++i) {
			for (int j = i + 1; j < cells.length; ++j) {
				for (Unity unit : Unity.values()) {
					if (checkWrongCells(cells[i], cells[j], c, unit)) {
						return result.get(cells[i]);
					}	
				}
			}
		}
		return null;
	}
	
	
	private boolean checkWrongCells(Cell c1, Cell c2, String cand, Unity u) {
		return result.get(c1).equals(result.get(c2)) 
				&& getCorOfUnity(c1, u) == getCorOfUnity(c2, u);
	}

	
	private void setResult(String c, Unity unit, int n) {
		result.clear();
		for (int i = 0; i < start.length; ++i) {
			Colour coul = Colour.values()[i];
			if (!result.containsKey(start[i])) {
				result.put(start[i], coul);
				setCellsOfResult(c, start[i], unit, coul.getNextColour());		
			}
		}
	}
	
	
	private void setCellsOfResult(String cand, Cell c, Unity unit, Colour coul) {
		for (Unity u : Unity.values()) {
			if (!u.equals(unit)) {
				Cell res = checkOnly2(c, cand, u, getCorOfUnity(c, u));
				if (res != null) {
					if (result.containsKey(res)) {
						return;
					}
					result.put(res, coul);
					setCellsOfResult(cand, res, u, coul.getNextColour());
				}
			}
		}
	}
	
	
	
	private int getCorOfUnity(Cell c, Unity unit) {
	
		switch (unit) {
		case LINE:
			return c.getCoordinate().getX();
		case COL:
			return c.getCoordinate().getY();
		case REGION:
			return (c.getCoordinate().getX() / Grid.REGION_SIZE) * Grid.REGION_SIZE 
					+ c.getCoordinate().getY() / Grid.REGION_SIZE;
		}
		return -1;
	}
	
	
	
	private Cell checkOnly2(Cell cell, String c, Unity u, int n) {
		Cell res = null;
		int count = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(u, n, i);
			int y = getY(u, n, i);
			Cell ci = grid.getCellAt(x, y);
			if (ci.getCandidates() != null) {
				if (ci.getCandidates().contains(c)) {
					if (count == IDENTICALS_CELLS) {
						return null;
					}
					if (ci != cell) {
						res = ci;
					}
					++count;
				}
			}
		}
		return count == IDENTICALS_CELLS ? res : null;
	}
	
	
	
	private void twoCandidat(String c, Unity u, int n) {
		clearStart();
		int count = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(u, n, i);
			int y = getY(u, n, i);
			Cell ci = grid.getCellAt(x, y);
			if (ci.getCandidates() != null) {
				if (ci.getCandidates().contains(c)) {
					if (count == IDENTICALS_CELLS) {
						clearStart();
						return;
					}
					start[count] = ci;
					++count;
				}
			}
		}
	}
	
	
	
	private int getX(Unity u, int n, int nb) {

		int x  = -1;
		switch (u) {
			case LINE:
				x = n;
				break;
			case COL:
				x = nb;
				break;
			case REGION:
				x = (n / Grid.REGION_SIZE) * Grid.REGION_SIZE + nb / Grid.REGION_SIZE;
				break;
			default:
				break;
		}
		return x;
	}
	
	
	
	private int getY(Unity u, int n, int nb) {
		
		int y  = -1;
		switch (u) {
			case LINE:
				y = nb;
				break;
			case COL:
				y = n;
				break;
			case REGION:
				y = (n % Grid.REGION_SIZE) * Grid.REGION_SIZE + nb % Grid.REGION_SIZE;
				break;
			default:
				break;
		}
		return y;
	}
	
	
	private boolean isStart() {
		for (Cell c : start) {
			if (c == null) {
				return false;
			}
		}
		return true;
	}
	
	
	
	private void clearStart() {
		for (int i = 0; i < start.length; ++i) {
			start[i] = null;
		}
	}
	

	


	private Cell checkResult_B(Cell[] cells, String c) {
		if (result.keySet().size() < MINIMUM_CELLS) {
			return null;
		}
	
		for (int i = 0; i < cells.length; ++i) {
			for (int j = i + 1; j < cells.length; ++j) {
				if (!result.get(cells[i]).equals(result.get(cells[j]))) {
					if (checkUnity(cells[i], cells[j])) {
						Cell ci = getCommonCell(cells[i], cells[j], c);
						if (ci != null) {
							return ci;
						}
					}
				}
			}
		}
		return null;
	}
	

	private Cell getCommonCell(Cell c1, Cell c2, String cand) {
		Cell ci = grid.getCellAt(c1.getCoordinate().getX(), 
				c2.getCoordinate().getY());
		if (!result.containsKey(ci) && ci.getCandidates() != null
				&& ci.getCandidates().contains(cand)) {
			return ci;
		}
		ci = grid.getCellAt(c2.getCoordinate().getX(), 
				c1.getCoordinate().getY());
		if (!result.containsKey(ci) && ci.getCandidates() != null
				&& ci.getCandidates().contains(cand)) {
			return ci;
		}
		return null;
	}
	
	
	private boolean checkUnity(Cell c1, Cell c2) {
		for (Unity u : Unity.values()) {
			if (getCorOfUnity(c1, u) == getCorOfUnity(c2, u)) {
				return false;
			}
		}
		return true;
}
		

	private enum Colour {
		GREEN("Green"),
		BLUE("Blue");
		
		
		
		private String name;
	
		
		
		
		Colour(String s) {
			name = s;
			
		}
		
	
		public String getName() {
			return name;
		}
		
		
		
		public Colour getNextColour() {
			return Colour.values()[(this.ordinal() + 1) % Colour.values().length];
		}
	}

	 

}
