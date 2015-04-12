package model.temporalView;

import model.common.Media;
import model.spatialView.PresentationProperty;

public class TimeSegment {

	private String name;
	private Double begin;
	private Double end;
	private Media parentMedia;
	private PresentationProperty presentationProperty;

	public TimeSegment(String name, Double begin, Double end, Media parentMedia){
		
		this.name = name;
		this.begin = begin;
		this.end = end;
		this.parentMedia = parentMedia;
		presentationProperty = new PresentationProperty();
		
	}
	
	public Double getEnd() {
		return end;
	}

	public void setEnd(Double end) {
		this.end = end;
	}

	public String getName() {
		return name;
	}

	public Double getBegin() {
		return begin;
	}

	public Media getParentMedia() {
		return parentMedia;
	}

	public PresentationProperty getPresentationProperty() {
		return presentationProperty;
	}
	
}
