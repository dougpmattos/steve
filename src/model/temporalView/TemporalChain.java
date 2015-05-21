package model.temporalView;

import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.common.Operation;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable {

	private static int temporalChainNumber = 0;
	
	private int id;
	private Media masterMedia;
	private ArrayList<Media> mediaList;
	private ArrayList<Relation> relationList;
	
	public void initialize(Media masterMedia) {
		
		this.id = temporalChainNumber;
		this.masterMedia = masterMedia;
		mediaList = new ArrayList<Media>();
		relationList = new ArrayList<Relation>();
		
		temporalChainNumber++;
		
	}
	
	public void initialize() {
		
		this.id = temporalChainNumber;
		mediaList = new ArrayList<Media>();
		relationList = new ArrayList<Relation>();
		
		temporalChainNumber++;
		
	}

	public void addMedia(Media media){
		
		mediaList.add(media);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN_MEDIA, media, getId());
        notifyObservers(operation);
        
	}
	
	public void addSynchronousRelation(Synchronous<Media> synchronousRelation){
		
		relationList.add(synchronousRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, getId());
        notifyObservers(operation);
        
	}
	
	public void setMasterMedia(Media masterMedia){
		
		this.masterMedia = masterMedia;
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.SET_MASTER_MEDIA, masterMedia, getId());
        notifyObservers(operation);
        
	}
	
	public Media getMasterMedia() {
		return masterMedia;
	}

	public ArrayList<Media> getMediaList() {
		return mediaList;
	}

	public ArrayList<Relation> getRelationList() {
		return relationList;
	}

	public int getId() {
		return id;
	}
	
}
