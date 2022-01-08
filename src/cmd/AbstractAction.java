package cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.MyMap;
import model.Cell;


public abstract class AbstractAction implements Action {
	
	// ATTRIBUTS
	public static final String ELIMINATE_CANDIDAT = "eleminate";
	public static final String SET_VALUE = "set_value"; 
	public static final String REMOVE_VALUE = "remove_value";
	protected Map<String, MyMap<String,Cell> > actions ;
	protected List<Cell> reasons ; 
	protected String details = null ; 
	protected State state;
	
	// CONSTRUCTEUR
	
	public AbstractAction() {
		actions = new HashMap<String, MyMap<String,Cell>>();
		reasons  = new ArrayList<Cell>();
		this.state = State.DO;
	}
	
	

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public boolean canDo() {
		return state == State.DO;
	}

	@Override
	public boolean canUndo() {
		return state == State.UNDO;
	}
	

	@Override
	public final void act() {
	    if (!canDo() && !canUndo()) {
	    	throw new AssertionError("act no state : AbstractAction");
	    }
	
	    if (canDo()) {
	        doIt();
	        state = State.UNDO;
	    } else { 
	        undoIt();
	        state = State.DO;
	    }
	}
    
    protected abstract void doIt();
    protected abstract void undoIt();

}
