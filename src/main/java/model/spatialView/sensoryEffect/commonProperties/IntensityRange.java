package model.spatialView.sensoryEffect.commonProperties;

import java.io.Serializable;

public class IntensityRange implements Serializable {

    private float fromValue;
    private float toValue;

    public float getFromValue() {
        return fromValue;
    }

    public void setFromValue(float fromValue) {
        this.fromValue = fromValue;
    }

    public float getToValue() {
        return toValue;
    }

    public void setToValue(float toValue) {
        this.toValue = toValue;
    }

}
