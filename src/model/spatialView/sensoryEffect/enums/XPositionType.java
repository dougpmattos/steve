package model.spatialView.sensoryEffect.enums;

public enum XPositionType {

	LEFT("Left"),
	CENTER_LEFT("Center Left"),
	CENTER("Center"),
	CENTER_RIGHT("Center Right"),
	RIGHT("Right");
	
	private String name;
	
	private XPositionType(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}
	
}
