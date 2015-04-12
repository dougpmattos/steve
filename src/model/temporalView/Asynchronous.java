package model.temporalView;

import java.util.ArrayList;

public class Asynchronous<T> extends Relation<T> {

	private TemporalChain newTemporalChain;
	
	public Asynchronous(int id, T masterMedia, ArrayList<T> slaveMediaList) {
		
		super(id, masterMedia, slaveMediaList);
		
	}
	
	public Asynchronous(int id) {
		
		super(id);
		
	}

	public TemporalChain getNewTemporalChain() {
		return newTemporalChain;
	}

	public void setNewTemporalChain(TemporalChain newTemporalChain) {
		this.newTemporalChain = newTemporalChain;
	}
	
}
