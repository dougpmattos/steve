package model.temporalView;

import java.util.ArrayList;

public abstract class Relation<E> {

	private static int relationNumber = 0;
	
	private int id;
	private E masterMedia;
	private ArrayList<E> slaveMediaList;
	private Boolean explicit;
	
	public Relation(){
		
		this.id = relationNumber;
		explicit = false; 
		
		relationNumber++;
		
	}

	public Relation(E masterMedia){
		
		this.id = relationNumber;
		this.masterMedia = masterMedia;
		this.slaveMediaList = new ArrayList<E>();
		explicit = false;
		
		relationNumber++;
		
	}

	public E getMasterMedia() {
		return masterMedia;
	}

	public void setMasterMedia(E masterMedia) {
		this.masterMedia = masterMedia;
	}

	public ArrayList<E> getSlaveMediaList() {
		return slaveMediaList;
	}

	public void setSlaveMediaList(ArrayList<E> slaveMediaList) {
		this.slaveMediaList = slaveMediaList;
	}

	public int getId() {
		return id;
	}

	public Boolean getExplicit() {
		return explicit;
	}

	public void setExplicit(Boolean explicit) {
		this.explicit = explicit;
	}
	
	public void addSlave(E slave){
		slaveMediaList.add(slave);
	}
	
	public void removeSlave(E slave){
		slaveMediaList.remove(slave);
	}
	
}
