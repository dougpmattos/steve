package model.spatialView.sensoryEffect.enums;

public enum ScentType {
	
	ROSE("Rose"),
	ACACIA("Acacia"),
	CHRYSANTHEMUM("Chrysanthemum"),
	LILAC("Lilac"),
	MINT("Mint"),
	JASMINE("Jasmine"),
	PINE_TREE("Pine Tree"),
	ORANGE("Orange"),
	GRAPE("Grape");
	
	private String name;
	
	private ScentType(String name) {
        this.name = name;
    }
	
	@Override
	public String toString() {
		return name;
	}

}
