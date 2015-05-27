package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Relation<E> implements Serializable{

	private static final long serialVersionUID = 3044752885230388480L;

	private static int relationNumber = 0;
	
	private int id;
	private E masterMedia;
	private ArrayList<E> slaveMediaList = new ArrayList<E>();
	private Boolean explicit = false;
	
	public Relation(){
		
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
		slaveMediaList.remove(slaveMedia);
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
