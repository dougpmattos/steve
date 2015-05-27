package model.spatialView.enums;

public enum FontWeight {

	NORMAL,
	BOLD;
	
	@Override
	public String toString(){
		
		switch(this) {
		
			case NORMAL:
				return "Normal";
	               
			case BOLD:
				return "Bold";
				
		}
		
		return null; 

	}
	
}
