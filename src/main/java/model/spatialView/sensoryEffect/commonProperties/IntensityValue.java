package model.spatialView.sensoryEffect.commonProperties;

import java.io.Serializable;

public class IntensityValue implements Serializable {

    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
