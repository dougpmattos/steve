package model.NCLSupport.enums;

public enum ImportedNCLCausalConnectorType {

	ONBEGIN_START,
	ONBEGIN_START_DELAY,
	ONEND_STOP,
	ONEND_STOP_DELAY,
	ONEND_START,
	ONEND_START_DELAY,
	ONBEGIN_STOP,
	ONBEGIN_STOP_DELAY;
	
	public String getDescription() {

		switch (this) {
			
			case ONBEGIN_START: return "onBeginStart"; 
            case ONBEGIN_START_DELAY: return "onBeginStartDelay";
            case ONEND_STOP: return "onEndStop";
            case ONEND_STOP_DELAY: return "onEndStopDelay";
            case ONEND_START: return "onEndStart";
            case ONEND_START_DELAY: return "onEndStartDelay";
            case ONBEGIN_STOP: return "onBeginStop";
            case ONBEGIN_STOP_DELAY: return "onBeginStopDelay";
			
		}
		
		return this.toString();
	
	}
	
}