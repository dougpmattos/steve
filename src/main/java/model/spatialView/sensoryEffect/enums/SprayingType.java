package model.spatialView.sensoryEffect.enums;

public enum SprayingType {

    WATER("Purified Water");

    private String name;

    private SprayingType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
