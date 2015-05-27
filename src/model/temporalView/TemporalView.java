package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;

public class TemporalView extends Observable implements Serializable{

	private static final long serialVersionUID = 1818548173102220176L;
	
	private ArrayList<TemporalChain> temporalChainList = new ArrayList<TemporalChain>();
	
	public TemporalView(){
		
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
