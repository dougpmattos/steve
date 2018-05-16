package model.spatialView.media.enums;

public enum AspectRatio {

	FILL,
	SLICE;
	
	@Override
	public String toString(){
		
		switch(this) {
		
			case FILL:
				return "Fill";
	               
			case SLICE:
				return "Slice";
				
				
		}
		
		return null; 

	}
	
}
