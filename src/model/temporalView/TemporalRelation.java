package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;

public abstract class TemporalRelation<E> extends Observable implements Serializable {

	private static final long serialVersionUID = 3044752885230388480L;

	public static int relationNumber = 0;
	
	private int id;
	private E masterMedia;
	private ArrayList<E> slaveMediaList = new ArrayList<E>();
	private Boolean explicit = false;
	
	public TemporalRelation(){
		
		this.id = relationNumber; 
		relationNumber++;
		
	}

	public int getId() {
		return id;
	}
	
	public void setMasterMedia(E masterMedia) {
		this.masterMedia = masterMedia;
	}
	
	public E getMasterMedia() {
		return masterMedia;
	}
	
	public void addSlaveMedia(E slaveMedia) {
		slaveMediaList.add(slaveMedia);
	}
	
	public void removeSlaveMedia(E slaveMedia) {
		
		if(slaveMediaList.remove(slaveMedia)){
			
			setChanged();
			Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_SLAVE_MEDIA_OF_SYNC_RELATION, slaveMedia, this);
	        notifyObservers(operation);
	        
		}
	}

	public ArrayList<E> getSlaveMediaList() {
		return slaveMediaList;
	}
	
	public void setExplicit(Boolean explicit){
		this.explicit = explicit;
	}
	
	public Boolean isExplicit(){
		return explicit;
	}
	
}
