package model.spatialView.sensoryEffect.rigidBodyMotion;

public class Spin {

    private TermReference direction;
    private float count;
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

    public int getInterval() {
        return interval;
    }

    /**
     * @param   interval must be positive
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }
}
