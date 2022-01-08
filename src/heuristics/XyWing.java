package heuristics;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import cmd.Move;
import model.Coordinate;
import model.Cell;
import model.Grid;

public class XyWing implements Heuristic {
	private Grid grid;
	public XyWing(Grid grid) {
		this.grid = grid;
	}

	 
	public Move getSolution() {
	
		boolean contain ;
		for (Cell cell : grid.getCells()) {

			if (cell.getCandidates() != null && cell.getCandidates().size() == 2) {
				Coordinate Cordinate = cell.getCoordinate();
				Map<Cell, String> Firstmap = new HashMap<Cell, String>();
				Map<Cell, String> Secondmap = new HashMap<Cell, String>();
				contain = false;	
				for (int k = 0; k < Grid.size; ++k) {
					if (k != Cordinate.getX()) {
						Cell firstCell = grid.getCellAt(Cordinate.getX(), k);
						
						if (firstCell.getCandidates() != null &&
								firstCell.getCandidates().size() == 2) {

							String value = getDifferenceOnlyOne(cell.getCandidates(), firstCell.getCandidates());
							if (value != null) {
								contain = true;
								Firstmap.put(firstCell, value);
							}
						}
					}
				}
				
				if (contain) {
					contain = false;

					for (int k = 0; k < Grid.size; ++k) {
						if (k != Cordinate.getY()) {
							Cell SecondCell = grid.getCellAt(k, Cordinate.getY());

							if (SecondCell.getCandidates() != null &&
									SecondCell.getCandidates().size() == 2) {

								String value = getDifferenceOnlyOne(cell.getCandidates(), SecondCell.getCandidates());
								if (value != null) {
									contain = true;
									Secondmap.put(SecondCell, value);
								}
							}
						}
					}
				}
				
				if (contain) {
					for (Cell cellFirst : Firstmap.keySet()) {
						String valueFirst = Firstmap.get(cellFirst);
						
						for (Cell cellSecond : Secondmap.keySet()) {
							String valueSecond = Secondmap.get(cellSecond);
							
							if (valueFirst.equals(valueSecond)) {
								Cell remove = grid.getCellAt(
										cellSecond.getCoordinate().getX(),
										cellFirst.getCoordinate().getY());
								if (remove.getCandidates() != null &&
										remove.getCandidates().contains(valueFirst)) {
									Move move = new Move();
									setSolution( move, cell, cellFirst, cellSecond, remove, valueFirst);
									return move;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private void setSolution(Move move, Cell beginning, Cell first, Cell second, Cell remove, String value) {
	
			move.addReason(beginning);
			move.addReason(first);
			move.addReason(second);
			move.addReason(remove);
			move.addAction(Move.ELIMINATE_CANDIDAT,remove, value);
		StringBuilder sb = getDescription(beginning, first, second, remove, value);
		move.setDetails(sb.toString());
	}
	
	private StringBuilder getDescription(Cell beginning, Cell first, Cell second, Cell remove, String value) {
		String Value = null;
		String Value_ = null;
		for (String v : beginning.getCandidates()) {
			if (Value == null) {
				Value = v;
			} else {
				Value_ = v;
			}
		}
		StringBuilder Data = new StringBuilder();
		Data.append("\n XYWING :\n");
		Data.append("Quel que puisse être le choix dans la case de départ de coordonnée " + beginning.getCoordinate());
		Data.append(", on est certain de ne pas retrouver le candidat ");
		Data.append(value + " dans la case finale case en couleur.\n");//to do
		Data.append("Si on choisit " + Value + ", c'est " + value + " qui va prendre place ");
		if (getCommunValue(beginning, first) == Value) {
			Data.append("dans la colonne.");
		} else {
			Data.append("dans la ligne.");
		}
		Data.append(" Et si on choisit le " + Value_ + ", le " + value + " prendra la place ");
		if (getCommunValue(beginning, second) == Value) {
			Data.append("dans la colonne.");
		} else {
			Data.append("dans la ligne.");
		}
		Data.append("Dans les deux cas, la case en orange ne peut pas être " + value + ".");
		Data.append("On peut donc supprimer " + value + " des candidats pour cette case.");
		return Data;
	}
	
	private String getDifferenceOnlyOne(Set<String> a, Set<String> b) {
		String value = null;
		for (String currentValue : b) {
			if (value == null) {
				value = (!a.contains(currentValue) ? currentValue : null);
			} else {
				if (!a.contains(currentValue)) {
					return null;
				}
			}
		}
		return value;
	}
	
	private String getCommunValue(Cell cell1, Cell cell2) {
		String value = null;
		boolean isGood = false;
		for (String candidateLine : cell2.getCandidates()) {
			if (cell1.getCandidates().contains(candidateLine)) {
				if (isGood) {
					return null;
				}
				isGood = true;
				value = candidateLine;
			}
		}
		return value;
	}



}

