package heuristics;

import java.util.ArrayList;
import java.util.List;
import cmd.Move;
import model.Cell;
import model.Coordinate;
import model.Grid;
import model.StdGrid;

public class TwinsAndTriplet implements Heuristic  {

	private Grid grid;
	public TwinsAndTriplet(Grid grid) {
		this.grid=grid;
	}

	 
	public Move getSolution() {
		
		Move move = new Move();
		int size = 3;
		String Solution= null;
		StringBuilder sb = new StringBuilder();
		for (String c : StdGrid.defaultValueSet()) {
			
			for (int k = 0; k < size; ++k) {
				int[] regions = new int[size];
				for (int j = 0; j < size; ++j) {
					regions[j] = k * size + j;
				}
				for (int numRegion : regions) {
					int shiftX = (numRegion % size) * size;
					int shiftY = (numRegion / size) * size;
					List<Cell> cellFromRegion = new ArrayList<Cell>();
					for (int i = 0; i < size; ++i) {
						for (int j = 0; j < size; ++j) {
							Cell cell = grid.getCellAt(i + shiftX, j + shiftY);
							if (cell.getCandidates() != null &&
									cell.getCandidates().contains(c)) {
								cellFromRegion.add(cell);
							}
						}
					}
					int numLine = regionLineIsCorrect(cellFromRegion, size);
					if (numLine != -1) {
						List<Cell> cellCorrectY = new ArrayList<Cell>();
						for (int j = 0; j < size * size; ++j) {
							Cell cell = grid.getCellAt(j, numLine);
							if (!cellFromRegion.contains(cell) && cell.getCandidates() != null && cell.getCandidates().contains(c)) {
								cellCorrectY.add(cell);
							}
						}
						
						if (cellCorrectY.size() > 0) {
							Coordinate cor=null;
							
							for (Cell cell : cellCorrectY) {
								cor=cell.getCoordinate();
								move.addAction(Move.ELIMINATE_CANDIDAT, cell, c);
						}
							for (Cell cell : cellFromRegion) {
								move.addReason(cell);
							}
							Coordinate tmp = cellFromRegion.get(0).getCoordinate();
							int num = 
									tmp.getX() ;
									
							 numRegion = (tmp.getX() / size) + (tmp.getY() / size) + 1;
							 sb.append(getDescription(num, numRegion, c));
							 
						}
					}
				}
			}
			
			for (int k = 0; k < size; ++k) {
				int[] regions = new int[size];
				for (int j = 0; j < size; ++j) {
					regions[j] = j * size + k;
				}

				for (int numRegion : regions) {

					int shiftX = (numRegion % size) * size;
					int shiftY = (numRegion / size) * size;
					List<Cell> cellFromRegion = new ArrayList<Cell>();
					for (int i = 0; i < size; ++i) {
						for (int j = 0; j < size; ++j) {
							Cell cell = grid.getCellAt(j + shiftX, i + shiftY);
							if (cell.getCandidates() != null &&
									cell.getCandidates().contains(c)) {
								cellFromRegion.add(cell);
							}
						}
					}

					int numColumn = regionColumnIsCorrect(cellFromRegion, size);
					if (numColumn != -1) {
						List<Cell> cellCorrectX = new ArrayList<Cell>();
						for (int j = 0; j < size * size; ++j) {
							Cell cell = grid.getCellAt(numColumn, j);
							if (!cellFromRegion.contains(cell) && cell.getCandidates() != null && cell.getCandidates().contains(c)) {
								cellCorrectX.add(cell);
							}
						}
						
						if (cellCorrectX.size() > 0) {
							Coordinate cor=null;
							for (Cell cell : cellCorrectX) {
								move.addAction(Move.ELIMINATE_CANDIDAT, cell, c);
						}
							for (Cell cell : cellFromRegion) {
								move.addReason(cell);
							}
							Coordinate tmp = cellFromRegion.get(0).getCoordinate();
							int num = tmp.getY() ;
									
							 numRegion = (tmp.getX() / size) + (tmp.getY() / size) + 1;
							 sb.append(getDescription(num, numRegion, c));
					}
				}
			}
		}
		}
		if(move.getActions().size()>0) {
		
		move.setDetails(sb.toString());
		
		return move;}
		
		else return null;
	}
	private String getDescription (int num,int numRegion,String c) {
		StringBuilder Data = new StringBuilder();
		Data.append("twins and Triplet:\n");
		Data.append("Le candidat '" + c + "'");
		Data.append(" n'étant présent uniquement sur la ");
		Data.append("ligne");
		Data.append(" numéro " + (num + 1));
		Data.append(" dans la région numéro " + numRegion);
		Data.append(", alors on peut retirer tout autres mêmes candidats sur la meme ");
		Data.append("ligne");
		return Data.toString();
	}
	

	
	private int regionColumnIsCorrect(List<Cell> cells, int size) {
		int numLine = -1;
		for (Cell cell : cells) {
			if (numLine == -1) {
				numLine = cell.getCoordinate().getX();
			} else {
				if (cell.getCoordinate().getX() != numLine) {
					return -1;
				}
			}
		}
		return (cells != null && cells.size() > 1 ? numLine : -1);
	}
	
	
	private int regionLineIsCorrect(List<Cell> cells, int size) {
		int numLine = -1;
		for (Cell cell : cells) {
			if (numLine == -1) {
				numLine = cell.getCoordinate().getY();
			} else {
				if (cell.getCoordinate().getY() != numLine) {
					return -1;
				}
			}
		}
		return numLine;
	}

}