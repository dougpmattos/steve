package model.spatialView.enums;

public enum FontStyle {

	NORMAL,
	ITALIC;
	
	@Override
	public String toString(){
		
		switch(this) {
		
			case NORMAL:
				return "Normal";
	               
			case ITALIC:
				return "Italic";
				
		}
		
		return null; 

	}
	
}
