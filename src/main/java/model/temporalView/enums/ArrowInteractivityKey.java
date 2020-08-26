package model.temporalView.enums;

import view.common.Language;

public enum ArrowInteractivityKey {

	CURSOR_LEFT, CURSOR_RIGHT, CURSOR_UP, CURSOR_DOWN;
	
	public String toString(){
		
		switch (this) {
	
			case CURSOR_LEFT: return Language.translate("leftwards.arrow");
			case CURSOR_RIGHT: return Language.translate("rightwards.arrow");
			case CURSOR_UP: return Language.translate("upwards.arrow");
			case CURSOR_DOWN: return Language.translate("downwards.arrow");

			default: return super.toString();
		
		}
		
	}

}
