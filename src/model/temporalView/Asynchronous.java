package model.temporalView;

import java.util.ArrayList;

public abstract class Asynchronous<T> extends Relation<T> {

	private static final long serialVersionUID = 5310555901053772316L;
	
	private ArrayList<TemporalChain> temporalChainList;
	
	public Asynchronous() {
		
	}

	public ArrayList<TemporalChain> getTemporalChainList() {
		return temporalChainList;
	}

	public void setTemporalChainList(ArrayList<TemporalChain> temporalChainList) {
		this.temporalChainList = temporalChainList;
	}
	
}
