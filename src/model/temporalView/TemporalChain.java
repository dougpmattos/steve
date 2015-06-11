package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable implements Serializable {

	private static final long serialVersionUID = -6154510036093880684L;
	
	private static int temporalChainNumber = 0;
	private static int temporalViewMediaNumber = 0;
	
	private int id;
	private Media masterMedia;
	private ArrayList<Media> mediaList = new ArrayList<Media>();
	private ArrayList<Relation> relationList = new ArrayList<Relation>();
	
	public TemporalChain() {
		
		this.id = temporalChainNumber;
		temporalChainNumber++;
		
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setMasterMedia(Media masterMedia){
		
		this.masterMedia = masterMedia;
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.SET_MASTER_MEDIA, masterMedia, this);
        notifyObservers(operation);
        
	}
	
	public Media getMasterMedia() {
		return masterMedia;
	}
	
	public void addMedia(Media media){

		mediaList.add(media);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN_MEDIA, media, this);
        notifyObservers(operation);
        
        temporalViewMediaNumber++;
        
	}
	
	public void removeMedia(Media media){
		
		mediaList.remove(media);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN_MEDIA, media, getId());
        notifyObservers(operation);
        
        temporalViewMediaNumber--;
        
	}
	
	public ArrayList<Media> getMediaList() {
		return mediaList;
	}
	
	public void addSynchronousRelation(Synchronous<Media> synchronousRelation){
		
		relationList.add(synchronousRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, getId());
        notifyObservers(operation);
        
	}
	
	public void removeSynchronousRelation(Synchronous<Media> synchronousRelation){
		
		relationList.remove(synchronousRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, getId());
        notifyObservers(operation);
        
	}

	public ArrayList<Relation> getRelationList() {
		return relationList;
	}
	
	public static int getTemporalViewMediaNumber(){
		return temporalViewMediaNumber;
	}
	
}
