package model.temporalView.enums;

import view.common.Language;

public enum ChannelChangeInteractivityKey {
	
	CHANNEL_UP, CHANNEL_DOWN;
	
	public String toString(){
		
		switch (this) {
		
			case CHANNEL_UP: return Language.translate("channel.up");
			case CHANNEL_DOWN: return Language.translate("channel.down");

			default: return super.toString();
		
		}
		
	}

}
