package model.spatialView.sensoryEffect;

import model.spatialView.sensoryEffect.rigidBodyMotion.RigidBodyMotionPresentationProperty;

public class PassiveKinestheticPresentationProperty extends RigidBodyMotionPresentationProperty {

    private int updateRate;

    public int getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }
}
