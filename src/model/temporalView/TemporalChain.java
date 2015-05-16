package model.temporalView;

import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.common.Operation;
import model.common.Operator;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable {

	private static int temporalChainNumber = 0;
	
	private int id;
	private Media masterMedia;
	private ArrayList<Media> mediaList;
	private ArrayList<Relation> relationList;
	
	public TemporalChain(Media masterMedia){
		
		this.id = temporalChainNumber;
		this.masterMedia = masterMedia;
		mediaList = new ArrayList<Media>();
		relationList = new ArrayList<Relation>();
		
		temporalChainNumber++;
		
	}
	
	public TemporalChain(){
		
		this.id = temporalChainNumber;
		mediaList = new ArrayList<Media>();
		relationList = new ArrayList<Relation>();
		
		temporalChainNumber++;
		
	}

	public void addMedia(Media media){
		
		mediaList.add(media);
		
		setChanged();
		Operation operation = new Operation(Operator.ADD, media);
        notifyObservers(operation);
        
	}
	
	public Media getMasterMedia() {
		return masterMedia;
	}

	public void setMasterMedia(Media masterMedia) {
		this.masterMedia = masterMedia;
	}

	public ArrayList<Media> getMediaList() {
		return mediaList;
	}

	public void setMediaList(ArrayList<Media> mediaList) {
		this.mediaList = mediaList;
	}

	public ArrayList<Relation> getRelationList() {
		return relationList;
	}

	public void setRelationList(ArrayList<Relation> relationList) {
		this.relationList = relationList;
	}

	public int getId() {
		return id;
	}
	
}
