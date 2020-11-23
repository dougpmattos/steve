package model.spatialView.sensoryEffect.rigidBodyMotion;

public class TurnAngle {

    private int angle;

    public int getAngle() {
        return angle;
    }

    /**
     * @param   angle must be a value between -180 and 180
     */
    public void setAngle(int angle) {
        this.angle = angle;
    }

}
