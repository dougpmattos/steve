package model.temporalView;


public class Synchronous<T> extends Relation<T>{

	private RelationType type;
	private Double delay;

	public Synchronous(T masterMedia, RelationType type) {
		
		super(masterMedia);
		
		this.type = type;
		
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
