package model.spatialView.sensoryEffect.temperature;

import model.spatialView.sensoryEffect.EffectPresentationProperty;
import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;

public abstract class TemperaturePresentationProperty extends EffectPresentationProperty {

    protected IntensityValue intensityValue;
    protected IntensityRange intensityRange;

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
