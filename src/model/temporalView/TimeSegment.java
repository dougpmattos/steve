package model.temporalView;

import java.io.Serializable;

import model.common.Media;
import model.common.Node;
import model.common.enums.MediaType;
import model.spatialView.media.MediaPresentationProperty;

public class TimeSegment extends Node<MediaType> implements Serializable {

	private static final long serialVersionUID = 4226201547356335315L;
	
	private Media parentMedia;
	private MediaPresentationProperty mediaPresentationProperty;
	
	public TimeSegment(){
		
	}

	public void setParentMedia(Media parentMedia) {
		this.parentMedia = parentMedia;
	}
	
	public Media getParentMedia() {
		return parentMedia;
	}

	public void setPresentationProperty(MediaPresentationProperty mediaPresentationProperty) {
		this.mediaPresentationProperty = mediaPresentationProperty;
	}
	
	public MediaPresentationProperty getPresentationProperty() {
		return mediaPresentationProperty;
	}
	
}
