package model.temporalView.enums;

import view.common.Language;

public enum VolumeChangeInteractivityKey {

	VOLUME_UP, VOLUME_DOWN;
	
	public String toString(){
		
		switch (this) {

			case VOLUME_UP: return Language.translate("volume.up");
			case VOLUME_DOWN: return Language.translate("volume.down");

			default: return super.toString();
		
		}
		
	}

}
