package model.temporalView;

import java.util.ArrayList;

public class Asynchronous<T> extends Relation<T> {

	private TemporalChain newTemporalChain;
	
	public Asynchronous(T masterMedia) {
		
		super(masterMedia);
		
	}
	
	public Asynchronous(int id) {
		
		super();
		
	}

	public TemporalChain getNewTemporalChain() {
		return newTemporalChain;
	}

	public void setNewTemporalChain(TemporalChain newTemporalChain) {
		this.newTemporalChain = newTemporalChain;
	}
	
}
