package model.spatialView.media;

import java.io.Serializable;

public class StyleProperty implements Serializable{

	private static final long serialVersionUID = 5446453641081608193L;
	
	private Double transparency = 0.0;
	
	public StyleProperty(){
		
	}
	
	public void setTransparency(Double transparency){
		this.transparency = transparency;
	}
	
	public Double getTransparency(){
		return transparency;
	}
	
}
