package model.temporalView;

import java.io.Serializable;

import model.common.MediaNode;
import model.common.Node;
import model.common.enums.MediaType;
import model.spatialView.media.MediaPresentationProperty;

public class TimeSegment extends Node<MediaType> implements Serializable {

	private static final long serialVersionUID = 4226201547356335315L;
	
	private MediaNode parentMediaNode;
	private MediaPresentationProperty mediaPresentationProperty;
	
	public TimeSegment(){
		
	}

	public void setParentMediaNode(MediaNode parentMediaNode) {
		this.parentMediaNode = parentMediaNode;
	}
	
	public MediaNode getParentMediaNode() {
		return parentMediaNode;
	}

	public void setPresentationProperty(MediaPresentationProperty mediaPresentationProperty) {
		this.mediaPresentationProperty = mediaPresentationProperty;
	}
	
	public MediaPresentationProperty getPresentationProperty() {
		return mediaPresentationProperty;
	}
	
}
