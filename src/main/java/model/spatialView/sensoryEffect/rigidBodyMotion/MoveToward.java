package model.spatialView.sensoryEffect.rigidBodyMotion;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;

public class MoveToward {

    private float speed;
    private float acceleration;
    private MoveTowardAngle directionV;
    private MoveTowardAngle directionH;
    private IntensityRange distanceRange;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public MoveTowardAngle getDirectionV() {
        return directionV;
    }

    public void setDirectionV(MoveTowardAngle directionV) {
        this.directionV = directionV;
    }

    public MoveTowardAngle getDirectionH() {
        return directionH;
    }

    public void setDirectionH(MoveTowardAngle directionH) {
        this.directionH = directionH;
    }

    public IntensityRange getDistanceRange() {
        return distanceRange;
    }

    public void setDistanceRange(IntensityRange distanceRange) {
        this.distanceRange = distanceRange;
    }

}
