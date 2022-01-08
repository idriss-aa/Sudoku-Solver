package heuristics;



import cmd.Move;
import model.Cell;
import model.Grid;

public class InteractionsBetweenRegion implements Heuristic {
	
	
	
	private Grid grid;
	
	
	public InteractionsBetweenRegion(Grid grid) {
		this.grid=grid;
	}
	
	
	
	
	public Move getSolution() {
		
		for (int i = 0; i < Grid.size; ++i) {
			for (int j = 0; j < Grid.size; ++j) {
			}
		}
		for (int i = 0; i < Grid.size; ++i) {
			for (int j = 0; j < Grid.size; ++j) {
				if (grid.getCellAt(i, j).getCandidates() != null) {
					for (String v : grid.getCellAt(i, j).getCandidates()) {
						// on the line
						int findedL = -1;
						for (int k = 0; k < Grid.size; ++k) {
							if (!isOnTheSameBlock(j, k)) {
								Cell cc = grid.getCellAt(i, k);
								if (cc.getCandidates() != null 
										&& cc.getCandidates().contains(v)) {
									findedL = k;;
									break;
								}
							}
						}
						if (findedL == -1 
								&& isInteraction(i, j, v, true)
								&& isUseful(i, j, v, true)
								&& test(i, j, v, true)) {
							return setSolution(i, j, v, true);
						}
					
						// on the column
						int findedC = -1;
						for (int k = 0; k < Grid.size; ++k) {
							if (!isOnTheSameBlock(i, k)) {
								Cell cc = grid.getCellAt(k, j);
								if (cc.getCandidates() != null 
										&& cc.getCandidates().contains(v)) {
									findedC = k;
									break;
								}
							}
						}
						if (findedC == -1 
								&& isInteraction(i, j, v, false)
								&& isUseful(i, j, v, false)
								&& test(i, j, v, false)) {
							return setSolution(i, j, v, false);
						}
					}
					
				}
			}
		}
		return null;
	}
	
	
	
	private Move setSolution(int line, int col, String value, boolean isLine) {
		Move move = new Move();

		int base = (isLine ? line / Grid.size : col / Grid.size);
		int t = isLine ? col : line;
		int tbase = t / Grid.size;
		for (int i = base; i < base + Grid.REGION_SIZE; ++i) {
			for (int j = 0; j < Grid.size; ++j) {
				if (i != (isLine ? line : col)) {
					Cell c = isLine ? grid.getCellAt(i, j) 
							: grid.getCellAt(j, i);
					if (c.getCandidates() != null) {
						if (j != tbase + j % Grid.REGION_SIZE) {
							if (c.getCandidates().contains(value)) {
								move.addReason(c);
								
							}
						} else {
							if (c.getCandidates().contains(value)) {
								
								move.addAction(Move.ELIMINATE_CANDIDAT,c,value);
								move.addReason(c);
							}
						}
					}
				}
			}
		}
		
		move.setDetails(getDescription(value, isLine));
		
		return move;
	}
	
	private String getDescription(String value, boolean isLine) {
		StringBuilder res = new StringBuilder();
		res.append("\n interaction between regions:\n");
		String s = isLine ? "lignes" : "colonnes";
		res.append("Le candidat " + value + " n'est présent que sur 2 " + s + " de 2 régions alignées.\n"
				+ "On peut donc le supprimer dans ces " + s + " de la Grid.REGION_SIZEeme région.");
		return res.toString();
	}
	
	private boolean isOnTheSameBlock(int x1, int x2) {
	
		return x1 / Grid.REGION_SIZE == x2 / Grid.REGION_SIZE;
	}
	
	private boolean isInteraction(int numL, int numC, String v, boolean isLine) {
	
		int t = (isLine ? numL : numC);
		int base = t / Grid.REGION_SIZE * Grid.REGION_SIZE;
		for (int i = base; i < base + Grid.REGION_SIZE; ++i) {
			int inv = (!isLine ? numL : numC) / Grid.REGION_SIZE * Grid.REGION_SIZE; 
			if (i != t) {
				int cpt = 0;
				boolean finded = false;
				for (int j = 0; j < Grid.size; ++j) {
					if (j != inv + j % Grid.REGION_SIZE) {
						if (j % Grid.REGION_SIZE == 0) {
							finded = false;
						}
						Cell c = isLine ? grid.getCellAt(i, j) : grid.getCellAt(j, i);
						if (v.equals(c.getValue())) {
							return false;
						}
						if (!finded && c.getCandidates() != null && c.getCandidates().contains(v)) {
							finded = true;
							cpt += 1;
						}
					}
				}
				if (cpt != 2) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isUseful (int numL, int numC, String v, boolean isLine) {
	
		int t = (isLine ? numL : numC);
		int base = t / Grid.REGION_SIZE * Grid.REGION_SIZE;
		for (int i = base; i < base + Grid.REGION_SIZE; ++i) {
			if (i != t) {
				int inv = (!isLine ? numL : numC) / Grid.REGION_SIZE * Grid.REGION_SIZE; 
				for (int j = inv; j < Grid.REGION_SIZE + inv; ++j) {
					Cell c = isLine ? grid.getCellAt(i, j) : grid.getCellAt(j, i);
					if (c != grid.getCellAt(numL, numC) 
							&& c.getCandidates() != null 
							&& c.getCandidates().contains(v)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	private boolean test(int line, int col, String value, boolean isLine) {
		int size  = Grid.size;
		
		
		int base = (isLine ? line / Grid.REGION_SIZE * Grid.REGION_SIZE : col / Grid.REGION_SIZE * Grid.REGION_SIZE);
		int count = 0;
		int t = isLine ? col : line;
		int tbase = t / Grid.REGION_SIZE * Grid.REGION_SIZE;
		for (int i = base; i < base + Grid.REGION_SIZE; ++i) {
			for (int j = 0; j < size; ++j) {
				if (i != (isLine ? line : col)) {
					Cell c = isLine ? grid.getCellAt(i, j) 
							: grid.getCellAt(j, i);
					if (c.getCandidates() != null) {
						if (j != tbase + j % Grid.REGION_SIZE) {
							continue;
						} else {
							if (c.getCandidates().contains(value)) {
								++count;
							}
						}
					}
				}
			}
		}
		return count != 0;

	}


}