package cmd;

import java.util.List;
import java.util.Map;

import helpers.MyMap;
import model.Cell;


public interface Action {
	

	void setReasons(List<Cell> reasons) ;
	

	 void addReason(Cell cell) ;
	 
	 
	  void addAction(String action , Cell cell,String val );
	  
	  void setDetails(String details);
	  
	  Map<String, MyMap<String,Cell>> getActions();
	  List<Cell> getReasons() ;
	  
	  String getDetails();


	State getState();
	

	boolean canDo();

	boolean canUndo();
	
	void act();
}
