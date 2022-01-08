package helpers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Cell;
import model.Grid;
import model.StdCell;
import model.StdCoordinate;
import model.StdGrid;

public class DifficultyHelper {

	
	private Grid grille;
    public static final int[] EASY = {36, 37, 38, 39, 40};
    public static final int[] MEDIUM = {41, 42, 43, 44, 45};
    public static final int[] HARD = {46, 47, 48, 49, 50};
    private List<Integer> possibleValues = Arrays.asList(1,2,3,4,5,6,7,8,9);
    private int[][] grid = new int[Grid.size][Grid.size];
	public DifficultyHelper(Difficulty dif) {
		// TODO Auto-generated constructor stub
		//DifficultyHelper sudokuGenerator = new DifficultyHelper();
		 insertCellValue(0,0);
		 switch(dif) {
		 case EASY:
			  this.insertZeros(EASY[new Random().nextInt(EASY.length)]);
			 break;
		 case MEDUIM :
			  this.insertZeros(MEDIUM[new Random().nextInt(MEDIUM.length)]);
			 break;
		 case HARD:
			 this.insertZeros(HARD[new Random().nextInt(HARD.length)]);
		 	break;
		 }
        
        //this.print();
		
	}
	public int getDifficulty() {
		/**
		 * difficulty probabilty based on the number of cells
		 */
		int count = 0;
		for(Cell c :grille.getCells()) {
			if(c.isLocked()) count++;
		}
		int prob = 100-((count*82)/100);
		if(prob<=30) {
			System.out.println("easy");
		}else if(prob >30 && prob<=60) {
			System.out.println("meduim");
		}else if(prob>60) {
			System.out.println("HARD");
		}
		/**
		 * Difficulty probabilty based on the position of the cells on grid
		 */
		
		
		
		return prob;
		
	}
	private boolean insertCellValue(int x, int y) {
        int nextX = x;
        int nextY = y;

        Collections.shuffle(possibleValues);

        for(int i=0;i<possibleValues.size();i++) {
            if(canInsertValueInCell(x, y, possibleValues.get(i))) {
                grid[x][y] = possibleValues.get(i);
                if(x == 8) {
                    if(y == 8) {
                        return true;
                    }
                    else {
                        nextX = 0;
                        nextY = y + 1;
                    }
                }
                else {
                    nextX = x + 1;
                }
                if(insertCellValue(nextX, nextY)) {
                    return true;
                }
            }
        }
        grid[x][y] = 0;
        return false;
    }
	   public void insertZeros(int zerosToInsert) {

			double remainingSquares = Grid.size * Grid.size;
	        double remainingZeros = (double)zerosToInsert;

	        for(int i=0;i<9;i++) {
	            for(int j=0;j<9;j++) {
	                if(Math.random() <= (remainingZeros/remainingSquares))
	                {
	                    grid[i][j] = 0;
	                    remainingZeros--;
	                }
	                remainingSquares--;
	            }
	        }
	    }
	   private boolean canInsertValueInCell(int x, int y, int value) {
	        for(int i=0;i<9;i++) {
	            if(value == grid[x][i]) {
	                return false;
	            }
	        }
	        for(int i=0;i<9;i++) {
	            if(value == grid[i][y]) {
	                return false;
	            }
	        }

	        int cornerX = 0;
	        int cornerY = 0;
	        if(x > 2) {
	            if(x > 5) {
	                cornerX = 6;
	            }
	            else {
	                cornerX = 3;
	            }
	        }
	        if(y > 2) {
	            if(y > 5) {
	                cornerY = 6;
	            }
	            else {
	                cornerY = 3;
	            }
	        }

	        for(int i=cornerX;i<10 && i<cornerX+3;i++) {
	            for(int j=cornerY;j<10 && j<cornerY+3;j++) {
	                if(value == grid[i][j]) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }
	   public Grid print() {
		   StdGrid grid = new StdGrid();
		   Cell[][] cells = new Cell[9][9];
		   grid = new StdGrid(StdGrid.defaultValueSet(), cells);
		   for(int i=0;i<9;i++) {
	            for(int j=0;j<9;j++) {
	            	cells[i][j]=  new StdCell(new StdCoordinate(i,j));
	            	cells[i][j].setGrid(grid);
	               if(this.grid[i][j]!=0) {
	            	   
	            	  cells[i][j].setValue(String.valueOf(this.grid[i][j]));
	            	  cells[i][j].lock();
	               }
	              
	            	   
	               }
	            }
		 
		   grid.generateAllCandidat();
		   return grid;
	        }
	  

	
}
