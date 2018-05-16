package model.spatialView.sensoryEffect.enums;

public enum ZPositionType {

	BACK("Back"),
	MIDWAY("Midway"),
	FRONT("Front");
	
	private String name;
	
	private ZPositionType(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
}
