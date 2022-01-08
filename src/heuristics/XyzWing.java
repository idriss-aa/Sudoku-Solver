package heuristics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cmd.Move;
import model.Cell;
import model.Coordinate;
import model.Grid;
import model.StdGrid;

public class XyzWing implements Heuristic  {

	private Map<String, Integer> mapStrings;
	private Map<Integer, String> mapValues;
	private Grid grid;
	public XyzWing(Grid grid) {
		this.grid  = grid;
		mapStrings = new HashMap<String, Integer>();
		mapValues = new HashMap<Integer, String>();
		setMapStrings(StdGrid.defaultValueSet());
		setMapValues(StdGrid.defaultValueSet());
	}

	 
	public Move getSolution() {
	
		boolean contain;
		
		for (Cell cell : grid.getCells()) {

			if (cell.getCandidates() != null && cell.getCandidates().size() == 3) {
				Coordinate bc = cell.getCoordinate();
				Map<Cell, String> Firstmap = new HashMap<Cell, String>();
				Map<Cell, String> Secondmap = new HashMap<Cell, String>();			
				contain = false;
				for (int k = 0; k < Grid.size; ++k) {
					if (k != bc.getX()) {
						Cell firstCell = grid.getCellAt(bc.getX(), k);
						if (firstCell.getCandidates() != null &&
							firstCell.getCandidates().size() == 2) {
							String value = getDifferenceOnlyOne(firstCell.getCandidates(), cell.getCandidates());
							if (value != null) {
								contain = true;
								Firstmap.put(firstCell, value);
							}
						}
					}
				}
				
				if (contain) {
					int startX = bc.getX() - bc.getX() % Grid.REGION_SIZE;
					int startY = bc.getY() - bc.getY() % Grid.REGION_SIZE;
					for (int i = 0; i < Grid.REGION_SIZE ; ++i) {
						for (int j = 0; j < Grid.REGION_SIZE; ++j) {
							if ((i + startX) != bc.getX() && (j + startY) != bc.getY()) {
								Cell secondCell = grid.getCellAt(i + startX, j + startY);
								if (secondCell.getCandidates() != null &&
										secondCell.getCandidates().size() == 2) {
									String value = getDifferenceOnlyOne(secondCell.getCandidates(), cell.getCandidates());
									if (value != null) {
										contain = true;
										Secondmap.put(secondCell, value);
									}
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
							if (!valueFirst.equals(valueSecond)) {
								String valueToRemove = getFirstNotContain(cell.getCandidates(), valueFirst, valueSecond);
								List<Cell> removes = new ArrayList<Cell>();
								boolean fromLine = false;
								if (fromSameRegion(cell, cellFirst)) {
									if (fromSameLine(cell, cellSecond)) {
										fromLine = true;
									}
								} else {
									if (fromSameLine(cell, cellFirst)) {
										fromLine = true;
									}
								}
								if (fromLine) {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getY() - bc.getY() % Grid.REGION_SIZE) + k != bc.getY()) {
											Cell cellRemove = grid.getCellAt(
													bc.getX(),
													(bc.getY() - bc.getY() % Grid.REGION_SIZE) + k);
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								} else {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getX() - bc.getX() % Grid.REGION_SIZE) + k != bc.getX()) {
											Cell cellRemove = grid.getCellAt(
													(bc.getX() - bc.getX() % Grid.REGION_SIZE) + k,
													bc.getY());
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								}

								if (removes.size() > 0) {
									Move move =new  Move();
									setSolution(move, cell, cellFirst, cellSecond, removes, valueToRemove, fromLine);
									return move;
								}
								
							}
						}
					}
				}
				
				Firstmap.clear();
				Secondmap.clear();
				contain = false;

				for (int k = 0; k < Grid.size; ++k) {
					if (k != bc.getX()) {
						Cell firstCell = grid.getCellAt(k, bc.getY());
						
						if (firstCell.getCandidates() != null &&
								firstCell.getCandidates().size() == 2) {

							String value = getDifferenceOnlyOne(firstCell.getCandidates(), cell.getCandidates());
							if (value != null) {
								contain = true;
								Firstmap.put(firstCell, value);
							}
						}
					}
				}
				
				if (contain) {
					int startX = bc.getX() - bc.getX() % Grid.REGION_SIZE;
					int startY = bc.getY() - bc.getY() % Grid.REGION_SIZE;
					for (int i = 0; i < Grid.REGION_SIZE ; ++i) {
						for (int j = 0; j < Grid.REGION_SIZE; ++j) {
							
							
							if ((i + startX) != bc.getX() && (j + startY) != bc.getY()) {
								Cell secondCell = grid.getCellAt(i + startX, j + startY);
								
								if (secondCell.getCandidates() != null &&
										secondCell.getCandidates().size() == 2) {

									String value = getDifferenceOnlyOne(secondCell.getCandidates(), cell.getCandidates());
									if (value != null) {
										contain = true;
										Secondmap.put(secondCell, value);
									}
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
				
							if (!valueFirst.equals(valueSecond)) {
								String valueToRemove = getFirstNotContain(cell.getCandidates(), valueFirst, valueSecond);
								List<Cell> removes = new ArrayList<Cell>();
								boolean fromLine = false;
								if (fromSameRegion(cell, cellFirst)) {
									if (fromSameLine(cell, cellSecond)) {
										fromLine = true;
									}
								} else {
									if (fromSameLine(cell, cellFirst)) {
										fromLine = true;
									}
								}

								if (fromLine) {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getY() - bc.getY() % Grid.REGION_SIZE) + k != bc.getY()) {
											Cell cellRemove = grid.getCellAt(
													bc.getX(),
													(bc.getY() - bc.getY() % Grid.REGION_SIZE) + k);
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								} else {
									for (int k = 0; k < Grid.REGION_SIZE; ++k) {
										if ((bc.getX() - bc.getX() % Grid.REGION_SIZE) + k != bc.getX()) {
											Cell cellRemove = grid.getCellAt(
													(bc.getX() - bc.getX() % Grid.REGION_SIZE) + k,
													bc.getY());
											if (cellRemove.getCandidates() != null &&
													cellRemove.getCandidates().contains(valueToRemove)) {
												removes.add(cellRemove);
											}
										}
									}
								}

								if (removes.size() > 0) {
									Move move =new  Move();
									setSolution(move, cell, cellFirst, cellSecond, removes, valueToRemove, fromLine);
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
	
	
	
	private void setSolution(Move move, Cell beginning, Cell first, Cell second, List<Cell> removes,
		String valueToRemove, boolean fromLine) {
		move.addReason( beginning);
		move.addReason( first);
		move.addReason( second);
		
			for (Cell cell : removes) {
				move.addReason( cell);
				move.addAction(Move.ELIMINATE_CANDIDAT, cell, valueToRemove);
			}
		
		StringBuilder Data = getDescription(beginning, first, second, removes, valueToRemove, fromLine);
		move.setDetails(Data.toString());
		
	}

	private StringBuilder getDescription(Cell beginning, Cell first, Cell second, List<Cell> removes, String valueToRemove, boolean fromLine) {
		String Value = null;
		String Value_ = null;
		
		for (String v : beginning.getCandidates()) {
			if (Value == null) {
				Value = v;
			} else if (Value_ == null) {
				Value_ = v;
			}
		}
		StringBuilder Data = new StringBuilder();
		Data.append(" \n XYZWING :\n");
		Data.append("Quel que puisse être le choix dans la case de départ de coordonnée " + beginning.getCoordinate());
		Data.append(", on est certain de ne pas retrouver le candidat ");
		Data.append(valueToRemove + " dans les case finale en couleur.");//to do
		Data.append("Si on choisit " + Value + ", c'est " + valueToRemove + " qui va prendre place ");
		if (getCommunValue(beginning, first, valueToRemove) == Value) {
			if (fromLine) {
				Data.append("sur la colonne.");
			} else {
				Data.append("sur la ligne.");
			}
		} else {
			Data.append("dans celui dans le même carré.");
		}
		Data.append(" Et si on choisit le " + Value_ + ", le " + valueToRemove + " prendra la place ");
		if (getCommunValue(beginning, second, valueToRemove) == Value_) {
			Data.append("dans celui dans le même carré.");
		} else {
			if (fromLine) {
				Data.append("sur la colonne.");
			} else {
				Data.append("sur la ligne.");
			}
		}
		Data.append(" Dans les deux cas, les cases en magenta ne peuvent pas être " + valueToRemove + ".");
		Data.append("On peut donc supprimer " + valueToRemove + " des candidats pour ces cases.");
		return Data;
	}

	private String getFirstNotContain(Set<String> list, String a, String b) {
		for (String s : list) {
			if (s != a && s != b) {
				return s;
			}
		}
		return null;
	}
	
	private boolean fromSameRegion(Cell first, Cell second) {
		Coordinate CordinateOne = first.getCoordinate();
		Coordinate CordinateTwo = second.getCoordinate();
		
		int startX1 = CordinateOne.getX() - CordinateOne.getX() % Grid.REGION_SIZE;
		int startY1 = CordinateOne.getY() - CordinateOne.getY() % Grid.REGION_SIZE;
		int startX2 = CordinateTwo.getX() - CordinateTwo.getX() % Grid.REGION_SIZE;
		int startY2 = CordinateTwo.getY() - CordinateTwo.getY() % Grid.REGION_SIZE;
		return (startX1 == startX2 && startY1 == startY2);
	}
	
	private boolean fromSameLine(Cell first, Cell second) {
		Coordinate CordinateOne = first.getCoordinate();
		Coordinate CordinateTwo = second.getCoordinate();
		return (CordinateOne.getX() == CordinateTwo.getX());
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
	
	

	private String getCommunValue(Cell cell1, Cell cell2, String notThisValue) {
		String value = null;
		boolean isGood = false;
		for (String candidate : cell1.getCandidates()) {
			if (candidate != notThisValue && cell2.getCandidates().contains(candidate)) {
				if (isGood) {
					return null;
				}
				isGood = true;
				value = candidate;
			}
		}
		return value;
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
