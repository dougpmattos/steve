package model.common.enums;

public enum AssessmentComparator {

	EQ("Equal to"),
	NE("Not Equal to"),
	GT("Greater than"),
	LT("Less than"),
	GTE("Greater than or Equal to"),
	LTE("Less than or Equal to");
	
	private String name;
	
	private AssessmentComparator(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
}
