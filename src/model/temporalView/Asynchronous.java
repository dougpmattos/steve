package model.temporalView;

import java.util.ArrayList;

public abstract class Asynchronous<T> extends TemporalRelation<T> {

	private static final long serialVersionUID = 5310555901053772316L;
	
	private ArrayList<TemporalChain> temporalChainList = new ArrayList<TemporalChain>();
	private Double startDelay;
	private Double stopDelay;

	public Asynchronous() {
		
	}

	public ArrayList<TemporalChain> getTemporalChainList() {
		return temporalChainList;
	}

	
	public void addTemporalChain(TemporalChain temporalChain) {
		temporalChainList.add(temporalChain);
	}
	
	public void removeTemporalChain(TemporalChain temporalChain) {
		temporalChainList.remove(temporalChain);
	}
	
	public Double getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(Double startDelay) {
		this.startDelay = startDelay;
	}

	public Double getStopDelay() {
		return stopDelay;
	}

	public void setStopDelay(Double stopDelay) {
		this.stopDelay = stopDelay;
	}
	
}
