package model.temporalView;

import java.util.ArrayList;
import java.util.Observable;

import model.common.Operation;

public class TemporalView extends Observable{
	
	private ArrayList<TemporalChain> temporalChainList;
	
	public void initialize(){
		
		temporalChainList = new ArrayList<TemporalChain>();
		
		TemporalChain temporalChain = new TemporalChain();
		temporalChain.initialize();
		addTemporalChain(temporalChain);
		
	}

	public void addTemporalChain(TemporalChain temporalChain){
		
		temporalChainList.add(temporalChain);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN, temporalChain);
	    notifyObservers(operation);
		
	}
	
	public void removeTemporalChain(TemporalChain temporalChain){
		
		temporalChainList.remove(temporalChain);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_TEMPORAL_CHAIN, temporalChain);
	    notifyObservers(operation);
	    
	}
	
	public ArrayList<TemporalChain> getTemporalChainList() {
		return temporalChainList;
		
	}
	
}
