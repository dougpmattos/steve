package model.spatialView.sensoryEffect.rigidBodyMotion;

import model.spatialView.sensoryEffect.commonProperties.IntensityRange;

public class Shake {

    private TermReference direction;
    private float count;
    private IntensityRange distanceRange;
    private float distance;
    private int interval;

    public TermReference getDirection() {
        return direction;
    }

    public void setDirection(TermReference direction) {
        this.direction = direction;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public IntensityRange getDistanceRange() {
        return distanceRange;
    }

    public void setDistanceRange(IntensityRange distanceRange) {
        this.distanceRange = distanceRange;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getInterval() {
        return interval;
    }

    /**
     * @param   interval must be a value between positive
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }
}
