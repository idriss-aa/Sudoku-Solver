package heuristics;



import java.util.Collection;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cmd.Move;
import model.Grid;
import model.StdGrid;


public class Xwing implements Heuristic  {

	private Map<String, Integer> mapStrings;
	private Map<Integer, String> mapValues;
	private Grid  grid ; 
	private static final int NB_ELEMENT = 2;
	
	public Xwing(Grid grid) {
		this.grid = grid;
		mapStrings = new HashMap<String, Integer>();
		mapValues = new HashMap<Integer, String>();
		setMapStrings(StdGrid.defaultValueSet());
		setMapValues(StdGrid.defaultValueSet());
	}

	 
	public Move getSolution() {
		Move sol = null;
		if ((sol = checkFirstSolution()) != null && sol.getActions().size() > 0) {
			return sol;
		}
		return null;
	}
	

	

	private Move checkFirstSolution() {	
		Set<Integer> FLine = new TreeSet<Integer>();
		Set<Integer> FColumn = new TreeSet<Integer>();
		Set<Integer> SLine = new TreeSet<Integer>();
		Set<Integer> SColumn = new TreeSet<Integer>();
		for (String value : mapValues.values()) {
			int indexFirst = 0;
			int indexSecond = 0;
			boolean notValide = false;
			while (indexFirst < Grid.size - 1) {
				int k = 0;
				while (k < Grid.size && 
						(FLine.size() <= NB_ELEMENT || FColumn.size() <= NB_ELEMENT)) {
					if (grid.getCellAt(indexFirst, k).getCandidates() != null &&
							grid.getCellAt(indexFirst, k).getCandidates().contains(value)) {
						FLine.add(k);
					}
					if (grid.getCellAt(k, indexFirst).getCandidates() != null &&
							grid.getCellAt(k, indexFirst).getCandidates().contains(value)) {
						FColumn.add(k);
					}
					++k;
				}
				indexSecond = indexFirst + 1;
				notValide = false;
				if (FLine.size() == NB_ELEMENT) {
					while (indexSecond < Grid.size) {
						k = 0;
						while (k < Grid.size && SLine.size() <= NB_ELEMENT) {
							if (!notValide && 
									grid.getCellAt(indexSecond, k).getCandidates() != null &&
									grid.getCellAt(indexSecond, k).getCandidates().contains(value)) {
								notValide = !FLine.contains(k);
								SLine.add(k);
							}
							++k;
						}
						if (!notValide && FLine.equals(SLine)) {
							Move move = new Move();
							setFirstSolution((Move) move,  value, indexFirst, indexSecond,
									SLine, true);
							if (move.getActions().size() > 0) {
								return move;
							}
						}
						SLine.clear();
						notValide = false;
						++indexSecond;
					}
				}
				indexSecond = indexFirst + 1;
				notValide = false;
				if (FColumn.size() == NB_ELEMENT) {
					while (indexSecond < Grid.size) {
						k = 0;
						while (k < Grid.size && SColumn.size() <= NB_ELEMENT) {
							if (!notValide && 
									grid.getCellAt(k, indexSecond).getCandidates() != null &&
									grid.getCellAt(k, indexSecond).getCandidates().contains(value)) {
								notValide = !FColumn.contains(k);
								SColumn.add(k);
							}
							++k;
						}
						if (!notValide && FColumn.equals(SColumn)) {
							Move move = new Move();
							setFirstSolution(move,  value, indexFirst, indexSecond,
									SColumn, false);
							if (move.getActions().size() > 0) {
								return move;
							}
						}
						SColumn.clear();
						notValide = false;
						++indexSecond;
					}
				}
				FLine.clear();
				FColumn.clear();
				++indexFirst;
			}
		}
		return null;
	}
	
	private void setFirstSolution(Move move, String value, int num1, int num2,
			Set<Integer> list, boolean fromLine) {
	
		for (int i = 0; i < Grid.size; ++i) {
			if (i != num1 && i != num2) {
				for (Integer j : list) {
					if (fromLine && grid.getCellAt(i, j).getCandidates() != null &&
							grid.getCellAt(i, j).getCandidates().contains(value)) {
						
							
						move.addAction(Move.ELIMINATE_CANDIDAT,grid.getCellAt(i, j),value);
							move.addReason( grid.getCellAt(i, j));
					
					} else if (!fromLine && grid.getCellAt(j, i).getCandidates() != null &&
							grid.getCellAt(j, i).getCandidates().contains(value)){
						
						move.addAction(Move.ELIMINATE_CANDIDAT,grid.getCellAt(j, i),value);
							move.addReason( grid.getCellAt(j, i));
						
					}
				}
			} else {
				for (Integer j : list) {
					move.addReason( grid.getCellAt(i, j));
				}
			}
		}
		StringBuilder description = getDescription(value, num1, num2, list, fromLine);
		move.setDetails(description.toString());
	}
	
	private StringBuilder getDescription(String value, int num1, int num2, Set<Integer> list,
			boolean fromLine) {
		StringBuilder Data = new StringBuilder();
		Data.append(" XWING :\n");
		Data.append("Le candidat '" + value + "'");
		Data.append(" n'est disponible qu'à " + list.size() + " emplacement sur les ");
		Data.append(fromLine ? "lignes" : "colonnes");
		Data.append(" numéro " + num1 + " et " + num2);
		Data.append(".\nDe plus, ces candidats font partie des ");
		Data.append(fromLine ? "colonnes" : "lignes");
		Object[] values = list.toArray();
		Data.append(" " + values[0]);
		for (int i = 1; i < list.size(); ++i) {
			Data.append(" et " + values[i]);
		}
		Data.append(".\nIl est donc possible de supprimer tous les autres" +
				" candidats '" + value + "' de ces " + list.size() + " ");
		Data.append(fromLine ? "colonnes." : "lignes.");
		return Data;
	}
	
	private void setMapStrings(Collection<String> collection) {
		int i = 0;
		for (String s : collection) {
			mapStrings.put(s, i);
			++i;
		}
	}
	
	private void setMapValues(Collection<String> collection) {
		int i = 0;
		for (String s : collection) {
			mapValues.put(i, s);
			++i;
		}
	}



}
