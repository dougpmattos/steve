package model.spatialView;

public enum Size {

	PX,
	PERCENTAGE;
	
	@Override
	public String toString(){
		
		switch(this) {
		
			case PERCENTAGE:
				return "%";
	               
			case PX:
				return "px";
				
		}
		
		return null; 

	}
}
