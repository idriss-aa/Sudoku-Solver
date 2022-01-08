package heuristics;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cmd.Move;
import helpers.Unity;
import model.Cell;
import model.Grid;
import model.StdGrid;


public class Swordfish implements Heuristic  {


	
	public static final int MinCand = 2;
	public static final int MaxCand = 3;
	
	public static final int MinCells = 2;
	public static final int MaxCells = 3;
		
	private Map<Unity, Map<Integer, Cell[]>> data;
	private Grid grid;
	
	
	public Swordfish(Grid grid) {
		this.grid =grid;
		data = new HashMap<Unity, Map<Integer, Cell[]>>();
	}
	
	

	 
	public Move getSolution() {

		for (String cand : StdGrid.defaultValueSet()) {
			fillData(cand);
			if (!data.isEmpty()) {
				// 3 lignes / colonnes ==> 3 colonnes / lignes
				for (Unity u : Unity.values()) {
					if (!u.equals(Unity.REGION)) {
						int[] res = checkGetSolution(u, cand);
						if (res != null) {
							boolean correct = true;
							Set<Integer> op = getOppositeUnities(u, res);
							for (Integer i : res) {
								for (int j = 0; j < Grid.size; ++j) {
									if (op.contains(j)) {
										Cell ci = u.equals(Unity.LINE) ? grid.getCellAt(i, j) : 
											grid.getCellAt(j, i);
										if (ci.getValue() == null && !ci.getCandidates().contains(cand)) {
											correct = false;
										}
									}
								}
							}
							if (correct) {
								return setSolution(u, cand, res);
							}
							correct = false;
						}
					}
				}
						
				data.clear();	
			}
		}
		return null;
	}
	
	
		
	
	private void fillData(String cand) {
		for (Unity u : Unity.values()) {
			for (int i = 0; i < Grid.size; ++i) {
				Cell[] cells = getValidCells(u, i, cand);
				if (cells != null && cells.length > 0) {
					fillData(u, i, cells);
				}
			}
		}
	}
	
	
	private Move setSolution(Unity unit, String cand, int[] unities) {
		Move move = new Move();
		Set<Integer> op = getOppositeUnities(unit, unities);
		for (Integer i : unities) {
			for (int j = 0; j < Grid.size; ++j) {
				if (op.contains(j)) {
					Cell ci = unit.equals(Unity.LINE) ? grid.getCellAt(i, j) : 
						grid.getCellAt(j, i);
					move.addReason( ci);
				}
			}
		}
		for (int i : op) {
			for (int j = 0; j < Grid.size; ++j) {
				if (!isInTab(unities, j)) {
					Cell ci = !unit.equals(Unity.LINE) 
							? grid.getCellAt(i, j) 
							: grid.getCellAt(j, i);
					if (ci.getCandidates() != null 
							&& ci.getCandidates().contains(cand)) {
					
							move.addAction(Move.ELIMINATE_CANDIDAT,ci, cand);
							move.addReason(  ci);
						
					}
				}
			}
		}
		move.setDetails(setDescription(unit, cand));
	
		return move;
	}
	
	private String setDescription(Unity unit, String cand) {
		StringBuilder res = new StringBuilder();
		res.append("\n Swordfish :\n");
		res.append("Swordfish avec les " + cand + ". Il est possible de "
				+ "supprimer les candidats " + cand + " des cases en rouge");
		
		return res.toString();
	}
	

	
	private Unity getOppositeUnity(Unity unit) {
		switch (unit) {
		case LINE:
			return Unity.COL;
		case COL: 
			return Unity.LINE;
		case REGION:
			return null;
		default:
			return null;
		}
	}

	
	
	private boolean isUseful(Unity unit, String cand, int[] unities, 
			Set<Integer> opposite) {
		Unity op = getOppositeUnity(unit);
		if (op == null) {
			return false;
		}
		
		for (Integer i : opposite) {
			for (int j = 0; j < Grid.size; ++j) {
				int x = unit.equals(Unity.LINE) ? j : i;
				int y = unit.equals(Unity.LINE) ? i : j;
				Cell ci = grid.getCellAt(x, y);
				int coord = unit.equals(Unity.LINE) ? ci.getCoordinate().getX() 
						: ci.getCoordinate().getY();
				if (!isInTab(unities, coord) 
						&& ci.getCandidates() != null
						&& ci.getCandidates().contains(cand)) {
					return true;
				}
			}
		}
		return false;
	}
		

	
	private boolean checkUnities(Unity unit, String cand, int[] unities) {
		for (int unity : unities) {
			if (data.get(unit).get(unity) == null) {
				return false;
			}
		}
		Set<Integer> res = getOppositeUnities(unit, unities);
		if (res.size() == MaxCells) {
			if (isUseful(unit, cand, unities, res)) {
				return true;
			}
		}
		return false;
	}
	
	private Set<Integer> getOppositeUnities(Unity unit, int[] unities) {
		Set<Integer> res = new HashSet<>();
		for (int i : unities) {
			if (data.get(unit).get(i) != null) {
				for (Cell ci : data.get(unit).get(i)) {
					res.add((unit.equals(Unity.LINE) ? ci.getCoordinate().getY() 
							: ci.getCoordinate().getX()));
				}	
			}
		}
		return res;
	}

	private int[] checkGetSolution(Unity unit, String cand) {
		if (unit.equals(Unity.REGION) || data.get(unit) == null) {
			return null;
		}
		final int UNITY_NB = MaxCells;
		int[] res = new int[UNITY_NB];
		return checkGetSolution(unit, cand, res, 0, 0, UNITY_NB);
	}
	
	private int[] checkGetSolution(Unity unit, String cand, int[] tab, int nb, int index, int max) {
		if (nb >= max) {
			if (checkUnities(unit, cand, tab)) {
				return tab;
			}
			return null;
		} else {
			for (int i = index; i < data.get(unit).size(); ++i) {
				tab[nb] = (int) data.get(unit).keySet().toArray()[i];
				int[] res = checkGetSolution(unit, cand, tab, nb + 1, i + 1, max);
				if (res != null) {
					return res;
				}
			}
		}
		return null;
	}
	
	
	private Cell[] getValidCells(Unity unit, int nb, String cand) {
		int cpt = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(unit, nb, i);
			int y = getY(unit, nb, i);
			if (grid.getCellAt(x, y).getCandidates() != null) {
				if (grid.getCellAt(x, y).getCandidates().contains(cand)) {
					cpt++;
				}
			}
		}
		if (cpt < MinCand || cpt > MaxCand) {
			return null;
		}
		Cell[] res = new Cell[cpt];
		cpt = 0;
		for (int i = 0; i < Grid.size; ++i) {
			int x = getX(unit, nb, i);
			int y = getY(unit, nb, i);
			Cell ci = grid.getCellAt(x, y);
			if (ci.getCandidates() != null) {
				if (ci.getCandidates().contains(cand)) {
					res[cpt] = ci;
					cpt++;
				}
			}
		}
		return res;
	}
	
	
	private void fillData(Unity unit, int nb, Cell[] cells) {
		if (unit == null || nb < 0 || nb >= Grid.size 
				|| cells == null) {
			return;
		}

		if (!data.containsKey(unit)) {
			data.put(unit, new HashMap<Integer, Cell[]>());
		}
		data.get(unit).put(nb, cells);
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
	
	
	private boolean isInTab(int[] tab, int n) {
		for (int i : tab) {
			if (n == i) {
				return true;
			}
		}
		return false;
	}

	 
}
