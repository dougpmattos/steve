package model.spatialView.sensoryEffect.temperature;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;

public class ColdPresentationProperty extends TemperaturePresentationProperty {

    public ColdPresentationProperty(){

        intensityValue = new IntensityValue();
        intensityValue.setValue(20.0F);
        intensityRange = new IntensityRange();
        intensityRange.setFromValue(18.0F);
        intensityRange.setToValue(22.0F);

    }

}
