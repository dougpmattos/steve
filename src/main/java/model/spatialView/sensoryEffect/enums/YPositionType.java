package model.spatialView.sensoryEffect.enums;

public enum YPositionType {

	BOTTOM("Bottom"),
	MIDDLE("Middle"),
	TOP("Top");
	
	private String name;
	
	private YPositionType(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
}
