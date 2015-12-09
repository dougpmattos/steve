package model.temporalView.enums;

import view.common.Language;

public enum ProgrammingGuideInteractivityKey {

	MENU, INFO, GUIDE;
	
	public String toString(){
		
		switch (this) {
		
			case MENU: return "Menu";
			case INFO: return "Info";
			case GUIDE: return Language.translate("guide");
	
			default: return super.toString();
		
		}
		
	}

}
