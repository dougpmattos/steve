package model.temporalView;

import java.util.ArrayList;

public class Synchronous<T> extends Relation<T>{

	private RelationType type;
	private Double delay;

	public Synchronous(int id, T masterMedia, ArrayList<T> slaveMediaList) {
		
		super(id, masterMedia, slaveMediaList);
		
	}
	
	public Synchronous(int id) {
		
		super(id);
		
	}
	
	public RelationType getType() {
		return type;
	}

	public void setType(RelationType type) {
		this.type = type;
	}

	public Double getDelay() {
		return delay;
	}

	public void setDelay(Double delay) {
		this.delay = delay;
	}

	

}
