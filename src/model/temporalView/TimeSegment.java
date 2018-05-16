package model.temporalView;

import java.io.Serializable;

import model.common.Media;
import model.spatialView.media.MediaPresentationProperty;

public class TimeSegment implements Serializable {

	private static final long serialVersionUID = 4226201547356335315L;
	
	private String name;
	private Double begin;
	private Double end;
	private Media parentMedia;
	private MediaPresentationProperty presentationProperty;
	
	public TimeSegment(){
		
	}
	
	public void setBegin(Double begin) {
		this.begin = begin;
	}
	
	public Double getBegin() {
		return begin;
	}
	
	public void setEnd(Double end) {
		this.end = end;
	}
	
	public Double getEnd() {
		return end;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setParentMedia(Media parentMedia) {
		this.parentMedia = parentMedia;
	}
	
	public Media getParentMedia() {
		return parentMedia;
	}

	public void setPresentationProperty(MediaPresentationProperty presentationProperty) {
		this.presentationProperty = presentationProperty;
	}
	
	public MediaPresentationProperty getPresentationProperty() {
		return presentationProperty;
	}
	
}
