package model.spatialView.sensoryEffect;

import javafx.scene.paint.Color;
import model.spatialView.sensoryEffect.commonProperties.IntensityRange;
import model.spatialView.sensoryEffect.commonProperties.IntensityValue;

public class LightPresentationProperty extends EffectPresentationProperty {

    private IntensityValue intensityValue;
    private IntensityRange intensityRange;
    private Color color;

    public LightPresentationProperty(){
        intensityValue = new IntensityValue();
        intensityValue.setValue(50.0F);
        intensityRange = new IntensityRange();
        intensityRange.setToValue(0.0F);
        intensityRange.setToValue(100.0F);
        color = Color.WHITE;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
