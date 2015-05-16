package controller;

import model.common.Media;
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
		
		temporalView.addTemporalChain(temporalChain);
		
	}
	
	public void addMedia(Media media){
		
		temporalView.getTemporalChainList().get(0).addMedia(media);
		
	}
	
	public TemporalView getTemporalView(){
		return temporalView;
	}

}
