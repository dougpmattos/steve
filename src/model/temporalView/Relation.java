package model.temporalView;

import java.util.ArrayList;

public abstract class Relation<E> {

	private int id;
	private E masterMedia;
	private ArrayList<E> slaveMediaList;
	
	public Relation(int id){
		
		this.id = id;
		
	}

	public Relation(int id, E masterMedia, ArrayList<E> slaveMediaList){
		
		this.id = id;
		this.masterMedia = masterMedia;
		this.slaveMediaList = slaveMediaList;
		
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
	
	
	
}
