package model.temporalView.enums;

import view.common.Language;

public enum ControlInteractivityKey {

	BACK, EXIT, POWER, REWIND, STOP, EJECT, PLAY, RECORD, PAUSE, OK;
	
	public String toString(){
		
		switch (this) {
	
			case BACK: return Language.translate("back");
			case EXIT: return Language.translate("exit");
			case POWER: return Language.translate("power");
			case REWIND: return Language.translate("rewind");
			case STOP: return Language.translate("stop");
			case EJECT: return Language.translate("eject");
			case PLAY: return Language.translate("play");
			case RECORD: return Language.translate("record");
			case PAUSE: return Language.translate("pause");

			default: return super.toString();
		
		}
		
	}

}
