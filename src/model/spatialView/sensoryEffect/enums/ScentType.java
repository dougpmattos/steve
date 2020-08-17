package model.spatialView.sensoryEffect.enums;

public enum ScentType {
	
	ROSE("Scent of Rose"),
	ACACIA("Scent of Acacia"),
	CHRYSANTHEMUM("Scent of Chrysanthemum");
	
	private String name;
	
	private ScentType(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}

}
