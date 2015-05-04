package model.temporalView;

import java.util.ArrayList;

public class TemporalView{
	
	private ArrayList<TemporalChain> temporalChainList;
	
	public TemporalView(){
		
		temporalChainList = new ArrayList<TemporalChain>();
		
	}

	public ArrayList<TemporalChain> getTemporalChainList() {
		return temporalChainList;
	}

	public void setTemporalChainList(ArrayList<TemporalChain> temporalChainList) {
		this.temporalChainList = temporalChainList;
	}
	
}
