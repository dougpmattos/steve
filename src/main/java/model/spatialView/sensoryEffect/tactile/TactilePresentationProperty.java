package model.spatialView.sensoryEffect.tactile;

import model.spatialView.sensoryEffect.EffectPresentationProperty;
import model.spatialView.sensoryEffect.rigidBodyMotion.FloatMatrix;
import model.spatialView.sensoryEffect.rigidBodyMotion.TermReference;

public class TactilePresentationProperty extends EffectPresentationProperty {

    private FloatMatrix arrayIntensity;
    private TactileResource tactileResource;
    private TermReference tactileEffect;
    private int updateRate;

    public FloatMatrix getArrayIntensity() {
        return arrayIntensity;
    }

    public void setArrayIntensity(FloatMatrix arrayIntensity) {
        this.arrayIntensity = arrayIntensity;
    }

    public TactileResource getTactileResource() {
        return tactileResource;
    }

    public void setTactileResource(TactileResource tactileResource) {
        this.tactileResource = tactileResource;
    }

    public TermReference getTactileEffect() {
        return tactileEffect;
    }

    public void setTactileEffect(TermReference tactileEffect) {
        this.tactileEffect = tactileEffect;
    }

    public int getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }
}
