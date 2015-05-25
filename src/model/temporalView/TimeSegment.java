package model.temporalView;

import model.common.Media;
import model.spatialView.AudioProperty;
import model.spatialView.ImageProperty;
import model.spatialView.PresentationProperty;
import model.spatialView.TextProperty;
import model.spatialView.VideoProperty;

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
		
		createPresentationProperty();
		
	}
	
	public void createPresentationProperty(){

		switch(parentMedia.getType()) {
		
			case IMAGE:
				presentationProperty = new ImageProperty();
				break;
               
			case VIDEO:
				presentationProperty = new VideoProperty();
				break;
               
			case AUDIO:
				presentationProperty = new AudioProperty();
				break; 
           
			case TEXT:
				presentationProperty = new TextProperty();
				break;
                   
			default:
				break;                
       } 
		
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
