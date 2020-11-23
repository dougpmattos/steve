package model.spatialView.sensoryEffect;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;
import model.spatialView.sensoryEffect.enums.ScentType;

public class ScentPresentationProperty extends EffectPresentationProperty {

    private ScentType scentType;
    private IntensityValue intensityValue;
    private IntensityRange intensityRange;

    public ScentPresentationProperty(){
        intensityValue = new IntensityValue();
        intensityValue.setValue(0);
        intensityRange = new IntensityRange();
        intensityRange.setToValue(0.0F);
        intensityRange.setToValue(12.0F);
        scentType = ScentType.ROSE;
    }

    public ScentType getScentType() {
        return scentType;
    }

    public void setScentType(ScentType scentType) {
        this.scentType = scentType;
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
}
