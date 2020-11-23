package model.spatialView.sensoryEffect;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;
import model.spatialView.sensoryEffect.enums.SprayingType;
import view.enums.WindUnit;

public class WaterSprayerPresentationProperty extends EffectPresentationProperty {

    private IntensityValue intensityValue;
    private IntensityRange intensityRange;
    private SprayingType sprayingType;

    public WaterSprayerPresentationProperty(){
        intensityValue = new IntensityValue();
        intensityValue.setValue(0);
        intensityRange = new IntensityRange();
        intensityRange.setToValue(0.0F);
        intensityRange.setToValue(100.0F);
        sprayingType = SprayingType.WATER;
    }

    public IntensityValue getIntensityValue() {
        return intensityValue;
    }

    public void setIntensityValue(IntensityValue intensityValue) {
        this.intensityValue = intensityValue;
    }

    public IntensityRange getIntensityRange() {
        return intensityRange;
    }

    public void setIntensityRange(IntensityRange intensityRange) {
        this.intensityRange = intensityRange;
    }

    public SprayingType getSprayingType() {
        return sprayingType;
    }

    public void setSprayingType(SprayingType sprayingType) {
        this.sprayingType = sprayingType;
    }
}
