package model.temporalView.enums;

public enum TemporalRelationType {
	
	STARTS,
	STARTS_DELAY,
	FINISHES,
	FINISHES_DELAY,
	MEETS, 
	MEETS_DELAY,
	MET_BY, 
	MET_BY_DELAY,
	BEFORE;
	
	public String getDescription() {

		switch (this) {
			
			case STARTS: return "Left Alignment"; 
            case STARTS_DELAY: return "Left Alignment with Delay";
            case FINISHES: return "Right Alignment";
            case FINISHES_DELAY: return "Right Alignment with Delay";
            case MEETS: return "Right Alignment Relative to the Master End";
            case MEETS_DELAY: return "Right Alignment Relative to Master End with Delay";
            case MET_BY: return "Left Alignment Relative to the Master End";
            case MET_BY_DELAY: return "Left Alignment Relative to the Master End with Delay";
            case BEFORE: return "Sequence Alignment";
			
		}
		
		return this.toString();
	
	}
	
}
