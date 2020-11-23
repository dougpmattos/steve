package model.spatialView.sensoryEffect;

import model.spatialView.sensoryEffect.rigidBodyMotion.FloatMatrix;

public class PassivePresentationProperty extends EffectPresentationProperty {

    private FloatMatrix PassiveKinestheticForce;
    private int updateRate;

    public FloatMatrix getPassiveKinestheticForce() {
        return PassiveKinestheticForce;
    }

    public void setPassiveKinestheticForce(FloatMatrix passiveKinestheticForce) {
        PassiveKinestheticForce = passiveKinestheticForce;
    }

    public int getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }

}
