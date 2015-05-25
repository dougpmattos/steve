package model.spatialView;

public enum AspectRatio {

	FILL,
	SLICE,
	NONE;
	
	@Override
	public String toString(){
		
		switch(this) {
		
			case FILL:
				return "Fill";
	               
			case SLICE:
				return "Slice";
				
			case NONE:
				return "None";
				
		}
		
		return null; 

	}
	
}
