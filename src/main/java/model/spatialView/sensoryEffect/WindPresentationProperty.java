package model.spatialView.sensoryEffect;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;
import view.enums.WindUnit;

public class WindPresentationProperty extends EffectPresentationProperty {

    private IntensityValue intensityValue;
    private IntensityRange intensityRange;
    private WindUnit unit;

    public WindPresentationProperty(){
        intensityValue = new IntensityValue();
        intensityValue.setValue(0);
        intensityRange = new IntensityRange();
        intensityRange.setFromValue(0.0F);
        intensityRange.setToValue(12.0F);
        unit = WindUnit.BEAUFORT;
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

    public WindUnit getUnit() {
        return unit;
    }

    public void setUnit(WindUnit unit) {
        this.unit = unit;
    }

}
