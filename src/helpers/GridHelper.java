package helpers;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cmd.Move;
import heuristics.Burma;
import heuristics.Coloring;
import heuristics.Heuristic;
import heuristics.IdenticalCandidates;
import heuristics.InteractionsBetweenRegion;
import heuristics.IsolatedGroups;
import heuristics.Jellyfish;
import heuristics.MixedGroups;
import heuristics.OneCandidate;
import heuristics.Squirmbag;
import heuristics.Swordfish;
import heuristics.TwinsAndTriplet;
import heuristics.UniqueCandidate;
import heuristics.Xwing;
import heuristics.XyWing;
import heuristics.XyzWing;
import model.Cell;
import model.Grid;
import model.StdCell;
import model.StdCoordinate;
import model.StdGrid;
import org.w3c.dom.Node;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GridHelper {
	 public static final String xmlFilePath = "solution.xml";
	 private static  Grid grid;
	 

	 public static void Solve(Grid board) {
		    Move solution = null;
		    boolean stop=false;
			while(!stop) {
				
				 solution=new OneCandidate(board).getSolution();
				
				 
				 if(solution==null || solution.getActions().size() == 0) {
	 
					 solution=new UniqueCandidate(board).getSolution();			
					 
				 }
				 
							
				if(solution==null || solution.getActions().size() == 0) {
					 solution=new IdenticalCandidates(board).getSolution();	 
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution=new TwinsAndTriplet(board).getSolution();
					 
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution=new InteractionsBetweenRegion(board).getSolution();	 
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution=new IsolatedGroups(board).getSolution();	 
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new MixedGroups(board).getSolution();
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new Xwing(board).getSolution();
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new XyWing(board).getSolution();
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new Coloring(board).getSolution();
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new Burma(board).getSolution();
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new Squirmbag(board).getSolution();
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new Swordfish(board).getSolution();
				 }
				 if(solution==null || solution.getActions().size() == 0) {
					 solution = new Jellyfish(board).getSolution();
				 }
				 if(solution!=null && solution.getActions().size() != 0) solution.act();
				
				 if(solution==null) {
					 stop=true;
				 }
				 
				 
				 
			
				 
			}
		}

	 
	public static void Solve(Grid board,Move sol) {
	   // Move solution = null;
		List<Heuristic> Heuristics = new ArrayList<Heuristic>();
		Heuristics.add(new OneCandidate(board));
		Heuristics.add(new UniqueCandidate(board));
		Heuristics.add(new Burma(board));
		Heuristics.add(new Coloring(board));
		Heuristics.add(new IdenticalCandidates(board));
		Heuristics.add(new IsolatedGroups(board));
		Heuristics.add(new Jellyfish(board));
		Heuristics.add(new MixedGroups(board));
		Heuristics.add(new Squirmbag(board));
		Heuristics.add(new Swordfish(board));
		Heuristics.add(new TwinsAndTriplet(board));
		Heuristics.add(new Xwing(board));
		Heuristics.add(new XyWing(board));
		Heuristics.add(new XyzWing(board));
		for (Heuristic algo : Heuristics) {
			sol = algo.getSolution();
			if(board.checkSolution()==true) {
				return;
			}
			if(sol!=null) {
				sol.act();
			}else {
				Solve(board,sol);
			}
		}
		//heuristicsObject
		Move solution = null;
		boolean stop = false;
		if(sol == null) {
			
			 sol=new OneCandidate(board).getSolution();
			
			 
			 if(solution==null || solution.getActions().size() == 0) {
 
				 solution=new UniqueCandidate(board).getSolution();			
				 
			 }
			 
						
			if(solution==null || solution.getActions().size() == 0) {
				 solution=new IdenticalCandidates(board).getSolution();	 
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution=new TwinsAndTriplet(board).getSolution();
				 
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution=new InteractionsBetweenRegion(board).getSolution();	 
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution=new IsolatedGroups(board).getSolution();	 
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new MixedGroups(board).getSolution();
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new Xwing(board).getSolution();
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new XyWing(board).getSolution();
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new Coloring(board).getSolution();
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new Burma(board).getSolution();
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new Squirmbag(board).getSolution();
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new Swordfish(board).getSolution();
			 }
			 if(solution==null || solution.getActions().size() == 0) {
				 solution = new Jellyfish(board).getSolution();
			 }
			 if(solution!=null && solution.getActions().size() != 0) solution.act();
			
			 if(solution==null) {
				 stop=true;
			 }
			 
			 
			 
		
			 
		}
	}

	public static StdGrid loadGrid(InputStream stream) {
		StdGrid board = null;
	  
		Cell[][] grille = new Cell[9][9];
		  
	

		try {

		    
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(stream);
		        
		    
		    doc.getDocumentElement().normalize();

		   
		    NodeList nList = doc.getElementsByTagName("Row");
       
		    String[][] row= new String[9][9];
		    for (int temp = 0; temp < nList.getLength(); temp++) {
                 
		        Node nNode = nList.item(temp);
		        
		        
		        row[temp] = nNode.getTextContent().toString().split(",");
		      
		        
		    }
		    for (int i = 0; i < 9; i++) {
		      for(int j=0;j<nList.getLength();j++) {
	        	
	        	  grille[i][j] = new StdCell(new StdCoordinate(i,j));
	        	  if(!row[i][j].equals("-")) {
	        		  grille[i][j].setValue(row[i][j]); 
	        		  grille[i][j].lock();
	        	  } 
	          }
		    }
		    
		    board = new StdGrid(StdGrid.defaultValueSet(),grille);
		    board.generateAllCandidat();
		    for(int i=0;i<9;i++) {
		    	for(int j=0;j<9;j++) {
		    		board.getCellAt(i, j).setGrid(board);
			    }
		    }
		
		    
		   
		    
		  
		    
		   
		    } catch (Exception e) {
		    e.printStackTrace();
		    }
		grid = board.deepCopy(board);
		return board;
	}
	
	public static StdGrid loadGrid(File file) {
		StdGrid board = null;
	  
		Cell[][] grille = new Cell[9][9];
		  
	

		try {

		    
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(file);
		        
		    
		    doc.getDocumentElement().normalize();

		   
		    NodeList nList = doc.getElementsByTagName("Row");
       
		    String[][] row= new String[9][9];
		    for (int temp = 0; temp < nList.getLength(); temp++) {
                 
		        Node nNode = nList.item(temp);
		        
		        
		        row[temp] = nNode.getTextContent().toString().split(",");
		      
		        
		    }
		    for (int i = 0; i < 9; i++) {
		      for(int j=0;j<nList.getLength();j++) {
	        	
	        	  grille[i][j] = new StdCell(new StdCoordinate(i,j));
	        	  if(!row[i][j].equals("-")) {
	        		  grille[i][j].setValue(row[i][j]); 
	        		  grille[i][j].lock();
	        	  } 
	          }
		    }
		    
		    board = new StdGrid(StdGrid.defaultValueSet(),grille);
		    board.generateAllCandidat();
		    for(int i=0;i<9;i++) {
		    	for(int j=0;j<9;j++) {
		    		board.getCellAt(i, j).setGrid(board);
			    }
		    }
		
		    
		   
		    
		  
		    
		   
		    } catch (Exception e) {
		    e.printStackTrace();
		    }
		grid = board.deepCopy(board);
		return board;
	}
	  
	    public static void Save (Cell[][] grille) {
	 
	        try {
	 
	            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	 
	            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	 
	            Document document = documentBuilder.newDocument();
	 
	    
	            Element root = document.createElement("Sudoku");
	            document.appendChild(root);
	 
	         
	            Element hints = document.createElement("Hints");
	 
	            root.appendChild(hints);
	 
	            Element rows = document.createElement("Rows");
	    
	            hints.appendChild(rows);
	            StringBuilder myRow;
	            for(int i=0;i<9;i++) {
	            	myRow=new StringBuilder();
	           	 Element row = document.createElement("Row");
	            	 for(int j=0;j<9;j++) {
	            		 		myRow.append(checkVal(grille[i][j].getValue())+",");
	                       }
	            	 row.appendChild(document.createTextNode(myRow.toString()));
	                 rows.appendChild(row);

	            }
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	            StreamResult result = new StreamResult(new File(xmlFilePath));
	            DOMSource source = new DOMSource(document);
	            transformer.transform(source, result);
	 
	        } catch (ParserConfigurationException pce) {
	            pce.printStackTrace();
	        } catch (TransformerException tfe) {
	            tfe.printStackTrace();
	        }
	    }
	    public static String checkVal(String string) {
	    	if(string==null) return "-";
	    	else return string;
	    }
	
	    
	    public static Grid getInitialGrid(){
	    	return grid.deepCopy(grid);
	    }
	    
	    public static StdGrid loadGridByDif(Difficulty dif) {

	    	switch(dif) {
	    	case EASY:
	    		return loadGrid(Difficulty.EASY.getPath());
	    	case MEDUIM: 
	    		return loadGrid(Difficulty.MEDUIM.getPath());
	    	case HARD: 
	    		return loadGrid(Difficulty.HARD.getPath());
	    	default:
	    		throw new IllegalArgumentException("Difficulty not Working Exception :" + dif.name());
	    	}

	    		
	    	}
		
	    	public static StdGrid loadGrid(String path) {
	    		StdGrid board = null;
	    	    Set<String> CANDIDATES;
	    		Cell[][] grille = new Cell[9][9];
	    		  
	    		CANDIDATES = StdGrid.defaultValueSet();
	    		

	    		try {

	    		    File fXmlFile = new File(path);
	    		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    		    Document doc = dBuilder.parse(fXmlFile);
	    		        
	    		    
	    		    doc.getDocumentElement().normalize();

	    		    
	    		    NodeList nList = doc.getElementsByTagName("Row");
	                
	    		 
	    		    String[][] row= new String[9][9];
	    		    for (int temp = 0; temp < nList.getLength(); temp++) {
	                     
	    		        Node nNode = nList.item(temp);
	    		       
	    		        
	    		        row[temp] = nNode.getTextContent().toString().split(",");
	    		      
	    		        
	    		    }
	    		    for (int i = 0; i < 9; i++) {
	    		      for(int j=0;j<nList.getLength();j++) {
	    	        	
	    	        	  grille[i][j] = new StdCell(new StdCoordinate(i,j));
	    	        	  if(!row[i][j].equals("-")) {
	    	        		  grille[i][j].setValue(row[i][j]); 
	    	        		  grille[i][j].lock();
	    	        	  } 
	    	          }
	    		    }
	    		    
	    		    board = new StdGrid(CANDIDATES,grille);
	    		    board.generateAllCandidat();
	    		    for(int i=0;i<9;i++) {
	    		    	for(int j=0;j<9;j++) {
	    		    		board.getCellAt(i, j).setGrid(board);
	    			    }
	    		    }
	    		
	    		    
	    		   
	    		    
	    		  
	    		    
	    		   
	    		    } catch (Exception e) {
	    		    e.printStackTrace();
	    		    }
	    		grid = board.deepCopy(board);
	    		return board;
	    	}
	    	
	    	public static void Save (Cell[][] grille,String Path) {
	    	     
	            try {
	     
	                DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	     
	                DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	     
	                Document document = documentBuilder.newDocument();
	     
	        
	                Element root = document.createElement("Sudoku");
	                document.appendChild(root);
	     
	             
	                Element hints = document.createElement("Hints");
	     
	                root.appendChild(hints);
	     
	                Element rows = document.createElement("Rows");
	        
	                hints.appendChild(rows);
	                StringBuilder myRow;
	                for(int i=0;i<9;i++) {
	                    myRow=new StringBuilder();
	                    Element row = document.createElement("Row");
	                     for(int j=0;j<9;j++) {
	                                 myRow.append(checkVal(grille[i][j].getValue())+",");
	                           }
	                     row.appendChild(document.createTextNode(myRow.toString()));
	                     rows.appendChild(row);

	                }
	                Transformer transformer = TransformerFactory.newInstance().newTransformer();
	                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	                StreamResult result = new StreamResult(new File(Path));
	                DOMSource source = new DOMSource(document);
	                transformer.transform(source, result);
	     
	            } catch (ParserConfigurationException pce) {
	                pce.printStackTrace();
	            } catch (TransformerException tfe) {
	                tfe.printStackTrace();
	            }
	        }
	}

