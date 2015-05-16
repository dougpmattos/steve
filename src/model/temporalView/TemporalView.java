package model.temporalView;

import java.util.ArrayList;
import java.util.Observable;

import model.common.Operation;
import model.common.Operator;

public class TemporalView extends Observable{
	
	private ArrayList<TemporalChain> temporalChainList;
	
	public TemporalView(){
		
		temporalChainList = new ArrayList<TemporalChain>();
		
	}

	public void addTemporalChain(TemporalChain temporalChain){
		
		temporalChainList.add(temporalChain);
		
		setChanged();
		Operation operation = new Operation(Operator.ADD, temporalChain);
	    notifyObservers(operation);
		
	}
	
	public void removeTemporalChain(TemporalChain temporalChain){
		
		temporalChainList.remove(temporalChain);
		
		setChanged();
		Operation operation = new Operation(Operator.REMOVE, temporalChain);
	    notifyObservers(operation);
	    
	}
	
	public ArrayList<TemporalChain> getTemporalChainList() {
		return temporalChainList;
		
	}
	
}
