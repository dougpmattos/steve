package model.spatialView.sensoryEffect;

public class RainstormPresentationProperty extends EffectPresentationProperty {

    private FlashPresentationProperty flashPresentationProperty;
    private WaterSprayerPresentationProperty waterSprayerPresentationProperty;

    public RainstormPresentationProperty(){
        flashPresentationProperty = new FlashPresentationProperty();
        waterSprayerPresentationProperty = new WaterSprayerPresentationProperty();
    }

    public FlashPresentationProperty getFlashPresentationProperty() {
        return flashPresentationProperty;
    }

    public void setFlashPresentationProperty(FlashPresentationProperty flashPresentationProperty) {
        this.flashPresentationProperty = flashPresentationProperty;
    }

    public WaterSprayerPresentationProperty getWaterSprayerPresentationProperty() {
        return waterSprayerPresentationProperty;
    }

    public void setWaterSprayerPresentationProperty(WaterSprayerPresentationProperty waterSprayerPresentationProperty) {
        this.waterSprayerPresentationProperty = waterSprayerPresentationProperty;
    }
}
