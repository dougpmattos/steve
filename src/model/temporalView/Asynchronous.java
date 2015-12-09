package model.temporalView;


public abstract class Asynchronous<T> extends Relation<T> {

	private static final long serialVersionUID = 5310555901053772316L;
	
	private TemporalChain newTemporalChain;
	
	public Asynchronous() {
		
	}

	public TemporalChain getNewTemporalChain() {
		return newTemporalChain;
	}

	public void setNewTemporalChain(TemporalChain newTemporalChain) {
		this.newTemporalChain = newTemporalChain;
	}
	
}
