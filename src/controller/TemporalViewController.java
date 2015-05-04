package controller;

import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;

public class TemporalViewController {

	private static TemporalViewController temporalViewController = null;
	
	private TemporalView temporalView;

	public static TemporalViewController getTemporalViewController(){
		
		if (temporalViewController==null){
			temporalViewController = new TemporalViewController();
		}
		return temporalViewController;
				
	}
	
	public void createTemporalView(){
		
		temporalView = new TemporalView();
		
	}
	
	public void addTemporalChain(TemporalChain temporalChain){
		
		temporalView.getTemporalChainList().add(temporalChain);
		
	}
	
}
