package model.spatialView.sensoryEffect;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;

public class VibrationPresentationProperty extends EffectPresentationProperty {

    private IntensityValue intensityValue;
    private IntensityRange intensityRange;

    public VibrationPresentationProperty(){

        intensityValue = new IntensityValue();
        intensityValue.setValue(0.0F);
        intensityRange = new IntensityRange();
        intensityRange.setFromValue(0.0F);
        intensityRange.setToValue(50.0F);

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
