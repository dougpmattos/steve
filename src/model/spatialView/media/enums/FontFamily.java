package model.spatialView.media.enums;

public enum FontFamily {

	TIMES_NEW_ROMAN;
	
	@Override
	public String toString(){
		
		switch(this) {
		
			case TIMES_NEW_ROMAN:
				return "Times New Roman";
	               
		}
		
		return null; 

	}
	
}
