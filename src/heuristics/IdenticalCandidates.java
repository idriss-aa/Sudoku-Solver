package heuristics;

import java.util.HashSet;
import java.util.Set;

import cmd.Move;
import model.Cell;
import model.Grid;

public class IdenticalCandidates implements Heuristic  {
	
	
	private Grid grid;
	
	public IdenticalCandidates(Grid grid) {
		this.grid=grid;
	}

	
	public Move getSolution() {
		int size = Grid.size;
	
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				Cell c = grid.getCellAt(i, j);
				Set<String> cands = c.getCandidates();
				if (cands != null) {
					Set<Cell> reasons = new HashSet<Cell>();
					// on a line
					for (int k = j + 1; k < size; ++k) {
						Cell cc = grid.getCellAt(i, k);
						if (identicalsCandidates(c, cc)) {
							reasons.add(cc);
						}
					}
					if (reasons.size() + 1 == c.getCandidates().size()) {
						if (isUseful(c, reasons, 0)) {
							return setSolution(c, reasons, 0);
						}
					}
					reasons.clear();
					
					// on a column
					for (int k = i + 1; k < size; ++k) {
						Cell cc = grid.getCellAt(k, j);
						if (identicalsCandidates(c, cc)) {
							reasons.add(cc);
						}
					}
					if (reasons.size() + 1 == c.getCandidates().size()) {
						if (isUseful(c, reasons, 1)) {
							return setSolution(c, reasons, 1);
						}
					}
					reasons.clear();
					
					//on a region
					
					for (int m = i / Grid.size; m < i / 12; ++m) {
						for (int n = j / Grid.size; n < j /12; ++n) {
							if (m != i || n != j) {
								Cell cc = grid.getCellAt(m, n);
								if (identicalsCandidates(c, cc)) {
									reasons.add(cc);
								}
							}
						}
					}
					if (reasons.size() + 1 == c.getCandidates().size()) {
						if (isUseful(c, reasons, 2)) {
							return setSolution(c, reasons, 2);
						}
					}
					reasons.clear();
				}
			}
		}
		return null;
	}

	
	private Move setSolution(Cell c, Set<Cell> reasons, int type) {
		Move move = new Move();
		
		for (Cell cl : reasons) {
			move.addReason(cl);
			
		}
		move.addReason(c);

		int line = c.getCoordinate().getX();
		int col = c.getCoordinate().getY();
		for (String v : c.getCandidates()) {
			
				switch (type) {
				case 0:
					for (int i = 0; i < Grid.size; ++i) {
						Cell cc = grid.getCellAt(line, i);
						if (cc != c && !reasons.contains(cc) && containsCandidate(cc, v)) {
						
						move.addAction(Move.ELIMINATE_CANDIDAT, cc, v);
						move.addReason(cc);
							
						}
					}
					break;
				case 1:
					for (int i = 0; i < Grid.size; ++i) {
						Cell cc = grid.getCellAt(i, col);
						if (cc != c && !reasons.contains(cc) && containsCandidate(cc, v)) {
							move.addAction(Move.ELIMINATE_CANDIDAT, cc, v);
							move.addReason(cc);
						}
					}
					break;
				case 2:
					
					int baseL = line / Grid.size;
					int baseC = col / Grid.size;
					for (int i = baseL; i < baseL + Grid.REGION_SIZE; ++i) {
						for (int j = baseC; j < baseC + Grid.REGION_SIZE; ++j) {
							Cell cc = grid.getCellAt(i, j);
							if (cc != c && !reasons.contains(cc) && containsCandidate(cc, v)) {
								move.addAction(Move.ELIMINATE_CANDIDAT, cc, v);
								move.addReason(cc);
								
							}
						}
					}
					break;
				default:
					return null;
				}
			
				
		}
		move.setDetails(setDescription(c, type));

		return move;
	}
	
	private String setDescription(Cell c, int type) {
		StringBuilder res = new StringBuilder();
		res.append("\nCandidat Identique :\n");
		String unity = null;
		int coord = -1;
		switch (type) {
			case 0:
				unity = "ligne";
				coord = c.getCoordinate().getX() + 1;
				break;
			case 1:
				unity = "colonne";
				coord = c.getCoordinate().getY() + 1;
				break;
			case 2:
				
				unity = "région";
				coord = (c.getCoordinate().getX() / Grid.REGION_SIZE) * Grid.REGION_SIZE
						+(c.getCoordinate().getY() / Grid.REGION_SIZE) * Grid.REGION_SIZE + 1;
				break;
			default:
				return "Pas de solution";
		}
		int size = c.getCandidates().size();
		res.append("Les " + size + " candidats ");
		for (String v : c.getCandidates()) {
			res.append(v + " ");
		}
		res.append("sont présents tous les " + size + " dans " + size 
				+ " cases de la meme " + unity + " : né" + coord + ", il est donc possible de "
				+ "les supprimer dans les autres cases de cette unité.");
		return res.toString();
	}
	
	private boolean identicalsCandidates(Cell c1, Cell c2) {
		if (c1.getCandidates() == null || c2.getCandidates() == null 
				|| c1.getCandidates().size() != c2.getCandidates().size()) {
			return false;
		}
		
		int count = 0;
		for (String v : c2.getCandidates()) {
			if (!c1.getCandidates().contains(v)) {
				return false;
			}
			count++;
		}
		return count == c1.getCandidates().size();
	}
	
	private boolean containsCandidate(Cell c1, String v) {
		if (c1.getCandidates() == null) {
			return false;
		}
		return c1.getCandidates().contains(v);
	}
	
	
	private boolean containsCandidates(Cell c1, Cell c2) {
		if (c1.getCandidates() == null || c2.getCandidates() == null) {
			return false;
		}
		
		for (String v : c1.getCandidates()) {
			if (c2.getCandidates().contains(v)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isUseful(Cell c, Set<Cell> cells, int type) {
		int line = c.getCoordinate().getX();
		int col = c.getCoordinate().getY();
		switch (type) {
			case 0:
				for (int i = 0; i < Grid.size; ++i) {
					Cell cc = grid.getCellAt(line, i);
					if (cc != c && !cells.contains(cc) 
							&& containsCandidates(c, cc)) {
						return true;
					}
				}
				break;
				
			case 1:
				for (int i = 0; i < Grid.size; ++i) {
					Cell cc = grid.getCellAt(i, col);
					if (cc != c && !cells.contains(cc) 
							&& containsCandidates(c, cc)) {
						return true;
					}
				}
				break;
				
			case 2:
				
				int baseL = line / Grid.size;
				int baseC = col / Grid.size;
				for (int i = baseL; i < baseL + Grid.REGION_SIZE; ++i) {
					for (int j = baseC; j < baseC + Grid.REGION_SIZE; ++j) {
						Cell cc = grid.getCellAt(i, j);
						if (cc != c && !cells.contains(cc) 
								&& containsCandidates(c, cc)) {
							return true;
						}
					}
				}
				break;
				
			default:
				break;
		}
		
		return false;
	}


	
}