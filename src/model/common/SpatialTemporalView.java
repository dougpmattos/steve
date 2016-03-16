package model.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.temporalView.TemporalChain;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;

public class SpatialTemporalView extends Observable implements Serializable{

	private static final long serialVersionUID = 1818548173102220176L;
	
	private ArrayList<TemporalChain> temporalChainList = new ArrayList<TemporalChain>();
	
	public SpatialTemporalView(){
		
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
	
	public void clearTemporalChainList(){
		
		temporalChainList.clear();
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.CLEAR_TEMPORAL_CHAIN_LIST);
	    notifyObservers(operation);
		
	}
	
	public ArrayList<TemporalChain> getTemporalChainList() {
		return temporalChainList;
		
	}

	public void openExistingTemporalView(SpatialTemporalView existingTemporalView) {
		
		clearTemporalChainList();
		
		for(TemporalChain existingTemporalChain : existingTemporalView.getTemporalChainList()){
			
			TemporalChain temporalChain = new TemporalChain(existingTemporalChain.getName());
			temporalChain.setId(existingTemporalChain.getId());
			addTemporalChain(temporalChain);
			if(existingTemporalChain.getMasterMedia() != null){
				temporalChain.setMasterMedia(existingTemporalChain.getMasterMedia());
			}
			
		}
		
	}
	
}
