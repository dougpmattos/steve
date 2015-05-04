package model.temporalView;

import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.repository.ListUpdateOperation;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable {

	private int id;
	private Media masterMedia;
	private ArrayList<Media> mediaList;
	private ArrayList<Relation> relationList;
	
	public TemporalChain(int id, Media masterMedia){
		
		this.id = id;
		this.masterMedia = masterMedia;
		mediaList = new ArrayList<Media>();
		relationList = new ArrayList<Relation>();
		
	}

	public void addMedia(Media media){
		
		
		
		setChanged();
        //ListUpdateOperation listUpdateOperation = new ListUpdateOperation(media, ADD);
        //notifyObservers(listUpdateOperation);
        
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
