package model.spatialView.sensoryEffect.temperature;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;

public class HotPresentationProperty extends TemperaturePresentationProperty {

    public HotPresentationProperty(){

        intensityValue = new IntensityValue();
        intensityValue.setValue(28.0F);
        intensityRange = new IntensityRange();
        intensityRange.setFromValue(26.0F);
        intensityRange.setToValue(30.0F);

    }

}
