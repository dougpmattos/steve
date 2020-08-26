package model.common.enums;

public enum AutoExtractionType {

	AUDIO("Audio"),
	VISULA("Visual"),
	BOTH("Both");
	
	private String name;
	
	private AutoExtractionType(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
}
