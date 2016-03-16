package model.temporalView;

import model.temporalView.enums.TemporalRelationType;

public class Synchronous<T> extends TemporalRelation<T>{

	private static final long serialVersionUID = -3957097957495083244L;
	
	private TemporalRelationType type;
	private Double delay;

	public Synchronous(){
		
	}
	
	public void setType(TemporalRelationType type) {
		this.type = type;
	}
	
	public TemporalRelationType getType() {
		return type;
	}

	public void setDelay(Double delay) {
		this.delay = delay;
	}
	
	public Double getDelay() {
		return delay;
	}

	

	

}
