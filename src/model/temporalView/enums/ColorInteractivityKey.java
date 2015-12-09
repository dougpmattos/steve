package model.temporalView.enums;

import view.common.Language;


public enum ColorInteractivityKey {

	RED, GREEN, YELLOW, BLUE;
	
	public String toString(){
		
		switch (this) {
		
			case RED: return Language.translate("red");
			case GREEN: return Language.translate("green");
			case YELLOW: return Language.translate("yellow");
			case BLUE: return Language.translate("blue");

			default: return super.toString();
		
		}
		
	}

}
