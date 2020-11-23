package model.spatialView.sensoryEffect.rigidBodyMotion;

public class MoveTowardAngle {

    private int angle;

    public int getAngle() {
        return angle;
    }

    /**
     * @param   angle must be a value between 0 and 359
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }

}
