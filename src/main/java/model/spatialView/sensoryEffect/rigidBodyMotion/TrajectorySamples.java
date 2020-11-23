package model.spatialView.sensoryEffect.rigidBodyMotion;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;

public class TrajectorySamples {

    private FloatMatrix floatMatrix;
    private IntensityRange intensityRange;

    public FloatMatrix getFloatMatrix() {
        return floatMatrix;
    }

    public void setFloatMatrix(FloatMatrix floatMatrix) {
        this.floatMatrix = floatMatrix;
    }

    public IntensityRange getIntensityRange() {
        return intensityRange;
    }

    public void setIntensityRange(IntensityRange intensityRange) {
        this.intensityRange = intensityRange;
    }
}
