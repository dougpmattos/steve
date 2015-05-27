package model.temporalView;

import model.temporalView.enums.RelationType;

public class Synchronous<T> extends Relation<T>{

	private static final long serialVersionUID = -3957097957495083244L;
	
	private RelationType type;
	private Double delay;

	public Synchronous(){
		
	}
	
	public void setType(RelationType type) {
		this.type = type;
	}
	
	public RelationType getType() {
		return type;
	}

	public void setDelay(Double delay) {
		this.delay = delay;
	}
	
	public Double getDelay() {
		return delay;
	}

	

	

}
