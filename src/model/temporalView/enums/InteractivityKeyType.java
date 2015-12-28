package model.temporalView.enums;

import view.common.Language;


public enum InteractivityKeyType {

	NUMERIC,
	ALPHABETICAL,
	PROGRAMMING_GUIDE,
	ARROWS,
	CHANNEL_CHANGE,
	VOLUME_CHANGE,
	COLORS,
	CONTROL,
	ACTUATION;
	
	public String toString(){
		
		switch (this) {
		
			case NUMERIC: return Language.translate("numeric");
			case ALPHABETICAL: return Language.translate("alphabetical");
			case PROGRAMMING_GUIDE: return Language.translate("programming.guide");
			case ARROWS: return Language.translate("arrows"); 
			case CHANNEL_CHANGE: return Language.translate("channel.change");
			case VOLUME_CHANGE: return Language.translate("volume.change");
			case COLORS: return Language.translate("colors");
			case CONTROL: return Language.translate("control");
			case ACTUATION: return Language.translate("actuation");

			default: return super.toString();
		
		}
		
	}

}
