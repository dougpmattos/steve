package model.temporalView;


public class Interactivity<K> extends Interaction {

	private static final long serialVersionUID = 4039625712116589691L;
	
	private K interactivityKey;
	
	public Interactivity(){
		
	}

	public K getInteractivityKey() {
		return interactivityKey;
	}

	public void setInteractivityKey(K interactivityKey) {
		this.interactivityKey = interactivityKey;
	}

}
