package model.spatialView.enums;

public enum AspectRatio {

	FILL,
	SLICE,
	HIDDEN;
	
	@Override
	public String toString(){
		
		switch(this) {
		
			case FILL:
				return "Fill";
	               
			case SLICE:
				return "Slice";
				
			case HIDDEN:
				return "Hidden";
				
				
		}
		
		return null; 

	}
	
}
