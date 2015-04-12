package model.temporalView;

import java.util.ArrayList;

import model.common.Media;

public class TemporalChain {

	private int id;
	private Media masterMedia;
	private ArrayList<Media> mediaList;
	private ArrayList<Relation> relationList;
	
	public TemporalChain(int id){
		
		this.id = id;
		
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
